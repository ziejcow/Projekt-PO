

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.shape.Circle;
import javafx.scene.Group;

import java.util.Vector;

/**
 * Created by osm on 03/05/15.
 */
public class ExperimentManager {
    private TablePane<MyCircle> tablePane;
    public Double scrollValue = 0.0;
    public Group movingObjects;
    public Vector<MyCircle> figures;
    private Boolean stopEngine = false;
    private Timeline timeline;
    public Boolean getStopEngine() {
        return stopEngine;
    }

    public void setStopEngine(Boolean stopEngine) {
        this.stopEngine = stopEngine;
    }

//    public Thread getEngine() {
//        return engine;
//    }

    //private Thread engine;
    //public Vector<Thread> myThreads;
    boolean going;
    double eps = 1;
    double howFast;

    {
        figures = new Vector<>();
        //myThreads = new Vector<>();
        movingObjects = new Group();
        going = false;
    }

    boolean isGoing() {
        return going;
    }


    private double abs(double a) {
        if (a < 0.0) {
            return -a;
        }
        return a;
    }

    private double length(Circle a, Circle b) {
        return java.lang.Math.sqrt((a.getCenterX() - b.getCenterX()) * (a.getCenterX() - b.getCenterX())
                + (a.getCenterY() - b.getCenterY()) * (a.getCenterY() - b.getCenterY()));
    }

    private boolean tangent(Circle cir1, Circle cir2) {
        double mylen = length(cir1, cir2);
        if (abs(mylen - cir1.getRadius() - cir2.getRadius()) < eps) {
            return true;
        }
        return false;
    }
    void setTablePane( TablePane<MyCircle> t){
        tablePane = t;
    }
    void myRun() {
        timeline = new Timeline(new KeyFrame(javafx.util.Duration.millis(1000/30), event -> {
            for (int i = 0; i < figures.size(); i++) {
                for (int j = i + 1; j < figures.size(); j++) {

                    MyCircle cir1 = figures.get(i);
                    MyCircle cir2 = figures.get(j);
                    if (!tangent(cir1, cir2)) {
                        continue;
                    }
                    if (abs(cir1.vecX - cir2.vecX) < eps && abs(cir1.vecY - cir2.vecY) < eps) {
                        continue;
                    }
                    double cs = ((double) cir1.getCenterX() - (double) cir2.getCenterX())/((double) (cir1.getRadius() + cir2.getRadius())) ;
                    double ss = ((double) cir1.getCenterY() - (double) cir2.getCenterY())/((double) (cir1.getRadius() + cir2.getRadius())) ;


                    double x1 = cir1.vecX * cs - cir1.vecY * ss;
                    double y1 = cir1.vecX * ss + cir1.vecY * cs;

                    double x2 = cir2.vecX * cs - cir2.vecY * ss;
                    double y2 = cir2.vecX * ss + cir2.vecY * cs;

                    double temp = (cir1.mass * x1 + cir2.mass * x2)/(2.0 * (cir1.mass + cir2.mass));
                    x2 = temp;
                    x1 = temp;
                    System.out.println(temp);

                    cir1.vecX = x1 * cs - y1 * ss;
                    cir1.vecX = y1 * ss + y1 * cs;

                    cir2.vecX = x2 * cs - y2 * ss;
                    cir2.vecY= x2 * ss + y2 * cs;
                    System.out.println(cir1.vecX + " " + cir1.vecY + " " + cir2.vecX + " " +  cir2.vecY);
                }
            }
            //System.out.println(figures.size());
            for (MyCircle cir : figures) {
                double sec = 0.1;
                //sec *= scroll.getValue();
                sec *= scrollValue;
                sec /= 100;
                cir.move(sec);
            }
            tablePane.refresh();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        //timeline.play();

    }
    void play(){
        timeline.play();
    }
    void pause() {
        timeline.pause();
        going = false;
    }
    void restart() {
        going = false;
    }
}
