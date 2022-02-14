import org.bzdev.roadanim.*;
import org.bzdev.anim2d.*;
import org.bzdev.geom.*;
import org.bzdev.graphs.RefPointName;
import org.bzdev.lang.Callable;

import java.io.File;
import java.awt.*;
import java.awt.geom.*;

public class CarTest6 {

    public static void main(String argv[]) throws Exception {
	Animation2D animation = new Animation2D();
	animation.setRanges(0.0, 0.0, 0.5, 0.5, 25.0, 25.0);

	AnimationPath2D apath =
	    new AnimationPath2D(animation, "path", true);

	double ay = 3.0;
	double x = -30.0;
	BasicSplinePathBuilder spb = new BasicSplinePathBuilder();
	SplinePathBuilder.CPoint[] cpoints= new SplinePathBuilder.CPoint[201];
	cpoints[0] = new SplinePathBuilder.CPoint
	    (SplinePathBuilder.CPointType.MOVE_TO, x, ay);
	for (int i = 1; i < 200; i++) {
	    x = -30.0 + 3*i/10.0;
	    double y = ay* Math.tanh(-x);
	    cpoints[i] =
		new SplinePathBuilder.CPoint
		(SplinePathBuilder.CPointType.SPLINE,
		 x, y);
	}
	cpoints[200] = new SplinePathBuilder.CPoint
		(SplinePathBuilder.CPointType.SEG_END,
		 30.0, -ay);
	spb.initPath(cpoints);
	BasicSplinePath2D path = spb.getPath();
	apath.setPath(path);
	apath.setZorder(2, true);
	apath.setColor(Color.BLUE);
	apath.setStroke(new BasicStroke(2.0F));

	final Car car = new Car(animation, "car", true);
	car.configure(3.0, 1.5, 1.0, 0.5, 0.25, Color.RED);
	car.setRefPointByName(RefPointName.UPPER_RIGHT);
	car.setZorder(1, true);

	double length = path.getPathLength();
	System.out.println("path length = " + length);

	final double speed = length / 7.5;
	int maxFrames = animation.estimateFrameCount(15.0);
	int maxSimtime = 15000;

	animation.initFrames(maxFrames, "ttmp/col-", "png");
	animation.scheduleFrames(0, maxFrames);
	File dir = new File("ttmp");
	dir.mkdirs();
	for (File file: dir.listFiles()) {
	    file.delete();
	}


	car.setReverseMode(true);
	car.setPath(apath, 0.0, 0.0, true, 0.0);
	car.setPathVelocity(speed);
	car.setPathAcceleration(0.0);

	/*
	animation.scheduleCall(new Callable() {
		public void call() {
		    car.setPathVelocity(0.0);
		}
	    },
	    7400);
	*/
	animation.scheduleCall(new Callable() {
		public void call() {
		    car.update();
		    System.out.println("x = " + car.getX());
		    car.setPathVelocity(-speed);
		}
	    },
	    7500);

	animation.run(maxSimtime);
    }
}
