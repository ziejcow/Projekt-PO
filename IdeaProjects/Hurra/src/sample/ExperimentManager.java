package sample;

import javafx.scene.control.ScrollBar;

/**
 * Created by osm on 03/05/15.
 */
public class ExperimentManager {
    public ScrollBar scroll;
    void setScroll(ScrollBar a) {
        scroll = a;
    }
    boolean going;
    double howFast;
    {
        going = false;
    }
    boolean isGoing() {
        return going;
    }
    void stop() {
        going = false;
    }
    void run() {
        going  = true;
    }
    void restart() {
        going = false;
    }
    

}
