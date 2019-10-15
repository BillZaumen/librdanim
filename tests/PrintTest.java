import org.bzdev.roadanim.*;
import org.bzdev.anim2d.*;

public class PrintTest {
    public static void main(String argv[]) throws Exception {

	Animation2D a2d = new Animation2D();

	Car car = new Car(a2d, "car", true);
	Bicycle bike = new Bicycle(a2d, "bike", true);
	Pedestrian ped = new Pedestrian(a2d, "ped", true);

	car.printConfiguration();
	bike.printConfiguration();
	ped.printConfiguration();

	System.out.println("NOW PRINT STATES");

	car.printState();
	bike.printState();
	ped.printState();
    }
}