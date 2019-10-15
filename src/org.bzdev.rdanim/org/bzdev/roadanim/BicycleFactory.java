package org.bzdev.roadanim;
import org.bzdev.anim2d.*;

/**
 * Bicycle factory.
* <P>
 * The parameters this factory supports are shown in the following table:
 * <P>
 * <IFRAME SRC="{@docRoot}/factories-api/org/bzdev/roadanim/BicycleFactory.html" style="width:95;height:600px;border: 3px solid steelblue">
 * Please see
 *  <A HREF="{@docRoot}/factories-api/org/bzdev/roadanim/BicycleFactory.html">
 *    the parameter documentation</A> for a table of the parameters supported
 * by this factory.
 * </IFRAME>
 */
public class BicycleFactory extends AbstractBicycleFactory<Bicycle> {
    Animation2D a2d;


    /**
     * Constructor for service provider.
     * This constructor should not be used directly. It is necessary
     * because of the introduction of modules in Java 9, and is
     * used by a service provider that allows factories to be listed,
     * possibly with documentation regarding their parameters. It
     * jst calls the default constructor with a null argument.
     */
    public BicycleFactory() {
	this(null);
    }


    /**
     * Constructor.
     * @param a2d the animation
     */
    public BicycleFactory(Animation2D a2d) {
	super(a2d);
	this.a2d = a2d;
    }

    @Override
    protected Bicycle newObject(String name) {
	return new Bicycle(a2d, name, willIntern());
    }
}

//  LocalWords:  IFRAME SRC HREF
