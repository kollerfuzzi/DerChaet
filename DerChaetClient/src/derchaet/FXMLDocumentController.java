/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package derchaet;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author koller
 */
public class FXMLDocumentController implements Initializable {

    MessageContainer messageContainer;
    StringProperty username = new SimpleStringProperty();
    StringProperty password = new SimpleStringProperty();
    StringProperty serverAddr = new SimpleStringProperty();

    @FXML
    private void onLogin() {
        username.set(tfUser.getText());
        tfUser.setText("");
        password.set(tfPwd.getText());
        tfPwd.setText("");
        serverAddr.set(tfServerAddr.getText());
        tfServerAddr.setText("");

        if (!(username.isEmpty().or(serverAddr.isEmpty()).or(password.isEmpty()).get())) {
            tabPane.getSelectionModel().select(1);
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Fehler");
            a.setHeaderText("Ein oder Mehrere Felder sind leer!");
            a.setContentText("Füllen Sie bitte alle Felder aus!");
            a.showAndWait();
        }
    }

    @FXML
    private void onLogout() {
        tabPane.getSelectionModel().select(0);
    }

    @FXML
    private void onKeyPressed(KeyEvent k) {
        taChat.toBack();
        //taChat.setRotate(taChat.getRotate() + 10);
                
        if (k.getCode() == KeyCode.ENTER) {
            onMessageSend();
        }
    }

    @FXML
    private void onMessageSend() {
        String message = tfMessage.getText();
        tfMessage.setText("");
        taChat.setRotate(0);
        messageContainer.append(lbName.getText() + ": " + message);
        
        taChat.positionCaret(taChat.textProperty().length().get());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //taChat.textProperty().bind(textContent);
        messageContainer = new MessageContainer(taChat.textProperty());
        lbName.textProperty().bind(username);
        taChat.textProperty().addListener((observable, oldValue, newValue) -> {
            taChat.selectPositionCaret(taChat.getLength());
            System.out.println("TRIGGERED");
        });
    }
    
    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField tfPwd;
    @FXML
    private TextField tfServerAddr;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label lbName;
    @FXML
    private TextField tfMessage;
    @FXML
    private TextArea taChat;

    /*
    Wenn eine nachricht eingeht, soll das chatfenster vibrieren
     */
}
