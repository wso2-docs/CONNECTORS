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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class mockServer {
    private Logger log = Logger.getLogger(getClass());
    private int port;
    Socket socket = null;

    public void startServer() throws IOException, ISOException {
        try {
            port = Integer.parseInt(serverConstant.PORT);
        } catch (NumberFormatException e) {
            log.error("The port number does not contain a parsable integer :" + e.getMessage(), e);
        }
        ServerSocket serverSocket = new ServerSocket(port);
        log.info("Server is waiting for client on port " + port);
        while (!serverSocket.isClosed()) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                log.error("Server is not accept the connection on port ", e);
            }
            new connectionHandler(socket);
        }
    }

    public static void main(String[] args) throws IOException, ISOException {
        mockServer server = new mockServer();
        server.startServer();
    }
}