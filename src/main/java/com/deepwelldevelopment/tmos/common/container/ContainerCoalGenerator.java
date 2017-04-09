package com.deepwelldevelopment.tmos.common.container;

import com.deepwelldevelopment.tmos.common.tile.generator.TileGeneratorCoal;
import com.deepwelldevelopment.tmos.core.gui.ContainerTMOSBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCoalGenerator extends ContainerTMOSBase {

    TileGeneratorCoal te;
    private int ticksRemaining;
    private int energyStored;

    public ContainerCoalGenerator(IInventory playerInventory, TileGeneratorCoal te) {
        super(te);
        this.te = te;

        int x = 40;
        int y = 8;

        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        addSlotToContainer(new SlotItemHandler(itemHandler, 0, 82, 38));

        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                x = 10 + col * 18;
                y = row * 18 + 70;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            x = 10 + row * 18;
            y = 58 + 70;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.listeners.size(); i++) {
            IContainerListener listener = this.listeners.get(i);
            if (ticksRemaining != te.getField(0)) {
                listener.sendProgressBarUpdate(this, 0, te.getField(0));
            }
            if (energyStored != te.getField(1)) {
                listener.sendProgressBarUpdate(this, 1, te.getField(1));
            }
        }
        ticksRemaining = te.getField(0);
        energyStored = te.getField(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.te.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }
}
