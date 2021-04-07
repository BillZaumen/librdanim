package org.bzdev.roadanim.provider;
import java.io.InputStream;
import java.util.ResourceBundle;
import org.bzdev.lang.spi.ONLauncherData;

public class RoadAnimationLauncherData implements ONLauncherData {

    public RoadAnimationLauncherData() {}

    @Override
    public String getName() {
	return "rdanim";
    }

    @Override
    public InputStream getInputStream() {
	return getClass().getResourceAsStream("RoadAnimationLauncherData.yaml");
    }

    private static final String BUNDLE
	= "org.bzdev.roadanim.provider.lpack.RoadAnimationLauncherData";

    @Override
    public String description() {
	return ResourceBundle.getBundle(BUNDLE).getString("description");
    }
}
