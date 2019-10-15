import org.bzdev.roadanim.*;
import org.bzdev.anim2d.*;
import org.bzdev.geom.*;
import org.bzdev.graphs.RefPointName;
import org.bzdev.lang.Callable;

import java.io.File;
import java.awt.*;
import java.awt.geom.*;

public class PedTest {

    public static void main(String argv[]) throws Exception {
	Animation2D animation = new Animation2D();
	animation.setRanges(0.0, 0.0, 0.5, 0.5, 50.0, 50.0);

	AnimationPath2DFactory pf = new AnimationPath2DFactory(animation);

	pf.set("cpoint.type", 0, "MOVE_TO");
	pf.set("cpoint.x", 0, -10.0);
        pf.set("cpoint.y", 0, -5.0);
	pf.set("cpoint.type", 1, "CONTROL");
	pf.set("cpoint.x", 1, 10.0);
	pf.set("cpoint.y", 1, -5.0);
	pf.set("cpoint.type", 2, "SEG_END");
	pf.set("cpoint.x", 2, 10.0);
	pf.set("cpoint.y", 2, 5.0);

	AnimationPath2D apath = pf.createObject("path");

	final Pedestrian pedestrian =
	    new Pedestrian(animation, "ped1", true);
	pedestrian.configure(null, null, null, 1.0);

	pedestrian.setZorder(1, true);
	double length = apath.getPathLength();
	double speed = length / 10.0;

	int maxFrames = animation.estimateFrameCount(15.0);
	int maxSimtime = 15000;

	animation.initFrames(maxFrames, "ptmp/col-", "png");
	animation.scheduleFrames(0, maxFrames);
	File dir = new File("ptmp");
	dir.mkdirs();
	for (File file: dir.listFiles()) {
	    file.delete();
	}

	pedestrian.setPath(apath, 0.0);
	pedestrian.setPathVelocity(speed);
	pedestrian.setPathAcceleration(0.0);
	pedestrian.setAngleRelative(true);

	final Pedestrian ped2 = new Pedestrian(animation, "ped2", true);
	ped2.setZorder(1, true);
	ped2.setPosition(0.0, 0.0, 0.0);


	animation.scheduleCall(new Callable() {
		public void call() {
		    pedestrian.clearPath();
		    ped2.setVisible(false);
		    pedestrian.setPosition(pedestrian.getX(),
					   pedestrian.getY(),
					   Math.toRadians(135.0));
		}
	    },
	    animation.getTicks(12.0));


	animation.run(maxSimtime);
    }
}
