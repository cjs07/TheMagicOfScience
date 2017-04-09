package com.deepwelldevelopment.tmos.common.tile.generator;

import com.deepwelldevelopment.tmos.common.lib.TMOSProps;
import com.deepwelldevelopment.tmos.common.lib.util.FuelManager;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileGeneratorCoal extends TileGeneratorBase {

    private ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            TileGeneratorCoal.this.markDirty();
        }
    };

    int currentFuelRf = getEnergyValue(coal);
    int currentFuelBurnTime = TileEntityFurnace.getItemBurnTime(coal);

    public TileGeneratorCoal() {
        super(120000, 100);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList nbtTagList = nbt.getTagList("items", 10);
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
            int j = nbtTagCompound.getByte("slot");

            if (j >= 0 && j < itemStackHandler.getSlots()) {
                itemStackHandler.setStackInSlot(j, new ItemStack(nbtTagCompound));
            }
            currentFuelRf = nbt.getInteger("currentFuelRf");
            currentFuelBurnTime = nbt.getInteger("currentFuelBurnTime");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setByte("slot", (byte) i);
            itemStackHandler.getStackInSlot(i).writeToNBT(nbtTagCompound);
            nbtTagList.appendTag(nbtTagCompound);
        }
        nbt.setTag("items", nbtTagList);
        nbt.setInteger("currentFuelRf", currentFuelRf);
        nbt.setInteger("currentFuelBurnTime", currentFuelBurnTime);
        return nbt;
    }

    @Override
    public int getScaledDuration(int scale) {
        if (currentFuelRf <= 0) {
            currentFuelRf = coalRF;
        }
        return fuelRf * scale / currentFuelRf;
    }

    @Override
    public boolean canGenerate() {
        if (fuelRf > 0 || fuelTicks > 0) {
            return true;
        }
        return getEnergyValue(itemStackHandler.getStackInSlot(0)) > 0;
    }

    @Override
    public void generate() {
        ItemStack stack = itemStackHandler.getStackInSlot(0);
        if (fuelRf <= 0 && !stack.isEmpty()) {
            int energy = getEnergyValue(stack);
            fuelRf += energy;
            currentFuelRf = energy;
            fuelTicks = TileEntityFurnace.getItemBurnTime(stack);
            currentFuelBurnTime = TileEntityFurnace.getItemBurnTime(stack);
            stack.shrink(1);
        }
        int toStore = currentFuelRf / currentFuelBurnTime;
        energyStorage.modifyEnergyStored(toStore);
        fuelRf -= toStore;
        fuelTicks--;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public int getInvSlotCount() {
        return itemStackHandler.getSlots();
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemStackHandler;
        }
        return super.getCapability(capability, facing);
    }

    static int coalRF = 48000;
    static int charcoalRF = 32000;
    static int woodRF = 4500;
    static int blockCoalRF = coalRF * 10;
    static int otherRF = woodRF / 3;

    static ItemStack coal = new ItemStack(Items.COAL, 1, 0);
    static ItemStack charcoal = new ItemStack(Items.COAL, 1, 1);
    static ItemStack blockCoal = new ItemStack(Blocks.COAL_BLOCK);

    static TObjectIntHashMap<ItemStack> fuels = new TObjectIntHashMap<>();

    static {
        String category = "Fuels.Coal";
        FuelManager.configFuels.getCategory(category).setComment(
                "You can adjust fuel values for the Steam Dynamo in this section. New fuels cannot be added at this time.");
        coalRF = FuelManager.configFuels.get(category, "coal", coalRF);
        charcoalRF = FuelManager.configFuels.get(category, "charcoal", charcoalRF);
        woodRF = FuelManager.configFuels.get(category, "wood", woodRF);
        blockCoalRF = coalRF * 10;
        otherRF = woodRF / 3;
        System.out.println("[TMOS] Coal RF: " + coalRF);
    }

    public static boolean addFuel(ItemStack stack, int energy) {
        if (stack == null || energy < 640 || energy > 200000000) {
            return false;
        }
        fuels.put(stack, energy);
        return true;
    }

    public static int getEnergyValue(ItemStack stack) {
        if (stack == null) {
            return 0;
        }
        if (stack.isItemEqual(coal)) {
            return coalRF;
        }
        if (stack.isItemEqual(charcoal)) {
            return charcoalRF;
        }
        if (stack.isItemEqual(blockCoal)) {
            return blockCoalRF;
        }
        Item item = stack.getItem();

        if (stack.getItem() instanceof ItemBlock && ((ItemBlock) item).getBlock().getBlockState().getBaseState().getMaterial() == Material.WOOD) {
            return woodRF;
        }
        if (item == Items.STICK || item instanceof ItemBlock && ((ItemBlock) item).block == Blocks.SAPLING) {
            return otherRF;
        }
        return GameRegistry.getFuelValue(stack) * TMOSProps.RF_PER_MJ * 3 / 2;
    }
}
