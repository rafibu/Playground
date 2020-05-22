package Cesarencryption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cesar extends JFrame implements ActionListener {

/*    /**
     * Starts encryption or decryption.
     * @param args not used
     * @throws IOException thrown if there is no clear Input
     */
  /*  public static void main(String[] args) throws IOException{
        boolean dec;
        Scanner scan = new Scanner(System.in);
        System.out.println("Would you like to encrypt a message?");
        String answer = scan.next();
        switch (answer){
            case "yes":
                dec = false;
                break;
            case "y":
                dec = false;
                break;
            case "no":
                dec = true;
                break;
            case "n":
                dec = true;
                break;
                default:
                    throw new IOException("We couldn't understand your Input.");
        }
        if(dec)
        System.out.println("which file would you like to decrypt?");
        else
            System.out.println("which file would you like to encrypt?");
        String filename = scan.next();
        System.out.println("which letter should represent the A?");
        char A1 = scan.next().charAt(0);
        char lowercaseA = Character.toLowerCase(A1);
        System.out.println("Your translated Message: ");
        if(dec)
            System.out.println(Decrypter.decrypt(filename, lowercaseA));
        else
        System.out.println(Encrypter.encrypt(filename, lowercaseA));
    } */
   JPanel panel;
   JButton decrypt;
   JButton encrypt;
   JLabel label;

   public Cesar(){
       this.setTitle("Cäsarverschlüsselung");
       this.setSize(800, 600);
       panel = new JPanel();
       label = new JLabel();
       JLabel question = new JLabel("Was möchten sie tun? \n", SwingConstants.CENTER);
       question.setVerticalAlignment(SwingConstants.TOP);
       Font font = question.getFont();
       question.setFont(new Font(font.getName(), Font.PLAIN, 50));
       panel.add(question);
       encrypt = new JButton("Verschlüsseln");
       encrypt.setFont(new Font(font.getName(), Font.PLAIN, 50));
       encrypt.addActionListener(this);
       decrypt = new JButton("Entschlüsseln");
       decrypt.setFont(new Font(font.getName(), Font.PLAIN, 50));
       decrypt.addActionListener(this);
       panel.add(encrypt);
       panel.add(decrypt);
       panel.add(label);
       this.add(panel);
   }

   public static void main(String... args){
       Cesar cesar = new Cesar();
       cesar.setVisible(true);
   }

   public void actionPerformed(ActionEvent ae){
       if(ae.getSource() == this.decrypt)
           label.setText("Entschlüsseln");
       if(ae.getSource() == this.encrypt)
           label.setText("Verschlüsseln");
   }
}