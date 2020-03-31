package Infectionsimulation;

import javafx.scene.paint.Color;

import static Utilities.MathUtilities.probability;

public class Person {
    private Status status;
    private final City city;
    private float x, y; //Position
    private float vx, vy; //Geschwindigkeit
    float r; //Radius
    int infectionPeriod;
    boolean newlyInfected;
    private int secondsSymptomatic;

    public Person(float x, float y, float r, Status status, City city) {
        this.x = x;
        this.y = y;
        vx = 2*(float)Math.random()-1;
        vy = 2*(float)Math.random()-1;
        float tmp = (float)Math.sqrt((double)vx*vx+vy*vy);
        this.vx = vx/tmp*city.getSpeed();
        this.vy = vy/tmp*city.getSpeed();
        this.r = r;
        this.city = city;
        setStatus(status);
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) {
        switch(status){
            case HEALTHY:
                break;
            case INFECTED:
                newlyInfected = true;
                infectionPeriod = city.getSecondsToSymptom();
                break;
            case SYMPTOMATIC:
                secondsSymptomatic = 0;
                break;
            case RECOVERED:
                city.addNoninfectable();
                break;
            case DEAD:
                city.addNoninfectable();
                setVx(0);
                setVy(0);
                break;
        }
        this.status = status;
    }

    public Color getColor() { return status.getColor(); }

    public void move() {
        x = x+vx;
        y = y+vy;
    }

    public boolean doesCollide(float bx, float by, float nx, float ny) {
        if((x-bx)*nx+(y-by)*ny < r)
            return true;
        else
            return false;
    }

    public boolean doesCollide(Person b) {
        if((x-b.getX())*(x-b.getX())+(y-b.getY())*(y-b.getY()) < 4*r*r)
            return true;
        else
            return false;
    }

    public void resolveCollision(Person b) {
        // The normal direction at the contact point.
        float dx = x-b.getX();
        float dy = y-b.getY();
        float l = (float)Math.sqrt((double)(dx*dx+dy*dy));
        dx = dx/l;
        dy = dy/l;

        // Compute tangent and normal components of velocities.
        // Note that the tangent vector is (-dy,dx).
        float vn = dx*vx+dy*vy;
        float vt = -dy*vx+dx*vy;

        float vnb = dx*b.vx+dy*b.vy;
        float vtb = -dy*b.vx+dx*b.vy;

        // Tangent component of velocity stays the same for each ball.
        // Normal velocity is exchanged between balls.
        vx = vt*(-dy) + vnb*dx;
        vy = vt*dx + vnb*dy;

        b.setVx(vtb * (-dy) + vn * dx);
        b.setVy(vtb * dx + vn * dy);

        // Move one of the balls away to avoid repeated collision.
        b.setX(x - dx * 2 * r);
        b.setY(y - dy * 2 * r);
    }

    public void resolveCollision(float bx, float by, float nx, float ny) {
        // Move the ball back into the bounds of the simulation.
        float xp = x+nx*(r-((x-bx)*nx+(y-by)*ny));
        float yp = y+ny*(r-((x-bx)*nx+(y-by)*ny));
        x = xp;
        y = yp;

        // Determine speed after collision by reflecting it about the normal.
        // (vxp,vyp) = 2*dot((-vx,-vy),(nx,ny))*(nx,ny)+(vx,vy)
        float vxp = 2*(-vx*nx-vy*ny)*nx+vx;
        float vyp = 2*(-vx*nx-vy*ny)*ny+vy;
        vx = vxp;
        vy = vyp;
    }

    private int speedBefore;
    private float oldVx;
    private float oldVy;
    public void resetSpeed(int oldSpeed, int speed) {
        if(speed == 0 && oldSpeed != 0){
            speedBefore = oldSpeed;
            if(vx != 0 && vy != 0){
                oldVx = vx;
                oldVy = vy;
            }
        }
        if(oldSpeed != 0) {
            vx *= (float) speed / oldSpeed;
            vy *= (float) speed / oldSpeed;
        } else if(speed != 0){
            vx *= oldVx*speed / speedBefore;
            vy *= oldVy*speed / speedBefore;
            if(getStatus() != Status.DEAD) {
                if (vx == 0) vx = oldVx;
                if (vy == 0) vy = oldVy;
            }
        }
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public void setVx(float vx) { this.vx = getStatus() == Status.DEAD  ? 0 : vx; }
    public void setVy(float vy) { this.vy = getStatus() == Status.DEAD  ? 0 : vy; }

    public void decreaseInfectionperiod() {
        if(!newlyInfected) {
            infectionPeriod -= 1;
            if (infectionPeriod <= 0) {
                setStatus(Status.SYMPTOMATIC);
            }
        }
        newlyInfected = false;
    }

    public boolean isNewlyInfected() { return newlyInfected; }

    public void beSymptomatic() {
        assert getStatus() == Status.SYMPTOMATIC;
        if(probability(city.getDeathRate()/(100*city.getSectondsToRecovered()))){
            die();
        } else {
            if(secondsSymptomatic == city.getSectondsToRecovered()){
                setStatus(Status.RECOVERED);
            }
            secondsSymptomatic++;
        }
    }

    private void die() {
        setStatus(Status.DEAD);
    }

    public enum Status {
        HEALTHY("Healthy", Color.WHITE), RECOVERED("Recovered", Color.CYAN), INFECTED("Infected", Color.ORANGE), SYMPTOMATIC("Symptomatic", Color.RED), DEAD("Dead", Color.GRAY);

        String name;
        Color color;
        Status(String name, Color color) {
            this.name = name;
            this.color = color;
        }
        public String getName(){ return  name; }
        Color getColor(){ return color; }
    }
}
