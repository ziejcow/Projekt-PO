

import javafx.scene.shape.Circle;

/**
 * Created by osm on 09/05/15.
 */
public class MyCircle extends Circle {
    final double moveback = 1;
    public int vecX;
    public int vecY;
    public int mass;
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
    MyCircle() {
        super();
    }
}
