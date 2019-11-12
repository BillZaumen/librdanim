/**
 * Module providing animation objects representing bicyles, cars, and
 * pedestrians.
 * <P>
 * This module augments the {@link org.bzdev.anim2d} module by adding
 * a few classes that represent objects one might find on a road so
 * that one can produce animations showing the interaction of
 * motor vehicles, bicycles, and pedestrians.
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
    provides org.bzdev.obnaming.NamedObjectFactory with
	org.bzdev.roadanim.BicycleFactory,
	org.bzdev.roadanim.CarFactory,
	org.bzdev.roadanim.PedestrianFactory;
}
