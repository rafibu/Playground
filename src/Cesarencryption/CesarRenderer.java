package Cesarencryption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.lang.Character.toUpperCase;

/**
 * @author Zoggi
 */
public class CesarRenderer{

    private static final int[] DIM = new int[]{700,600};
    private static final Dimension WINDOW_SIZE = new Dimension(DIM[0], DIM[1]);
    private JFrame Start = new JFrame("Cäsarverschlüsselung");
    private JFrame encry = new JFrame("Cäsarverschlüsselung Verschlüsseln");
    private JFrame decry = new JFrame("Cäsarverschlüsselung Entschlüsseln");
    private JFrame morse = new JFrame("Morseverschlüsselung");
    private Font font = new Font("Helvetica", Font.PLAIN, 25|50);
    private char keyChar;
    private boolean encrypt;

    public void Startmenu(){
        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        JLabel question = new JLabel("Was möchten sie tun? \n", SwingConstants.CENTER);
        question.setVerticalAlignment(SwingConstants.TOP);
        question.setFont(font);
        panel.add(question);
        JButton encrypt = new JButton("Verschlüsseln");
        JButton decrypt = new JButton("Entschlüsseln");
        JButton morse = new JButton("Morsecode");
        ActionListener ae = ae1 -> {
            if(ae1.getSource() == decrypt)
                encryptMenu();
            if(ae1.getSource() == encrypt) {
                setEncrypt(true);
                encryptMenu();
            }
            if(ae1.getSource() == morse)
                morseText();
        };
        encrypt.setFont(new Font(font.getName(), Font.PLAIN, 45));
        encrypt.addActionListener(ae);
        decrypt.setFont(new Font(font.getName(), Font.PLAIN, 45));
        decrypt.addActionListener(ae);
        morse.setFont(new Font(font.getName(), Font.PLAIN, 45));
        morse.addActionListener(ae);
        encrypt.setHorizontalAlignment(SwingConstants.CENTER);
        decrypt.setHorizontalAlignment(SwingConstants.CENTER);
        encrypt.setVerticalAlignment(SwingConstants.CENTER);
        decrypt.setVerticalAlignment(SwingConstants.CENTER);
        decrypt.setPreferredSize(new Dimension(DIM[0]/2 - DIM[0]/30, DIM[0]/10));
        encrypt.setPreferredSize(new Dimension(DIM[0]/2 - DIM[0]/30, DIM[0]/10));
        panel.add(encrypt);
        panel.add(decrypt);
        panel.add(morse);
        panel.add(label);
        Start.add(panel);
        Start.setSize(WINDOW_SIZE);
        Start.setVisible(true);
    }

    public void encryptMenu(){
        Start.setVisible(false);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JDialog key = new JDialog();
        key.setTitle("Schlüssel");
        key.setSize(500, 300);
        key.setModal(true);
        JLabel keyq;
        if(encrypt) {
            keyq = new JLabel("Welcher Buchstabe wird zu A?");
        } else {
            keyq = new JLabel("Welcher Buchstabe repräsentiert A?");
        }
        keyq.setFont(new Font(font.getName(), Font.PLAIN, 25));
        panel.add(keyq);
        JTextField tf = new JTextField(1);
        tf.setFont(font);
        panel.add(tf);
        JButton button = new JButton("OK");
        ActionListener ae = ae1 -> {
            if(ae1.getSource() == button && tf.getText().length() == 1 && ((tf.getText().charAt(0) <= 'z')&&(tf.getText().charAt(0) >= 'a') || (tf.getText().charAt(0) <= 'Z')&&(tf.getText().charAt(0) >= 'A'))) {
                keyChar = tf.getText().charAt(0);
                key.setVisible(false);
                key.setModal(false);
                encryptText();
            } else {
                JLabel err = new JLabel("Gib nur ein Buchstabe ein.");
                key.add(err);
            }
        };
        button.setPreferredSize(new Dimension(100,50));
        button.addActionListener(ae);
        panel.add(button);
        key.add(panel);
        key.setVisible(true);
    }

    public void encryptText(){
        Encrypter encrypter = new Encrypter();
        Decrypter decrypter = new Decrypter();
        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        pane.setPreferredSize(WINDOW_SIZE);
        JPanel right = new JPanel();
        JPanel left = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel sch;
        if(encrypt)
        sch = new JLabel("A -> " + toUpperCase(keyChar));
        else
            sch = new JLabel(toUpperCase(keyChar) + " -> A");
        sch.setHorizontalAlignment(SwingConstants.LEFT);
        JLabel inp;
        if(encrypt) {
            inp = new JLabel("Originaltext");
        } else {
            inp = new JLabel("Verschlüsselter Text");
        }
        Box b1 = Box.createHorizontalBox();
        b1.add(inp);
        b1.add(Box.createHorizontalGlue());
        inp.setFont(new Font(font.getName(), Font.PLAIN, 30));
        JLabel trim = new JLabel(" ");
        trim.setPreferredSize(new Dimension(DIM[0]/2 - 50, 16));
        JLabel out;
        if(encrypt){
            out = new JLabel("Verschlüsselter Text");
        } else {
            out = new JLabel("Originaltext");
        }
        out.setFont(new Font(font.getName(), Font.PLAIN, 30));
        out.setHorizontalAlignment(SwingConstants.LEFT);
        left.add(b1);
        left.add(trim);
        JTextArea tf = new JTextArea();
        tf.setFont(new Font(font.getName(), Font.PLAIN, 30));
        left.add(tf);
        Box b2 = Box.createHorizontalBox();
        b2.add(out);
        b2.add(Box.createHorizontalGlue());
        right.add(b2);
        right.add(sch);
        JButton translate = new JButton("Übersetzen");
        translate.setFont(new Font(font.getName(), Font.PLAIN, 30));
        JTextArea encfile = new JTextArea(encrypter.encryptString(tf.getText(), keyChar));
        encfile.setFont((new Font(font.getName(), Font.PLAIN, 30)));
        encfile.setPreferredSize(tf.getPreferredSize());
        ActionListener ae = ae1 -> {
            if(ae1.getSource() == translate && encrypt) {
                encfile.setText(encrypter.encryptString(tf.getText(), keyChar));
            }
            if(ae1.getSource() == translate && !encrypt){
                encfile.setText(decrypter.decryptString(tf.getText(), keyChar));
            }
        };
        translate.addActionListener(ae);
        right.add(encfile);
        Box b3 = Box.createHorizontalBox();
        b3.add(translate);
        b3.add(Box.createHorizontalGlue());
        right.add(b3);
        right.setSize(DIM[0]/2, DIM[1]);
        left.setSize(DIM[0]/2, DIM[1]);
        pane.setLeftComponent(left);
        pane.setRightComponent(right);
        pane.setDividerLocation(0.5);
        if(encrypt) {
            encry.setSize(WINDOW_SIZE);
            encry.add(pane);
            encry.setVisible(true);
        } else {
            decry.setSize(WINDOW_SIZE);
            decry.add(pane);
            decry.setVisible(true);
        }
    }

    public void morseText(){
        Start.setVisible(false);
        Morscode morscode = new Morscode();
        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        pane.setPreferredSize(WINDOW_SIZE);
        JPanel right = new JPanel();
        JPanel left = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel inp = new JLabel("Originaltext");
        Box b1 = Box.createHorizontalBox();
        b1.add(inp);
        b1.add(Box.createHorizontalGlue());
        inp.setFont(new Font(font.getName(), Font.PLAIN, 30));
        JLabel out = new JLabel("Morsecode");
        out.setFont(new Font(font.getName(), Font.PLAIN, 30));
        out.setHorizontalAlignment(SwingConstants.LEFT);
        left.add(b1);
        JButton translate = new JButton("Übersetzen");
        JTextArea tf = new JTextArea();
        tf.setFont(new Font(font.getName(), Font.PLAIN, 30));
        left.add(tf);
        translate.setFont(new Font(font.getName(), Font.PLAIN, 30));
        JTextArea morsfile = new JTextArea(morscode.translate(tf.getText()));
        morsfile.setFont((new Font(font.getName(), Font.PLAIN, 30)));
        morsfile.setPreferredSize(tf.getPreferredSize());
        ActionListener ae = ae1 -> {
            if(ae1.getSource() == translate) {
                morsfile.setText(morscode.translate(tf.getText()));
            }
        };
        translate.addActionListener(ae);
        right.add(morsfile);
        Box b3 = Box.createHorizontalBox();
        b3.add(translate);
        b3.add(Box.createHorizontalGlue());
        right.add(b3);
        right.setSize(DIM[0]/2, DIM[1]);
        left.setSize(DIM[0]/2, DIM[1]);
        pane.setLeftComponent(left);
        pane.setRightComponent(right);
        pane.setDividerLocation(0.5);
        morse.setSize(WINDOW_SIZE);
        morse.add(pane);
        morse.setVisible(true);
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public static void main(String... args){
        CesarRenderer cesarRenderer = new CesarRenderer();
        cesarRenderer.Startmenu();
    }
}