import javafx.scene.shape.Circle;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.shape.Circle;
import javafx.scene.Group;

import java.util.Vector;

/**
 * Created by osm on 07/06/15.
 */

public class CircleCollider {
    public static double eps = 0.5;
    private static double length(Circle a, Circle b) {
        return java.lang.Math.sqrt((a.getCenterX() - b.getCenterX()) * (a.getCenterX() - b.getCenterX())
                + (a.getCenterY() - b.getCenterY()) * (a.getCenterY() - b.getCenterY()));
    }

    private static double abs(double a) {
        if (a < 0.0) {
            return -a;
        }
        return a;
    }

    private static boolean tangent(Circle cir1, Circle cir2) {
        double mylen = length(cir1, cir2);
        if (mylen - cir1.getRadius() - cir2.getRadius() < 2 * eps) {
            return true;
        }
        return false;
    }
    public static void collideCircle(MyCircle cir1, MyCircle cir2) {

        if (!tangent(cir1, cir2)) {
            return;
        }
        double cs = ((double) cir2.getCenterX() - (double) cir1.getCenterX())/(length(cir1, cir2));
        double ss = ((double) cir2.getCenterY() - (double) cir1.getCenterY())/(length(cir1,cir2)) ;
        System.out.println(ss +  " " + cs);
        System.out.println(cir1.vecX + " " + cir1.vecY + " " + cir2.vecX + " " +  cir2.vecY);

        double tempx1 = cs * cir1.getCenterX() - ss * cir1.getCenterY();

        double tempx2 = cs * cir1.getCenterX() - ss * cir1.getCenterY();


        double x1 = cir1.vecX * cs - cir1.vecY * ss;
        double y1 = cir1.vecX * ss + cir1.vecY * cs;

        double x2 = cir2.vecX * cs - cir2.vecY * ss;
        double y2 = cir2.vecX * ss + cir2.vecY * cs;

        if( abs(x1) > abs(x2) ) {
            if(x1 * (tempx2-tempx1) > 0 ) {
                return;
            }
        }
        else {
            if(x2 * (tempx1-tempx2) > 0 ) {
                return;
            }
        }

        System.out.println(x1 + " " + y1  + " " + x2 + " " + y2);



        double temp = x2;
        x2 = x1;
        x1 = temp;
        System.out.println(temp);

        cir1.vecX = x1 * cs + y1 * ss;
        cir1.vecY =  -x1 * ss + y1 * cs;

        cir2.vecX = x2 * cs + y2 * ss;
        cir2.vecY= -x2 * ss + y2 * cs;
        System.out.println(cir1.vecX + " " + cir1.vecY + " " + cir2.vecX + " " +  cir2.vecY);
    }
}
