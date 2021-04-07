// tmpdir will be bound to a directory accessor by scrunner

// importPackage(org.bzdev.anim2d);
// importPackage(org.bzdev.roadanim);
// importClass(org.bzdev.util.units.MKS);

scripting.importClass("org.bzdev.anim2d.Animation2D");
scripting.importClass("org.bzdev.anim2d.AnimationPath2DFactory");
scripting.importClass("org.bzdev.anim2d.AnimationLayer2DFactory");
scripting.importClass("org.bzdev.anim2d.GraphViewFactory");
scripting.importClass("org.bzdev.anim2d.KinematicOps2D");
scripting.importClass("org.bzdev.roadanim.CarFactory");
scripting.importClass("org.bzdev.roadanim.BicycleFactory");
scripting.importClass("org.bzdev.roadanim.PedestrianFactory");
scripting.importClass("org.bzdev.util.units.MKS");
if (typeof(tmpdir) == undefined) {
    err.println("tmpdir undefined (need scrunner -d:tmpdir:TMP option)");
    exit(1);
}

a2d = scripting.create(Animation2D.class, scripting,
		       1920, 1080, 10000.0, 400);

gvf = a2d.createFactory(GraphViewFactory.class);
cf = a2d.createFactory(CarFactory.class);
bf = a2d.createFactory(BicycleFactory.class);
pedf = a2d.createFactory(PedestrianFactory.class);
pathf = a2d.createFactory(AnimationPath2DFactory.class);
alf = a2d.createFactory(AnimationLayer2DFactory.class);

scaleFactor = 1080.0/MKS.feet(60.0);
a2d.setRanges(0.0, 0.0, 0.0, 0.5, scaleFactor, scaleFactor);

YELLOW = {red: 255, green: 255, blue: 0};
GREEN = {green: 255, red: 0, blue: 0};
BLUE = {red: 0, green: 0, blue: 255};
DARKGRAY = {red: 96, green: 96, blue: 96};
LIGHTGRAY = {red: 160, green: 160, blue: 160};
BLACK = {red: 0, green: 0, blue: 0};
WHITE = {red: 255, green: 255, blue: 255};

SQUARE6 = {gcsMode: true, cap: "SQUARE", width: MKS.inches(6.0)}

pathConfig = [
    {withPrefix: "color", config: BLUE},
    {withPrefix: "stroke",
     config: {width: 2.0, gcsMode: false}}];

vpath = pathf.createObject("vpath", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	{"type": "MOVE_TO", "x": 1.0, "y": 0.0},
	{"type": "SEG_END", "x": 70.0, "y": 0.0}]}]);

gv = gvf.createObject("view", [
    {initialX: 1.0, initialY: 0.0},
    {xFrameFraction: 0.0, yFrameFraction: 0.5},
    {scaleX: scaleFactor, scaleY: scaleFactor},
    {withPrefix: "timeline", withIndex: [
	{time: 0.0, path: vpath, u0: 0.0, velocity: 0,
	 acceleration: MKS.mphPerSec(5)},
	{time: 3.0, acceleration: 0.0},
	{time: 7.7, acceleration: -MKS.mphPerSec(7.5)},
	{time: 9.7, velocity: 0, acceleration: 0}]}]);

alf.createObject("background", [
    {visible: true},
    {zorder: 0},
    {withPrefix: "object", withIndex: [
	[{type: "RECTANGLE",
	  refPoint: "CENTER_LEFT",
	  width: 120.0,  height: 20.0,
	  x: 0.0, y: 0.0},
	 {withPrefix: "fillColor", config: DARKGRAY},
	 {withPrefix: "drawColor", config: DARKGRAY},
	 {withPrefix: "stroke",
	  config: {width: 1.0, dashPattern: "-",
		   dashPhase: 0.0,
		   dashIncrement: 1.0}},
	 {fill: true, draw: true}],
	 [{type: "RECTANGLE",
	   refPoint: "LOWER_LEFT",
	   x: 0.0, y: MKS.feet(36.0/2),
	   width: 120.0, height: 10.0},
	  {withPrefix: "fillColor", config: GREEN},
	  {fill: true}],
	 [{type: "ROUND_RECTANGLE",
	   refPoint: "UPPER_LEFT",
	   x: 0.0, y: -MKS.feet(36.0/2),
	   width: 70.0, height: 10.0,
	   arcwidth: MKS.feet(5),
	   archeight: MKS.feet(5)},
	  {withPrefix: "fillColor", config: GREEN},
	  {fill: true}],
	 [{type: "RECTANGLE",
	  refPoint: "UPPER_LEFT",
	   width: 70.0, height: MKS.feet(6),
	   x: 0.0, y: -MKS.feet(36.0/2.0 + 3.0)},
	  {withPrefix: "fillColor", config: LIGHTGRAY},
	  {fill: true}],
	 [{type: "RECTANGLE",
	   refPoint: "UPPER_RIGHT",
	   width: MKS.feet(6), height: MKS.feet(10.0),
	   x: 70.0 - MKS.feet(3),
	   y: -MKS.feet(36.0/2.0 + 3.0 + 5.9)},
	  {withPrefix: "fillColor", config: LIGHTGRAY},
	  {fill: true}],
	 [{type: "ROUND_RECTANGLE",
	   refPoint: "UPPER_LEFT",
	   x: 70.0 + MKS.feet(36), y: -MKS.feet(36.0/2),
	   width: 50.0 - MKS.feet(36), height: 10.0,
	   arcwidth: MKS.feet(5),
	   archeight: MKS.feet(5)},
	  {withPrefix: "fillColor", config: GREEN},
	  {fill: true}],
	 [{type: "RECTANGLE",
	   refPoint: "UPPER_LEFT",
	   width: 50.0 - MKS.feet(36),
	   height: MKS.feet(6),
	   x: 70.0 + MKS.feet(36),
	   y: -MKS.feet(36.0/2.0 + 3.0)},
	  {withPrefix: "fillColor", config: LIGHTGRAY},
	  {fill: true}],
	 [{type: "LINE",
	   x: 0.0, y: 0.0,
	   xend: 120.0, yend: 0.0},
	  {withPrefix: "stroke",
	   config: {gcsMode: true,
		   cap: "SQUARE",
		   width: MKS.inches(3.0),
		   dashIncrement: MKS.feet(1.0),
		   dashPhase: 0.0,
		   dashPattern: "----  "}},
	  {withPrefix: "drawColor",
	   config: {red: 255, green: 255, blue: 0.0}},
	  {draw: true}],
	 [{type: "LINE",
	   x: 0.0, y: MKS.feet(12),
	   xend: 120.0, yend: MKS.feet(12)},
	  {withPrefix: "stroke", config: SQUARE6},
	  {withPrefix: "drawColor", config: WHITE},
	  {draw: true}],
	 [{type: "LINE",
	   x: 0.0, y: -MKS.feet(12),
	   xend: 50.0, yend: -MKS.feet(12)},
	  {withPrefix: "stroke", config: SQUARE6},
	  {withPrefix: "drawColor", config: WHITE},
	  {draw: true}],
	 [{type: "LINE",
	   x: 50, y: -MKS.feet(12),
	   xend: 65.0, yend: -MKS.feet(12)},
	  {withPrefix: "stroke",
	   config: [SQUARE6, {dashIncrement: MKS.feet(1.0),
		     dashPhase: 0.0, dashPattern: "--  "}]},
	  {withPrefix: "drawColor", config: WHITE},
	  {draw: true}],
	 [{type: "LINE",
	   x: 70 + MKS.feet(36), y: -MKS.feet(12),
	   "xend": 200.0, "yend": -MKS.feet(12)},
	  {withPrefix: "stroke", config: SQUARE6},
	  {withPrefix: "drawColor", config: WHITE},
	  {draw: true}]]}]);
		
bpath = pathf.createObject("bpath", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	{type: "MOVE_TO", x: 0.0, y: -MKS.feet(14)},
	{type: "SEG_END", x: 120,  y: -MKS.feet(14)}]}]);

bikeConfig = [
    {withPrefix: "bicycleColor", config: GREEN},
    {withPrefix: "helmetColor", config: YELLOW}];

bike = bf.createObject("bicycle1", [
    {zorder: 2, visible: true},
    bikeConfig,
    {withPrefix: "timeline", withIndex: [
	 {time: 0.0, path: bpath, u0: 0,
	  velocity: MKS.mph(15.0)},
	 {time: 13.0,  visible: false}]}]);

cpath1 = pathf.createObject("cpath1", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	 {type: "MOVE_TO", x: 0.0, y: -MKS.feet(2)},
	 {type: "SEG_END", x: 120,  y: -MKS.feet(2)}]}]);

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
	  velocity: MKS.mph(30)}]}]);

s2 = s1 + MKS.mph(30) * 3.0;
t2 = t1 + 3.0 - 0.75;

cpath2 = pathf.createObject("cpath2", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	 {type: "MOVE_TO", x: 0.0, y: -MKS.feet(2)},
	 {type: "SEG_END", x: s2 - 6, y: -MKS.feet(2)},
	 {type: "CONTROL", x: s2 - 3, y: -MKS.feet(2)},
	 {type: "CONTROL", x: s2 - 3, y: MKS.feet(4)},
	 {type: "SEG_END", x: s2,  y: MKS.feet(4)},
	 {type: "SEG_END", x: s2 + 3,  y: MKS.feet(4)},
	 {type: "CONTROL", x: s2 + 6, y: MKS.feet(4)},
	 {type: "CONTROL", x: s2 + 6, y: -MKS.feet(2)},
	 {type: "SEG_END", x: s2 + 9,  y: -MKS.feet(2)},
	 {type: "SEG_END", x: 120,  y: -MKS.feet(2)}]}]);

s3 = s2 + 15;

cpath3 = pathf.createObject("cpath3", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	 {type: "MOVE_TO", x: 120, y: MKS.feet(2)},
	 {type: "SEG_END", x: s3, y: MKS.feet(2)},
	 {type: "CONTROL", x: s3 - 3, y: MKS.feet(2)},
	 {type: "CONTROL", x: s3 - 3, y: MKS.feet(9)},
	 {type: "SEG_END", x: s3 - 9, y: MKS.feet(9)},
	 {type: "CONTROL", x: s3 - 15, y: MKS.feet(9)},
	 {type: "CONTROL", x: s3 - 15, y: MKS.feet(2)},
	 {type: "SEG_END", x: s3 - 21, y: MKS.feet(2)},
	 {type: "SEG_END", x: 0.0, y: MKS.feet(2)}]}]);

car2 = cf.createObject("car2", [
    {zorder: 2, visible: true},
    {refPointMode: "BY_NAME", refPointName: "UPPER_RIGHT"},
    {initialX: 0.0, initialY: -MKS.feet(2), initialAngle: 0.0},
    {withPrefix: "timeline", withIndex: [
	 {time: t2, path: cpath2, u0: 0.0,
	  velocity: MKS.mph(30)}]}]);

t3 = t2 + (s2-3)/MKS.mph(30) - s3/MKS.mph(25) + 0.8;

car3 = cf.createObject("car3", [
    {zorder: 2, visible: true},
    {refPointMode: "BY_NAME", refPointName: "UPPER_RIGHT"},
    {initialX: 120.0, initialY: MKS.feet(2), initialAngle: 180.0},
    {withPrefix: "timeline", withIndex: [
	{time: t3, path: cpath3, u0: 0.0, angleRelative: true,
	 velocity: MKS.mph(25)}]}]);

// start the second bike several seconds after the first.
tb2 = 4.0;
tb2end = tb2 + KinematicOps2D.timeGivenDVA(120.0, MKS.mph(15.0), 0.0);

bike2 = bf.createObject("bicycle2", [
    {zorder: 2, visible: "false"},
    bikeConfig,
    {withPrefix: "timeline", withIndex: [
	 {time: tb2, path: bpath, u0: 0,
	  velocity: MKS.mph(15.0), visible: true},
	 {time: tb2end, visible: false}]}]);

cpath4 = pathf.createObject("cpath4", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	 {type: "MOVE_TO", x: 0, y: -MKS.feet(2)},
	 {type: "SEG_END", x: 55, y: -MKS.feet(2)},
	 {type: "CONTROL", x: 55 + 6, y: -MKS.feet(2)},
	 {type: "CONTROL", x: 55 + 6, y: -MKS.feet(10)},
	 {type: "SEG_END", x: 55 + 10, y: -MKS.feet(10)},
	 {type: "SEG_END", x:70, y: -MKS.feet(10)},
	 {type: "CONTROL", x:70 + MKS.feet(16), y: -MKS.feet(10)},
	 {type: "SEG_END", x:70 + MKS.feet(16), y: -MKS.feet(36.0)},
	 {type: "SEG_END",  x:70 + MKS.feet(16), y: -20.0}]}]);

s4a = cpath4.getPathLength(0.0, 1.0);
s4b = cpath4.getPathLength(1.0, 2.0);
s4c = cpath4.getPathLength(2.0, 4.0);
s4d = cpath4.getPathLength(4.0, 5.0);

accel1 = KinematicOps2D.accelGivenDVV(s4b, MKS.mph(30), MKS.mph(15));
tdecel = KinematicOps2D.timeGivenVVA(MKS.mph(30), MKS.mph(15), accel1);
t4 = tb2 + 70/MKS.mph(15) - 55/MKS.mph(30) - tdecel;
t4a = t4 + 55/MKS.mph(30);
t4b = t4a + tdecel;
t4c = t4b + s4c/MKS.mph(15);

accel2 = -accel1;
taccel = KinematicOps2D.timeGivenDVA(s4d, MKS.mph(15), accel2);

t4d = t4c + taccel - 0.1;

car4 = cf.createObject("car4", [
    {zorder: 2, visible: true},
    {refPointMode: "BY_NAME", refPointName: "UPPER_RIGHT"},
    {initialX: 0.0, initialY: -MKS.feet(2), initialAngle: 0.0},
    {withPrefix: "timeline", withIndex: [
	{time: t4, path: cpath4, u0: 0.0,
	 velocity: MKS.mph(30)},
	{time: t4a, acceleration: accel1},
	{time: t4b , velocity: MKS.mph(15), acceleration: 0.0},
	{time: t4c, acceleration: accel2}]}]);

tb3 = tb2 + 2.5;
tb3end = tb3 + KinematicOps2D.timeGivenDVA(120.0, MKS.mph(15.0), 0.0);

bike3 = bf.createObject("bicycle3", [
    {zorder: 2, visible: "false"},
    bikeConfig,
    {withPrefix: "timeline", withIndex: [
	{time: tb3, path: bpath, u0: 0,
	 velocity: MKS.mph(15.0), visible: true},
	{time: tb3end, visible: false}]}]);

tb4 = tb3 + 2.0;
tb4a = tb4 + 55/MKS.mph(15);
baccel1 = KinematicOps2D.accelGivenDVV(10.0, MKS.mph(15), MKS.mph(5));
tb4b = tb4a + KinematicOps2D.timeGivenDVA(10.0, MKS.mph(15), baccel1);
tb4c1 = tb4b + 2.5/MKS.mph(5);
tb4c2 = tb4b + 5.0/MKS.mph(5);
baccel2 = MKS.gFract(0.2);
tb4d =  tb4c2 + KinematicOps2D.timeGivenVVA(MKS.mph(5), MKS.mph(15), baccel2);
bdist1 = KinematicOps2D.distGivenVVA(MKS.mph(5), MKS.mph(15), baccel2);

tb4end = tb4d + KinematicOps2D.timeGivenDVA(MKS.feet(36) + 50.0 - bdist1,
					    MKS.mph(15.0), 0.0);

bike4 = bf.createObject("bicycle4", [
    {zorder: 2, visible: "false"},
    bikeConfig,
    {withPrefix: "timeline", withIndex: [
	{time: tb4, path: bpath, u0: 0,
	 velocity: MKS.mph(15.0), visible: true},
	{time: tb4a, acceleration: baccel1},
	{time: tb4b, acceleration: 0.0, velocity: MKS.mph(5)},
	{time: tb4c1, helmetAngle: 90.0},
	{time: tb4c2, helmetAngle: 0.0, acceleration: baccel2},
	{time: tb4d, velocity: MKS.mph(15.0), acceleration: 0.0},
	{time: tb4end, visible: false}]}]);

cpath5 = pathf.createObject("cpath5", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	{type: "MOVE_TO", x: 0, y: -MKS.feet(2)},
	{type: "SEG_END", x: 60, y: -MKS.feet(2)},
	{type: "SEG_END", x: 70, y: -MKS.feet(2)},
	{type: "CONTROL", x: 70 + MKS.feet(16), y: -MKS.feet(2)},
	{type: "SEG_END", x: 70 + MKS.feet(16), y: -MKS.feet(36)},
	{type: "SEG_END", x: 70 + MKS.feet(16), y: -20}]}]);

t5 =  tb3 + 70/MKS.mph(15) - 60/MKS.mph(30);
t5a = t5 + 60/MKS.mph(30);
accel3 = KinematicOps2D.accelGivenDVV(15, MKS.mph(30), 0.0);
t5b = t5a + KinematicOps2D.timeGivenDVA(10.0, MKS.mph(30), accel3);
t5c = tb4c2 + 1.0;
// accel4 = KinematicOps2D.accelGivenDVV(s5b, 0.0, MKS.mph(30));
accel4 = MKS.gFract(0.3);
s5b = KinematicOps2D.distGivenVVA(0.0, MKS.mph(30.0), accel4);
s5c = cpath5.getPathLength(2.0, 4.0) - s5b;
if (s5c < 0.0) s5c = 0.0;
t5d = t5c + KinematicOps2D.timeGivenDVA(s5b, 0.0, accel4);
t5end = t5d + s5c/MKS.mph(30);
car5 = cf.createObject("car5", [
    {zorder: 2, visible: true},
    {refPointMode: "BY_NAME", refPointName: "UPPER_RIGHT"},
    {initialX: 0.0, initialY: -MKS.feet(2), initialAngle: 0.0},
    {withPrefix: "timeline", withIndex: [
	{time: t5, path: cpath5, u0: 0.0,
	 velocity: MKS.mph(30)},
	{time: t5a, acceleration: accel3},
	{time: t5b, acceleration: 0.0, velocity: 0.0},
	{time: t5c, acceleration: accel4},
	{time: t5d, acceleration: 0.0, velocity: MKS.mph(30)}]}]);

t6 = t5 + 5.0;
t6a = t5a + 4.7;
t6b = t5b + 4.7;
car6 = cf.createObject("car6", [
    {zorder: 2, visible: true},
    {refPointMode: "BY_NAME", refPointName: "UPPER_RIGHT"},
    {initialX: 0.0, initialY: -MKS.feet(2), initialAngle: 0.0},
    {withPrefix: "timeline", withIndex: [
	{time: t6, path: cpath1, u0: 0.0,
	 velocity: MKS.mph(30)},
	{time: t6a, acceleration: accel3},
	{time: t6b, acceleration: 0.0, velocity: 0.0}]}]);

tb5 = t6b - 60/MKS.mph(15);
tb5a = t6b + 4/MKS.mph(15);
baccel3 = KinematicOps2D.accelGivenDVV(5.0, MKS.mph(15), 0.0);
tb5b = tb5a + KinematicOps2D.timeGivenDVA(5.0, MKS.mph(15), baccel3);

s6 = MKS.mph(30)*(t6a-t6)
    + KinematicOps2D.distGivenTVA(t6b-t6a, MKS.mph(30), accel3);

t7 = t6 + 0.5;
tincr = 3/MKS.mph(30);
t7a = t6a + 0.5 - tincr;
t7b = t6b + 0.5 - tincr;

s7 = MKS.mph(30)*(t7a-t7)
    + KinematicOps2D.distGivenTVA(t7b-t7a, MKS.mph(30), accel3);

alf.createObject("X", [
    {zorder: 20, visible: false},
    {withPrefix: "object", withIndex: [
	 [{type: "TEXT", x: s7, y: -MKS.feet(2) - 0.75,
	   text: "X"},
	  {withPrefix: "fontParms",
	   config: [{style: "BOLD", size: 64,
		     baselinePosition: "CENTER",
		     justification: "CENTER"},
		    {withPrefix: "color", config: WHITE}]}]]},
    {withPrefix: "timeline", withIndex: [
	{time: t7b, visible: true}]}]);

car7 = cf.createObject("car7", [
    {zorder: 2, visible: true},
    {withPrefix: "color", config: BLACK},
    {refPointMode: "BY_NAME", refPointName: "UPPER_RIGHT"},
    {initialX: 0.0, initialY: -MKS.feet(2), initialAngle: 0.0},
    {withPrefix: "timeline", withIndex: [
	{time: t7, path: cpath1, u0: 0.0,
	 velocity: MKS.mph(30)},
	{time: t7a, acceleration: accel3},
	{time: t7b, acceleration: 0.0, velocity: 0.0}]}]);

ped1x = s6 - car6.getHoodLength() - car6.getWindshieldLength()/2.0;
ped2x = s7 - car7.getHoodLength() - car7.getWindshieldLength()/2.0;
middle = (ped1x + ped2x)/2.0;

ppath1 = pathf.createObject("ppath1", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	{type: "MOVE_TO", x: ped1x , y: -MKS.feet(4)},
	{type: "SEG_END", x: ped1x, y: 0.0},
	{type: "SEG_END", x: middle + 0.4, y: 0.0}]}]);

ppath2 = pathf.createObject("ppath2", [
    {zorder: 10, visible: false},
    pathConfig,
    {withPrefix: "cpoint", withIndex: [
	{type: "MOVE_TO", x: ped2x , y: -MKS.feet(4)},
	{type: "SEG_END", x: ped2x, y: 0.0},
	{type: "SEG_END", x: middle - 0.4, y: 0.0}]}]);

bike5 = bf.createObject("bicycle5", [
    {zorder: 2, visible: "false"},
    bikeConfig,
    {withPrefix: "timeline", withIndex: [
	{time: tb5, path: bpath, u0: 0,
	 velocity: MKS.mph(15.0), visible: true},
	{time: t7b-0.5, helmetAngle: 90.0},
	{time: tb5a, acceleration: baccel3, helmetAngle: 0.0},
	{time: tb5b, velocity: 0.0, acceleration: 0.0,
	 helmetAngle: -45.0},
	{time: tb5b + 1.0, helmetAngle: 140.0}]}]);

ped1 = pedf.createObject("ped1", [
    {zorder: 1, visible: false},
    {withPrefix: "timeline", withIndex: [
	{time:tb5b + 1.5, path: ppath1, u0: 0,
	 angleRelative: true,  pathAngle: 0.0,
	 velocity: MKS.mph(3), visible: true}]}
]);

ped2 = pedf.createObject("ped2", [
    {zorder: 1, visible: false},
    {withPrefix: "timeline", withIndex: [
	{time:tb5b + 1.5, path: ppath2, u0: 0,
	 angleRelative: true, pathAngle: 0.0,
	 velocity: MKS.mph(3), visible: true}]}]);

maxFrames = a2d.estimateFrameCount(33.0);
scripting.getWriter().println("maxFrames = " + maxFrames);
scripting.getWriter().flush();
a2d.initFrames(maxFrames, "col-", "png", tmpdir);
a2d.scheduleFrames(0.0, maxFrames);

a2d.run();
