package com.deepwelldevelopment.tmos.core.gui;

import com.deepwelldevelopment.tmos.common.tile.TileTMOSBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;

public class ContainerTMOSBase extends Container {

    TileTMOSBase tile;

    protected ContainerTMOSBase(TileTMOSBase tile) {
        this.tile = tile;
    }

    public  int getSizeInventory() {
        return tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN) ? tile.getInvSlotCount() : 0;
    }

    protected boolean performMerge(int slotIndex, ItemStack stack) {

        int invBase = getSizeInventory();
        int invFull = inventorySlots.size();

        if (slotIndex < invBase) {
            return mergeItemStack(stack, invBase, invFull, true);
        }
        return mergeItemStack(stack, 0, invBase, false);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = null;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack inSlot = slot.getStack();
            stack = inSlot.copy();

            if (!performMerge(index, inSlot)) {
                return null;
            }

            slot.onSlotChange(inSlot, stack);

            if (inSlot.getCount() <= 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.putStack(inSlot);
            }

            if (inSlot.getCount() == stack.getCount()) {
                return null;
            }
            slot.onTake(player, inSlot);
        }
        return stack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
