

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
    public double eps = 1;
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



    void setTablePane( TablePane<MyCircle> t){
        tablePane = t;
    }
    void myRun() {
        timeline = new Timeline(new KeyFrame(javafx.util.Duration.millis(1000/30), event -> {
            for (int i = 0; i < figures.size(); i++) {
                for (int j = i + 1; j < figures.size(); j++) {

                    MyCircle cir1 = figures.get(i);
                    MyCircle cir2 = figures.get(j);
                    CircleCollider.collideCircle(cir1, cir2);
                }
            }
            //System.out.println(figures.size());
            for (MyCircle cir : figures) {
                double sec = 0.01;
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
