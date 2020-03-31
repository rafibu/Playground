package Infectionsimulation;

import Utilities.Stopwatch;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class SimulationRenderer extends Canvas implements Runnable{
    private City city;
    private LinkedList<Person> balls;    // List of balls.
    private int w, h;                    // Width an height of image.
    private GraphicsContext gi;          // Graphics object to draw balls.
    private float r;                     // Radius of balls.
    private int speed;
    private SimulationGraph graph;
    private int count = 0;

    /**
     * Initializes the simulation.
     *
     * @param w width of simulation window.
     * @param h height of simulation window.
     */
    public SimulationRenderer(City city, int w, int h){
        super(w,h);
        this.city = city;

        this.r = (float)city.getRadius();
        this.w = city.getWidth();
        this.h = city.getLength();

        this.speed = city.getSpeed();
        balls = city.generateStartPopulation(w, h);
        graph = new SimulationGraph(this);
    }

    public void paint(GraphicsContext g) {
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, w, h);

        for (Person ball : balls) {
            g.setFill(ball.getColor());
            g.fillOval(ball.getX() - r, ball.getY() - r, 2 * r, 2 * r);
        }
    }

    /**
     * Starts the simulation loop.
     */
    public void start() {
        gi = this.getGraphicsContext2D();
        paint(gi);
        // Thread that runs simulation loop.
        Thread thread = new Thread(this);
        thread.run();
    }


    /**
     * The simulation loop.
     */
    public void run() {
        ArrayList<ArrayList<LinkedList<Person>>> hash = new ArrayList<>(h+5);
        ArrayList<LinkedList<Person>> hashX = new ArrayList<>(w+5);
        for(int i = 0; i < h+5; i++)
            hash.add(null);
        for(int i = 0; i < w+5; i++)
            hashX.add(null);

        Stopwatch stopwatch = new Stopwatch();
        Stopwatch graphStopwatch = new Stopwatch();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                for (Person b : balls) {
                    try {
                        LinkedList<Person> liste = new LinkedList<>();
                        liste.add(b);
                        hashX.set((int) b.getX(), liste);
                        hash.set((int) b.getY(), hashX);
                    } catch(IndexOutOfBoundsException e){
                        if(b.getX() < 0) b.setX(b.getX() + 1);
                        else if(b.getY() < 0) b.setY(b.getY() + 1);
                        else if(b.getX() > hashX.size()) b.setX(b.getX() - 1);
                        else if(b.getY() > hash.size()) b.setY(b.getY() - 1);
                    }
                }
                // Run one simulation step.
                Iterator<Person> it = balls.iterator();

                // Iterate over all balls.
                while (it.hasNext()) {
                    Person ball = it.next();

                    // Move the ball.
                    ball.move();

                    // Handle collisions with boundaries.
                    if (ball.doesCollide((float) w, 0.f, -1.f, 0.f))
                        ball.resolveCollision((float) w, 0.f, -1.f, 0.f);
                    if (ball.doesCollide(0.f, 0.f, 1.f, 0.f))
                        ball.resolveCollision(0.f, 0.f, 1.f, 0.f);
                    if (ball.doesCollide(0.f, (float) h, 0.f, -1.f))
                        ball.resolveCollision(0.f, (float) h, 0.f, -1.f);
                    if (ball.doesCollide(0.f, 0.f, 0.f, 1.f))
                        ball.resolveCollision(0.f, 0.f, 0.f, 1.f);

                    // Handle collisions with other balls.
                    if (graphStopwatch.isXMilliSecondPassed(speed, 500)) {
                        graph.repaint();
                    }
                    if(stopwatch.isSecondPassed(speed)){
                        count += checkFinished(count);
                        city.changePeople();
                    }
                    if(speed != 0) {
                        paint(gi);
                    }
                    try {
                        for (int i = -1; i < 2; i++) {
                            ArrayList<LinkedList<Person>> f = hash.get(i + (int) ball.getY());
                            for (int j = -1; j < 2; j++) {
                                LinkedList<Person> g;
                                if (f != null) {
                                    g = f.get(j + (int) ball.getX());
                                    if (g != null) {
                                        while (g.size() > 0) {
                                            Person ball2 = g.remove();
                                            if (ball2 != ball) {
                                                if (ball.doesCollide(ball2)) {
                                                    ball.resolveCollision(ball2);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IndexOutOfBoundsException e) { /* do nothing */ }
                }
            }
        }.start();
    }
    private int checkFinished(int count) {
        if(count == 1) {
            setSpeed(0);
            return -1;
        }
        if(getCity().isFinished()){
            return 1;
        }
        return 0;
    }

    public City getCity() { return city; }

    public void setSpeed(int speed){
        this.speed = speed;
        getCity().setSpeed(speed);
    }
    public  SimulationGraph getGraph(){
        return graph;
    }
}
