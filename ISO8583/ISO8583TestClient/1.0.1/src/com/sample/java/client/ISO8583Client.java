package com.sample.java.client;
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Class for handling the iso message request and responses.
 */
public class ISO8583Client {

    private Logger log = Logger.getLogger(getClass());
    private static final String host = "localhost";
    private static final int port = 5000;

    private ISO8583Client() {

        Socket socket = null;
        try {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            log.info(" ISO8583 Data : ");
            String input = inFromUser.readLine();
            socket = new Socket(host, port);
            clientHandler(socket, input.getBytes());
        } catch (IOException e) {
            log.error("Couldn't create Socket", e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("Couldn't close the Socket", e);
                }
            }
        }
    }

    /**
     * Handle the iso8583 message request and responses

     * @param connection  Socket connection with backend Test server
     * @param isoMessage  packed ISOMessage
     */

    private void clientHandler(Socket connection, byte[] isoMessage) {

        DataOutputStream outStream = null;
        BufferedReader inFromServer = null;
        try {
            outStream = new DataOutputStream(connection.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (connection.isConnected()) {
                outStream.write(isoMessage);
                outStream.flush();

                /* Sender will receive the Acknowledgement here */
                log.info("Response From Server :" + inFromServer.readLine());
            }
        } catch (IOException e) {
            log.error("An exception occurred in sending the iso8583 message", e);
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
                if (inFromServer != null) {
                    inFromServer.close();
                }
            } catch (IOException e) {
                log.error("Couldn't close the I/O Streams", e);
            }
        }
    }

    public static void main(String[] args) {

        new ISO8583Client();
    }
}
