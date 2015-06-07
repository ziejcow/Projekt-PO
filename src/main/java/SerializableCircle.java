import java.io.Serializable;

/**
 * Created by mateusz on 07.06.15.
 */
public class SerializableCircle implements Serializable{
	public Double vecX;
	public Double vecY;

	public Double mass;
	public int radius;
	public int id;
	double centerX;
	double centerY;
	boolean movable;

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
