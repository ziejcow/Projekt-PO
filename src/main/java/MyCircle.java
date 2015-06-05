

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.shape.Circle;

/**
 * Created by osm on 09/05/15.
 */
public class MyCircle extends Circle {
    final double moveback = 1;

    /*
    * Contains speed in each axis in "unit" per second
    * */
    public Integer vecX;
    public Integer vecY;

    public Integer mass;
    public int radius;

    volatile static Integer counter = 0;
    public Integer id;
    public SimpleStringProperty idString;
    {
        synchronized (counter){
            this.id = counter++;
            idString = new SimpleStringProperty(id.toString());
        }
    }
    public MyCircle(double x, double y, int mass, double radius){
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(radius);
        this.mass = mass;
    }
    public void move(double sec) {
        double myX = getCenterX();
        double myY = getCenterY();
        double myVecY = vecY;
        double myVecX = vecX;
        myVecY/=moveback;
        myVecX/=moveback;
        setCenterX(myX + myVecX*sec);
        setCenterY(myY + myVecY * sec);
    }

    /*
    * TO DO
    * */
    public void changeMovementVector(double x, double y){
    }

    /*
    * Moves circle from current position by provided values
    * */
    public void translate(double x, double y){
        setCenterX(getCenterX()+x);
        setCenterY(getCenterY()+y);
    }

    /*
    * Check if coordinates are inside circle
    * */
    public boolean inCircle(double x, double y){
        if( (getCenterX()-x)*(getCenterX()-x)+(getCenterY()-y)*(getCenterY()-y)< getRadius()*getRadius()){
            return true;
        } else {
            return false;
        }
    }

    /*
    * Checks if coordinates are such that creating new circle with center in those coordinates will
    * create overlapping circles
    * */
    public boolean inCollisionProximity(double x, double y){
        if( (getCenterX()-x)*(getCenterX()-x)+(getCenterY()-y)*(getCenterY()-y)< 4*getRadius()*getRadius()){
            return true;
        } else {
            return false;
        }
    }
    MyCircle() {
        super();
    }
}
