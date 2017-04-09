package com.deepwelldevelopment.tmos.common.lib.util;

import com.deepwelldevelopment.tmos.common.TMOS;
import com.deepwelldevelopment.tmos.common.lib.TMOSProps;
import com.deepwelldevelopment.tmos.lib.util.ConfigHandler;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class FuelManager {

    public static ConfigHandler configFuels = new ConfigHandler(TMOS.version);

    static {
        configFuels.setConfiguration(new Configuration(new File(TMOSProps.configDir, "tmos/fuels.cfg"), true));
    }

    private FuelManager() {

    }

	/* COAL */
	public static boolean addCoalFuel(String name, int energy) {
        return true;
    }


    public static void parseFuels() {
        configFuels.cleanUp(true, false);
    }
}
