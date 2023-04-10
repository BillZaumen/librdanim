package org.bzdev.roadanim;

import org.bzdev.anim2d.*;
import org.bzdev.graphs.*;
import org.bzdev.geom.*;
import org.bzdev.math.RungeKutta;
import org.bzdev.math.RealValuedFunction;
import org.bzdev.math.RealValuedFunctOps;
import org.bzdev.devqsim.SimFunction;

import java.awt.*;
import java.awt.geom.*;

/**
 * Class representing a car.
 *
 * Starting at the center line of the car, let L be the distance from
 * the rear wheels to the front wheels or some other reference point,
 * with the distance measured perpendicular to the wheel base.  If the
 * front wheels move a distance &Delta;s at an angle with the X axis
 * of &theta;<sub>t</sub>, and if the angle from a line perpendicular
 * to the rear axis and pointing towards the front of the car and with
 * the X axis is &phi;, then if the front wheels move a distance
 * &Delta;x, the rear wheels (which have to move perpendicular to
 * their axis), will move a distance &Delta;x cos (&theta;<sub>t</sub>
 * -&phi;) in the direction of the line perpendicular to the rear
 * axis. Similar, the distance perpendicular to this line is
 * &Delta;s sin(&phi; - &theta;<sub>t</sub>).  This distance is
 * positive when phi is decreasing, so the change in the angle &phi;
 * is therefore -(&Delta;s) sin (&theta;<sub>t</sub> -&phi;) / L.
 * As &Delta;s goes to zero, one obtains the following differential
 * equation
 * <P>
 * d&phi;/ds = - (sin (&phi; - &theta;<sub>t</sub>)) / L
 * <P>
 * The angle &theta;<sub>t</sub> can be computed using the
 * {@link org.bzdev.geom.BasicSplinePath2D} methods
 * {@link org.bzdev.geom.BasicSplinePath2D#dxDu(double)} and
 * {@link org.bzdev.geom.BasicSplinePath2D#dyDu(double)} together
 * with {@link java.lang.Math#atan2(double,double)}, with u determined
 * by the {@link org.bzdev.geom.BasicSplinePath2D} method
 * {@link org.bzdev.geom.BasicSplinePath2D#u(double) u(s)}.
 * To have the vehicle oriented so that it
 * moves backwards with increasing values of s, the differential
 * equation is
 * <P>
 * d&phi;/ds = (sin (&phi; - &theta;<sub>t</sub>))/L
 * <P>
 * and again the sign will change if the vehicle is moving in the
 * direction of decreasing s.
 */
public class Car extends DirectedObject2D {

    double length;
    double width;
    double hoodLength;
    double windshieldLength;
    double rearOffset;

    // default door dimensions are 1 meter long and
    // 15 cm thick.
    static final double DEFAULT_DOOR_LENGTH = 1.0;
    static final double DEFAULT_DOOR_WIDTH = 0.15;

    // A door angle is the angle between the door and
    // the car's side, measured from a vertex at the
    // door hinge.  The  minimum value is 0.0 and
    // the maximum value is Math.PI.

    double doorLength = 0.0;
    double doorWidth = 0.0;

    double leftDoorAngle = 0.0;
    double rightDoorAngle = 0.0;
    double leftDoorRate = Math.PI/2; // 90 degrees per second
    double rightDoorRate = Math.PI/2; // 90 degrees per second
    double leftDoorMaxAngle = Math.PI/2;
    double rightDoorMaxAngle = Math.PI/2;

    /**
     * Get the length of the vehicle.
     * @return the length of the vehicle in meters
     */
    public double getLength() {return length;}

    /**
     * Get the width of the vehicle.
     * @return the width of the vehicle in meters
     */
    public double getWidth() {return width;}

    /**
     * Get the length of the hood.
     * @return the length of the hood in meters
     */
    public double getHoodLength() {return hoodLength;}

    /**
     * Get the length of the windshield.
     * The length is measure horizontally along the vehicle's axis
     * (parallel to its direction of motion when moving in a straight
     *  line).
     * @return the length of the windshield in meters
     */
    public double getWindshieldLength() {return windshieldLength;}

    /**
     * Get the distance from the rear of the vehicle to the rear wheels.
     * @return the distance from the rear of the vehicle to the rear wheels
     *         in meters
     */
    public double getRearOffset() {
	return rearOffset;
    }

    /**
     * Get the length of a door.
     * @return the length of the door
     */
    public double getDoorLength() {
	return (doorLength == 0.0)? DEFAULT_DOOR_LENGTH: doorLength;
    }

    /**
     * Get the width of a door.
     * @return the width of a door.
     */
    public double getDoorWidth() {
	return (doorWidth == 0.0)? DEFAULT_DOOR_WIDTH: doorWidth;
    }


    double ell;

    private void setEll() {
	ell = getReferencePointX() + length/2.0 - rearOffset;
	if (ell < 0) ell = -ell;
    }

    static final Color DEFAULT_COLOR = Color.RED;

    Color color /*= DEFAULT_COLOR */;

    /**
     * Get the color of the vehicle.
     * @return the color of the vehicle
     */
    public Color getColor() {return color;}

    static final Color DEFAULT_WINDSHIELD_COLOR = Color.WHITE;

    Color windshieldColor = DEFAULT_WINDSHIELD_COLOR;

    /**
     * Get the color of the windshield.
     * The default is Color.WHITE, but in some cases it is useful
     * to change it (e.g., for a white vehicle).
     * @return the color of the windshield
     */
    public Color getWindshieldColor() {return windshieldColor;}



    private boolean noSkidMode = true;
    private boolean configured = false;

    /**
     * Set skid mode on or off.
     * When skid mode is off (the default), an angle function is
     * used that will prevent the car from "fishtailing" as it follows
     * a path. Otherwise the angle the car makes with a path is
     * determined by a user-specified angle function, or alternatively
     * an angular velocity and angular acceleration, and whether the angle
     * is absolute or relative (see
     * {@link org.bzdev.anim2d.DirectedObject2D#setAngleRelative(boolean)}).
     * @param mode true if skid mode is on; false if it is off
     */

    public void setSkidMode(boolean mode) {
	mode = !mode;
	boolean prevMode = noSkidMode;
	if (mode == prevMode) return;
	update();
	noSkidMode = mode;
	trace(RoadAnimation.level1, "skid mode set to %b", !mode);
	if (noSkidMode) {
	    double s = getS();
	    if (!Double.isNaN(s)) {
		double angle=getAngle();
		if (RoadAnimation.level1 > -1) {
		    trace(RoadAnimation.level1,
			  "initializing Runge-Kutta algorithm: s=%g, \u03d5=%g"
			  + " degrees", s, Math.toDegrees(angle));
		}
		rk.setInitialValues(s,angle);
	    }
	    super.setPathAngleByF(noSkidAngleF);
	} else {
	    if (savedPathAngleSF != null) {
		super.setPathAngleBySF(savedPathAngleSF);
	    } else {
		super.setPathAngleByF(savedPathAngleF);
	    }
	}
    }

    /**
     * Determine if skid mode is turned on.
     * @return true of skid mode is in effect; false otherwise
     */
    public boolean getSkidMode() {
	return !noSkidMode;
    }

    boolean reverse = false;


    /**
     * Set reverse mode on or off.
     * When reverse mode is on, and one of the car's setPath
     * methods is called with the relative-angle flag set to true
     * (the default for a car), the car's angle will be flipped by
     * 180 degrees.  In the case where the angle (relative to the tangent)
     * is 0, the rear of the car will point in the same direction as the
     * path's tangent vector.
     * <P>
     * This method should not be called after setPath is called and
     * before the car reaches its final point along the path: otherwise
     * the behavior may be erratic.
     * @param reverse true to turn reverse mode on; false to turn it off.
     */
    public void setReverseMode(boolean reverse) {
	update();
	this.reverse = reverse;
	trace(RoadAnimation.level1, "reverse mode set to %b", reverse);
    }

    /**
     * Determine if reverse mode is turned on.
     * When reverse mode is on, and one of the car's setPath
     * methods is called with the relative-angle flag set to true
     * (the default for a car), the car's angle will be flipped by
     * 180 degrees.  In the case where the angle (relative to the tangent)
     * is 0, the rear of the car will point in the same direction as the
     * path's tangent vector.
     * @return true if reverse mode is in effect; false otherwise.
     */
    public boolean getReverseMode() {
	return reverse;
    }

    double lastT = 0.0;		// for noSkidAngleF
    double lastS = Double.NaN;	// for determining Runge Kutta step size.

    RungeKutta rk = new RungeKutta() {
	    protected double function(double sx, double phi) {
		if (!isClosedPath()) {
		    // avoid going past the end of the path due to
		    // round-off errors.
		    if (sx < 0.0) sx = 0.0;
		    double plen = getPathLength();
		    if (sx > plen) sx = plen;
		}
		double u = getU(sx);
		double tangent = Math.atan2(getPathDyDu(u), getPathDxDu(u));
		/*
		System.out.format("\ts = %g: u = %g, phi = %g, theta_t = %g\n",
				  sx, u, Math.toDegrees(phi),
				  Math.toDegrees(tangent));
		*/
		boolean test = reverse;
		// When the path velocidy is negative, s is decreasing
		// but the Runge Kutta method assumes the path parameter
		// is increasing, so we have to flip the sign when the
		// path velocity is negative.
		if (getPathVelocity() < 0.0) test = !test;
		double result = (test /*reverse*/)? Math.sin(phi-tangent)/ell:
		    (-Math.sin(phi-tangent)/ell);
		if (RoadAnimation.level4 > -1) {
		    trace(RoadAnimation.level4,
			  "Runge-Kutta function: s = %g, u = %g, \u03d5=%g"
			  + " degrees, \u03b8 = %g degrees, returns %g"
			  + " (degrees per sec)",
			  sx, u,
			  Math.toDegrees(phi), Math.toDegrees(tangent),
			  Math.toDegrees(result));
		}
		return result;
	    }
	};

    private static final int NSCALE = 16;

    private final double getCorrectedAngle(double angle) {
	double result;
	if (angleIsRelative()) {
	    double u = getU();
	    result =  angle - Math.atan2(getPathDyDu(u), getPathDxDu(u));
	} else {
	    result = angle;
	}
	if (RoadAnimation.level3 > -1) {
	    trace(RoadAnimation.level3,
		  "no-skid angle function returning %g degrees",
		  Math.toDegrees(result));
	}
	return result;
    }

    RealValuedFunction noSkidAngleF = new RealValuedFunction() {
	    public double valueAt(double t) {
		if (!pathExists()) {
		    // if the path does not yet exist, we just return
		    // the current angle.
		    return Car.this.getAngle();
		} else if (t < 0.0) {
		    // if the path exists, but it is too early to
		    // move along it, return the current angle,
		    // corrected for whether the angle is relative or not.
		    // (remember that t is the the time increment from
		    // when the pass traversal starts, not the
		    // absolute time).
		    double angle = Car.this.getAngle();
		    return getCorrectedAngle(angle);
		} else {
		    double u1 = getU(lastS);
		    double u2 = getU();
		    double sval = lastS;
		    if (u1 == u2) {
			return getCorrectedAngle(rk.getValue());
		    }
		    double du = u2 - u1;
		    double xn1 = ((u2 - u1) < 0.0)? Math.floor(u1):
			Math.ceil(u1);
		    double xn2 = ((u2 - u1) < 0.0)? Math.ceil(u2):
			Math.floor(u2);

		    if (Math.abs(du) < Math.abs(xn1-u1)) {
			double h = (getS()-lastS);
			int nscale = (int)(NSCALE*Math.rint(h/ell));
			if (nscale < NSCALE) nscale = NSCALE;
			rk.update(h, nscale);
			sval += h;
			return getCorrectedAngle(rk.getValue());
		    }
		    int n1 = (int)Math.round(xn1);
		    int n2 = (int)Math.round(xn2);

		    if (u1 != xn1) {
			double h = (getS(xn1) - lastS);
			int nscale = (int)(NSCALE*Math.rint(h/ell));
			if (nscale < NSCALE) nscale = NSCALE;
			rk.update(h, nscale);
			sval += h;
		    }
		    int deltaN = n1 < n2? 1: -1;
		    for (int i = n1; i != n2; i += deltaN) {
			double h =
			    (getS(i+deltaN) - getS((double)i));
			int nscale = (int)(NSCALE*Math.rint(h/ell));
			if (nscale < NSCALE) nscale = NSCALE;
			rk.update(h, nscale);
			sval += h;
		    }
		    if (u2 != xn2) {
			double h =(getS() - getS(xn2));
			int nscale = (int)(NSCALE*Math.rint(h/ell));
			if (nscale < NSCALE) nscale = NSCALE;
			rk.update(h, nscale);
			sval += h;
		    }
		    return getCorrectedAngle(rk.getValue());
		}
	    }
	};

    @Override
    protected void update(double t, long simtime) {
	lastS = getS();
	super.update(t, simtime);
    }

    /**
     * Set a path for this object to follow, specifying an initial
     * path parameter and angle.
     * The object's position will held at the start of the path
     * until the object's time passes time0. The object's
     * angle will match that of a tangent to the path at the object's
     * reference point's current location.
     * The path angle function will be set to null, as will the corresponding
     * simulation function. If one is desired, it should be reset
     * after setPath is called.
     * <P>
     * Skid mode is recognized by this method. The implementation for
     * skid mode installs a path-angle function, but keeps a copy of
     * the previous one and restores it when skid mode is turned off.
     * This method sets the saved value to null as the normal behavior
     * of this method is to set the path angle function to null. A
     * path simulation function is treated similarly.
     * <P>
     * Subclasses that modify how paths are processed should override
     * this method and in most cases call super.setPathImplementation and,
     * of course, document any changes in behavior.
     * @param path the path the object will follow
     * @param u0 the path's parameter at a time equal to time0
     * @param angle the initial angle
     * @param angleRelative true if the angle is measured relative to
     *        path's tangent; false if the angle is absolute
     * @param time0 the increment from the current time
     *        at which the object starts moving along the path
     */
    @Override
    protected void setPathImplementation(BasicSplinePath2D path,
					 double u0, double angle,
					 boolean angleRelative,
					 double time0)
    {
	if (reverse && angleRelative) {
	    angle += (angle <= 0.0)? Math.PI: -Math.PI;
	}
	super.setPathImplementation(path, u0, angle, angleRelative, time0);
	savedPathAngleF = null;
	savedPathAngleSF = null;
	if (noSkidMode) {
	    double sx = path.s(u0);
	    double ourAngle = getAngle();
	    if (RoadAnimation.level1 > -1) {
		trace(RoadAnimation.level1,
		      "setPath initializing Runge-Kutta algorithm: "
		      + "s=%g, \u03d5=%g"
		      + " degrees", sx, Math.toDegrees(ourAngle));
	    }
	    rk.setInitialValues(sx, ourAngle);
	    super.setPathAngleByF(noSkidAngleF);
	}
    }

    @Override
    public void clearPath() {
	super.clearPath();
	savedPathAngleF = null;
	savedPathAngleSF = null;
    }

    /**
     * Constructor.
     * @param a2d the animation
     * @param name the name of this object
     * @param intern true if the object is interned in the animation's
     *        name space; false otherwise.
     */
    public Car(Animation2D a2d, String name, boolean intern) {
	super(a2d, name, intern);
    }

    /**
     * Initialize the Car.
     * All lengths are measured on a two-dimensional surface so that
     * for any component (e.g., a windshield) that is slanted vertically,
     * the length of the projection onto the x-y plane must be used.
     * The door widths and lengths are set to zero to indicate that
     * default values should be used.
     * @param length the length of the car in meters
     * @param width the width of the car in meters
     * @param hoodLength the length of the hood in meters
     * @param windshieldLength length the length of the windshield in meters
     *        (measured horizontally)
     * @param rearOffset distance from the rear of the car to the rear
     *        wheels
     * @param color the color of the car
     * @exception IllegalStateException attempt to change parameters after
     *            the car was configured
     */
    public void configure(double length, double width,
			  double hoodLength, double windshieldLength,
			  double rearOffset,
			  Color color)
	throws IllegalStateException
    {
	configure(length, width, hoodLength, windshieldLength,
		  rearOffset, color, 0.0, 0.0);
    }
    /**
     * Initialize the Car, specifying door dimensions.
     * All lengths are measured on a two-dimensional surface so that
     * for any component (e.g., a windshield) that is slanted vertically,
     * the length of the projection onto the x-y plane must be used.
     * @param length the length of the car in meters
     * @param width the width of the car in meters
     * @param hoodLength the length of the hood in meters
     * @param windshieldLength length the length of the windshield in meters
     *        (measured horizontally)
     * @param rearOffset distance from the rear of the car to the rear
     *        wheels
     * @param color the color of the car
     * @param doorLength the length of the door in meters
     * @param doorWidth the width (thickness) of the door in meters
     * @exception IllegalStateException attempt to change parameters after
     *            the car was configured
     */
    public void configure(double length, double width,
			  double hoodLength, double windshieldLength,
			  double rearOffset,
			  Color color,
			  double doorLength, double doorWidth)
	throws IllegalStateException
    {
	if (configured) {
	    if (this.length == length && this.width == width
		&& this.hoodLength == hoodLength
		&& this.windshieldLength == windshieldLength
		&& this.rearOffset == rearOffset
		&& this.color.equals(color)
		&& this.doorLength == doorLength
		&& this.doorWidth == doorWidth) {
		// don't throw an exception if we call it with
		// the same arguments used in the first call.
		return;
	    } else {
		throw new IllegalStateException("already configured");
	    }
	}
	setRefPointBounds(length, width);
	this.length = length;
	this.width = width;
	this.hoodLength = hoodLength;
	this.windshieldLength = windshieldLength;
	this.rearOffset = rearOffset;
	this.color = color;
	this.doorLength = doorLength;
	this.doorWidth = doorWidth;
	configured = true;
	setupBlindSpots();
    }

    /**
     * Initialize the object, also specifying a color for the windshield.
     * All lengths are measured on a two-dimensional surface so that
     * for any component (e.g., a windshield) that is slanted vertically,
     * the length of the projection onto the x-y plane must be used.
     * @param length the length of the car in meters
     * @param width the width of the car in meters
     * @param hoodLength the length of the hood in meters
     * @param windshieldLength length the length of the windshield in meters
     *        (measured horizontally)
     * @param rearOffset distance from the rear of the car to the rear
     *        wheels
     * @param color the color of the car
     * @param windshieldColor the color to use to represent the
     *        windshield
     * @exception IllegalStateException attempt to change parameters after
     *            the car was configured
     */
    public void configure(double length, double width,
			  double hoodLength, double windshieldLength,
			  double rearOffset,
			  Color color, Color windshieldColor)
	throws IllegalStateException
    {
	if (configured) {
	    if (!this.windshieldColor.equals(windshieldColor)) {
		throw new IllegalStateException("already configured");
	    }
	}
	configure(length, width, hoodLength, windshieldLength,
		  rearOffset, color);
	if (windshieldColor == null) windshieldColor = color.WHITE;
	this.windshieldColor = windshieldColor;
	setupBlindSpots();
    }

    /**
     * Initialize the object, also specifying a color for the windshield.
     * All lengths are measured on a two-dimensional surface so that
     * for any component (e.g., a windshield) that is slanted vertically,
     * the length of the projection onto the x-y plane must be used.
     * @param length the length of the car in meters
     * @param width the width of the car in meters
     * @param hoodLength the length of the hood in meters
     * @param windshieldLength length the length of the windshield in meters
     *        (measured horizontally)
     * @param rearOffset distance from the rear of the car to the rear
     *        wheels
     * @param color the color of the car
     * @param windshieldColor the color to use to represent the
     *        windshield
     * @param doorLength the length of the door in meters
     * @param doorWidth the width (thickness) of the door in meters
     * @exception IllegalStateException attempt to change parameters after
     *            the car was configured
     */
    public void configure(double length, double width,
			  double hoodLength, double windshieldLength,
			  double rearOffset,
			  Color color, Color windshieldColor,
			  double doorLength, double doorWidth)
	throws IllegalStateException
    {
	if (configured) {
	    if (!this.windshieldColor.equals(windshieldColor)) {
		throw new IllegalStateException("already configured");
	    }
	}
	configure(length, width, hoodLength, windshieldLength,
		  rearOffset, color, doorLength, doorWidth);
	if (windshieldColor == null) {
	    windshieldColor = DEFAULT_WINDSHIELD_COLOR;
	}
	this.windshieldColor = windshieldColor;
	setupBlindSpots();
    }


    @Override
    public void setRefPointByName(RefPointName loc) {
	super.setRefPointByName(loc);
	setEll();
    }

    @Override
    public void setRefPoint(double x, double y) {
	super.setRefPoint(x, y);
	setEll();
    }

    @Override
    public void setRefPointByFraction(double xf, double yf) {
	super.setRefPointByFraction(xf, yf);
	setEll();
    }

    // SimFunction savedAngleSF = null;
    // RealValuedFunction savedAngleF = null;

    SimFunction savedPathAngleSF = null;
    RealValuedFunctOps savedPathAngleF = null;

    @Override
    public void setPathAngleByF(RealValuedFunctOps f) {
	if (noSkidMode) {
	    savedPathAngleF = f;
	    savedPathAngleSF = null;
	    // savedAngleF = null;
	    // savedAngleSF = null;
	} else {
	    savedPathAngleF = f;
	    savedPathAngleSF = null;
	    // savedAngleF = null;
	    // savedAngleSF = null;
	    super.setPathAngleByF(f);
	}
    }

    @Override
    public void setPathAngleBySF(SimFunction f) {
	if (noSkidMode) {
	    savedPathAngleF = (f == null)? null: f.getFunction();
	    savedPathAngleSF = f;
	    // savedAngleF = null;
	    // savedAngleSF = null;
	} else {
	    savedPathAngleF = (f == null)? null: f.getFunction();
	    savedPathAngleSF = f;
	    // savedAngleF = null;
	    // savedAngleSF = null;
	    super.setPathAngleBySF(f);
	}
    }

    boolean leftDoorOpen = false;
    boolean rightDoorOpen = false;
    long leftDoorTicks = 0;
    long rightDoorTicks = 0;

    /**
     * Set the mode for the door.
     * After being set, the door's position will adjust,
     * constrained by its angular speed.
     * @param mode true if the door is open; false if closed.
     */
    public void setLeftDoorMode(boolean mode) {
	if (leftDoorOpen == mode) return;
	leftDoorTicks = getAnimation().currentTicks();
	leftDoorOpen = mode;
	if (RoadAnimation.level1 > -1) {
	    trace(RoadAnimation.level1,
		  "left-door-open mode set to %b", leftDoorOpen);
	}
    }

    /**
     * Get the left-door mode.
     * @return true if the left door is configured to be open;
     *         false otherwise
     */
    public boolean getLeftDoorMode() {return leftDoorOpen;}


    /**
     * Set the angular speed (absolute value of the angular velocity)
     * at which a door opens or closes.
     * @param angularSpeed the absolute value of the angular velocity
     *        while the door is moving in units of radians per second
     * @exception IllegalArgumentException the argument was negative
     */
    public void setLeftDoorRate(double angularSpeed)
	throws IllegalArgumentException
    {
	if (angularSpeed < 0.0) throw new IllegalArgumentException();
	leftDoorRate = angularSpeed;
    }

    /**
     * Get the angular rate at which the left door moves.
     * @return the angular rate in radians per second
     */
    public double getLeftDoorRate() {
	return leftDoorRate;
    }


    /**
     * Set the maximum door angle for the left door.
     * The value must be in the range [0.0, &pi;).
     * @param angle the angle in radians
     * @exception IllegalArgumentException the angle was out of range
     */
    public void setLeftDoorMaxAngle(double angle)
	throws IllegalArgumentException
    {
	if (angle < 0.0 || angle >= Math.PI) {
	    throw new IllegalArgumentException();
	}
	leftDoorMaxAngle = angle;
    }

    /**
     * Get the maximum angle for the left door when open.
     * @return the angle in radians
     */
    public double getLeftDoorMaxAngle() {
	return leftDoorMaxAngle;
    }

    /**
     * Set the mode for the door.
     * After being set, the door's position will adjust,
     * constrained by its angular speed.
     * @param mode true if the door is open; false if closed.
     */
    public void setRightDoorMode(boolean mode) {
	if (rightDoorOpen == mode) return;
	rightDoorTicks = getAnimation().currentTicks();
	rightDoorOpen = mode;
	if (RoadAnimation.level1 > -1) {
	    trace(RoadAnimation.level1,
		  "right-door-open mode set to %b", rightDoorOpen);
	}
    }

    /**
     * Get the right-door mode.
     * @return true if the right door is configured to be open;
     *         false otherwise
     */
    public boolean getRightDoorMode() {return rightDoorOpen;}

    /**
     * Set the angular speed (absolute value of the angular velocity)
     * at which a door opens or closes.
     * @param angularSpeed the absolute value of the angular velocity
     *        while the door is moving in units of radians per second
     * @exception IllegalArgumentException the argument was negative
     */
    public void setRightDoorRate(double angularSpeed)
	throws IllegalArgumentException
    {
	if (angularSpeed < 0.0) throw new IllegalArgumentException();
	rightDoorRate = angularSpeed;
    }

    /**
     * Get the angular rate at which the right door moves.
     * @return the angular rate in radians per second
     */
    public double getRightDoorRate() {
	return rightDoorRate;
    }


    /**
     * Set the maximum door angle for the right door.
     * The value must be in the range [0.0, &pi;).
     * @param angle the angle in radians
     * @exception IllegalArgumentException the angle was out of range
     */
    public void setRightDoorMaxAngle(double angle)
	throws IllegalArgumentException
    {
	if (angle < 0.0 || angle >= Math.PI) {
	    throw new IllegalArgumentException();
	}
	rightDoorMaxAngle = angle;
    }

    /**
     * Get the maximum angle for the right door when open.
     * @return the angle in radians
     */
    public double getRightDoorMaxAngle() {
	return rightDoorMaxAngle;
    }


    private static double DEFAULT_LOOKANGLE = 0.0;

    double lookAngle = DEFAULT_LOOKANGLE;

    /**
     * Set the angle the driver looks relative to the car
     * An angle of zero points in the direction in which the car
     * is pointed, and angles are measured counterclockwise in radians
     * @param angle the angle
     */
    public void setLookAngle(double angle) {
	lookAngle = angle;
	if (RoadAnimation.level2 > -1) {
	    trace(RoadAnimation.level2,
		  "driver 'look' angle set to %g degrees",
		  Math.toDegrees(lookAngle));
	}
    }

    boolean looking = false;
    /**
     * Set whether or not the word "Look", or the value provided by
     * the method setLookString, should be displayed in
     * front of the driver's location.
     * @param value true if the string should be displayed, false otherwise.
     */
    public void setLooking(boolean value) {
	looking = value;
	trace(RoadAnimation.level1, "look mode set to %b", looking);
    }

    Graph.FontParms lookFontParms = new Graph.FontParms();

    /**
     * Set the font color for a "looking" indication.
     * @param color the text color
     */
    public void setLookParams(Color color) {
	if (color == null) {
	    color = Color.YELLOW.darker();
	}
	if (color != null) lookFontParms.setColor(color);
	lookFontParms.setJustification(Graph.Just.CENTER);
	lookFontParms.setBaseline(Graph.BLineP.BOTTOM);
    }

    static String LOOK_DEFAULT = "Look";

    String look = LOOK_DEFAULT;
    
    /**
     * Get the string to display when setLooking is given the argument
     * 'true'.
     * @return the string to display
     */
    public String getLookString() {return look;}

    /**
     * Set the string to display when setLooking is given the argument
     * 'true'.
     * @param string the string to display; null or an empty string
     *        for the default (the string "Look")
     */
    public void setLookString(String string) {
	look = (string == null || string.length() == 0)? LOOK_DEFAULT: string;
	trace(RoadAnimation.level1, "car 'look' string set to \"%s\"",
	      look);
    }

    private double driverX = 0.3;
    private double driverY = 0.3;
    private double leftBlindSpotOffset = 0.05;
    private double rightBlindSpotOffset = 0.05;;
    private double thetaLeft;
    private double phiLeft = Math.toRadians(7.5);
    private double thetaRight;
    private double phiRight = Math.toRadians(3.0);
    private double leftBlindSpotLength = 5.0;
    private double rightBlindSpotLength = 6.0;
    private boolean leftBlindSpotVisible = false;
    private boolean rightBlindSpotVisible = false;

    private double leftBSx1, leftBSy1, leftBSx2, leftBSy2;
    private double rightBSx1, rightBSy1, rightBSx2, rightBSy2;

    private void setupBlindSpots() {
	thetaLeft =
	    Math.atan2(driverY,
		       driverX + windshieldLength - leftBlindSpotOffset);

	double cosTheta = Math.cos(thetaLeft);
	double sinTheta = Math.sin(thetaLeft);
	double xc = leftBlindSpotLength*cosTheta;
	double yc = leftBlindSpotLength*sinTheta;
	double hw = leftBlindSpotLength*Math.tan(phiLeft/2.0);
	leftBSx1 = xc + hw*sinTheta;
	leftBSy1 = yc - hw*cosTheta;
	leftBSx2 = xc - hw*sinTheta;
	leftBSy2 = yc + hw*cosTheta;

	thetaRight =
	    Math.atan2(width - driverY,
		       driverX + windshieldLength - rightBlindSpotOffset);

	cosTheta = Math.cos(thetaRight);
	sinTheta = Math.sin(-thetaRight);
	xc = rightBlindSpotLength*cosTheta;
	yc = rightBlindSpotLength*sinTheta;
	hw = rightBlindSpotLength*Math.tan(phiRight/2.0);
	rightBSx1 = xc + hw*sinTheta;
	rightBSy1 = yc - hw*cosTheta;
	rightBSx2 = xc - hw*sinTheta;
	rightBSy2 = yc + hw*cosTheta;
    }

    static final Color BLIND_SPOT_COLOR_DEFAULT =
	new Color(255,255,128,128);

    Color blindSpotColor = BLIND_SPOT_COLOR_DEFAULT;

    /**
     * Set the position of the driver.
     * This method is used when blind spots are displayed, as the
     * blind spots depend on the driver's location in the vehicle.
     * @param x the distance in meters from the top of the windshield,
     *          towards the rear of the car
     * @param y the distance in meters from the left side of the car,
     *          towards the right side
     */
    public void setDriverPosition(double x, double y) {
	trace(RoadAnimation.level1, "driver position set to (%g, %g)", x, y );
	driverX = x;
	driverY = y;
	setupBlindSpots();
    }

    /**
     * Set the X coordinate of the driver.
     * This method is used when blind spots are displayed, as the
     * blind spots depend on the driver's location in the vehicle.
     * It is provided primarily because factories may have to set
     * the X and Y components of the driver's position indpendently.
     * @param x the distance in meters from the top of the windshield,
     *          towards the rear of the car
     * @see #setDriverPosition(double,double)
     */
    public void setDriverX(double x) {
	driverX = x;
	trace(RoadAnimation.level1, "driver position set to (%g, %g)",
	      driverX, driverY );
	setupBlindSpots();
    }

    /**
     * Return the X coordinate of a driver's position in graph coordinate
     * space.
     * @return the X coordinate in graph coordinate space
     */
    public double getDriverXGCS() {
	AffineTransform af = getAddToTransform();
	double y2 =  width/2.0;
	double y1 = - width/2.0;
	double xr = - length/2.0;
	double xf =  length/2.0;
	double xws2 = xf - hoodLength;
	double xws1 = xws2 - windshieldLength;
	double dx = xws1 - driverX;
	double dy = y2 - driverY;
	double tmp[] = new double[4];
	tmp[0] = dx; tmp[1] = dy;
	af.transform(tmp, 0, tmp, 2, 1);
	return tmp[2];
    }

    /**
     * Return the Y coordinate of a driver's position in graph coordinate
     * space.
     * @return the Y coordinate in graph coordinate space
     */
    public double getDriverYGCS() {
	AffineTransform af = getAddToTransform();
	double y2 =  width/2.0;
	double y1 = - width/2.0;
	double xr = - length/2.0;
	double xf =  length/2.0;
	double xws2 = xf - hoodLength;
	double xws1 = xws2 - windshieldLength;
	double dx = xws1 - driverX;
	double dy = y2 - driverY;
	double tmp[] = new double[4];
	tmp[0] = dx; tmp[1] = dy;
	af.transform(tmp, 0, tmp, 2, 1);
	return tmp[3];
    }

    /**
     * Set the Y coordinate of the driver.
     * This method is used when blind spots are displayed, as the
     * blind spots depend on the driver's location in the vehicle.
     * It is provided primarily because factories may have to set
     * the X and Y components of the driver's position indpendently.
     * @param y the distance in meters from the left side of the car,
     *          towards the right side
     * @see #setDriverPosition(double,double)
     */
    public void setDriverY(double y) {
	driverY = y;
	trace(RoadAnimation.level1, "driver position set to (%g, %g)",
	      driverX, driverY );
	setupBlindSpots();
    }


    /**
     * Get the horizontal distance to the driver from the top of the
     * windshield.
     * @return the distance in meters.
     */
    public double getDriverX() {return driverX;}

    /**
     * Get the  distance to the driver from the left side of the car.
     * @return the distance in meters.
     */
    public double getDriverY() {return driverY;}

    /**
     * Set the left blind spot angular extent.
     * The extent (angular width) is in units of radians.
     * @param phiLeft the angular width of the left blind spot
     */
    public void setLeftBlindSpotExtent(double phiLeft) {
	trace(RoadAnimation.level1,
	      "setting left blind-spot extent: %g  (degrees)",
	      Math.toDegrees(phiLeft));
	this.phiLeft = phiLeft;
	setupBlindSpots();
    }

    /**
     * Get the left blind-spot angle.
     * @return the left blind-spot angle in radians
     */
    public double getLeftBlindSpotAngle() {
	return thetaLeft;
    }

    /**
     * Get the left blind-spot angular extent.
     * @return the left blind-spot angular width in radians
     */
    public double getLeftBlindSpotExtent() {
	return phiLeft;
    }

    /**
     * Set the right blind spot angular extent.
     * The angular extend (angular width) of the blind spot is
     * in units of radians.
     * @param phiRight the angular width of the right blind spot
     */
    public void setRightBlindSpotExtent(double phiRight) {
	trace(RoadAnimation.level1,
	      "setting right blind-spot angle and width: %g, %g  (degrees)",
	      Math.toDegrees(thetaRight), Math.toDegrees(phiRight));
	this.thetaRight = thetaRight;
	this.phiRight = phiRight;
	setupBlindSpots();
    }

    /**
     * Get the right blind-spot angle.
     * @return the right blind-spot angle in radians
     */
    public double getRightBlindSpotAngle() {
	return thetaRight;
    }

    /**
     * Get the right blind-spot angular extent.
     * @return the right blind-spot angular width in radians
     */
    public double getRightBlindSpotExtent() {
	return phiRight;
    }

    /**
     * Set the left blind-spot offset.
     * The center of the blind spot is located at an angle
     * &theta; = arctan (y/x) where y = driverY,
     * x = driverX + windshieldLength - offset, and the angle
     * is measured counterclockwise from the car's centerline.
     * @param offset the offset in units of meters
     */
    public void setLeftBlindSpotOffset(double offset) {
	leftBlindSpotOffset = offset;
    }

    /**
     * Get the left blind-spot offset.
     * The center of the blind spot is located at an angle
     * &theta; = arctan (y/x) where y = driverY,
     * x = driverX + windshieldLength - offset, and the angle
     * is measured counterclockwise from the car's centerline.
     * @return the offset in units of meters.
     */
    public double getLeftBlindSpotOffset() {
	return leftBlindSpotOffset;
    }

    /**
     * Set the right blind-spot offset.
     * The center of the blind spot is located at an angle
     * &theta; = arctan (y/x) where y = width - driverY,
     * x = driverX + windshieldLength - offset, and  the
     * angle is measured clockwise from the car's centerline.
     * @param offset the offset in units of meters
     */
    public void setRightBlindSpotOffset(double offset) {
	rightBlindSpotOffset = offset;
    }

    /**
     * Get the right blind-spot offset.
     * The center of the blind spot is located at an angle
     * &theta; = arctan (y/x) where y = width - driverY,
     * x = driverX + windshieldLength - offset, and  the
     * angle is measured clockwise from the car's centerline.
     * @return the offest in units of meters
     */
    public double getRightBlindSpotOffset() {
	return rightBlindSpotOffset;
    }

    /**
     * Set the length of the left blind spot as displayed.
     * The length is the height of the triangle used to depict the
     * blind spots.
     * @param leftLength the length in meters for the left blind spot
     */
    public void setLeftBlindSpotLength(double leftLength) {
	trace(RoadAnimation.level1,
	      "setting left blind-spot length: %g",
	      leftLength);
	leftBlindSpotLength = leftLength;
	setupBlindSpots();
    }

    /**
     * Set the length of the left blind spot as displayed.
     * The length is the height of the triangle used to depict the
     * blind spots.
     * @param rightLength the length in meters for the right blind spot
     */
    public void setRightBlindSpotLength(double rightLength) {
	trace(RoadAnimation.level1,
	      "setting left blind-spot length: %g",
	      rightLength);
	rightBlindSpotLength = rightLength;
	setupBlindSpots();
    }

    /**
     * Get the left blind-spot length.
     * This length is the height of the triangle used to display the
     * blind spot.
     * @return the length in meters.
     */
    public double getLeftBlindSpotLength() {
	return leftBlindSpotLength;
    }

    /**
     * Get the right blind-spot length.
     * This length is the height of the triangle used to display the
     * blind spot.
     * @return the length in meters.
     */
    public double getRightBlindSpotLength() {
	return rightBlindSpotLength;
    }

    /**
     * Set the blind spot color.
     * @param c the color; null for a default
     */
    public void setBlindSpotColor(Color c) {
	if (c == null) {
	    blindSpotColor = BLIND_SPOT_COLOR_DEFAULT;
	} else {
	    blindSpotColor = c;
	}
	trace(RoadAnimation.level1, "setting blind-spot color to %s",
	      blindSpotColor);
    }

    /**
     * Get the color used to display a blind spot.
     * @return the color
     */
    public Color getBlindSpotColor() {
	return blindSpotColor;
    }

    /**
     * Set whether or not the left blind spot is displayed.
     * @param visibility true if the blind spot is displayed; false if
     *        the blind spot is not displayed
     */
    public void setLeftBlindSpotVisible(boolean visibility) {
	trace(RoadAnimation.level1,
	      "setting left blind-spot visibility to %b", visibility);
	leftBlindSpotVisible = visibility;
    }


    /**
     * Determine if the left blind spot should be displayed.
     * @return true of the left blind spot should be displayed; false if
     *         the left blind spot should not be displayed
     */
    public boolean getLeftBlindSpotVisible() {
	return  leftBlindSpotVisible;
    }

    /**
     * Set whether or not the right blind spot is displayed.
     * @param visibility true if the blind spot is displayed; false if
     *        the blind spot is not displayed
     */
    public void setRightBlindSpotVisible(boolean visibility) {
	trace(RoadAnimation.level1,
	      "setting right blind-spot visibility to %b", visibility);
	rightBlindSpotVisible = visibility;
    }

    /**
     * Determine if the right blind spot should be displayed.
     * @return true of the right blind spot should be displayed; false if
     *         the right blind spot should not be displayed
     */
    public boolean getRightBlindSpotVisible() {
	return  rightBlindSpotVisible;
    }


    // Specified by interface.
    @Override
    public void addTo(Graph graph, Graphics2D g2d, Graphics2D g2dGcS) {
	double angle = getAngle();
	/*
	System.out.format("%s time = %g: x = %g, y = %g, angle = %g\n",
			  getName(),
			  getAnimation().currentTime(),
			  getX(), getY(), Math.toDegrees(getAngle()));
	*/
	double y2 =  width/2.0;
	double y1 = - width/2.0;
	double xr = - length/2.0;
	double xf =  length/2.0;
	double xws2 = xf - hoodLength;
	double xws1 = xws2 - windshieldLength;
	double dx = xws1 - driverX;
	double dy = y2 - driverY;

	AffineTransform af = getAddToTransform();

	Shape leftDoor = null;
	Shape rightDoor = null;
	boolean showLeftDoor = leftDoorOpen;
	boolean showRightDoor = rightDoorOpen;

	if (leftDoorOpen) {
	    if (leftDoorAngle < leftDoorMaxAngle) {
		long ticks = getAnimation().currentTicks();
		double delta = getAnimation().getTime(ticks - leftDoorTicks);
		leftDoorAngle += leftDoorRate*delta;
		if (leftDoorAngle > leftDoorMaxAngle) {
		    leftDoorAngle = leftDoorMaxAngle;
		}
		leftDoorTicks = ticks;
	    } else if (leftDoorAngle > leftDoorMaxAngle) {
		long ticks = getAnimation().currentTicks();
		double delta = getAnimation().getTime(ticks - leftDoorTicks);
		leftDoorAngle -= leftDoorRate*delta;
		if (leftDoorAngle > leftDoorMaxAngle) {
		    leftDoorAngle = leftDoorMaxAngle;
		}
		leftDoorTicks = ticks;
	    }
	} else {
	    if (leftDoorAngle > 0.0) {
		long ticks = getAnimation().currentTicks();
		double delta = getAnimation().getTime(ticks - leftDoorTicks);
		leftDoorAngle -= leftDoorRate*delta;
		if (leftDoorAngle < 0.0) {
		    leftDoorAngle = 0.0;
		}
		leftDoorTicks = ticks;
		if (leftDoorAngle > 0.0) {
		    showLeftDoor = true;
		}
	    }
	}

	if (rightDoorOpen) {
	    if (rightDoorAngle < rightDoorMaxAngle) {
		long ticks = getAnimation().currentTicks();
		double delta = getAnimation().getTime(ticks - rightDoorTicks);
		rightDoorAngle += rightDoorRate*delta;
		if (rightDoorAngle > rightDoorMaxAngle) {
		    rightDoorAngle = rightDoorMaxAngle;
		}
		rightDoorTicks = ticks;
	    } else if (rightDoorAngle > rightDoorMaxAngle) {
		long ticks = getAnimation().currentTicks();
		double delta = getAnimation().getTime(ticks - rightDoorTicks);
		rightDoorAngle -= rightDoorRate*delta;
		if (rightDoorAngle > rightDoorMaxAngle) {
		    rightDoorAngle = rightDoorMaxAngle;
		}
		rightDoorTicks = ticks;
	    }
	} else {
	    if (rightDoorAngle > 0.0) {
		long ticks = getAnimation().currentTicks();
		double delta = getAnimation().getTime(ticks - rightDoorTicks);
		rightDoorAngle -= rightDoorRate*delta;
		if (rightDoorAngle < 0.0) {
		    rightDoorAngle = 0.0;
		}
		rightDoorTicks = ticks;
		if (rightDoorAngle > 0.0) {
		    showRightDoor = true;
		}
	    }
	}
	
	Shape leftBlindSpot = null;
	Shape rightBlindSpot = null;

	if (leftBlindSpotVisible) {
	    Path2D bspath = new Path2D.Double();
	    bspath.moveTo(dx, dy);
	    bspath.lineTo(dx+leftBSx1, dy+leftBSy1);
	    bspath.lineTo(dx+leftBSx2, dy+leftBSy2);
	    bspath.closePath();
	    leftBlindSpot = new Path2D.Double(bspath, af);
	}

	if (rightBlindSpotVisible) {
	    Path2D bspath = new Path2D.Double();
	    bspath.moveTo(dx, dy);
	    bspath.lineTo(dx+rightBSx1, dy+rightBSy1);
	    bspath.lineTo(dx+rightBSx2, dy+rightBSy2);
	    bspath.closePath();
	    rightBlindSpot = new Path2D.Double(bspath, af);
	}


	Shape hood = new 
	    Path2D.Double(new Rectangle2D.Double(xws2, y1, hoodLength,
						 width),
			  af);
	Shape window = new
	    Path2D.Double(new Rectangle2D.Double(xws1, y1, windshieldLength,
						 width),
			  af);
	Shape rest = new
	    Path2D.Double(new Rectangle2D.Double (xr, y1, length - hoodLength
						  - windshieldLength,
						  width),
			  af);
	Shape outer = new
	    Path2D.Double(new Rectangle2D.Double(xr, y1, length, width), af);
	    
	if (showLeftDoor) {
	    double l = getDoorLength();
	    double w = getDoorWidth();
	    Rectangle2D r = new Rectangle2D.Double(xws2 - l, y2 - w, l, w);
	    AffineTransform rot =
		AffineTransform.getRotateInstance(-leftDoorAngle, xws2, y2);
	    leftDoor = new Path2D.Double(r, rot);
	    leftDoor = new Path2D.Double(leftDoor, af);
	}
	if (showRightDoor) {
	    double l = getDoorLength();
	    double w = getDoorWidth();
	    Rectangle2D r = new Rectangle2D.Double(xws2 - l, y1, l, w);
	    AffineTransform rot =
		AffineTransform.getRotateInstance(rightDoorAngle, xws2, y1);
	    rightDoor = new Path2D.Double(r, rot);
	    rightDoor = new Path2D.Double(rightDoor, af);
	}

	Color savedColor = g2d.getColor();
	Stroke savedStroke = g2d.getStroke();
	try {
	    if (leftBlindSpot != null) {
		g2d.setColor(blindSpotColor);
		graph.draw(g2d, leftBlindSpot);
		graph.fill(g2d, leftBlindSpot);
	    }
	    if (rightBlindSpot != null) {
		g2d.setColor(blindSpotColor);
		graph.draw(g2d, rightBlindSpot);
		graph.fill(g2d, rightBlindSpot);
	    }
	    if (showLeftDoor) {
		g2d.setColor(color);
		graph.draw(g2d, leftDoor);
		graph.fill(g2d, leftDoor);
	    }
	    if (showRightDoor) {
		g2d.setColor(color);
		graph.draw(g2d, rightDoor);
		graph.fill(g2d, rightDoor);
	    }
	    g2d.setColor(windshieldColor);

	    graph.draw(g2d, window);
	    graph.fill(g2d, window);
	    g2d.setColor(color);
	    graph.draw(g2d, hood);
	    graph.fill(g2d, hood);
	    graph.draw(g2d, rest);
	    graph.fill(g2d, rest);
	    g2d.setStroke(new BasicStroke(2.0F));
	    graph.draw(g2d, outer);

	    if (looking) {
		AffineTransform afr = (AffineTransform) (af.clone());
		afr.translate(xws2, width/4.0);
		afr.rotate(lookAngle);
		double edgeLength = 2.0 * windshieldLength;
		double xyCoordLength = edgeLength/Math.sqrt(2.0);
		Point2D loc = new Point2D.Double(xyCoordLength, 0.0);
		Point2D loc1 = new Point2D.Double(xyCoordLength, xyCoordLength);
		Point2D loc2 = new Point2D.Double(xyCoordLength,
						  -xyCoordLength);

		Point2D gloc = afr.transform(loc, new Point2D.Double());
		Point2D gloc1 = afr.transform(loc1, new Point2D.Double());
		Point2D gloc2 = afr.transform(loc2, new Point2D.Double());

		lookFontParms.setFont(graph.getFontToFit(look, gloc1, gloc2));
		double la = Math.toDegrees(lookAngle + getAngle()) - 90.0;
		while (la > 180.0) la -= 180.0;
		while (la < -180.0) la += 180.0;
		if (la < -90.0 || la > 90.0) {
		    lookFontParms.setBaseline(Graph.BLineP.TOP);
		    la = la - 180.0;
		} else {
		    lookFontParms.setBaseline(Graph.BLineP.BOTTOM);
		}
		lookFontParms.setAngle(la);
		graph.drawString(look, gloc.getX(), gloc.getY(),
				 lookFontParms);
	    }
	} finally {
	    g2d.setColor(savedColor);
	    g2d.setStroke(savedStroke);
	}
    }

    /**
     * Print the configuration for an instance of Car.
     * The documentation for method
     * {@link org.bzdev.devqsim.SimObject#printConfiguration(String,String,boolean,java.io.PrintWriter)}
     * contains a description of how this method is used and how to
     * override it. The method
     * {@link org.bzdev.anim2d.DirectedObject2D#printConfiguration(String,String,boolean,java.io.PrintWriter)}
     * describes the data that will be printed for the
     * superclass of this class. The data that will be printed
     * when this method is called is the following.
     * <P>
     * For class {@link Car}:
     * <UL>
     *   <LI> the length of the car.
     *   <LI> the width of the car.
     *   <LI> the length of the windshield (measured horizontally).
     *   <LI> the rear offset.
     *   <LI> the color.
     *   <LI> the windshield color.
     *   <LI> whether no-skid mode is active.
     *   <LI> whether the car is in reverse.
     * </UL>
     * @param iPrefix the prefix to use for an initial line when printName is
     *        true with null treated as an empty string
     * @param prefix a prefix string (typically whitespace) to put at
     *        the start of each line other than the initial line that is
     *        printed when printName is true
     * @param printName requests printing the name of an object
     * @param out the output print writer
     */
    @Override
    public void printConfiguration(String iPrefix, String prefix,
				   boolean printName,
				   java.io.PrintWriter out)
    {
	super.printConfiguration(iPrefix, prefix, printName, out);
	out.println(prefix + "length: " + length);
	out.println(prefix + "width: " + width);
	out.println(prefix + "windshield length: " + windshieldLength);
	out.println(prefix + "rear offset: " + rearOffset);
	out.println(prefix + "door length: " + doorLength);
	out.println(prefix + "door width: " + doorWidth);
	out.println(prefix + "color: " + color);
	out.println(prefix + "windshield color: " + windshieldColor);
	out.println(prefix + "no-skid mode: " + noSkidMode);
	out.println(prefix + "in reverse: " + reverse);
	out.println(prefix + "driverX: " + driverX);
	out.println(prefix + "driverY: " + driverY);
	out.println(prefix + "left blind-spot angle: "
		    + Math.toDegrees(thetaLeft) + " (degrees)");
	out.println(prefix + "left blind-spot width: "
		    + Math.toDegrees(phiLeft) + " (degrees)");
	out.println(prefix + "right blind-spot angle: "
		    + Math.toDegrees(thetaRight) + " (degrees)");
	out.println(prefix + "right blind-spot width: "
		    + Math.toDegrees(phiRight) + " (degrees)");
	out.println(prefix + "left blind-spot length:  "
		    + leftBlindSpotLength + "(meters)");
	out.println(prefix + "right blind-spot length:  "
		    + rightBlindSpotLength + "(meters)");
	out.println(prefix + "left blind-spot displayed: "
		    + leftBlindSpotVisible);
	out.println(prefix + "right blind-spot displayed: "
		    + rightBlindSpotVisible);
    }

    /**
     * Print the state for an instance of Car.
     * The documentation for method
     * {@link org.bzdev.devqsim.SimObject#printState(String,String,boolean,java.io.PrintWriter)}
     * contains a description of how this method is used and how to
     * override it. The method
     * {@link org.bzdev.anim2d.DirectedObject2D#printState(String,String,boolean,java.io.PrintWriter)}
     * describes the data that will be printed for the
     * superclass of this class. The data that will be printed
     * when this method is called is the following.
     * <P>
     * For class {@link Car}:
     * <UL>
     *   <LI> whether no-skid mode is in effect.
     *   <LI> whether the car is in reserve.
     * </UL>
     * @param iPrefix the prefix to use for an initial line when printName is
     *        true with null treated as an empty string
     * @param prefix a prefix string (typically whitespace) to put at
     *        the start of each line other than the initial line that is
     *        printed when printName is true
     * @param printName requests printing the name of an object
     * @param out the output print writer
     */
    @Override
	public void printState(String iPrefix, String prefix, boolean printName,
			   java.io.PrintWriter out)
    {
	super.printState(iPrefix, prefix, printName, out);
	out.println(prefix + "no-skid mode: " + noSkidMode);
	out.println(prefix + "in reverse: " + reverse);
	out.println(prefix + "left-door mode: " + leftDoorOpen);
	out.println(prefix + "left-door angular rate: "
		    + leftDoorRate + " radians/sec ("
		    + Math.toDegrees(leftDoorRate) + "\u00B0 per sec");
	out.println(prefix + "left-door maximum angle: "
		    + leftDoorMaxAngle + " radians ("
		    + Math.toDegrees(leftDoorMaxAngle) + "\u00B0)");
	out.println(prefix + "right-door mode: " + rightDoorOpen);
	out.println(prefix + "right-door angular rate: "
		    + rightDoorRate + " radians/sec ("
		    + Math.toDegrees(rightDoorRate) + "\u00B0 per sec)");
	out.println(prefix + "right-door maximum angle: "
		    + rightDoorMaxAngle + " radians ("
		    + Math.toDegrees(rightDoorMaxAngle) + "\u00B0)");
	out.println(prefix + "left blind-spot displayed: "
		    + leftBlindSpotVisible);
	out.println(prefix + "right blind-spot displayed: "
		    + rightBlindSpotVisible);
    }
}

//  LocalWords:  ds dxDu dyDu atan setAngleRelative boolean Runge sx
//  LocalWords:  Kutta noSkidAngleF toDegrees setPath angleRelative
//  LocalWords:  setPathImplementation hoodLength windshieldLength
//  LocalWords:  rearOffset IllegalStateException windshieldColor
//  LocalWords:  SimFunction savedAngleSF RealValuedFunction getName
//  LocalWords:  savedAngleF setLookString setLooking getAnimation
//  LocalWords:  currentTime getX getY getAngle printConfiguration
//  LocalWords:  superclass whitespace printName printState iPrefix
//  LocalWords:  Subclasses doorLength doorWidth angularSpeed
//  LocalWords:  IllegalArgumentException
