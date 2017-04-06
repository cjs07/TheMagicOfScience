package com.deepwelldevelopment.tmos.core.asm;

import gnu.trove.map.hash.TObjectByteHashMap;
import gnu.trove.set.hash.THashSet;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.util.ArrayList;

public class ASMCore {

    static TObjectByteHashMap<String> hashes = new TObjectByteHashMap<String>(30, 1, (byte) 0);
    static THashSet<String> parsables;

    static void init() {
    }

    static {
        //TODO: ADD CLASSES TO MODIFY TO HASHES
    }

    static final ArrayList<String> workingPath = new ArrayList<String>();
    private static final String[] emptyList = {};

    static byte[] parse(String name, String transformedName, byte[] bytes) {
        workingPath.add(transformedName);
        workingPath.remove(workingPath.size()-1);
        return bytes;
    }

    static byte[] transform(int index, String name, String transformedName, byte[] bytes) {
        return bytes;
    }

    static void scrapeData(ASMDataTable table) {

    }
}
