import org.bzdev.roadanim.*;
import org.bzdev.anim2d.*;
import org.bzdev.geom.*;
import org.bzdev.graphs.RefPointName;
import org.bzdev.lang.Callable;
import org.bzdev.devqsim.TraceSet;

import java.io.File;
import java.awt.*;
import java.awt.geom.*;

public class BikeTest {

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

	final Bicycle bicycle = new Bicycle(animation, "bicycle", true);
	bicycle.configure(Color.YELLOW, Color.RED);
	bicycle.setZorder(1, true);

	double dx = bicycle.getSeatXGCS();
	double dy = bicycle.getSeatYGCS();
	double cx = bicycle.getX();
	double cy = bicycle.getY();
	System.out.format("cx = %g, cy = %g\n", cx, cy);
	System.out.format("dx = %g, dy = %g\n", dx, dy);
	System.out.println("sep = " + Point2D.distance(dx, dy, cx, cy));

	// final Bicycle bicycle2 = new Bicycle(animation,"bicycle2", true);
	// bicycle2.configure(Color.YELLOW, Color.RED, 2.0);

	double length = 2.0 * r * Math.PI;

	double speed = length / 15.0;
	int maxFrames = animation.estimateFrameCount(15.0);
	int maxSimtime = 15000;

	animation.initFrames(maxFrames, "btmp/col-", "png");
	animation.scheduleFrames(0, maxFrames);
	File dir = new File("btmp");
	dir.mkdirs();
	for (File file: dir.listFiles()) {
	    file.delete();
	}

	Animation2D.setTraceLevels(0,1,2,3);
	animation.setTraceOutput(System.out);
	TraceSet traceSet = new TraceSet(animation, "traceSet", true);
	traceSet.setLevel(0);

	bicycle.addTraceSet(traceSet);
	bicycle.setPath(apath, 0.0);
	bicycle.setPathVelocity(speed);
	bicycle.setPathAcceleration(0.0);


	BicycleFactory bf = new BicycleFactory(animation);
	bf.add("traceSets", traceSet);
	bf.set("helmetScaleFactor", 1.5);
	bf.set("visible", false);
	bf.set("timeline.time", 0, 0.0);
	bf.set("timeline.path", 0, apath);
	bf.set("timeline.t0", 0, 3.0);
	bf.set("timeline.velocity", 0, speed);
	bf.set("timeline.acceleration", 0, 0.0);
	bf.set("timeline.time", 1, 2.9);
	bf.set("timeline.visible", 1, true);
	final Bicycle bicycle2 = bf.createObject("bicycle2");


	bf.set("helmetScaleFactor", 1.5);
	bf.set("lookStringScaleFactor", 2.0);
	bf.set("visible", false);
	bf.set("timeline.time", 0, 0.0);
	bf.set("timeline.path", 0, apath);
	bf.set("timeline.t0", 0, 6.0);
	bf.set("timeline.velocity", 0, speed);
	bf.set("timeline.acceleration", 0, 0.0);
	bf.set("timeline.time", 1, 5.9);
	bf.set("timeline.visible", 1, true);
	final Bicycle bicycle3 = bf.createObject("bicycle3");

	// bicycle2.setPath(apath, 3.0);
	// bicycle2.setPathVelocity(speed);
	// bicycle2.setPathAcceleration(0.0);
	// bicycle2.setVisible(false);

	/*
	animation.scheduleCall(new Callable() {
		public void call() {
		    bicycle2.setVisible(true);
		}
	    },
	    animation.getTicks(2.9));
	*/

	animation.scheduleCall(new Callable() {
		public void call() {
		    double dx = bicycle.getSeatXGCS();
		    double dy = bicycle.getSeatYGCS();
		    double cx = bicycle.getX();
		    double cy = bicycle.getY();
		    System.out.format("cx = %g, cy = %g\n", cx, cy);
		    System.out.format("dx = %g, dy = %g\n", dx, dy);
		    System.out.println("sep = "
				       + Point2D.distance(dx, dy, cx, cy));
		    bicycle.setHelmetAngle(Math.toRadians(135.0));
		    bicycle2.setHelmetAngle(Math.toRadians(135.0));
		}
	    },
	    animation.getTicks(7.5));

	animation.scheduleCall(new Callable() {
		public void call() {
		    double dx = bicycle.getSeatXGCS();
		    double dy = bicycle.getSeatYGCS();
		    double cx = bicycle.getX();
		    double cy = bicycle.getY();
		    System.out.format("cx = %g, cy = %g\n", cx, cy);
		    System.out.format("dx = %g, dy = %g\n", dx, dy);
		    System.out.println("sep = "
				       + Point2D.distance(dx, dy, cx, cy));
		    bicycle.setLooking(true);
		    bicycle2.setLooking(true);
		    bicycle3.setLooking(true);
		}
	    },
	    animation.getTicks(8.5));

	animation.scheduleCall(new Callable() {
		public void call() {
		    double dx = bicycle.getSeatXGCS();
		    double dy = bicycle.getSeatYGCS();
		    double cx = bicycle.getX();
		    double cy = bicycle.getY();
		    System.out.format("cx = %g, cy = %g\n", cx, cy);
		    System.out.format("dx = %g, dy = %g\n", dx, dy);
		    System.out.println("sep = "
				       + Point2D.distance(dx, dy, cx, cy));
		    bicycle.setLooking(false);
		    bicycle2.setLooking(false);
		    bicycle3.setLooking(false);
		}
	    },
	    animation.getTicks(11.5));

	animation.scheduleCall(new Callable() {
		public void call() {
		    double dx = bicycle.getSeatXGCS();
		    double dy = bicycle.getSeatYGCS();
		    double cx = bicycle.getX();
		    double cy = bicycle.getY();
		    System.out.format("cx = %g, cy = %g\n", cx, cy);
		    System.out.format("dx = %g, dy = %g\n", dx, dy);
		    System.out.println("sep = "
				       + Point2D.distance(dx, dy, cx, cy));
		    bicycle.setHelmetAngle(Math.toRadians(0.0));
		    bicycle2.setHelmetAngle(Math.toRadians(0.0));
		    bicycle3.setHelmetAngle(Math.toRadians(0.0));
		}
	    },
	    animation.getTicks(13.0));

	animation.run(maxSimtime);
    }
}
