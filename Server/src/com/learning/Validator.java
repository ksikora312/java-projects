package com.learning;

import java.util.Date;

import java.text.SimpleDateFormat;

public class Validator {


    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static boolean checkWhetherMessageIsAKeyword(String message){
        // we do know that the message is not null
        if(message.equals("quit") || message.equals("q") || message.equals("exit")){
            return true;
        }
        return false;
    }

    private static boolean isMadeOfSpaces(String message){
        for(int i = 0; i < message.length(); i++){
            if(message.charAt(i) != ' ')
                return false;
        }
        return true;
    }

    public static boolean checkWhetherMessageIsCorrect(String message){

        if(message == null){
            return false;
        }
        // now we do know that the message is not null
        if(message.isEmpty() || Validator.isMadeOfSpaces(message)) {
            return false;
        }
        return true;
    }

    public static String makeMessageLookLikeMessage(String clientName, String message){
        String msg;
        if(Validator.checkWhetherMessageIsAKeyword(message)) {
            // message is a keyword, do not need to add who is a sender, date etc
            return message;
        }

        msg = Validator.dateFormat.format(new Date()) + " " + clientName + ": " + message;
        return msg;
    }
}
