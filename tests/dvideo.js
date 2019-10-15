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

/*
alf.createObject("background", [
    {zorder: 1, visible: true},
    {withPrefix: "object",
     withIndex: [
	{type: "RECTANGLE",
	 config: [
	     {x: 0.0, y: -width/2.0, refPoint: "LOWER_LEFT",
	      width: length, height: width},
	     {withPrefix: "stroke", config: stroke},
	     {withPrefix: "fillColor", config: lightgray},
	     {withPrefix: "drawColor", config: lightgray},
	     {fill: true},
	     {draw: true}
	 ]},
	{type: "RECTANGLE",
	 config: [
	     {x: 0.0, y: -MKS.feet(18), refPoint: "LOWER_LEFT",
	      width: length, height: MKS.feet(36)},
	     {withPrefix: "stroke", config: stroke},
	     {withPrefix: "fillColor", config: gray},
	     {withPrefix: "drawColor", config: gray},
	     {fill: true},
	     {draw: true}
	 ]}
     ]
    }
]);
*/
/*
cf.createObject("car1", [
    {zorder: 2, visible: true},
    {initialX: length*0.25, initialY: -MKS.feet(18)},
    {refPointMode: "BY_NAME", refPointName: "LOWER_LEFT"},
    {withPrefix: "color", config: red}
]);
*/
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

/*
cf.createObject("car3", [
    {zorder: 2, visible: true},
    {initialX: length*0.6, initialY: MKS.feet(18), initialAngle: 180.0},
    {refPointMode: "BY_NAME", refPointName: "LOWER_LEFT"},
    {withPrefix: "color", config: red}
]);
*/

/*
pathConfig = [
    {withPrefix: "color", config: yellow},
    {withPrefix: "stroke", config: {width: 2.0, gcsMode: false}}
];

path1 = pathf.createObject("path1", [
    {zorder: 2, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	{type: "MOVE_TO", x: -MKS.feet(30), y: -MKS.feet(2)},
	{type: "SEG_END", x: length+10, y: -MKS.feet(2)}
    ]},
]);

path2 = pathf.createObject("path2", [
    {zorder: 2, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	{type: "MOVE_TO", x: length+10, y: MKS.feet(2)},
	{type: "SEG_END", x: -MKS.feet(30), y: MKS.feet(2)}
    ]},
]);

path3 = pathf.createObject("path3", [
    {zorder: 2, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	{type: "MOVE_TO", x: 0.0, y: -MKS.feet(18+2.5)},
	{type: "SEG_END", x: length*0.35, y: -MKS.feet(18+2.5)},
	{type: "CONTROL", x: length*0.45, y: -MKS.feet(18+2.5)},
	{type: "CONTROL", x: length*0.45, y: -MKS.feet(18-7.0)},
	{type: "SEG_END", x: length*0.55, y: -MKS.feet(18-7.0)},
	{type: "SEG_END", x: length*0.6, y: -MKS.feet(18-7.0)},
	{type: "CONTROL", x: length*0.65, y: -MKS.feet(18-7.0)},
	{type: "CONTROL", x: length*0.65, y: -MKS.feet(18-11.0)},
	{type: "SEG_END", x: length*0.7, y: -MKS.feet(18-11.0)},
	{type: "SEG_END", x: length+MKS.feet(30), y: -MKS.feet(18-11.0)},
    ]},
]);
*/
/*
bf.createObject("bike", [
    {zorder: 3, visible: true},
    {withPrefix: "bicycleColor", config: red},
    {withPrefix: "helmetColor", config: yellow},
    {withPrefix: "timeline", withIndex: [
	{time: 0.0, path: path3, velocity: MKS.feet(10)},
    ]},
]);
*/
/*
cf.createObject("car4", [
    {zorder: 4, visible: true},
    {initialX: 0.0-MKS.feet(30), initialY: -MKS.feet(2)},
    {refPointMode: "BY_NAME", refPointName: "UPPER_RIGHT"},
    {withPrefix: "color", config: red},
    {withPrefix: "timeline", withIndex: [
	{time: 3.0, path: path1, velocity: MKS.mph(20)},
	{time: 5.0, acceleration: MKS.gFract(-0.45)},
	{time: 7.0, acceleration: 0.0},
	{time: 8.0, acceleration: MKS.gFract(0.2)}
    ]}
]);

cf.createObject("car5", [
    {zorder: 4, visible: true},
    {initialX: length+MKS.feet(30), initialY: MKS.feet(2)},
    {refPointMode: "BY_NAME", refPointName: "UPPER_RIGHT"},
    {withPrefix: "color", config: red},
    {withPrefix: "timeline", withIndex: [
	{time: 3.0, path: path2, velocity: MKS.mph(20)}
    ]}
]);

cf.createObject("car6", [
    {zorder: 4, visible: true},
    {initialX: length+MKS.feet(30), initialY: MKS.feet(2)},
    {refPointMode: "BY_NAME", refPointName: "UPPER_RIGHT"},
    {withPrefix: "color", config: red},
    {withPrefix: "timeline", withIndex: [
	{time: 4.0, path: path2, velocity: MKS.mph(20)}
    ]}
]);
*/


out.println("length = " + length);

var maxframes = a2d.estimateFrameCount(12.0);
a2d.initFrames(maxframes, apg);



a2d.scheduleFrames(0, maxframes);

a2d.run();
apg.close();
