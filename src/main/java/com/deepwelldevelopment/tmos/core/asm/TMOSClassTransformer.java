package com.deepwelldevelopment.tmos.core.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import static com.deepwelldevelopment.tmos.core.asm.ASMCore.hashes;

public class TMOSClassTransformer implements IClassTransformer {

    private static boolean scrappedData = false;

    public TMOSClassTransformer() {
        ASMCore.init();
    }

    public static void scrapeData(ASMDataTable table) {
        ASMCore.scrapeData(table);
        scrappedData = true;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        int index = hashes.get(transformedName);
        if (index != 0) {
            bytes = ASMCore.transform(index, name, transformedName, bytes);
        }

        return bytes;
    }
}
