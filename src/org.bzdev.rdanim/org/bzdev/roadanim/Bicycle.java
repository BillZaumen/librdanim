package org.bzdev.roadanim;

import org.bzdev.anim2d.*;
import org.bzdev.graphs.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import javax.imageio.ImageIO;
import java.util.Formatter;

/**
 * Class representing a bicycle.  The bicycle's reference point is at
 * the location of the seat, which is at the center of the bicycle's
 * bounding box for the default configuration.
 */
public class Bicycle extends DirectedObject2D {

    static final double HELMET_LENGTH = 0.3;
    static final double HELMET_WIDTH = 0.2;
    static final double HELMET_SLOT_WIDTH = 0.025;
    static final double B1 = 0.2;
    static final double B2 = 0.1;
    static final double B3 = 0.3;
    static final double B4 = 0.3;
    Color bikeColor;
    Color helmetColor;

    Path2D helmet = new Path2D.Double();
    // used for printouts
    private double lookingScaleFactor = 1.0;
    private double helmetScaleFactor = 1.0;
    // next value is used for sizing the 'look' string.
    private double lookingSF = 1.0;
    private void createHelmet(Color c, double scaleFactor) {
	if (scaleFactor <= 0.0)
	    throw new IllegalArgumentException("scaleFactor must be positive");
	helmetScaleFactor = scaleFactor;
	helmetColor = c;
	double x1 = - 2.0 * HELMET_LENGTH/ 3.0;
	double y1 = 0.0;
	double x2 = x1;
	double y2 =  B1 * HELMET_WIDTH;
	double x3 =  - B2 * HELMET_LENGTH;
	double y3 = HELMET_WIDTH/2.0;
	double x4 = 0.0;
	double y4 = y3;

	double x5 = B3 * HELMET_LENGTH;
	double y5 = y4;
	double x6 = HELMET_LENGTH/3.0;
	double y6 = B4 * HELMET_WIDTH;
	double x7 = x6;
	double y7 = 0.0;
	
	if (scaleFactor != 1.0) {
	    x1 *= scaleFactor; y1 *= scaleFactor;
	    x2 *= scaleFactor; y2 *= scaleFactor;
	    x3 *= scaleFactor; y3 *= scaleFactor;
	    x4 *= scaleFactor; y4 *= scaleFactor;
	    x5 *= scaleFactor; y5 *= scaleFactor;
	    x6 *= scaleFactor; y6 *= scaleFactor;
	    x7 *= scaleFactor; y7 *= scaleFactor;
	}

	helmet.moveTo(x1 , y1);
	helmet.curveTo(x2, y2,
		       x3, y3,
		       x4, y4);
	helmet.curveTo(x5, y5,
		       x6, y6,
		       x7, y7);
	helmet.curveTo(x6, -y6,
		       x5, -y5,
		       x4, -y4);
	helmet.curveTo(x3, -y3,
		       x2, -y2,
		       x1, y1);
	helmet.closePath();
	setLookParams(null);
    }
    

    /**
     * Constructor.
     * @param a2d the animation
     * @param name the name of this object
     * @param intern true if the object is interned in the animation's
     *        name space; false otherwise.
     */
    public Bicycle(Animation2D a2d, String name, boolean intern) {
	super(a2d, name, intern);
    }

    boolean configureCalled = false;

    /**
     * Configure the the object representing a bicycle using
     * default dimensions.
     * @param helmetColor the color of the rider's helmet
     * @param bikeColor the color of the bicycle
     */
    public void configure(Color helmetColor, Color bikeColor) {
	if (configureCalled) return;
	configureCalled = true;
	createHelmet(helmetColor, 1.0);
	lookingSF = 1.0;
	setLookParams(null);
	this.bikeColor = bikeColor;
    }

    /**
     * Configure the the object representing a bicycle using
     * default dimensions but with helmet scaling.
     * @param helmetColor the color of the rider's helmet
     * @param bikeColor the color of the bicycle
     * @param helmetScaleFactor the factor by which the dimensions of
     *        a helmet will be multiplied when the helmet is configured
     */
    public void configure(Color helmetColor, Color bikeColor,
			  double helmetScaleFactor) {
	configure(helmetColor, bikeColor, helmetScaleFactor, 1.0);
    }

    /**
     * Configure the the object representing a bicycle using
     * default dimensions but with helmet scaling and look-string scaling.
     * <P>
     * The scale factor used to size the 'look' string  will be
     * lookStringScaleFactor when helmetScaleFactor is less than 1.0,
     * and helmetScaleFactor*lookStringScaleFactor otherwise.
     * The value of lookStringScaleFactor must not be less than 1.0.
     * @param helmetColor the color of the rider's helmet
     * @param bikeColor the color of the bicycle
     * @param helmetScaleFactor the factor by which the dimensions of
     *        a helmet will be multiplied when the helmet is configured
     * @param lookStringScaleFactor the fraction by which the dimensions of
     *        the look-string text will be scaled.
     */
    public void configure(Color helmetColor, Color bikeColor,
			  double helmetScaleFactor,
			  double lookStringScaleFactor)
    {
	if (configureCalled) return;
	configureCalled = true;
	createHelmet(helmetColor, helmetScaleFactor);
	lookingScaleFactor = lookStringScaleFactor;
	this.lookingSF = (helmetScaleFactor < 1.0)? lookStringScaleFactor:
	    helmetScaleFactor*lookStringScaleFactor;
	setLookParams(null);
	this.bikeColor = bikeColor;
    }


    /**
     * Configure the the object representing a bicycle with default helmet
     * dimensions.
     * @param helmetColor the color of the rider's helmet
     * @param bikeColor the color of the bicycle
     * @param bikeHead the distance from the seat to the front of the
     *        bicycle
     * @param bikeTail the distance from the seat to the rear of the
     *        bicycle
     * @param bikeWidth the width of the bicycle tubing (e.g., the frame)
     * @param handlebarPosition the distance from the seat to the
     *        handle bars
     * @param handlebarHalfLength the distance the handlebars extend
     *        perpendicular to the frame, measured from the frame and
     *        excluding the portion of the handlebars parallel to the
     *        frame
     * @param handlebarTail the length of the portion of the handlebars
     *        parallel to the bicycle
     */
    public void configure(Color helmetColor, Color bikeColor,
			  double bikeHead, double bikeTail,
			  double bikeWidth,
			  double handlebarPosition,
			  double handlebarHalfLength,
			  double handlebarTail) {
	configure(helmetColor, bikeColor, bikeHead, bikeTail,
		  bikeWidth, handlebarPosition,
		  handlebarHalfLength, handlebarTail,
		  1.0, 1.0);
    }

    /**
     * Configure the the object representing a bicycle and helmet,
     * providing a helmet-scaling factor.
     * When the helemtScaleFactor argument is larger than 1.0, the
     * position and font size of the string displayed when
     * {@link #setLooking(boolean) setLooking(true)} is called will be
     * changed from the default value.
     * @param helmetColor the color of the rider's helmet
     * @param bikeColor the color of the bicycle
     * @param bikeHead the distance from the seat to the front of the
     *        bicycle
     * @param bikeTail the distance from the seat to the rear of the
     *        bicycle
     * @param bikeWidth the width of the bicycle tubing (e.g., the frame)
     * @param handlebarPosition the distance from the seat to the
     *        handle bars
     * @param handlebarHalfLength the distance the handlebars extend
     *        perpendicular to the frame, measured from the frame and
     *        excluding the portion of the handlebars parallel to the
     *        frame
     * @param handlebarTail the length of the portion of the handlebars
     *        parallel to the bicycle
     * @param helmetScaleFactor the factor by which the dimensions of
     *        a helmet will be multiplied when the helmet is configured
     */
    public void configure(Color helmetColor, Color bikeColor,
			  double bikeHead, double bikeTail,
			  double bikeWidth,
			  double handlebarPosition,
			  double handlebarHalfLength,
			  double handlebarTail,
			  double helmetScaleFactor)
	throws IllegalArgumentException
    {
	configure(helmetColor, bikeColor, bikeHead, bikeTail, bikeWidth,
		  handlebarPosition, handlebarHalfLength, handlebarTail,
		  helmetScaleFactor, 1.0);
    }

    /**
     * Configure the the object representing a bicycle and helmet,
     * providing a helmet-scaling factor and look-string scaling factor.
     * When the helemtScaleFactor argument is larger than 1.0, the
     * position and font size of the string displayed when
     * {@link #setLooking(boolean) setLooking(true)} is called will be
     * changed from the default value.
     * <P>
     * The scale factor used to size the 'look' string  will be
     * lookStringScaleFactor when helmetScaleFactor is less than 1.0,
     * and helmetScaleFactor*lookStringScaleFactor otherwise.
     * The value of lookStringScaleFactor must not be less than 1.0.
     * @param helmetColor the color of the rider's helmet
     * @param bikeColor the color of the bicycle
     * @param bikeHead the distance from the seat to the front of the
     *        bicycle
     * @param bikeTail the distance from the seat to the rear of the
     *        bicycle
     * @param bikeWidth the width of the bicycle tubing (e.g., the frame)
     * @param handlebarPosition the distance from the seat to the
     *        handle bars
     * @param handlebarHalfLength the distance the handlebars extend
     *        perpendicular to the frame, measured from the frame and
     *        excluding the portion of the handlebars parallel to the
     *        frame
     * @param handlebarTail the length of the portion of the handlebars
     *        parallel to the bicycle
     * @param helmetScaleFactor the factor by which the dimensions of
     *        a helmet will be multiplied when the helmet is configured
     * @param lookStringScaleFactor the fraction by which the dimensions of
     *        the look-string text will be scaled.
     */
    public void configure(Color helmetColor, Color bikeColor,
			  double bikeHead, double bikeTail,
			  double bikeWidth,
			  double handlebarPosition,
			  double handlebarHalfLength,
			  double handlebarTail,
			  double helmetScaleFactor,
			  double lookStringScaleFactor)
	throws IllegalArgumentException
    {
	if (configureCalled) return;
	configureCalled = true;
	createHelmet(helmetColor, helmetScaleFactor);
	lookingScaleFactor = lookStringScaleFactor;
	if (lookStringScaleFactor < 1.0) {
	    throw new IllegalArgumentException("lookStringScaleFactor < 1.0");
	}
	this.lookingSF = (helmetScaleFactor < 1.0)? lookStringScaleFactor:
	    helmetScaleFactor*lookStringScaleFactor;
	setLookParams(null);
	this.bikeColor = bikeColor;
	this.bikeHead = bikeHead;
	this.bikeTail = bikeTail;
	this.bikeWidth = bikeWidth;
	this.handlebarPosition = handlebarPosition;
	this.handlebarHalfLength = handlebarHalfLength;
	this.handlebarTail = handlebarTail;
    }


    static final double DEFAULT_BIKEHEAD = 1.0;
    static final double DEFAULT_BIKETAIL = 1.0;
    static final double DEFAULT_BIKEWIDTH = 0.03;
    static final double DEFAULT_HANDLEBARPOSITION = DEFAULT_BIKEHEAD/2.0;
    static final double DEFAULT_HANDLEBARHALFLENGTH = 0.3;
    static final double DEFAULT_HANDLEBARTAIL = 0.15;
    static final double DEFAULT_HELMETANGLE = 0.0;


    double bikeHead = DEFAULT_BIKEHEAD;
    double bikeTail = DEFAULT_BIKETAIL;
    double bikeWidth = DEFAULT_BIKEWIDTH;
    double handlebarPosition = DEFAULT_HANDLEBARPOSITION;
    double handlebarHalfLength =DEFAULT_HANDLEBARHALFLENGTH;
    double handlebarTail = DEFAULT_HANDLEBARTAIL;
    double helmetAngle = DEFAULT_HELMETANGLE;

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
	trace(RoadAnimation.level1, "bicycle 'look' string set to \"%s\"",
	      look);
    }

    boolean looking = false;
    /**
     * Set whether or not the word "Look", or the value provided by
     * the method setLookString, should be displayed in
     * front of the helmet.
     * @param value true if the string should be displayed, false otherwise.
     */
    public void setLooking(boolean value) {
	looking = value;
	trace(RoadAnimation.level1, "look mode set to %b", looking);
    }

    Graph.FontParms lookFontParms = new Graph.FontParms();

    /**
     * Set the font color for a "looking" indication.
     * Calling any of the methods named <code>configure</code> will
     * set the color to the default value, so to set the color to a
     * different value, call this method after the config method is
     * called.
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


    /**
     * Set the angle of the helmet relative to the bicycle.
     * An angle of zero points in the direction in which the bicycle
     * is pointed, and angles are measured counterclockwise in radians.
     * @param angle the angle
     */
    public void setHelmetAngle(double angle) {
	helmetAngle = angle;
	if (RoadAnimation.level2 > -1) {
	    trace(RoadAnimation.level2,
		  "bicycle helmet angle set to %g degrees",
		  Math.toDegrees(helmetAngle));
	}
    }


    /**
     * Get the angle of the helmet relative to the bicycle.
     * An angle of zero points in the direction in which the bicycle
     * is pointed.
     * @return the helmet angle
     */
    public double getHelmetAngle() {
	return helmetAngle;
    }

    /**
     * Get the X coordinate of the seat in graph coordinate space.
     * @return the X coordinate of the seat in graph coordinate space.
     */
    public double getSeatXGCS() {
	AffineTransform af = getAddToTransform();
	return af.getTranslateX();
    }

    /**
     * Get the Y coordinate of the seat in graph coordinate space.
     * @return the Y coordinate of the seat in graph coordinate space.
     */
    public double getSeatYGCS() {
	AffineTransform af = getAddToTransform();
	return af.getTranslateY();
    }

    @Override
    public void addTo(Graph graph, Graphics2D g2d, Graphics2D g2dGcS) {
	Color savedColor = g2d.getColor();
	Stroke savedStroke = g2d.getStroke();
	try {
	    // Graphics2D g2d = graph.createGraphics();
	    g2d.setColor(bikeColor);
	    g2d.setStroke(new BasicStroke(1.0F));
	    // AffineTransform af = AffineTransform.getTranslateInstance(x,y);
	    // af.rotate(angle);
	    AffineTransform af = getAddToTransform();
	    AffineTransform afr = (AffineTransform) (af.clone());
	    afr.rotate(helmetAngle);

	    Rectangle2D rectangle1 = new
		Rectangle2D.Double(-bikeTail, -bikeWidth/2.0,
				   bikeTail + bikeHead, bikeWidth);
	    Rectangle2D rectangle2 = new
		Rectangle2D.Double(handlebarPosition, -handlebarHalfLength,
				   bikeWidth, 2*handlebarHalfLength);
	    Rectangle2D rectangle3 = new
		Rectangle2D.Double(handlebarPosition-handlebarTail+bikeWidth,
				   handlebarHalfLength,
				   handlebarTail, bikeWidth);
	    Rectangle2D rectangle4 = new
		Rectangle2D.Double(handlebarPosition-handlebarTail+bikeWidth,
				   -handlebarHalfLength,
				   handlebarTail, bikeWidth);

	    Shape s1 = new Path2D.Double(rectangle1, af);
	    Shape s2 = new Path2D.Double(rectangle2, af);
	    Shape s3 = new Path2D.Double(rectangle3, af);
	    Shape s4 = new Path2D.Double(rectangle4, af);

	    graph.draw(g2d, s1);
	    graph.fill(g2d, s1);
	    graph.draw(g2d, s2);
	    graph.fill(g2d, s2);
	    graph.draw(g2d, s3);
	    graph.fill(g2d, s3);
	    graph.draw(g2d, s4);
	    graph.fill(g2d, s4);

	// af.rotate(helmetAngle);
	    g2d.setColor(helmetColor);
	    Shape hs = new Path2D.Double(helmet, afr);
	    graph.draw(g2d, hs);
	    graph.fill(g2d, hs);
	    g2d.setColor(Color.BLACK);
	    Shape slot1 = new Rectangle2D.Double(-HELMET_LENGTH/4.0,
						 -HELMET_WIDTH/4.0 
						 - HELMET_SLOT_WIDTH/2.0,
						 HELMET_LENGTH/2.0,
						 HELMET_SLOT_WIDTH);
	    slot1 = new Path2D.Double(slot1, afr);
	    Shape slot2 = new Rectangle2D.Double(-HELMET_LENGTH/4.0,
						 HELMET_WIDTH/4.0 
						 - HELMET_SLOT_WIDTH/2.0,
						 HELMET_LENGTH/2.0,
						 HELMET_SLOT_WIDTH);
	    slot2 = new Path2D.Double(slot2, afr);
	    graph.draw(g2d, slot1);
	    graph.fill(g2d, slot1);
	    graph.draw(g2d, slot2);
	    graph.fill(g2d, slot2);

	    if (looking) {
		double edgeLength = 2.0 * HELMET_LENGTH * lookingSF;
		double xyCoordLength = edgeLength/Math.sqrt(2.0);
		Point2D loc = new Point2D.Double(xyCoordLength, 0.0);
		Point2D loc1 = new Point2D.Double(xyCoordLength, xyCoordLength);
		Point2D loc2 = new Point2D.Double(xyCoordLength,
						  -xyCoordLength);

		Point2D gloc = afr.transform(loc, new Point2D.Double());
		Point2D gloc1 = afr.transform(loc1, new Point2D.Double());
		Point2D gloc2 = afr.transform(loc2, new Point2D.Double());

		lookFontParms.setFont(graph.getFontToFit(look, gloc1, gloc2));
		double la = Math.toDegrees(helmetAngle + getAngle()) - 90.0;
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
     * Print the configuration for an instance of Bicycle.
     * The documentation for method
     * {@link org.bzdev.devqsim.SimObject#printConfiguration(String,String,boolean,java.io.PrintWriter)}
     * contains a description of how this method is used and how to
     * override it. The method
     * {@link org.bzdev.anim2d.DirectedObject2D#printConfiguration(String,String,boolean,java.io.PrintWriter)}
     * describes the data that will be printed for the
     * superclass of this class. The data that will be printed
     * when this method is called is the following.
     * <P>
     * For class {@link Bicycle}:
     * <UL>
     *   <LI> the bicycle's color.
     *   <LI> the color of the rider's helmet.
     *   <LI> the helmet scale factor (when "looking", the helmet
     **       can be magnified to make it more obvious).
     *   <LI> the bicycle head (the distance from the seat to the
     *        front of the bicycle).
     *   <LI> the bicycle tail (the distance from the seat to the
     *        rear of the bicycle).
     *   <LI> the width of the bicycle.
     *   <LI> the handlebar position (the distance from the seat to
     *        the handlebars).
     *   <LI> the handlebar half length (extent to which one half of
     *        the hanglebars extends outwards).
     *   <LI> the handlebar tail (the length of a line drawn parallel
     *        to the bicycle to represent the ends of the handlebars).
     *   <LI> the helmet angle (the angle from the direction in which
     *        the bicycle is pointed, indicating the direction in
     *        which a rider would be looking).
     *   <LI> the look string (the string that will be displayed to
     *        indicate that a rider is looking at something).
     *   <LI> looking (a boolean that, when true, indicates that the
     *        look string is displayed).
     *   <LI> look-string color (the color of the font used for the
     *        look string).
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
	out.println(prefix + "bicycle color: " + bikeColor);
	out.println(prefix + "helmet color: " + helmetColor);
	out.println(prefix + "helmet scale factor: " + helmetScaleFactor);
	out.println(prefix + "look-string scale factor: "
		    + lookingScaleFactor);
	out.println(prefix + "bicycle head: " + bikeHead);
	out.println(prefix + "bicycle tail: " + bikeTail);
	out.println(prefix + "bicycle width: " + bikeWidth);
	out.println(prefix + "handlebar position: " + handlebarPosition);
	out.println(prefix + "half length: " + handlebarHalfLength);
	out.println(prefix + "handlebar tail: " + handlebarTail);
	out.println(prefix + "helmetAngle: " + helmetAngle);
	out.println(prefix + "look string: \"" + look + "\"");
	out.println(prefix + "looking: " + looking);
	out.println(prefix + "look-string color: "
		    + lookFontParms.getColor());

    }

    /**
     * Print the state for an instance of Bicycle.
     * The documentation for method
     * {@link org.bzdev.devqsim.SimObject#printState(String,String,boolean,java.io.PrintWriter)}
     * contains a description of how this method is used and how to
     * override it. The method
     * {@link org.bzdev.anim2d.DirectedObject2D#printState(String,String,boolean,java.io.PrintWriter)}
     * describes the data that will be printed for the
     * superclass of this class. The data that will be printed
     * when this method is called is the following.
     * <P>
     * For class {@link Bicycle}:
     * <UL>
     *   <LI> the helmet angle (the angle from the direction in which
     *        the bicycle is pointed, indicating the direction in
     *        which a rider would be looking).
     *   <LI> the look string (the string that will be displayed to
     *        indicate that a rider is looking at something).
     *   <LI> looking (a boolean that, when true, indicates that the
     *        look string is displayed).
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
	out.println(prefix + "looking: " + looking);
	out.println(prefix + "helmetAngle: " + helmetAngle
		    + " radians (" + Math.toDegrees(helmetAngle) + "\u00B0)");
	out.println(prefix + "look string: \"" + look + "\"");
    }
}

//  LocalWords:  scaleFactor helmetColor bikeColor helmetScaleFactor
//  LocalWords:  bikeHead bikeTail bikeWidth handlebarPosition af
//  LocalWords:  handlebarHalfLength handlebarTail helemtScaleFactor
//  LocalWords:  setLooking boolean setLookString createGraphics
//  LocalWords:  AffineTransform getTranslateInstance helmetAngle
//  LocalWords:  printConfiguration superclass hanglebars whitespace
//  LocalWords:  printName printState iPrefix
