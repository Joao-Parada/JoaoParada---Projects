package io.codeforall.kernelfc;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter((new OutputStreamWriter(socket.getOutputStream())));
            this.bufferedReader = new BufferedReader((new InputStreamReader(socket.getInputStream())));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("Server : " + clientUserName + " has entered the chat ");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient == null) {
                    System.out.println(clientUserName + " disconnected");
                    break;
                }
                System.out.println(clientUserName + ": " + messageFromClient);
                broadcastMessage(clientUserName + ": " + messageFromClient);
            } catch (IOException e) {
                System.out.println("Error reading from client " + clientUserName);
                break;
            }
        }
    }
    public void broadcastMessage(String messageToSend){
        for (ClientHandler clientHandler : clientHandlers) {

            if (!clientHandler.clientUserName.equals(clientUserName)){
                try {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }





        }



    }

}
