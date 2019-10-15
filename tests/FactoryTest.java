import org.bzdev.roadanim.*;
import org.bzdev.anim2d.*;
import java.io.File;


public class FactoryTest {

    public static void main(String argv[]) throws Exception {
	Animation2D animation = new Animation2D();
	animation.setRanges(0.0, 0.0, 0.5, 0.5, 25.0*2, 25.0*2);
	
	CarFactory cf = new CarFactory(animation);
	BicycleFactory bf = new BicycleFactory(animation);

	cf.set("rightBlindSpotVisible", true);


	cf.set("timeline.time", 0, 1.0);
	cf.set("timeline.lookingFontColor.red", 0, 255);
	bf.set("timeline.time", 0, 1.0);
	bf.set("timeline.lookingFontColor.red", 0, 255);
	/*
	 * This test verifies that we can unset the font color.
	 * With the red component gone, the text will be green
	 * (due to a following setting) whereas if the red
	 * component is kept, the text will be yellow.
	 */
	cf.unset("timeline.lookingFontColor", 0);
	bf.unset("timeline.lookingFontColor", 0);

	bf.set("initialY", 5.0);
	bf.set("visible", true);
	cf.set("visible", true);
	cf.set("timeline.lookingFontColor.green", 0, 255);
	cf.set("timeline.looking", 0, true);
	cf.set("timeline.leftBlindSpotVisible", 0, true);
	bf.set("timeline.lookingFontColor.green", 0, 255);
	bf.set("timeline.looking", 0, true);

	cf.set("timeline.time", 1, 1.5);
	cf.set("timeline.driverOffsetY", 1, 0.2);
	cf.set("timeline.leftBlindSpotLength", 1, 10.0);

	bf.createObject("bike");
	cf.createObject("car");

	File dir = new File("ftmp");
	dir.mkdirs();
	for (File file: dir.listFiles()) {
	    file.delete();
	}

	int maxFrames = animation.estimateFrameCount(2.0);
	animation.initFrames(maxFrames, "ftmp/col-", "png");
	animation.scheduleFrames(0, maxFrames);
	animation.run();
    }
}
