

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.shape.Circle;

import java.util.Stack;
import java.util.Vector;

/**
 * Created by osm on 03/05/15.
 */
public class ExperimentManager {
	public boolean addingMovable;
	private TablePane<MyCircle> tablePane;
	public Double scrollValue = 0.0;
	public Group movingObjects;
	public Vector<MyCircle> figures;
	private Timeline timeline;
	private boolean isPlaying;

	public Stack<MyCircle> operations;

	double eps = 1;
	double howFast;

	{
		figures = new Vector<>();
		movingObjects = new Group();
		operations = new Stack<>();
		addingMovable = true;
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

	void setTablePane(TablePane<MyCircle> t) {
		tablePane = t;
	}

	void myRun() {
		timeline = new Timeline(new KeyFrame(javafx.util.Duration.millis(1000 / 30), event -> {
			for (int i = 0; i < figures.size(); i++) {
				for (int j = i + 1; j < figures.size(); j++) {

					MyCircle cir1 = figures.get(i);
					MyCircle cir2 = figures.get(j);
					CircleCollider.collideCircle(cir1, cir2);
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
	}

	void play() {
		isPlaying = true;
		timeline.play();
	}

	void pause() {
		isPlaying = false;
		timeline.pause();
	}

	public boolean getIsPlaying() {
		return isPlaying;
	}

	void restart() {
	}
}
