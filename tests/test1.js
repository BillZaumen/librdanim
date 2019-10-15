// tmpdir will be bound to a directory accessor by scrunner

importPackage(org.bzdev.anim2d);
importPackage(org.bzdev.roadanim);
importClass(org.bzdev.util.units.MKS);

if (typeof(tmpdir) == undefined) {
    err.println("tmpdir undefined (need scrunner -d:tmpdir:TMP option)");
    exit(1);
}

a2d = scripting.create(Animation2D, scripting,
		       1920, 1080, 10000.0, 400);

gvf = a2d.createFactory(GraphViewFactory);
cf = a2d.createFactory(CarFactory);
bf = a2d.createFactory(BicycleFactory);
pedf = a2d.createFactory(PedestrianFactory);
pathf = a2d.createFactory(AnimationPath2DFactory);
alf = a2d.createFactory(AnimationLayer2DFactory);

scaleFactor = 1080.0/MKS.feet(60.0);
a2d.setRanges(0.0, 0.0, 0.0, 0.5, scaleFactor, scaleFactor);

pathConfig =
    {"color.blue": 255, "color.red": 0, "color.green": 0,
     "stroke.width": 2.0, "stroke.gcsMode": false};

vpath = pathf.createObject("vpath", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	{"type": "MOVE_TO", "x": 1.0, "y": 0.0},
	{"type": "SEG_END", "x": 70.0, "y": 0.0}]}
]);

gv = gvf.createObject("view", [
    {initialX: 1.0, initialY: 0.0},
    {xFrameFraction: 0.0, yFrameFraction: 0.5},
    {scaleX: scaleFactor, scaleY: scaleFactor},
    {withPrefix: "timeline", withIndex: [
	{time: 0.0, path: vpath, u0: 0.0, velocity: 0,
	 acceleration: MKS.mphPerSec(5)},
	{time: 3.0, acceleration: 0.0},
	{time: 7.7, acceleration: -MKS.mphPerSec(7.5)},
	{time: 9.7, velocity: 0, acceleration: 0}]}
]);


cpath1 = pathf.createObject("cpath1", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	 {type: "MOVE_TO", x: 0.0, y: -MKS.feet(2)},
	 {type: "SEG_END", x: 120,  y: -MKS.feet(2)}]}
]);

s1 = 10.0;
// give the bike a 10 meter head start.
t1 = KinematicOps2D.timeGivenDVA(s1, MKS.mph(15.0), 0.0);
t1end = t1 + KinematicOps2D.timeGivenDVA(120, MKS.mph(30.0), 0.0);

car1 = cf.createObject("car1", [
    {zorder: 2, visible: true},
    {refPointMode: "BY_NAME", refPointName: "UPPER_RIGHT"},
    {initialX: 0.0, initialY: -MKS.feet(2), initialAngle: 0.0},
    {withPrefix: "timeline", withIndex: [
	 {time: t1, path: cpath1, u0: 0.0,
	  velocity: MKS.mph(30)}/*,
	 {time: t1end, velocity: 0}*/]}
]);


maxFrames = a2d.estimateFrameCount(t1end + 5.0);
println("maxFrames = " + maxFrames);
a2d.initFrames(maxFrames, "col-", "png", tmpdir);
a2d.scheduleFrames(0.0, maxFrames);

try {
    a2d.run();
} catch (ex) {
    println(ex.javaException.getMessage());
    ex.javaException.printStackTrace(java.lang.System.out);
}
