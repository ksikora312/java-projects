package com.learning;

import java.net.Socket;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Handler extends Thread {

    // Server's list of clients
    private List<Client> clientList;

    // Sender's and Receiver's Threads
    private Thread receiverThread;
    private Thread senderThread;

    // Blocking queue with messages to be send. Receiver puts messages, Sender takes them
    public static BlockingQueue<String> messagesToBeSend = new ArrayBlockingQueue<String>(10);

    // locks to assure that the multithreading will work fine
    public static Object SenderLock = new Object();
    public static Object ReceiverLock = new Object();


    public Handler(List<Client> clients){
        this.clientList = clients;
    }


    private void makeSender(){
        senderThread = new Sender(this.clientList);
    }

    private void makeReceiver(){
        receiverThread = new Receiver(this.clientList);
    }


    @Override
    public void run(){
        // set and run threads
        this.makeSender();
        this.makeReceiver();
        this.receiverThread.start();
        this.senderThread.start();

        while(Server.isServerRunning()) {
            try {
                Socket newSocket = Server.newClients.take();
                // TODO: remove sout
                System.out.println("Client being handled");
                synchronized (Handler.ReceiverLock) {
                    synchronized (Handler.SenderLock) {
                        // two upper locks acquired - can easily remove clients with closed socket and add new client
                        // removes clients with closed sockets
                        this.clientList.add(new Client(newSocket));
                        this.clientList.removeIf(client -> client.getClientSocket().isClosed());
                        // TODO: remove sout
                        System.out.println("Client Added - connected");
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
