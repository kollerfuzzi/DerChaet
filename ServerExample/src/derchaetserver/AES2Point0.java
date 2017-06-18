/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package derchaetserver;

/**
 *
 * @author koller
 */
public class AES2Point0 {

    public static String encryptHyperSecurely(String str) {
        String newString = "";
        for (int i = 0; i < str.length(); i++) {
            newString += (char) (str.charAt(i) + 14);
        }
        return newString;
    }

    public static String decryptHyperSecurely(String str) {
        String newString = "";
        for (int i = 0; i < str.length(); i++) {
            newString += (char) (str.charAt(i) - 14);
        }
        return newString;
    }
    
}
