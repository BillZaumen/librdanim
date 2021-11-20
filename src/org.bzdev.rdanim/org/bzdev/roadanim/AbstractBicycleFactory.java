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
 * Abstract factory to create instances of Bicycle.
 * <P>
 * The factory parameters this factory provides are the same as the parameters
 * provided by its subclass {@link BicycleFactory}:
 * <P>
 * <IFRAME SRC="{@docRoot}/factories-api/org/bzdev/roadanim/BicycleFactory.html" style="width:95%;height:600px;border: 3px solid steelblue">
 * Please see
 *  <A HREF="{@docRoot}/factories-api/org/bzdev/roadanim/BicycleFactory.html">
 *    the parameter documentation</A> for a table of the parameters supported
 * by this factory.
 * </IFRAME>
 */


@FactoryParmManager(value="AbstractBicycleFactoryParmManager",
		    labelResourceBundle="*.lpack.BicycleLabels",
		    tipResourceBundle="*.lpack.BicycleTips",
		    docResourceBundle="*.lpack.BicycleDocs",
		    stdFactory="BicycleFactory",
		    namerVariable="a2d",
		    namerDocumentation="the animation")
public abstract class AbstractBicycleFactory <Obj extends Bicycle>
    extends DirectedObject2DFactory<Obj>
{
    Animation2D animation;

    static final class YELLOW extends ColorParm {
	public YELLOW() {super(Color.YELLOW);}
    }

    @CompoundParm("helmetColor") ColorParm hcparm = new YELLOW();
    @CompoundParm("bicycleColor") ColorParm bcparm = new ColorParm.RED();
    @PrimitiveParm("lookString") String lookString = "Look";
    @PrimitiveParm("helmetAngle") double helmetAngle = 0.0;
    @PrimitiveParm("looking") boolean looking = false;

    static final class LOOKING_FONT_COLOR  extends ColorParm {
	public LOOKING_FONT_COLOR() {super(Color.YELLOW.darker());}
    }

    @CompoundParm("lookingFontColor") ColorParm lookingFontColor =
	new LOOKING_FONT_COLOR();

    @PrimitiveParm(value = "bikeHead",
		   lowerBound = "0.0", lowerBoundClosed = false)
	double bikeHead = Bicycle.DEFAULT_BIKEHEAD;
    @PrimitiveParm(value = "bikeTail",
		   lowerBound = "0.0", lowerBoundClosed = false)
	double bikeTail = Bicycle.DEFAULT_BIKETAIL;
    @PrimitiveParm(value = "bikeWidth",
		   lowerBound = "0.0", lowerBoundClosed = false)
	double bikeWidth = Bicycle.DEFAULT_BIKEWIDTH;
    @PrimitiveParm(value = "handlebarPosition",
		   lowerBound = "0.0", lowerBoundClosed = false)
	Double handlebarPosition = null;
    @PrimitiveParm(value = "handlebarHalfLength",
		   lowerBound = "0.0", lowerBoundClosed = false)
	double handlebarHalfLength = Bicycle.DEFAULT_HANDLEBARHALFLENGTH;
    @PrimitiveParm(value = "handlebarTail",
		   lowerBound = "0.0", lowerBoundClosed = false)
	double handlebarTail= Bicycle.DEFAULT_HANDLEBARTAIL;

    @PrimitiveParm(value = "helmetScaleFactor",
		   lowerBound = "0.0", lowerBoundClosed = false)
	double helmetScaleFactor = 1.0;

    @PrimitiveParm(value = "lookStringScaleFactor",
		   lowerBound = "1.0", lowerBoundClosed = false)
	double lookStringScaleFactor = 1.0;

    @CompoundParmType(tipResourceBundle = "*.lpack.BicycleTimelineTips",
		      labelResourceBundle = "*.lpack.BicycleTimelineLabels",
		      docResourceBundle="*.lpack.BicycleTimelineDocs")
    static class TimelineEntry {
	@PrimitiveParm("helmetAngle") Double helmetAngle = null;
	@PrimitiveParm("looking") Boolean looking = null;
	@PrimitiveParm("lookString") String lookString = null;
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
    }

    @KeyedCompoundParm("timeline")
    Map<Integer,TimelineEntry> timelineMap =
	new HashMap<Integer,TimelineEntry>();

    AbstractBicycleFactoryParmManager<Obj> pm;

    /**
     * Constructor.
     * @param a2d the animation
     */
    protected AbstractBicycleFactory(Animation2D a2d) {
	super(a2d);
	animation = a2d;
	pm = new AbstractBicycleFactoryParmManager<Obj>(this);
	initParms(pm, AbstractBicycleFactory.class);
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
	TimelineEntry tle = timelineMap.get(key);
	if (tle != null) {
	    final Obj obj = object;
	    final Double helmetAngle = tle.helmetAngle;
	    final String lookString = tle.lookString;
	    final Boolean looking = tle.looking;
	    final Color fontColor = (tle.red == null && tle.green == null
				     && tle.blue == null && tle.alpha == null)?
		null: new Color(((tle.red == null)? 0: tle.red),
				((tle.green == null)? 0: tle.green),
				((tle.blue == null)? 0: tle.blue),
				((tle.alpha == null)? 255: tle.alpha));
	   
	    addToTimelineResponse(new Callable() {
		    public void call() {
			if (fontColor != null) {
			    obj.setLookParams(fontColor);
			}
			if (lookString != null) {
			    obj.setLookString(lookString);
			}
			if (helmetAngle != null) {
			    double ha =  Math.toRadians(helmetAngle);
			    obj.setHelmetAngle(ha);
			}
			if (looking != null) {
			    obj.setLooking(looking);
			}
		    }
		});
	}
    }

    @Override
    protected void initObject(Obj object) {
	super.initObject(object);
	Color helmetColor = hcparm.createColor();
	Color bikeColor = bcparm.createColor();
	Color lookColor = lookingFontColor.createColor();
	double hbposition = (handlebarPosition == null)? bikeHead/2.0:
	    handlebarPosition.doubleValue();
	object.configure(helmetColor, bikeColor,
			 bikeHead, bikeTail, bikeWidth,
			 hbposition,
			 handlebarHalfLength,
			 handlebarTail,
			 helmetScaleFactor,
			 lookStringScaleFactor);
	object.setLookParams(lookColor);
	object.setLooking(looking);
	object.setHelmetAngle(Math.toRadians(helmetAngle));
	object.setLookString(lookString);
    }
}

//  LocalWords:  BicycleFactory IFRAME SRC HREF helmetColor bikeHead
//  LocalWords:  AbstractBicycleFactoryParmManager bicycleColor
//  LocalWords:  lookString helmetAngle lookingFontColor bikeTail
//  LocalWords:  bikeWidth handlebarPosition handlebarHalfLength
//  LocalWords:  handlebarTail helmetScaleFactor
