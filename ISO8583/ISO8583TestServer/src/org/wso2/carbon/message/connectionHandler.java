package org.wso2.carbon.message;
/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class connectionHandler {
    private Logger log = Logger.getLogger(getClass());
    private Socket serverSocket;
    private DataOutputStream outToClient;
    private DataInputStream inputStreamReader;
    private GenericPackager packager;

    public connectionHandler(Socket socket) throws IOException, ISOException {
        this.packager = new GenericPackager("iso87ascii.xml");
        this.serverSocket = socket;
        this.inputStreamReader = new DataInputStream(serverSocket.getInputStream());
        this.outToClient = new DataOutputStream(serverSocket.getOutputStream());
        run();
    }

    public void run() {
        try {
            if (serverSocket.isConnected()) {
                log.info("There is a client connected");
                String dataFromClient = inputStreamReader.readUTF();
                log.info("Data From Client : " + dataFromClient);
                String isomsg = unpackRequest(dataFromClient);
                outToClient.writeBytes(isomsg);
            }
        } catch (IOException ioe) {
            log.error("Error while receiving the messages", ioe);
        } catch (ISOException e) {
            log.error("Error while unpack the messages", e);
        } catch (Exception e) {
            log.error("Error while send the ack to Sender", e);
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                log.error("Couldn't close I/O Streams", e);
            }
        }
    }

    public String unpackRequest(String message) throws ISOException {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(message.getBytes());
        isoMsg.setMTI("0210");
        isoMsg.set("39", "00");
        byte[] msg = isoMsg.pack();
        String packedMessage = new String(msg).toUpperCase();
        isoMsg.dump(System.out, "");
        return packedMessage;

    }

    public String process(ISOMsg isomsg) throws Exception {
        log.info("ISO Message MTI is " + isomsg.getMTI());
        String message = "";
        for (int i = 0; i < 128; i++) {
            if (isomsg.hasField(i)) {
                message += getISO8583Properties().getProperty(Integer.toString(i)) + "=" + isomsg.getValue(i) + "\n";
            }
        }
        log.info(message);
        return message;
    }

    public Properties getISO8583Properties() {
        Properties prop = new Properties();
        try {
            FileInputStream input = new FileInputStream("iso87asciiProperties.xml");
            prop.loadFromXML(input);
            input.close();
        } catch (IOException e) {
            log.error("Couldn't read the input file", e);
        }
        return prop;
    }
}