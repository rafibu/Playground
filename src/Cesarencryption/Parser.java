package Cesarencryption;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * @author Zoggi
 */
public class Parser{
    public Parser(){
    }

    /**
     *Takes a txt file and parses it.
     * @param filename name of the file to parse
     * @return parsed file as a ArrayList of Strings (each Line is a new String)
     */
    public static ArrayList<String> parse(String filename){
        int n = 0;
        ArrayList<String> parsed = new ArrayList<>();
        try {
            Scanner fileScan = new Scanner(new File(filename));
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                parsed.add(line);
                n++;
            }
        } catch(IOException ie) {
            System.out.println("file couldn't be read");
        } catch (Exception e) {
            System.out.println("Something went wrong with the parsing: " + e);
        }
        for(int i = 0; i < 3; i++)
            assert parsed.isEmpty(): "There's nothing to Encrypt.";
        return parsed;
    }
    }
