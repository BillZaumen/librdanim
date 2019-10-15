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

sf = 1080.0/10.0;

a2d.setRanges(0.0, 0.0, 0.0, 0.0, sf, sf);

cf = a2d.createFactory(CarFactory);


car = cf.createObject("car", [
    {zorder: 1, visible: true},
    {initialX: 5.0, initialY: 5.0},
    {looking: true, lookAngle: 0.0, lookString: "looking"},
    {withPrefix: "lookingFontColor", config: {red: 0, green: 255, blue: 0}},
    {withPrefix: "timeline", withIndex: [
	{time: 1.0, looking: false},
	[{time: 2.0, looking: true, lookString: "Looking", lookAngle: 45.0},
	 {withPrefix: "lookingFontColor", config:{red: 200, green: 200}}],
	{time: 3.0, "lookingFontColor.green": 255},
	{time: 4.0, looking: false}]}]);


maxFrames = a2d.estimateFrameCount(5.0);
println("maxFrames = " + maxFrames);
a2d.initFrames(maxFrames, "col-", "png", tmpdir);
a2d.scheduleFrames(0.0, maxFrames);
a2d.run();


