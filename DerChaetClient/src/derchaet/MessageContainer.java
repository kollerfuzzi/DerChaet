/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package derchaet;

import javafx.beans.property.StringProperty;

/**
 *
 * @author koller
 */
public class MessageContainer {
    private StringProperty chatHistory;

    public MessageContainer(StringProperty textProp) {
        setTextProperty(textProp);
    }
        
    public void setTextProperty(StringProperty textProp) {
        this.chatHistory = textProp;
    }
    
    public void append(String message) {
        chatHistory.set(chatHistory.get() + message + "\n");
    }
}
