package com.learning;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import java.util.List;
import java.util.concurrent.TimeUnit;

import java.util.Date;
public class Receiver extends Thread {

    private List<Client> clientList;

    public Receiver(List<Client> clients){
        this.clientList = clients;
    }

    // util - get time at which user has left the chat
    private static SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss");


    void doActionsWithReceivedMessage(Client client, String messageReceived){
        // we do know that the message is not null - check not needed
        if("quit".equals(messageReceived) || "q".equals(messageReceived) || "exit".equals(messageReceived)){
            // put in message queue that an user has left
            String userLeft = Receiver.dataFormat.format(new Date()) + " " +
                    client.getClientName() + " has left the chat";

            try {
                Handler.messagesToBeSend.put(userLeft);
            }
            catch(InterruptedException e) {}

            try {
                client.getClientSocket().close();
            }
            catch(java.io.IOException e) {}
        }
        else {
            try {
                Handler.messagesToBeSend.put(messageReceived);
            }
            catch(InterruptedException e) {}
        }

    }


    private void makeAndRunThreads(){
        int threads = this.clientList.size();
        if(threads > 1)
            threads /= 2;
        ExecutorService threadPool = Executors.newFixedThreadPool(threads);
        for(Client client: this.clientList){
            // TODO: occasionally causes an error on exit
            threadPool.submit(() ->{
                String msg = client.receiveMessage();
                if(msg != null){
                    doActionsWithReceivedMessage(client, msg);
                }
            });
        }
        try {
            threadPool.awaitTermination(20, TimeUnit.MILLISECONDS);
        }
        catch(InterruptedException e) {}
    }

    @Override
    public void run(){
        while(Server.isServerRunning()){
            if(this.clientList.size() > 0) {
                synchronized (Handler.ReceiverLock) {
                    // TODO: remove sout
                    //System.out.println("Receiver: Running method to receive msg");
                    //System.out.println("Receiver: Clients: " + this.clientList.size());
                    this.makeAndRunThreads();
                }
            }
            try{
                Thread.sleep(20);
            }
            catch(InterruptedException e) {}
        }
    }
}
