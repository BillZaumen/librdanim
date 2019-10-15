import org.bzdev.roadanim.*;
import org.bzdev.anim2d.*;
import org.bzdev.geom.*;
import org.bzdev.graphs.RefPointName;

import java.io.File;
import java.awt.*;
import java.awt.geom.*;

public class CarTest2 {

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

	Car car = new Car(animation, "car", true);
	car.configure(3.0, 1.5, 1.0, 0.5, 0.25, Color.RED);
	car.setRefPointByName(RefPointName.CENTER_LEFT);
	car.setZorder(1, true);

	double length = 2.0 * r * Math.PI;

	double speed = length / 15.0;
	int maxFrames = animation.estimateFrameCount(15.0);
	int maxSimtime = 15000;

	animation.initFrames(maxFrames, "rtmp/col-", "png");
	animation.scheduleFrames(0, maxFrames);
	File dir = new File("rtmp");
	dir.mkdirs();
	for (File file: dir.listFiles()) {
	    file.delete();
	}


	car.setPath(apath, 0.0, Math.toRadians(45.0), true, 0.0);
	car.setPathVelocity(speed);
	car.setPathAcceleration(0.0);

	animation.run(maxSimtime);
	
    }
}
