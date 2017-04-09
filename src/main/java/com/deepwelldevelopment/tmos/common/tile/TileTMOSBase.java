package com.deepwelldevelopment.tmos.common.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public abstract class TileTMOSBase extends TileEntity implements ITickable {

    public int getInvSlotCount() {
        return 0;
    }
}
