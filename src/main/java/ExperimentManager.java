
import javafx.scene.control.ScrollBar;
import javafx.scene.shape.Circle;
import javafx.concurrent.Task;
import javafx.scene.Group;
import java.lang.Exception;import java.lang.Override;import java.lang.Thread;import java.lang.Void;
import java.util.Vector;
/**
 * Created by osm on 03/05/15.
 */
public class ExperimentManager {
    public ScrollBar scroll;
    public Group movingObjects;
    public Group staticObjects;
    public Vector<MyCircle> figures;
    public Vector<Thread> myThreads;
    boolean going;
    double eps = 1;
    double howFast;
    {
        movingObjects = new Group();
        going = false;
    }
    boolean isGoing() {
        return going;
    }
    void stop() {
        going = false;
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
    void myRun() {
        final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    try {
                        Thread.sleep(1000/60);
                    } catch (Exception e) {
                    }
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




                            double cs = (double) cir1.getCenterX() - (double) cir2.getCenterX() ;
                            double ss = (double) cir1.getCenterY() - (double) cir2.getCenterY() ;


                            double x1 = cir1.vecX * cs - cir1.vecY * ss;
                            double y1 = cir1.vecX * ss + cir1.vecY * cs;

                            double x2 = cir2.vecX * cs - cir2.vecY * ss;
                            double y2 = cir2.vecX * ss + cir2.vecY * cs;

                            x1 = x1 + x2;
                            x2 = x1;


                            cir1.vecX = (int)(x1 * cs - y1 * ss);
                            cir1.vecX = (int)(y1 * ss + y1 * cs);

                            x2 = x2 * cs - y2 * ss;
                            y2 = x2 * ss + y2 * cs;


                        }
                    }
                    for (MyCircle cir : figures) {
                        double sec = 0.1;
                        sec *= scroll.getValue();
                        sec /= 100;
                        cir.move(sec);
                    }
                }
            }
        };
        Thread moveCircles = new Thread(task);
        moveCircles.setDaemon(true);
        moveCircles.start();
        myThreads.add((moveCircles));
        going = true;
    }
    void restart() {
        going = false;
    }
    void setScroll(ScrollBar a) {
        scroll = a;
    }
}