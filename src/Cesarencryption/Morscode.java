package Cesarencryption;

/**
 * @author Zoggi
 */
public class Morscode{

    Morscode(){
    }
    String a = "•-";
    String b = "-•••";
    String c = "-•-•";
    String d = "-••";
    String e = "•";
    String f = "••-•";
    String g = "--•";
    String h = "••••";
    String i = "••";
    String j = "•---";
    String k = "-•-";
    String l = "•-••";
    String m = "--";
    String n = "-•";
    String o = "---";
    String p = "•--•";
    String q = "--•-";
    String r = "•-•";
    String s = "•••";
    String t = "-";
    String u = "••-";
    String v = "•••-";
    String w = "•--";
    String x = "-••-";
    String y = "-•--";
    String z = "--••";
    String n1 = "•----";
    String n2 = "••---";
    String n3 = "•••--";
    String n4 = "••••-";
    String n5 = "•••••";
    String n6 = "-••••";
    String n7 = "--•••";
    String n8 = "---••";
    String n9 = "----•";
    String n0 = "-----";
    String ä = "•-•-";
    String ö = "---•";
    String ü = "••--";

    /**
     * translates a message into Morsecode
     * @param message message to translate
     * @return translated message
     */
    public String translate(String message){
        String translation = "";
        message = message.toLowerCase();
        char[] mes = message.toCharArray();
        for(char cha: mes) {
            switch (cha){
                case 'a':
                    translation += a + " ";
                    break;
                case 'b':
                    translation += b + " ";
                    break;
                case 'c':
                    translation += c + " ";
                    break;
                case 'd':
                    translation += d + " ";
                    break;
                case 'e':
                    translation += e + " ";
                    break;
                case 'f':
                    translation += f + " ";
                    break;
                case 'g':
                    translation += g + " ";
                    break;
                case 'h':
                    translation += h + " ";
                    break;
                case 'i':
                    translation += i + " ";
                    break;
                case 'j':
                    translation += j + " ";
                    break;
                case 'k':
                    translation += k + " ";
                    break;
                case 'l':
                    translation += l + " ";
                    break;
                case 'm':
                    translation += m + " ";
                    break;
                case 'n':
                    translation += n + " ";
                    break;
                case 'o':
                    translation += o + " ";
                    break;
                case 'p':
                    translation += p + " ";
                    break;
                case 'q':
                    translation += q + " ";
                    break;
                case 'r':
                    translation += r + " ";
                    break;
                case 's':
                    translation += s + " ";
                    break;
                case 't':
                    translation += t + " ";
                    break;
                case 'u':
                    translation += u + " ";
                    break;
                case 'v':
                    translation += v + " ";
                    break;
                    case 'w':
                    translation += w + " ";
                    break;
                    case 'x':
                    translation += x + " ";
                    break;
                    case 'y':
                    translation += y + " ";
                    break;
                    case 'z':
                    translation += z + " ";
                    break;
                case '1':
                    translation += n1 + " ";
                    break;
                case '2':
                    translation += n2 + " ";
                    break;
                case '3':
                    translation += n3 + " ";
                    break;
                case '4':
                    translation += n4 + " ";
                    break;
                case '5':
                    translation += n5 + " ";
                    break;
                case '6':
                    translation += n6 + " ";
                    break;
                case '7':
                    translation += n7 + " ";
                    break;
                case '8':
                    translation += n8 + " ";
                    break;
                    case '9':
                    translation += n9 + " ";
                    break;
                case '0':
                    translation += n0 + " ";
                    break;
                case 'ä':
                    translation += ä + " ";
                    break;
                case 'ö':
                    translation += ö + " ";
                    break;
                case 'ü':
                    translation += ü + " ";
                    break;
                case ' ':
                    translation += "| ";
                    default:
                        translation += Character.toString(cha);
            }
        }
    return translation;
    }





}