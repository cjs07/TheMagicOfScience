package com.deepwelldevelopment.tmos.dynamics.multiblock;

import net.minecraft.world.World;

public interface IMultiBlock {

    public World world();

    public int x();

    public int y();

    public int z();

    public void setInvalidForForming();

    public void setValidForForming();

    public boolean isValidForForming();

    public MultiBlockGrid getNewGrid();

    public void setGrid(MultiBlockGrid newGrid);

    public MultiBlockGrid getGrid();

    public IMultiBlock getConnectedSide(byte side);

    public boolean isBlockedSide(int side);

    public boolean isSideConnected(byte side);

    // This side contains a grid that will not form, mark that side as not connected.
    public void setNotConnected(byte side);

    // Used by some multiblocks to start their formations. Removed from the ticking list after initial tick.
    public void tickMultiBlock();

    // Used to do multiblock steps passed off by the grid. IE: Distribute liquids.
    // return false if the grid has altered
    public boolean tickPass(int pass);

    public boolean isNode();

    public boolean existsYet();
}
