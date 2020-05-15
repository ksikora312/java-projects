package com.learning;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Sender extends Thread {

    private List<Client> clientList;
    private List<Client> clientsToBeRemoved;

    public Sender(List<Client> clients){
        this.clientList = clients;
        this.clientsToBeRemoved = new LinkedList<Client>();
    }

    private void makeAndRunThreads(String msg){
        int threads = this.clientList.size();
        if(threads > 1)
            threads /= 2;
        ExecutorService threadPool = Executors.newFixedThreadPool(threads);
        for(Client client: this.clientList){
            threadPool.submit(() ->{
                if(!client.sendMessage(msg)){
                    // client was not reachable - client will be removed from clientList
                    clientsToBeRemoved.add(client);
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
        while(Server.isServerRunning()) {
            try {
                // if there are any clients that should be removed
                if(this.clientsToBeRemoved.size() > 0){
                    // Synchronize with Receiver to avoid troubles
                    synchronized(Handler.ReceiverLock){
                        this.clientList.removeAll(this.clientsToBeRemoved);
                    }
                    System.out.println("Sender: Removed: " + this.clientsToBeRemoved.size() + " clients");
                    this.clientsToBeRemoved.clear();
                }
                String msg = Handler.messagesToBeSend.take();
                // if there is a msg, before running threads to send message
                // we need to acquire lock. To be sure that the clientList is not gonna change during sending message
                if(this.clientList.size() > 0) {
                    synchronized (Handler.SenderLock) {
                        // TODO: remove sout
                        System.out.println("Sender: Running method to send msg");
                        this.makeAndRunThreads(msg);
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
