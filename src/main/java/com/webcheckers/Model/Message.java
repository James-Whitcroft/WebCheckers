package com.webcheckers.Model;


import java.util.Objects;

/**
 * Represents the two different types of messages that
 * can be transmitted, error or informational. Holds the
 * type and the text that will be displayed
 * @author Cheyenne
 */

public class Message {

    private String text;
    private TYPE type;

    public enum TYPE {
        info,
        error
    }

    /**
     * Create Message with type specified
     * @param messageType the type of message, ERROR or INFO
     */
    public Message (TYPE messageType){
        this.type = messageType;
        if(messageType == TYPE.error){
            this.text = "ERROR: The player you have chosen is currently in a game, please select another.";
        }
        else{
            this.text = "This player is available for a game, enjoy your game!";
        }
    }
    public Message (TYPE messageType, String text){
        this.type = messageType;
        this.text = text;
    }

    /**
     * Retrieve the string text message stored based on the type
     * @return String text message
     */
    public String getText(){
        return text;
    }

    /**
     * Retrieve the type of message
     * @return  message type, ERROR or INFO
     */
    public TYPE getType(){
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            return this.type == ((Message) obj).type && this.text.equals(((Message) obj).text);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, text);
    }
}
