/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package derchaet;

import java.io.IOException;
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

    private MessageContainer messageContainer;
    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private StringProperty serverAddr = new SimpleStringProperty();
    private Client client;

    @FXML
    private void onLogin() {
        username.set(tfUser.getText());
        tfUser.setText("");
        password.set(tfPwd.getText());
        tfPwd.setText("");
        serverAddr.set(tfServerAddr.getText());
        tfServerAddr.setText("");

        if (!(username.isEmpty().or(serverAddr.isEmpty()).or(password.isEmpty()).get())) {

            try {
                client.connect(username.get(), password.get(), serverAddr.get());
                tabPane.getSelectionModel().select(1);
            } catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Fehler");
                a.setHeaderText("Verbindung zum Server fehlgeschlagen!");
                a.setContentText(ex.toString());
                a.showAndWait();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Fehler");
            a.setHeaderText("Ein oder mehrere Felder sind leer!");
            a.setContentText("Füllen Sie bitte alle Felder aus!");
            a.showAndWait();
        }
    }

    @FXML
    private void onLogout() {
        client.disconnect();
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
        if (!message.isEmpty()) {
            client.sendMsg(message);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        messageContainer = new MessageContainer(taChat);
        client = new Client(messageContainer);
        lbName.textProperty().bind(username);
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

}
