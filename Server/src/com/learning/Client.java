package com.learning;

import java.io.*;
import java.net.*;

// Date
import java.util.Date;
import java.text.SimpleDateFormat;


public class Client {

    // Client properties
    private Socket clientSocket;
    private String clientName;
    private final int clientId;




    // static field to set Client's id's
    // id will be used in constructor of Client, and then incremented
    // id is unique for each client
    private static int id = 0;

    public Client(Socket clientSocket){
        this.clientSocket = clientSocket;
        // we assume that the first message sent by the client
        // is their name. Will be implemented like that on the client side
        this.clientName = null;
        this.clientId = Client.id++;
    }




    public String receiveMessage(){

        // if client's socket is closed we do not want to process it.
        // cleanup of closed sockets is each time new client connects
        // so it is possible that the client socket is closed and then we need to return null

        if(this.clientSocket.isClosed()){
            return null;
        }

        // it is null by default
        String received = null;
        try{
            InputStream inputStream = this.clientSocket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            // if there is data available then read it
            // this is to avoid blocking thread on read when data is not available
            if(bufferedReader.ready()) {
                received = bufferedReader.readLine();
            }
        }
        catch(java.io.IOException e) {
            received = null;
        }
        // if clientName is not set, and received msg which is not null
        // set name to the content of message and return null
        if(this.clientName == null && received != null){
            this.clientName = received;
            // TODO: remove sout
            System.out.println("Set name to: " + this.clientName);
            return null;
        }
        if(Validator.checkWhetherMessageIsCorrect(received)){
            return Validator.makeMessageLookLikeMessage(this.clientName, received);
        }
        else{
            return null;
        }
    }



    // return true or false whether the message was successfully delivered or no
    public boolean sendMessage(String msg){
        // variable to determine whether message was correctly delivered

        // if client's socket is closed we do not want to process it.
        // cleanup of closed sockets is each time new client connects
        // so it is possible that the client socket is closed and then we need to return from method

        if(this.clientSocket.isClosed()){
            return false;
        }

        boolean check;
        try{
            OutputStream outputStream = this.clientSocket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
            check = true;
        }
        catch(java.io.IOException e) {
            // I/O exception occurred - message was not delivered successfully
            check = false;
            System.out.println("Could not deliver message to: " + this.clientName);
        }
        return check;
    }


    public Socket getClientSocket(){
        return this.clientSocket;
    }

    public String getClientName(){
        return this.clientName;
    }

}
