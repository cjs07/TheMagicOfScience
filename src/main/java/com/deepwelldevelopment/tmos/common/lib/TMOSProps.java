package com.deepwelldevelopment.tmos.common.lib;

import com.mojang.authlib.GameProfile;

import java.io.File;
import java.util.UUID;

public class TMOSProps {

    private static final String BUILD = "1448";
    public static final String FORGE_REQ = "10.13.4." + BUILD;
    public static final String FORGE_REQ_MAX = "10.14";

    public static final String FORGE_DEP = "required-after:Forge@[" + FORGE_REQ + "," + FORGE_REQ_MAX + ");";

    public static final String DOWNLOAD_URL = "http://teamcofh.com/downloads/";

    public static File configDir = null;

    /* Global Constants */
    public static final GameProfile DEFAULT_OWNER = new GameProfile(UUID.fromString("1ef1a6f0-87bc-4e78-0a0b-c6824eb787ea"), "[None]");;

    public static final int TIME_CONSTANT = 32;
    public static final int TIME_CONSTANT_HALF = TIME_CONSTANT / 2;
    public static final int TIME_CONSTANT_QUARTER = TIME_CONSTANT / 4;
    public static final int TIME_CONSTANT_EIGHTH = TIME_CONSTANT / 8;
    public static final int RF_PER_MJ = 10;
    public static final int LAVA_RF = 200000;
    public static final int ENTITY_TRACKING_DISTANCE = 64;

    /* Network */
    public static final int NETWORK_UPDATE_RANGE = 192;
}
