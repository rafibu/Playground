package Infectionsimulation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;

public class SimulationGraph extends Canvas {
    private City city;
    private ArrayList<int[]> stats;

    public SimulationGraph(SimulationRenderer simulation){
        super(simulation.getWidth()*0.9, simulation.getHeight()*0.5);
        city = simulation.getCity();
        stats = new ArrayList<>();
    }

    public void repaint(){
        GraphicsContext gi = this.getGraphicsContext2D();
        paint(gi);
    }

    public void paint(GraphicsContext g) {
        g.setFill(Color.BLACK);
        createStats();
        double singleP = getHeight()/getPeople().size();
        double w = getWidth()/stats.size();
        for(int i = 0; i < stats.size(); i++){
            int h = 0;
            for(int j = 0; j < Person.Status.values().length; j++){
                g.setFill(Person.Status.values()[j].getColor());
                g.fillRect(i*w, h, (i+1)*w, h+stats.get(i)[j]*singleP);
                h += stats.get(i)[j]*singleP;
            }
            g.setFill(Color.BLACK);
            g.fillRect(i*w, h, (i+1)*w, getHeight());
        }
    }
    private void createStats(){
        int[] p = new int[Person.Status.values().length];
        for(Person person: getPeople()){
            for(int i = 0; i < p.length; i++){
                if(person.getStatus() == Person.Status.values()[i]){
                    p[i]++; break;
                }
            }
        }
        stats.add(p);
    }

    private LinkedList<Person> getPeople(){ return city.getPeople(); }
}
