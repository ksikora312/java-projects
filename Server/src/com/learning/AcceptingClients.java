package com.learning;

import java.net.ServerSocket;
import java.net.Socket;

public class AcceptingClients extends Thread{

    private ServerSocket serverSocket;

    public AcceptingClients(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    @Override
    public void run(){
        while(Server.isServerRunning()){
            try {
                Socket newClientSocket = serverSocket.accept();
                // if new client connected, add to the blocking queue to be added to the connected clients
                // TODO: remove sout
                System.out.println("Client waiting for adding to the connected list");
                Server.newClients.put(newClientSocket);
            }
            catch(java.io.IOException e){
                // I/O error occurred during accepting new client
            }
            catch(InterruptedException e) {}
        }
    }
}
