/**
 * Module providing animation objects representing bicyles, cars, and
 * pedestrians.
 * <P>

 */
module org.bzdev.rdanim {
    exports org.bzdev.roadanim;
    opens org.bzdev.roadanim.lpack;
    requires java.base;
    requires java.desktop;
    requires org.bzdev.base;
    requires org.bzdev.obnaming;
    requires org.bzdev.desktop;
    requires org.bzdev.devqsim;
    requires org.bzdev.anim2d;
    requires static org.bzdev.parmproc;
    provides org.bzdev.obnaming.NamedObjectFactory with
	org.bzdev.roadanim.BicycleFactory,
	org.bzdev.roadanim.CarFactory,
	org.bzdev.roadanim.PedestrianFactory;
}
