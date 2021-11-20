scripting.importClasses("org.bzdev.anim2d", [
    "Animation2D",
]);

scripting.importClass("org.bzdev.swing.AnimatedPanelGraphics");
scripting.importClass("org.bzdev.util.units.MKS");

out = scripting.getWriter();

frameWidth = 1920/2;
frameHeight = 1080/2;

a2d = new Animation2D(scripting, frameWidth, frameHeight, 1000.0, 40);
apg = AnimatedPanelGraphics.newFramedInstance(a2d,
					      "Swerving",
					      true, true, null);

width = MKS.feet(46.0);
scalef = 1.0*frameHeight/width;
length = frameWidth/scalef;
a2d.setRanges(0.0, 0.0, 0.0, 0.5, scalef, scalef);
out.println("scalef = " + scalef);

a2d.createFactories("org.bzdev.anim2d", {
    alf: "AnimationLayer2DFactory",
    gvf: "GraphViewFactory",
    pathf: "AnimationPath2DFactory"
});

a2d.createFactories("org.bzdev.roadanim", {
    cf: "CarFactory",
    bf: "BicycleFactory",
    pf: "PedestrianFactory"
});

lightgray = {red: 200, blue: 200, green: 200, alpha: 255};
gray = {red: 64, blue: 64, green: 64, alpha: 255};
red = {red: 255, blue: 0, green: 0}
yellow = {blue: 0, red: 255, green: 255}

stroke = {width: 2.0, gcsMode: false};

car2 = cf.createObject("car2", [
    {zorder: 2, visible: true},
    {initialX: length*0.6, initialY: -MKS.feet(18)},
    {refPointMode: "BY_NAME", refPointName: "LOWER_LEFT"},
    {withPrefix: "color", config: red},
    {withPrefix: "timeline", withIndex: [
	{time: 2.0, rightDoorMode: true},
	{time: 5.0, leftDoorMode: true}
    ]},
]);

car2.printConfiguration();


out.println("length = " + length);

var maxframes = a2d.estimateFrameCount(12.0);
a2d.initFrames(maxframes, apg);



a2d.scheduleFrames(0, maxframes);

a2d.run();
apg.close();
