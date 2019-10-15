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


public class BlindSpotTest {

    public static void main(String argv[]) throws Exception {


    	Animation2D animation = new Animation2D();
	animation.setRanges(0.0, 0.0, 0.5, 0.5, 25.0, 25.0);

	AnimatedPanelGraphics apg =
	    AnimatedPanelGraphics.newFramedInstance(animation,
						    "Blind-Spot Test",
						    true, true,
						    null);


	final Car car = new Car(animation, "car", true);
	car.configure(3.0, 1.5, 1.0, 0.5, 0.25, Color.RED);
	car.setRefPointByName(RefPointName.CENTER_RIGHT);
	car.setZorder(1, true);

	car.setPosition(0.0, 0.0, 0.0);

	animation.scheduleCall(new Callable() {
		public void call() {
		    car.setLeftBlindSpotVisible(true);
		}
	    }, animation.getTicks(1.0));

	animation.scheduleCall(new Callable() {
		public void call() {
		    car.setRightBlindSpotVisible(true);
		    car.setLeftBlindSpotLength(10.0);
		}
	    }, animation.getTicks(2.0));

	animation.scheduleCall(new Callable() {
		public void call() {
		    car.setDriverPosition(0.3, 0.4);
		}
	    }, animation.getTicks(3.0));


	int maxframes = animation.estimateFrameCount(7.0);

	animation.initFrames(maxframes, apg);
       
	animation.scheduleFrames(0, maxframes);

	animation.run();

	apg.close();

    }

}
