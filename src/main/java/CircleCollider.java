import javafx.scene.shape.Circle;

/**
 * Created by osm on 07/06/15.
 */

public class CircleCollider {
	public static double eps = 1;

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
		if (!cir1.isMovable() && !cir2.isMovable()) {
			return;
		}

		if (!tangent(cir1, cir2)) {
			return;
		}
		double cs = ((double) cir2.getCenterX() - (double) cir1.getCenterX()) / (length(cir1, cir2));
		double ss = ((double) cir2.getCenterY() - (double) cir1.getCenterY()) / (length(cir1, cir2));
//        System.out.println(ss +  " " + cs);
//        System.out.println(cir1.vecX + " " + cir1.vecY + " " + cir2.vecX + " " +  cir2.vecY);

		double tempx1 = cs * cir1.getCenterX() - ss * cir1.getCenterY();

		double tempx2 = cs * cir1.getCenterX() - ss * cir1.getCenterY();


		double x1 = cir1.vecX * cs - cir1.vecY * ss;
		double y1 = cir1.vecX * ss + cir1.vecY * cs;

		double x2 = cir2.vecX * cs - cir2.vecY * ss;
		double y2 = cir2.vecX * ss + cir2.vecY * cs;


		if (abs(x1) > abs(x2)) {
			if (x1 * (tempx2 - tempx1) > 0) {
				return;
			}
		} else {
			if (x2 * (tempx1 - tempx2) > 0) {
				return;
			}
		}
		double massOfBoth = cir1.mass + cir2.mass;
		if (!cir1.isMovable()) {
			x2 = -x2;
		} else if (!cir2.isMovable()) {
			x1 = -x1;
		} else {
			double newx1 = (x1 * (cir1.mass - cir2.mass) + 2 * cir2.mass * x2) / (massOfBoth);
			double newx2 = (x2 * (cir2.mass - cir1.mass) + 2 * cir1.mass * x1) / (massOfBoth);

			x1 = newx1;
			x2 = newx2;
		}

//        System.out.println(x1 + " " + y1  + " " + x2 + " " + y2);

		cir1.setVecX(x1 * cs + y1 * ss);
		cir1.setVecY(-x1 * ss + y1 * cs);

		cir2.setVecX(x2 * cs + y2 * ss);
		cir2.setVecY(-x2 * ss + y2 * cs);
//        System.out.println(cir1.vecX + " " + cir1.vecY + " " + cir2.vecX + " " +  cir2.vecY);
	}
}
