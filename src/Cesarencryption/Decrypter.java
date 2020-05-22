package Cesarencryption;

import javax.swing.*;
/**
 * @author Zoggi
 */
public class Decrypter{

    public Decrypter(){
        JFrame frame = new JFrame("Display image");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    /**
     * decrypts a String in Cesar, when we know the key.
     * @param file String which has to be decrypted
     * @param isA Letter which becomes A.
     * @return decypted String file
     */
    public static String decrypt(String file, char isA){
        Encrypter encrypter = new Encrypter();
        isA = Character.toLowerCase(isA);
        int dec = isA - 'a';
        int tru = 26 - dec;
        char enc = (char)('a' + tru);
        return encrypter.encrypt(file, enc);
    }

    public String decryptString(String message, char isA){
        Encrypter encrypter = new Encrypter();
        isA = Character.toLowerCase(isA);
        int dec = isA - 'a';
        int tru = 26 - dec;
        char enc = (char)('a' + tru);
        return encrypter.encryptString(message, enc);
    }
}