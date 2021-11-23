import org.bzdev.roadanim.*;
import org.bzdev.anim2d.*;
import org.bzdev.devqsim.TraceSet;
import org.bzdev.geom.*;
import org.bzdev.graphs.RefPointName;
import org.bzdev.lang.Callable;

import java.io.File;
import java.awt.*;
import java.awt.geom.*;

public class CarTest {

    public static void main(String argv[]) throws Exception {
	Animation2D animation = new Animation2D();
	animation.setRanges(0.0, 0.0, 0.5, 0.5, 25.0, 25.0);

	AnimationPath2D apath =
	    new AnimationPath2D(animation, "path", true);

	double r = 6.0;
	BasicSplinePathBuilder spb = new BasicSplinePathBuilder();
	SplinePathBuilder.CPoint[] cpoints= new SplinePathBuilder.CPoint[37];
	cpoints[0] = new SplinePathBuilder.CPoint
	    (SplinePathBuilder.CPointType.MOVE_TO, r, 0.0);
	for (int i = 1; i < 36; i++) {
	    double theta = Math.toRadians(i * 10.0);
	    cpoints[i] =
		new SplinePathBuilder.CPoint
		(SplinePathBuilder.CPointType.SPLINE,
		 r * Math.cos(theta), r * Math.sin(theta));
	}
	cpoints[36] =
	    new
	    SplinePathBuilder.CPoint(SplinePathBuilder.CPointType.CLOSE);
	// apath.append(cpoints);
	spb.initPath(cpoints);
	BasicSplinePath2D path = spb.getPath();
	for (int i = 0; i <= 360; i++) {
	    double theta_r = Math.toRadians(1.0 * i);
	    double s = r * theta_r;
	    double u = path.u(s);
	    double xc = path.getX(u);
	    double yc = path.getY(u);
	    double xd = r * Math.cos(theta_r);
	    double yd = r * Math.sin(theta_r);
	    if (Math.abs(xc-xd) > 1.e-4 || Math.abs(yc-yd) > 1.e-4) {
		System.out.format("(%g,%g) != (%g,%g)\n",
				  xc, yc, xd, yd);
	    }
	    double dxdu = path.dxDu(u);
	    double dydu = path.dyDu(u);
	    double dot = dxdu*xc + dydu*yc;
	    if (dot > 0.03) {
		System.out.format("for angle = %d, dot product = %g\n",
				  i, dot);
	    }
	    
	}
	apath.setPath(path);

	final Car car = new Car(animation, "car", true);
	car.configure(3.0, 1.5, 1.0, 0.5, 0.25, Color.RED);
	car.setRefPointByName(RefPointName.CENTER_RIGHT);
	car.setZorder(1, true);

	double length = 2.0 * r * Math.PI;

	double speed = length / 15.0;
	int maxFrames = animation.estimateFrameCount(16.0);
	int maxSimtime = 16000;

	animation.initFrames(maxFrames, "ctmp/col-", "png");
	animation.scheduleFrames(0, maxFrames);
	File dir = new File("ctmp");
	dir.mkdirs();
	for (File file: dir.listFiles()) {
	    file.delete();
	}

	Animation2D.setTraceLevels(0,1,2,3);
	RoadAnimation.setTraceLevels(0,1,2,3);
	TraceSet traceSet = new TraceSet(animation, "trace", true);
	traceSet.setLevel(0);
	animation.setTraceOutput(System.out);

	car.addTraceSet(traceSet);

	car.setPath(apath, 0.0, Math.toRadians(45.0), true, 1.0);
	car.setPathVelocity(speed);
	car.setPathAcceleration(0.0);

	double dx = car.getDriverXGCS();
	double dy = car.getDriverYGCS();
	double cx = car.getX();
	double cy = car.getY();
	System.out.format("cx = %g, cy = %g\n", cx, cy);
	System.out.format("dx = %g, dy = %g\n", dx, dy);
	System.out.println("sep = " + Point2D.distance(dx, dy, cx, cy));

	animation.scheduleCall(new Callable() {
		public void call() {
		    double dx = car.getDriverXGCS();
		    double dy = car.getDriverYGCS();
		    double cx = car.getX();
		    double cy = car.getY();
		    System.out.format("cx = %g, cy = %g\n", cx, cy);
		    System.out.format("dx = %g, dy = %g\n", dx, dy);
		    System.out.println("sep = "
				       + Point2D.distance(dx, dy, cx, cy));
		    System.out.println("entering skid mode");
		    car.setSkidMode(true);
		    car.setAngleRelative(true);
		    car.setPathAngularVelocity(2*Math.PI/15.0);
		    car.setPathAngularAcceleration(0.0);
		}
	    },
	    animation.getTicks(7.5));

	animation.scheduleCall(new Callable() {
		public void call() {
		    System.out.println("leaving skidmode");
		    car.setSkidMode(false);
		}
	    },
	    animation.getTicks(11.0));

	animation.run(maxSimtime);
	
    }
}
