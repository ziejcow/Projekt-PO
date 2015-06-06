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
	private int radius;

	private volatile static Integer counter = 0;

	public Integer getCircleId() {
		return id;
	}

	private Integer id;
	private SimpleStringProperty idProperty;
	private SimpleDoubleProperty vecXProperty;
	private SimpleDoubleProperty vecYProperty;

	{
		synchronized (counter) {
			this.id = counter++;
		}
	}

	public MyCircle(double x, double y, double mass, double radius) {
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
		myVecY /= moveback;
		myVecX /= moveback;
		setCenterX(myX + myVecX * sec);
		setCenterY(myY + myVecY * sec);
		vecXProperty.set(vecX);
		vecYProperty.set(vecY);
	}

	/*
	* TO DO
	* */
	public void changeMovementVector(double x, double y) {
	}

	/*
	* Moves circle from current position by provided values
	* */
	public void translate(double x, double y) {
		setCenterX(getCenterX() + x);
		setCenterY(getCenterY() + y);
	}

	/*
	* Check if coordinates are inside circle
	* */
	public boolean inCircle(double x, double y) {
		if ((getCenterX() - x) * (getCenterX() - x) + (getCenterY() - y) * (getCenterY() - y) < getRadius() * getRadius()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	* Checks if coordinates are such that creating new circle with center in those coordinates will
	* create overlapping circles
	* */
	public boolean inCollisionProximity(double x, double y) {
		if ((getCenterX() - x) * (getCenterX() - x) + (getCenterY() - y) * (getCenterY() - y) < 4 * getRadius() * getRadius()) {
			return true;
		} else {
			return false;
		}
	}

	public static void clearId() {
		synchronized (counter) {
			counter = 0;
		}
	}
	public void setVecX(double a) {
		vecX =  a;
	}
	public void setVecY(double a) {
		vecY =  a;
	}
	public void setMass(double a) {
		mass =  a;
	}

	public String getIdProperty() {
		return idProperty.get();
	}

	public Double getVecXProperty() {
		return vecXProperty.get();
	}

	public Double getVecYProperty() {
		return vecYProperty.get();
	}

	public Double getMassProperty(){
		return mass;
	}
}
