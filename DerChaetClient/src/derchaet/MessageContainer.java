/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package derchaet;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;

/**
 *
 * @author koller
 */
public class MessageContainer {
    private TextArea ta;

    public MessageContainer(TextArea ta) {
        this.ta = ta;
    }
    
    public void append(String message) {
        this.ta.textProperty().set(this.ta.textProperty().get() + message + "\n");
        this.ta.positionCaret(this.ta.textProperty().length().get());
    }
}
