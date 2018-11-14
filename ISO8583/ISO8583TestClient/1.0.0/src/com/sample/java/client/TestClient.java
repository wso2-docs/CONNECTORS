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

public class TestClient {
    private static final String host = "localhost";
    private static final int port = 5000;

    public TestClient() {

        Socket clientSocket = null;
        BufferedReader inFromServer = null;
        DataOutputStream outToServer = null;
        Logger log = Logger.getLogger(getClass());
        try {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            clientSocket = new Socket(host, port);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            if (clientSocket.isConnected()) {
                log.info("ISO8583 data : ");
                String input = inFromUser.readLine(); // Read the command line input
                input = input.toUpperCase();
                outToServer.writeUTF(input + "\n");
                outToServer.flush();
                String messageFromServer;
                while ((messageFromServer = inFromServer.readLine()) != null) {
                    log.info("Response From Server :" + messageFromServer);
                }
            }
        } catch (IOException ioe) {
            log.info("Error while sending the message:" + ioe);
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (inFromServer != null) {
                    inFromServer.close();
                }
                if (outToServer != null) {
                    outToServer.close();
                }
            } catch (IOException e) {
                log.error("Couldn't close the I/O Streams", e);
            }
        }
    }

    public static void main(String[] args) {

        new TestClient();
    }
}