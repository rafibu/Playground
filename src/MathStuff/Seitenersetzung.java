package MathStuff;

import Utilities.MathUtilities;

import java.util.ArrayList;

/**
 * @author rbu
 * Zum lösen der Betriebssystem Aufgabe 8.5
 */
public class Seitenersetzung {
    private int kacheln = 6; //Wie viele Speicherplätze vorhanden sind

    /**
     * Berechnet wie viele Seitenfehler es gibt, Seitenersetzung mit FiFo, LRU oder Optimaler Ersetzung
     * @param args args[0] sollte der String der Prozessnamen (als Zahlen) sein, wobei diese Kommagetrennt sind
     *             Bsp: "1, 2, 3, 4, 2, 1, 5, 6, 2, 1, 2, 3, 7, 6, 3, 2, 1, 2, 3, 6"
     */
    public static void main(String... args){
        Seitenersetzung se = new Seitenersetzung();
        int[] prozesse = se.parseProzesliste(args[0]);
        se.optimalErsatz(prozesse);
    }

    /**
     * Wird benötigt um die Prozesse als Array darzustellen
     * @param liste String der Prozesse
     * @return Prozesse als Array
     */
    private int[] parseProzesliste(String liste) {
        if(liste != null) {
            String[] felder = liste.split(",");
            int[] res = new int[felder.length];
            for (int i = 0; i < felder.length; i++) {
                res[i] = Integer.parseInt(felder[i].trim());
            }
            return res;
        }
        return null;
    }

    /**
     * Berechnet den Ablauf mit Fifo ersetzung
     * @param prozesse
     * @return
     */
    private int fifo(int[] prozesse){
        ArrayList<Integer> list = new ArrayList<>();
        int count = 0;
        for(int prozess: prozesse){
            if(!MathUtilities.isIn(prozess, list.toArray())){
                count++;
                list.add(prozess);
                if(list.size() > kacheln) list.remove(0);
            }
            for(Integer i: list) System.out.print(i + ", ");
            System.out.println("Anzahl Seitenfehler: " + count);
        }
        return count;
    }

    /**
     * Berechnet den Ablauf mit Least Recently Used ersetzung
     * @param prozesse
     * @return
     */
    private int lru(int[] prozesse){
        ArrayList<Integer> list = new ArrayList<>();
        int count = 0;
        for(int prozess: prozesse){
            if(MathUtilities.isIn(prozess, list.toArray())) {
                list.remove((Object)prozess);
                list.add(prozess);
            } else {
                count++;
                list.add(prozess);
                if(list.size() > kacheln) list.remove(0);
            }
            for(Integer i: list) System.out.print(i + ", ");
            System.out.println("Anzahl Seitenfehler: " + count);
        }
        return count;
    }

    /**
     * Berechnet den Ablauf mit optimaler Ersetzung
     * @param prozesse
     * @return
     */
    private int optimalErsatz(int[] prozesse){
        ArrayList<Integer> list = new ArrayList<>();
        int count = 0;
        for(int x = 0; x < prozesse.length; x++){
            Integer prozess = prozesse[x];
            if(!MathUtilities.isIn(prozess, list.toArray())) {
                count++;
                if(list.size() >= kacheln){
                    int[] nextUse = new int[kacheln];
                    for(int c = 0; c < list.size(); c++){
                        for(int y = x; y < prozesse.length; y++){
                            if(prozesse[y] == list.get(c)){
                                nextUse[c] = y;
                                break;
                            }
                        }
                    }
                    removeBestOption(list, nextUse);
                }
                list.add(prozess);
            }
            for(Integer i: list) System.out.print(i + ", ");
            System.out.println("Anzahl Seitenfehler: " + count);
        }
        return count;
    }

    private void removeBestOption(ArrayList<Integer> list, int[] nextUse) {
        for(int i = 0; i < nextUse.length; i++){
            if(nextUse[i] == 0){
                list.remove(i);
                return;
            }
        }
        int max = 0;
        int place = -1;
        for(int i = 0; i < nextUse.length; i++){
            if(nextUse[i] > max){
                max = nextUse[i];
                place = i;
            }
        }
        list.remove(place);
    }
}
