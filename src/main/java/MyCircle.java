

import javafx.scene.shape.Circle;

/**
 * Created by osm on 09/05/15.
 */
public class MyCircle extends Circle {
    final double moveback = 1;
    public int vecX;
    public int vecY;
    public int mass;
    public int radius;
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
    public void changeMovementVector(double x, double y){

    }
    public void translate(double x, double y){
        setCenterX(getCenterX()+x);
        setCenterY(getCenterY()+y);
    }
    public boolean inCircle(double x, double y){
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
