package org.bzdev.roadanim;
import org.bzdev.anim2d.*;
import org.bzdev.obnaming.annotations.*;
import org.bzdev.lang.Callable;
import org.bzdev.obnaming.misc.ColorParm;
import java.awt.Color;
import java.util.Map;
import java.util.HashMap;

/**
 * Abstract factory for creating instances of Pedestrian.
 * <P>
 * The factory parameters this factory provides are the same as the parameters
 * provided by its subclass {@link PedestrianFactory}:
 * <IFRAME SRC="{@docRoot}/factories-api/org/bzdev/roadanim/PedestrianFactory.html" style="width:95%;height:600px;border: 3px solid steelblue">
 * Please see
 *  <A HREF="{@docRoot}/factories-api/org/bzdev/roadanim/PedestrianFactory.html">
 *    the parameter documentation</A> for a table of the parameters supported
 * by this factory.
 * </IFRAME>
 */

@FactoryParmManager(value="AbstractPedFactoryParmManager",
		    labelResourceBundle="*.lpack.PedestrianLabels",
		    tipResourceBundle="*.lpack.PedestrianTips",
		    stdFactory="PedestrianFactory",
		    namerVariable="a2d",
		    namerDocumentation="the animation")
public abstract class AbstractPedFactory <Obj extends Pedestrian>
    extends DirectedObject2DFactory<Obj>
{
    Animation2D animation;

    static final class EYE_COLOR extends ColorParm {
	public EYE_COLOR() {super(Pedestrian.EYE_COLOR);}
    }
    static final class HAIR_COLOR extends ColorParm {
	public HAIR_COLOR() {super(Pedestrian.HAIR_COLOR);}
    }
    static final class FACE_COLOR extends ColorParm {
	public FACE_COLOR() {super(Pedestrian.FACE_COLOR);}
    }

    @CompoundParm("eyeColor") ColorParm ecparm = new EYE_COLOR();
    @CompoundParm("hairColor") ColorParm hcparm = new HAIR_COLOR();
    @CompoundParm("faceColor") ColorParm fcparm = new FACE_COLOR();

    @PrimitiveParm(value="radius",
		   lowerBound = "0.0",
		   lowerBoundClosed = false)
	double radius = Pedestrian.DEFAULT_RADIUS;
 
    AbstractPedFactoryParmManager<Obj> pm;

    /**
     * Constructor.
     * @param a2d the animation
     */
    protected AbstractPedFactory(Animation2D a2d) {
	super(a2d);
	animation = a2d;
	pm = new AbstractPedFactoryParmManager<Obj>(this);
	initParms(pm, AbstractPedFactory.class);
    }

    @Override
    public void clear() {
	super.clear();
	pm.setDefaults(this);
    }
    
 
    @Override
    protected void initObject(Obj object) {
	super.initObject(object);
	Color eyeColor = ecparm.createColor();
	Color hairColor = hcparm.createColor();
	Color faceColor = fcparm.createColor();
	object.configure(hairColor, faceColor, eyeColor, radius);
    }
}

//  LocalWords:  PedestrianFactory IFRAME SRC HREF eyeColor hairColor
//  LocalWords:  AbstractPedFactoryParmManager faceColor
