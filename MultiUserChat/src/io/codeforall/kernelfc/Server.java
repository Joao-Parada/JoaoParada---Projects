package io.codeforall.kernelfc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept(); // Waits for a client to connect
                System.out.println("A new client is on");

                ClientHandler clientHandler = new ClientHandler(socket); // Handles the client
                Thread thread = new Thread(clientHandler); // Runs client handler in a separate thread
                thread.start(); // Starts the thread for handling client

            } catch (IOException e) {
            }
        }
    }
    //ServerSocket
    public void closeServerSocket() {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        public static void main(String[] args) {
        int portNumber = 8080;
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(portNumber);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Server server = new Server(serverSocket);
        server.startServer();




        }


    }


