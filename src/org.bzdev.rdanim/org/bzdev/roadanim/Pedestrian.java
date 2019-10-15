package org.bzdev.roadanim;

import org.bzdev.anim2d.*;
import org.bzdev.graphs.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Class representing a pedestrian.
 * The pedestrian's default reference point is at the center of the
 * circle representing the pedestrian.  This is generally the one that
 * one should use as a pedestrian is a small object in a typical
 * animation.
 * <P>
 * A pedestrian is represented by a circle, with each half a different
 * color (one to represent hair and the other a face).  The 'face' half
 * has two smaller ellipses representing eyes, primarily to provide a
 * sense of direction. All the features are scaled based on the circle's
 * radius.
 */
public class Pedestrian extends DirectedObject2D {

    /**
     * Constructor.
     * @param a2d the animation
     * @param name the name of this object
     * @param intern true if the object is interned in the animation's
     *        name space; false otherwise.
     */
    public Pedestrian(Animation2D a2d, String name, boolean intern) {
	super(a2d, name, intern);
    }

    /**
     * Configure the the object representing a pedestrian.
     * @param hairColor the color of pedestrian's hair; null for the
     *        default
     * @param faceColor the color of the pedestrian's face;
     *        null for the default
     * @param eyeColor the color of the pedestrian's eyes;
     *        null for the default
     */
    public void configure(Color hairColor, Color faceColor, Color eyeColor) {
	this.hairColor = (hairColor == null)? HAIR_COLOR:  hairColor;
	this.faceColor = (faceColor == null)? FACE_COLOR: faceColor;
	this.eyeColor = (eyeColor == null)? EYE_COLOR: eyeColor;
    }

    /**
     * Configure the the object representing a pedestrian, providing a radius.
     * @param hairColor the color of pedestrian's hair; null for the
     *        default
     * @param faceColor the color of the pedestrian's face;
     *        null for the default
     * @param eyeColor the color of the pedestrian's eyes;
     *        null for the default
     * @param radius the radius for the circular representation of a
     *        pedestrian (must be a positive real number)
     * @exception IllegalArgumentException the radius is less than or
     *            equal to zero
     */
    public void configure(Color hairColor, Color faceColor, Color eyeColor,
			  double radius) {
	if (radius <= 0.0) throw new IllegalArgumentException
			       ("radius less than or equal to zero");
	configure(hairColor, faceColor, eyeColor);
	this.radius = radius;
    }


    static final double DEFAULT_RADIUS = 0.20;
    private double radius = DEFAULT_RADIUS;
    static Ellipse2D head =
	new Ellipse2D.Double(-DEFAULT_RADIUS, -DEFAULT_RADIUS,
			     2.0*DEFAULT_RADIUS, 2.0 *DEFAULT_RADIUS);

    static Color HAIR_COLOR = new Color(139,69,19);

    Color hairColor = HAIR_COLOR;

    static Arc2D.Double face =
	new Arc2D.Double(-DEFAULT_RADIUS, -DEFAULT_RADIUS,
			 2.0*DEFAULT_RADIUS, 2.0 * DEFAULT_RADIUS,
			 -90.0, 180.0, Arc2D.CHORD);
						   
    static Color FACE_COLOR = new Color(239,206,207);
    Color faceColor = FACE_COLOR;

    /**
     * Get the color used to represent the pedestrian's hair.
     * @return the color used to represent the pedestrian's hair
     */
    public Color getHairColor() {return hairColor;}

    /**
     * Get the color used to represent the pedestrian's face.
     * @return the color used to represent the pedestrian's face
     */
    public Color getFaceColor() {return faceColor;}

    static final Color EYE_COLOR = Color.BLACK;
    Color eyeColor = EYE_COLOR;

    // need to scale, translate, and then rotate to display this.
    static Ellipse2D.Double eye = new Ellipse2D.Double(-1.0, -1.5, 2.0, 3.0);

    @Override
    public void addTo(Graph graph, Graphics2D g2d, Graphics2D g2dGcS) {
	Color savedColor = g2d.getColor();
	Stroke savedStroke = g2d.getStroke();
	try {
	    // Graphics2D g2d = graph.createGraphics();
	    g2d.setStroke(new BasicStroke(1.0F));
	    // AffineTransform af = AffineTransform.getTranslateInstance(x,y);
	    // af.rotate(angle);
	    AffineTransform af = getAddToTransform();
	    double sfactor0 = radius/DEFAULT_RADIUS;
	    af.scale(sfactor0, sfactor0);

	    Shape sh = new Path2D.Double(head, af);
	    Shape sf = new Path2D.Double(face, af);

	    g2d.setColor(hairColor);
	    graph.draw(g2d, sh);
	    graph.fill(g2d, sh);
	    g2d.setColor(faceColor);
	    graph.draw(g2d, sf);
	    graph.fill(g2d, sf);
	    g2d.setColor(eyeColor);
	    AffineTransform eaf = getAddToTransform();
	    double sfactor = 0.1 * radius;
	    double rf = radius/2.0;
	    eaf.translate(rf, rf);
	    eaf.scale(sfactor, sfactor);
	    // af.translate(rf, -rf);
	    // af.scale(sfactor, sfactor);
	    Shape eye1 = new Path2D.Double(eye, eaf);
	    eaf = getAddToTransform();
	    eaf.translate(rf, -rf);
	    eaf.scale(sfactor,sfactor);
	    Shape eye2 = new Path2D.Double(eye, eaf);
	    graph.draw(g2d, eye1);
	    graph.fill(g2d, eye1);
	    graph.draw(g2d, eye2);
	    graph.fill(g2d, eye2);
	} finally {
	    g2d.setColor(savedColor);
	    g2d.setStroke(savedStroke);
	}
    }

    /**
     * Print the configuration for an instance of Pedestrian.
     * The documentation for method
     * {@link org.bzdev.devqsim.SimObject#printConfiguration(String,String,boolean,java.io.PrintWriter)}
     * contains a description of how this method is used and how to
     * override it. The method
     * {@link org.bzdev.anim2d.DirectedObject2D#printConfiguration(String,String,boolean,java.io.PrintWriter)}
     * describes the data that will be printed for the
     * superclass of {@link Pedestrian}. The data that will be printed
     * when this method is called is the following.
     * <P>
     * For class {@link Pedestrian}:
     * <UL>
     *   <LI>the hair color.
     *   <LI>the face color.
     *   <LI>the eye color.
     *   <LI> the radius of the disk representing a pedestrian.
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
	out.println(prefix + "hair color: " + hairColor);
	out.println(prefix + "face color: " + faceColor);
	out.println(prefix + "eye color: " + eyeColor);
	out.println(prefix + "radius: " + radius);
    }
    /**
     * Print the state for an instance of Pedestrian.
     * The documentation for method
     * {@link org.bzdev.devqsim.SimObject#printState(String,String,boolean,java.io.PrintWriter)}
     * contains a description of how this method is used and how to
     * override it. The method
     * {@link org.bzdev.anim2d.DirectedObject2D#printState(String,String,boolean,java.io.PrintWriter)}
     * describes the data that will be printed for the
     * superclass of {@link Pedestrian} ({@link Pedestrian} itself does not
     * add any text).
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
    }
}

//  LocalWords:  hairColor faceColor eyeColor createGraphics af rf
//  LocalWords:  IllegalArgumentException AffineTransform sfactor
//  LocalWords:  getTranslateInstance printConfiguration boolean
//  LocalWords:  superclass whitespace printName printState iPrefix
