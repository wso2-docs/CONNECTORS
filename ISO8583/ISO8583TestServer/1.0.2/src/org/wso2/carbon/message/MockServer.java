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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MockServer {

    private Logger log = Logger.getLogger(getClass());
    private static final int port = 5010;

    private void startServer() throws IOException, ISOException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            log.info("Server is waiting for client on port " + port);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                new ConnectionHandler(socket);
            }
        } catch (IOException e) {
            log.error("Server is not accept the connection on port ", e);
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                log.error("Couldn't close the I/O Streams", e);
            }
        }
    }

    public static void main(String[] args) throws IOException, ISOException {

        MockServer server = new MockServer();
        server.startServer();
    }
}
