

import com.sun.javafx.beans.IDProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    public Double vecX;
    public Double vecY;

    public Double mass;
    public int radius;

    volatile static Integer counter = 0;
    public Integer id;
    public SimpleStringProperty idProperty;
    public SimpleDoubleProperty vecXProperty;
    public SimpleDoubleProperty vecYProperty;
    {
        synchronized (counter){
            this.id = counter++;
        }
    }
    public MyCircle(double x, double y, double mass, double radius){
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(radius);
        this.mass = mass;
        idProperty = new SimpleStringProperty(id.toString());
        vecXProperty = new SimpleDoubleProperty(0);
        vecYProperty = new SimpleDoubleProperty(0);
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
        vecXProperty.set(vecX);
        vecYProperty.set(vecY);
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
    public void setVecX(int a) {
        vecX = (double) a;
    }
    public void setVecY(int a) {
        vecY = (double) a;
    }
    public void setMass(int a) {
        mass = (double) a;
    }
    public String getIdProperty(){
        return idProperty.get();
    }
    public Double getVecXProperty(){
        return vecXProperty.get();
    }
    public Double getVecYProperty(){
        return vecYProperty.get();
    }
}
