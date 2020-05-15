package com.learning;

import java.util.List;
import java.util.LinkedList;

import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
public class Server implements Runnable{

    private ServerSocket serverSocket;
    private static boolean running;

    // list of connected clients
    private List<Client> clients;

    // Blocking queue with newClients, waiting to be added to the client list
    // createAcceptingClientsThread puts new clients, Handler takes them
    public static BlockingQueue<Socket> newClients = new ArrayBlockingQueue<Socket>(10);

    // threads doing their jobs
    private Thread acceptingNewClients;
    private Thread handlerClientInputAndOutput;



    // creating ServerSocket object, and setting static running variable
    public Server(int port){
        try {
            this.serverSocket = new ServerSocket(port);
            Server.running = true;
            this.clients = new LinkedList<Client>();
        }
        catch(java.io.IOException e){
            Server.running = false;
        }
    }


    // creating Thread responsible for accepting new clients and putting them in BlockingQueue
    private void createAcceptingClientsThread(){
        this.acceptingNewClients = new AcceptingClients(this.serverSocket);
    }

    // creating Thread responsible for handling requests from connected clients
    private void createHandler(){
        this.handlerClientInputAndOutput = new Handler(this.clients);
    }



    @Override
    public void run(){
        // TODO: remove sout
        System.out.println("Server started");
        this.createAcceptingClientsThread();
        this.createHandler();
        this.acceptingNewClients.start();
        this.handlerClientInputAndOutput.start();


        try {
            this.acceptingNewClients.join();
            this.handlerClientInputAndOutput.join();
        }
        catch(InterruptedException e) {}

    }





    // public static method to use in other classes to determine whether actions should be performed
    public static boolean isServerRunning(){
        return Server.running;
    }

    public void stopServer(){
        Server.running = false;
    }
}
