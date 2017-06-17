/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author koller
 */
public class Message {
    private String username;
    private String message;
    private LocalDateTime timestamp;

    public Message(String username, String message, LocalDateTime timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return username + "<" + timestamp.format(DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm")) + ">: " + message;
    }    
}
