import java.io.Serializable;

/**
 * Created by mateusz on 07.06.15.
 */
public class SerializableCircle implements Serializable{
	private Double vecX;
	private Double vecY;

	private Double mass;
	private int radius;
	private int id;
	private double centerX;
	private double centerY;
	private boolean movable;

	SerializableCircle(MyCircle circle){
		movable = circle.isMovable();
		vecX = circle.vecX;
		vecY = circle.vecY;
		mass = circle.mass;
		radius = (int) circle.getRadius();
		id = circle.getCircleId();
		centerX = circle.getCenterX();
		centerY = circle.getCenterY();
	}

	MyCircle getMyCircle(){

		MyCircle res =  new MyCircle(centerX, centerY, mass, radius, vecX, vecY, id);
		if(!movable) {
			res.setUnmovable();
		}
		return res;
	}
}
