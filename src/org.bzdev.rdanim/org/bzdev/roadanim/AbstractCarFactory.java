package org.bzdev.roadanim;
import org.bzdev.anim2d.*;
import org.bzdev.obnaming.Parm;
import org.bzdev.obnaming.ParmParser;
import org.bzdev.obnaming.annotations.*;
import org.bzdev.lang.Callable;
import org.bzdev.obnaming.misc.ColorParm;
import java.awt.Color;
import java.util.Map;
import java.util.HashMap;

/**
 * Abstract Factory for creating instances of Car.
 * <P>
 * The factory parameters this factory provides are the same as the parameters
 * provided by its subclass {@link CarFactory}:
 * <P>
 * <IFRAME SRC="{@docRoot}/factories-api/org/bzdev/roadanim/CarFactory.html" style="width:95;height:600px;border: 3px solid steelblue">
 * Please see
 *  <A HREF="{@docRoot}/factories-api/org/bzdev/roadanim/CarFactory.html">
 *    the parameter documentation</A> for a table of the parameters supported
 * by this factory.
 * </IFRAME>
 */
@FactoryParmManager(value="AbstractCarFactoryParmManager",
		    labelResourceBundle="*.lpack.CarLabels",
		    tipResourceBundle="*.lpack.CarTips",
		    docResourceBundle = "*.lpack.CarDocs",
		    stdFactory="CarFactory",
		    namerVariable="a2d",
		    namerDocumentation="the animation")
public abstract class AbstractCarFactory <Obj extends Car>
    extends DirectedObject2DFactory<Obj>
{
    Animation2D animation;

    @PrimitiveParm(value = "length", lowerBound = "0.0",
		   lowerBoundClosed  = false)
	double length = 3.0;

    @PrimitiveParm("lookString") String lookString = "Look";
    @PrimitiveParm("lookAngle") double lookAngle = 0.0;
    @PrimitiveParm("looking") boolean looking = false;

    final class DARK_YELLOW extends ColorParm {
	public DARK_YELLOW() {super(Color.YELLOW.darker());}
    }

    @CompoundParm("lookingFontColor") ColorParm lookingFontColor =
	new DARK_YELLOW();

    @PrimitiveParm(value = "width", lowerBound = "0.0",
		   lowerBoundClosed = false)
	double width = 1.5;
    @PrimitiveParm(value = "hoodLength", lowerBound = "0.0",
		   lowerBoundClosed  = false)
	double hoodLength = 1.0;

    @PrimitiveParm(value = "doorLength", lowerBound = "0.0",
		   lowerBoundClosed = false)
    double doorLength = 1.0;

    @PrimitiveParm(value = "doorWidth", lowerBound = "0.0",
		   lowerBoundClosed = false)
    double doorWidth = 0.15;

    @PrimitiveParm(value = "skidMode")
	boolean skidMode = false;

    @PrimitiveParm("reverseMode") boolean reverseMode = false;

    @PrimitiveParm(value = "windshieldLength", lowerBound = "0.0",
		   lowerBoundClosed  = false)
	double windshieldLength = 0.7;
    @PrimitiveParm(value = "rearOffset", lowerBound = "0.0",
		   lowerBoundClosed  = true)
	double rearOffset = 0.3;

    @CompoundParm("color") ColorParm cparm = new ColorParm.RED();
    @CompoundParm("windshieldColor") ColorParm wscparm
	= new ColorParm.WHITE();

    @PrimitiveParm(value="driverOffsetX") double driverX = 0.3;
    @PrimitiveParm(value="driverOffsetY") double driverY = 0.3;

    @PrimitiveParm("leftBlindSpotOffset") double leftBlindSpotOffset = 0.05;
    @PrimitiveParm("rightBlindSpotOffset") double rightBlindSpotOffset = 0.05;

    @PrimitiveParm(value="leftAngularWidth", lowerBound="0.0",
		   upperBound="135.0", lowerBoundClosed=false,
		   upperBoundClosed = true)
    double phiLeftDeg = 3.0;
    @PrimitiveParm(value="rightAngularWidth", lowerBound="0.0",
		   upperBound="135.0", lowerBoundClosed=false,
		   upperBoundClosed = true)
    double phiRightDeg = 3.0;
    @PrimitiveParm(value="leftBlindSpotLength", lowerBound="0.0")
    double leftBlindSpotLength = 5.0;
    @PrimitiveParm(value="rightBlindSpotLength", lowerBound="0.0")
    double rightBlindSpotLength = 5.0;

    static final class BLIND_SPOT_COLOR extends ColorParm {
	public BLIND_SPOT_COLOR() {super(Car.BLIND_SPOT_COLOR_DEFAULT);}
    }

    @CompoundParm("blindSpotColor") ColorParm blindSpotColor =
	new BLIND_SPOT_COLOR();

    @PrimitiveParm("leftBlindSpotVisible") boolean leftBlindSpotVisible=false;
    @PrimitiveParm("rightBlindSpotVisible") boolean rightBlindSpotVisible=false;


    @CompoundParmType(tipResourceBundle = "*.lpack.CarTimelineTips",
		      labelResourceBundle = "*.lpack.CarTimelineLabels",
		      docResourceBundle = "*.lpack.CarTimelineDocs")
    static class TimelineEntry {
	@PrimitiveParm("leftDoorMode") Boolean leftDoorMode  = null;
	@PrimitiveParm(value = "leftDoorRate", lowerBound = "0.0",
		       lowerBoundClosed = true)
	    Double leftDoorRate = null;
	@PrimitiveParm(value = "leftDoorMaxAngle", lowerBound = "0.0",
		       lowerBoundClosed = true)
	    Double leftDoorMaxAngle = null;
	@PrimitiveParm("rightDoorMode") Boolean rightDoorMode  = null;
	@PrimitiveParm(value = "rightDoorRate", lowerBound = "0.0",
		       lowerBoundClosed = true)
	    Double rightDoorRate = null;
	@PrimitiveParm(value = "rightDoorMaxAngle", lowerBound = "0.0",
		       lowerBoundClosed = true)
	    Double rightDoorMaxAngle = null;
	@PrimitiveParm("lookAngle") Double lookAngle = null;
	@PrimitiveParm("looking") Boolean looking = null;
	@PrimitiveParm("lookString") String lookString = null;
	@PrimitiveParm("skidMode") Boolean skidMode = null;
	@PrimitiveParm("reverseMode") Boolean reverseMode = null;
	@PrimitiveParm(value="lookingFontColor.red",
		       lowerBound="0", upperBound="255")
	    Integer red = null;
	@PrimitiveParm(value="lookingFontColor.green",
		       lowerBound="0", upperBound="255")
	    Integer green = null;
	@PrimitiveParm(value="lookingFontColor.blue",
		       lowerBound="0", upperBound="255")
	    Integer blue = null;
	@PrimitiveParm(value="lookingFontColor.alpha",
		       lowerBound="0", upperBound="255")
	    Integer alpha = null;

	@PrimitiveParm(value="driverOffsetX") Double driverX = null;
	@PrimitiveParm(value="driverOffsetY") Double driverY = null;
	@PrimitiveParm(value="leftBlindSpotLength", lowerBound="0.0")
	Double leftBlindSpotLength = null;;
	@PrimitiveParm(value="rightBlindSpotLength", lowerBound="0.0")
	Double rightBlindSpotLength = null;;
	@PrimitiveParm("leftBlindSpotVisible")
	Boolean leftBlindSpotVisible = null;
	@PrimitiveParm("rightBlindSpotVisible")
	Boolean rightBlindSpotVisible = null;
    };

    @KeyedCompoundParm("timeline")
    Map<Integer,TimelineEntry> timelineMap =
	new HashMap<Integer,TimelineEntry>();

    AbstractCarFactoryParmManager<Obj> pm;

    /**
     * Constructor.
     * @param a2d the animation
     */
    protected AbstractCarFactory(Animation2D a2d) {
	super(a2d);
	animation = a2d;
	pm = new AbstractCarFactoryParmManager<Obj>(this);
	initParms(pm, AbstractCarFactory.class);
	// Need a custom parameter to clear timeline.lookingFontColor
	// entries. It will not add an entry as it represents multiple values.
	initParm(new Parm("timeline.lookingFontColor",
			 int.class, null,
			 new ParmParser() {
			     public void clear(int key) {
				 TimelineEntry tle = timelineMap.get(key);
				 if (tle != null) {
				     tle.red = null;
				     tle.green = null;
				     tle.blue = null;
				     tle.alpha = null;
				 }
			     }
			 },
			 null,
			  null, true, null, true),
		 AbstractCarFactory.class);
    }

    @Override
    public void clear() {
	super.clear();
	pm.setDefaults(this);
    }

    @Override
    protected void addToTimelineRequest(Obj object, int key,
					double time)
    {
	super.addToTimelineRequest(object, key, time);
	final TimelineEntry tle = timelineMap.get(key);
	if (tle != null) {
	    final Obj obj = object;
	    final Double lookAngle = tle.lookAngle;
	    final String lookString = tle.lookString;
	    final Boolean looking = tle.looking;
	    final Boolean skidMode = tle.skidMode;
	    final Boolean reverseMode = tle.reverseMode;
	    final Color fontColor = (tle.red == null && tle.green == null
				     && tle.blue == null && tle.alpha == null)?
		null: new Color(((tle.red == null)? 0: tle.red),
				((tle.green == null)? 0: tle.green),
				((tle.blue == null)? 0: tle.blue),
				((tle.alpha == null)? 255: tle.alpha));

	    final Boolean leftDoorMode = tle.leftDoorMode;
	    final Double leftDoorRate = tle.leftDoorRate;
	    final Double leftDoorMaxAngle = tle.leftDoorMaxAngle;

	    final Boolean rightDoorMode = tle.rightDoorMode;
	    final Double rightDoorRate = tle.rightDoorRate;
	    final Double rightDoorMaxAngle = tle.rightDoorMaxAngle;

	    final Double dX = tle.driverX;
	    final Double dY = tle.driverY;
	    final Double leftBSL = tle.leftBlindSpotLength;
	    final Double rightBSL = tle.rightBlindSpotLength;
	    final Boolean leftBSVis = tle.leftBlindSpotVisible;
	    final Boolean rightBSVis = tle.rightBlindSpotVisible;

	    addToTimelineResponse(new Callable() {
		    public void call() {
			if (leftDoorMode != null) {
			    obj.setLeftDoorMode(leftDoorMode);
			}
			if (leftDoorRate != null) {
			    obj.setLeftDoorRate(Math.toRadians(leftDoorRate));
			}
			if (leftDoorMaxAngle != null) {
			    obj.setLeftDoorMaxAngle
				(Math.toRadians(leftDoorMaxAngle));
			}
			if (rightDoorMode != null) {
			    obj.setRightDoorMode(rightDoorMode);
			}
			if (rightDoorRate != null) {
			    obj.setRightDoorRate(Math.toRadians(rightDoorRate));
			}
			if (rightDoorMaxAngle != null) {
			    obj.setRightDoorMaxAngle
				(Math.toRadians(rightDoorMaxAngle));
			}
			if (lookString != null) {
			    obj.setLookString(lookString);
			}
			if (lookAngle != null) {
			    double la = Math.toRadians(lookAngle);
			    obj.setLookAngle(la);
			}
			if (looking != null) {
			    obj.setLooking(looking);
			}
			if (fontColor != null) {
			    obj.setLookParams(fontColor);
			}
			if (dX != null && dY != null) {
			    obj.setDriverPosition(dX,dY);
			} else if (dX != null) {
			    obj.setDriverX(dX);
			} else if (dY != null) {
			    obj.setDriverY(dY);
			}
			if (leftBSL != null) {
			    obj.setLeftBlindSpotLength(leftBSL);
			}
			if (rightBSL != null) {
			    obj.setLeftBlindSpotLength(rightBSL);
			}
			if (leftBSVis != null) {
			    obj.setLeftBlindSpotVisible(leftBSVis);
			}
			if (rightBSVis != null) {
			    obj.setLeftBlindSpotVisible(rightBSVis);
			}
			if (skidMode != null) {
			    obj.setSkidMode(skidMode);
			}
			if (reverseMode != null) {
			    obj.setReverseMode(reverseMode);
			}
		    }
		});
	}
    }

    @Override
    protected void initObject(Obj object) {
	super.initObject(object);
	Color color = cparm.createColor();
	Color windshieldColor = wscparm.createColor();
	Color lookColor = lookingFontColor.createColor();
	object.configure(length, width, hoodLength, windshieldLength,
			 rearOffset, color, windshieldColor,
			 doorLength, doorWidth);
	object.setReverseMode(reverseMode);
	object.setLookParams(lookColor);
	object.setLooking(looking);
	object.setLookAngle(Math.toRadians(lookAngle));
	object.setLookString(lookString);
	object.setDriverPosition(driverX, driverY);
	object.setLeftBlindSpotOffset(leftBlindSpotOffset);
	object.setRightBlindSpotOffset(rightBlindSpotOffset);
	object.setLeftBlindSpotExtent(Math.toRadians(phiLeftDeg));
	object.setRightBlindSpotExtent(Math.toRadians(phiRightDeg));
	object.setLeftBlindSpotLength(leftBlindSpotLength);
	object.setRightBlindSpotLength(rightBlindSpotLength);
	object.setBlindSpotColor(blindSpotColor.createColor());
	object.setLeftBlindSpotVisible(leftBlindSpotVisible);
	object.setRightBlindSpotVisible(rightBlindSpotVisible);
   }
}

//  LocalWords:  CarFactory IFRAME SRC HREF lookString lookAngle
//  LocalWords:  AbstractCarFactoryParmManager lookingFontColor
//  LocalWords:  hoodLength doorLength doorWidth skidMode reverseMode
//  LocalWords:  windshieldLength rearOffset windshieldColor
//  LocalWords:  leftDoorMode leftDoorRate leftDoorMaxAngle
//  LocalWords:  rightDoorMode rightDoorRate rightDoorMaxAngle
