package org.wso2.carbon.message;
/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

public class ConnectionHandler {
    private Logger log = Logger.getLogger(getClass());
    private Socket serverSocket;
    private DataOutputStream outToClient;
    private DataInputStream inputStreamReader;
    private GenericPackager packager;

    public ConnectionHandler(Socket socket) throws IOException, ISOException {
        this.packager = new GenericPackager("iso87ascii.xml");
        this.serverSocket = socket;
        this.inputStreamReader = new DataInputStream(serverSocket.getInputStream());
        this.outToClient = new DataOutputStream(serverSocket.getOutputStream());
        run();
    }

    private void run() {
        try {
            if (serverSocket.isConnected()) {
                log.info("There is a client connected");
                if (inputStreamReader.available() > 0) {
                    int length = inputStreamReader.available();
                    byte[] dataFromClient = new byte[length];
                    inputStreamReader.readFully(dataFromClient, 0, length);
                    log.info("Data From Client : " + new String(dataFromClient));
                    byte[] isomsg = unpackRequest(dataFromClient);
                    outToClient.write(isomsg);
                }
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

    private byte[] unpackRequest(byte[] message) throws ISOException {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);
        isoMsg.unpack(message);
        isoMsg.setMTI("0210");
        isoMsg.set("39", "00");
        return isoMsg.pack();

    }

    private String process(ISOMsg isomsg) throws Exception {
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

    private Properties getISO8583Properties() {
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
