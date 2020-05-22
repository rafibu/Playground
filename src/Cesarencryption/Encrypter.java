package Cesarencryption;

import Cesarencryption.Parser;

import java.util.ArrayList;

import static java.lang.Character.toLowerCase;
/**
 * @author Zoggi
 */
public class Encrypter{


    public Encrypter(){
    }

    /**
     * encrypts files
     * @param filename String of file.
     * @param lowercaseA which Letter becomes the a?
     * @return String of encrypted file.
     */
    public String encrypt(String filename, char lowercaseA){
        int n = 0;
        char smallA = 'a';
        ArrayList<String> file = Parser.parse(filename);
        lowercaseA = (char)(lowercaseA - smallA);
        String[] encrypted = new String[file.size()];
        while(!file.isEmpty()){
        char[] toEnc = file.remove(0).toCharArray();
            Encryptor(lowercaseA, smallA, toEnc);
            String s = new String(toEnc);
        encrypted[n] = s;
        n++;
        }
        for(int i = 1; i < encrypted.length; i++)
            encrypted[0] += "\n" + encrypted[i];
        return encrypted[0];
    }

    public String encryptString(String message, char lowercaseA){
        char smallA = 'a';
        lowercaseA = toLowerCase(lowercaseA);
        lowercaseA = (char)(lowercaseA - smallA);
        char[] toEnc = message.toCharArray();
        Encryptor(lowercaseA, smallA, toEnc);
        return new String(toEnc);
    }

    private void Encryptor(char lowercaseA, char smallA, char[] toEnc) {
        for(int i = 0; i < toEnc.length; i++) {
            if((toEnc[i] + lowercaseA) > 'z' && toEnc[i] <= 'z')
                toEnc[i] = (char) ((toEnc[i] + lowercaseA - smallA) % 26 + smallA);
            else
            if (toEnc[i] + lowercaseA <= 'z' && toEnc[i] >= 'a')
                toEnc[i] += lowercaseA;
            char uppercaseA = Character.toUpperCase(lowercaseA);
            if (toEnc[i] < 'Z' - uppercaseA && toEnc[i] >= 'A')
                toEnc[i] += uppercaseA;
            else
            if (toEnc[i] >= 'Z' - uppercaseA && toEnc[i] <= 'Z')
                toEnc[i] = (char) ((toEnc[i] + uppercaseA - 'A') % 26 + 'A');
        }
    }
}