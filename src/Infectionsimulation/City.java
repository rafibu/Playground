package Infectionsimulation;

import java.util.LinkedList;

import static Utilities.MathUtilities.*;

public class City {
    private final int MAX_X;
    private final int MAX_Y;
    private int speed;
    private LinkedList<Person> people;
    private int startPopulation;
    private final int r;

    private int infectionRadius; //Radius around Person in which his infecting People
    private double infectionChance; //Possibility of infection by meeting
    private int secondsToSymptom; //Seconds between infection and Symptoms
    private double deathRate;
    private int sectondsToRecovered;
    private int nonInfectable;
    private int healthies;

    public City(int MAX_X, int MAX_Y, int speed, int startPopulation, int r, int infectionRadius, double infectionChance, int secondsToSymptom, double deathRate, int sectondsToRecovered) {
        this.MAX_X = MAX_X;
        this.MAX_Y = MAX_Y;
        this.speed = speed;
        this.startPopulation = startPopulation;
        this.r = r;
        this.infectionRadius = infectionRadius;
        this.infectionChance = infectionChance;
        this.secondsToSymptom = secondsToSymptom;
        this.deathRate = deathRate;
        this.sectondsToRecovered = sectondsToRecovered;
        people = new LinkedList<>();
    }

    public Person generatePerson(float x, float y, Person.Status status){
        boolean dy = false;
        while(!isFree(x, y)){
            if (dy) {
                y++;
            } else {
                x++;
            }
            dy = !dy;
        }
        Person person =  new Person(x, y, r, status, this);
        people.add(person);
        if(status == Person.Status.HEALTHY) healthies++;
        return person;
    }
    private boolean isFree(float x, float y) {
        for(Person person: people){
            if(person.getY() == y && person.getX() == x) return false;
        }
        return true;
    }

    public int getWidth() { return MAX_Y; }
    public int getLength() { return MAX_X; }

    public LinkedList<Person> generateStartPopulation(int w, int h){
        for(int i=0; i<startPopulation; i++) {
            generatePerson(r+(float)Math.random()*(w-2*r), r+(float)Math.random()*(h-2*r), Person.Status.HEALTHY);
        }
        return people;
    }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) {
        int oldSpeed = this.speed;
        this.speed = speed;
        for(Person person: people){
            person.resetSpeed(oldSpeed, speed);
        }
    }

    public LinkedList<Person> getPeople() { return people; }

    public int getRadius() { return r; }

    public void changePeople() {
        for(Person person: people){
            switch(person.getStatus()){
                case HEALTHY:
                    break;
                case INFECTED:
                    person.decreaseInfectionperiod();
                    if(!person.isNewlyInfected()) infectAll(person.getX(), person.getY());
                    break;
                case SYMPTOMATIC:
                    infectAll(person.getX(), person.getY());
                    person.beSymptomatic();
                    break;
                case RECOVERED:
                case DEAD: break;
                default: throw new IllegalStateException("Status: " + person.getStatus().getName() + "nicht kofniguriert.");
            }
        }
    }

    private void infectAll(float x, float y) {
        for(Person person: people){
            if(person.getStatus() == Person.Status.HEALTHY){
                if(isBetween(person.getX(), x-r*infectionRadius, x+r*infectionRadius)) {
                    if (isBetween(person.getY(), y-r*infectionRadius, y+r*infectionRadius)) {
                        if (probability(infectionChance)) {
                            person.setStatus(Person.Status.INFECTED);
                            healthies--;
                        }
                    }
                }
            }
        }
    }

    public void setSecondsToSymptom(int secondsToSymptom) { this.secondsToSymptom = secondsToSymptom; }
    public int getSecondsToSymptom() { return secondsToSymptom; }

    public void setInfectionRadius(int infectionRadius) { this.infectionRadius = infectionRadius; }
    public int getInfectionRadius() { return infectionRadius; }

    public void setInfectionChance(double infectionChance) { this.infectionChance = infectionChance; }
    public double getInfectionChance() { return infectionChance; }

    public double getDeathRate() { return deathRate; }
    public void setDeathRate(double deathRate) { this.deathRate = deathRate; }

    public int getSectondsToRecovered() { return sectondsToRecovered; }
    public void setSectondsToRecovered(int sectondsToRecovered) { this.sectondsToRecovered = sectondsToRecovered; }

    private boolean hasInfected;
    public void addNoninfectable(){
        hasInfected = true;
        nonInfectable++;
    }

    public boolean isFinished() {
        if(hasInfected) {
            hasInfected = false;
            return (healthies + nonInfectable) == people.size();
        }
        return false;
    }
}
