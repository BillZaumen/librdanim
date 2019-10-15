package org.bzdev.roadanim;

/**
 * Package configuration class.
 */
public class RoadAnimation {

    static int level1 = -1; // slowly-varying data
    static int level2 = -1; // bursty data 
    static int level3 = -1; // update data
    static int level4 = -1; // Runge Kutta data

    /**
     * Set trace levels explicitly.
     * A level of -1 indicates that nothing will be displayed. Otherwise
     * the level must be a non-negative integer.
     * For the models in the org.bzdev.roadanim package, the levels are
     * used as follows:
     * <ul>
     *   <li> For level1, methods that change slowly varying parameters
     *        may be traced. For Car, this includes the following
     *        methods:
     *        <ul>
     *          <li> {@link Car#setSkidMode(boolean)}
     *          <li> {@link Car#setReverseMode(boolean)}
     *          <li> {@link Car#setPathImplementation(org.bzdev.geom.BasicSplinePath2D,double,double,boolean,double)}
     *               (which is called by the setPath methods of
     *               DirectedObject2D)
     *        </ul>
     *        The Car cases include all the points at which the Runge-Kutta
     *        algorithm is initialized.
     *        For Bicycle, the methods that may be traced include the
     *        following methods:
     *        <ul>
     *          <li> {@link Bicycle#setLookString(String)}
     *          <li> {@link Bicycle#setLooking(boolean)}
     *        </ul>
     *   <li> For level2, methods that may change either slowly or in
     *        bursts (given the typical usage of these classes) may be
     *        traced.  The only method is
     *        {@link Bicycle#setHelmetAngle(double)}.
     *   <li> For level3, The angle returned by the angle function
     *        handling the no-skid case for the Car class is traced.
     *   <li> For level4, The function used by the Runge-Kutta algorithm
     *        to compute the derivative of the angle &phi; for the Car
     *        class; is traced.
     *        Angles, including the returned rate of change, are given
     *        in units of degrees per second. Remember that the Runge-Kutta
     *        function is called multiple times for each step.
     * </ul>
     * @param level1 the trace level for slowly-varying parameters
     * @param level2 the trace level for bursty parameters
     * @param level3 the trace level for updates
     * @param level4 the trace level for use Runge-Kutta function calls
     * @exception IllegalArgumentException an argument was smaller than -1
     */
    public static void setTraceLevels(int level1, int level2, int level3,
				      int level4) 
	throws IllegalArgumentException
    {
	if (level1 < -1 || level2 < -1 || level3 < -1 || level4 < -1)
	    throw new IllegalArgumentException("level less than -1");
	RoadAnimation.level1 = level1;
	RoadAnimation.level2 = level2;
	RoadAnimation.level3 = level3;
	RoadAnimation.level4 = level4;
    }

    /**
     * Set trace levels using enumerations.
     * A level set to null indicates that nothing will be displayed. Otherwise
     * the level must be an enumeration constant, with the constant's ordinal
     * value providing the level.
     * <ul>
     *   <li> For level1, methods that change slowly varying parameters
     *        may be traced. For Car, this includes the following
     *        methods:
     *        <ul>
     *          <li> {@link Car#setSkidMode(boolean)}
     *          <li> {@link Car#setReverseMode(boolean)}
     *        </ul>
     *        For Bicycle, this includes the following methods:
     *        <ul>
     *          <li> {@link Bicycle#setLookString(String)}
     *          <li> {@link Bicycle#setLooking(boolean)}
     *        </ul>
     *   <li> For level2, methods that may change either slowly or in
     *        bursts (given the typical usage of these classes) may be
     *        traced.  The only method is
     *        {@link Bicycle#setHelmetAngle(double)}.
     *   <li> For level3, The angle returned by the angle function
     *        handling the no-skid case for the Car class is traced.
     *   <li> For level4, The function used by the Runge-Kutta algorithm
     *        to compute the derivative of the angle &phi; for the Car
     *        class; is traced.
     *        Angles, including the returned rate of change, are given
     *        in units of degrees per second.
     * </ul>
     * @param level1 the trace level for slowly-varying parameters
     * @param level2 the trace level for bursty parameters
     * @param level3 the trace level for updates
     * @param level4 the trace level for Runge-Kutta function calls
     */
    public static
	<T extends Enum<T>> void setTraceLevels(T level1, T level2,
						T level3, T level4)
    {
	RoadAnimation.level1 = (level1 == null)? -1: level1.ordinal();
	RoadAnimation.level2 = (level2 == null)? -1: level2.ordinal();
	RoadAnimation.level3 = (level3 == null)? -1: level3.ordinal();
	RoadAnimation.level4 = (level4 == null)? -1: level4.ordinal();
    }
}
//  LocalWords:  bursty Runge Kutta ul li setSkidMode boolean setPath
//  LocalWords:  setReverseMode setPathImplementation DirectedObject
//  LocalWords:  setLookString setLooking setHelmetAngle constant's
//  LocalWords:  IllegalArgumentException
