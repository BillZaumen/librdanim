import org.bzdev.roadanim.*;
import org.bzdev.anim2d.*;
import org.bzdev.devqsim.TraceSet;
import org.bzdev.geom.*;
import org.bzdev.graphs.RefPointName;
import org.bzdev.lang.Callable;
import org.bzdev.swing.AnimatedPanelGraphics;

import java.io.File;
import java.awt.*;
import java.awt.geom.*;


public class DoorFactoryTest {

    public static void main(String argv[]) throws Exception {


    	Animation2D animation = new Animation2D();
	animation.setRanges(0.0, 0.0, 0.5, 0.5, 25.0, 25.0);

	AnimatedPanelGraphics apg =
	    AnimatedPanelGraphics.newFramedInstance(animation,
						    "Door Factory Test",
						    true, true,
						    null);

	CarFactory cf = new CarFactory(animation);

	cf.set("doorLength", 1.0);
	cf.set("doorWidth", 0.15);

	cf.set("refPointMode", "BY_NAME");
	cf.set("refPointName", "CENTER_RIGHT");
	cf.set("visible", true);
	cf.set("zorder", 1);

	cf.set("initialX", 0.0);
	cf.set("initialY", 0.0);

	cf.set("timeline.time", 0, 1.0);
	cf.set("timeline.leftDoorMode", 0, true);

	cf.set("timeline.time", 1, 2.0);
	cf.set("timeline.rightDoorMode",  1, true);

	cf.set("timeline.time", 2, 3.0);
	cf.set("timeline.leftDoorMode",  2, false);

	cf.set("timeline.time", 3, 5.0);
	cf.set("timeline.rightDoorMode",  3, false);

	cf.createObject("car"); 

	int maxframes = animation.estimateFrameCount(7.0);

	animation.initFrames(maxframes, apg);
       
	animation.scheduleFrames(0, maxframes);

	animation.run();

	apg.close();

    }
}
