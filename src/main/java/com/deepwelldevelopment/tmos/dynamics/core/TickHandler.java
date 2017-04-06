package com.deepwelldevelopment.tmos.dynamics.core;

import com.deepwelldevelopment.tmos.dynamics.multiblock.IMultiBlock;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.WeakHashMap;

public class TickHandler {

    public static final TickHandler instance = new TickHandler();
    public static final WeakHashMap<World, WorldGridList> handlers = new WeakHashMap<World, WorldGridList>();
    public static final LinkedHashSet<WeakReference<IMultiBlock>> multiBlocksToCalculate = new LinkedHashSet<WeakReference<IMultiBlock>>();

    public static void addMultiBlockToCalculate(IMultiBlock multiBlock) {

        if (multiBlock.world() != null) {
            if (!multiBlock.world().isRemote) {
                getTickHandler(multiBlock.world()).tickingBlocks.add(multiBlock);
            }
        } else {
            synchronized (multiBlocksToCalculate) {
                multiBlocksToCalculate.add(new WeakReference<IMultiBlock>(multiBlock));
            }
        }
    }

    public static WorldGridList getTickHandler(World world) {

        if (world.isRemote) {
            throw new IllegalStateException("World Grid called client-side");
        }

        synchronized (handlers) {
            WorldGridList worldGridList = handlers.get(world);
            if (worldGridList != null) {
                return worldGridList;
            }

            worldGridList = new WorldGridList(world);
            handlers.put(world, worldGridList);
            return worldGridList;
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {

        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        synchronized (multiBlocksToCalculate) {
            if (!multiBlocksToCalculate.isEmpty()) {
                Iterator<WeakReference<IMultiBlock>> iterator = multiBlocksToCalculate.iterator();
                while (iterator.hasNext()) {
                    IMultiBlock multiBlock = iterator.next().get();
                    if (multiBlock == null) {
                        iterator.remove();
                    } else if (multiBlock.world() != null) {
                        if (!multiBlock.world().isRemote) {
                            getTickHandler(multiBlock.world()).tickingBlocks.add(multiBlock);
                        }
                        iterator.remove();
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent evt) {

        synchronized (handlers) {
            WorldGridList worldGridList = handlers.get(evt.world);
            if (worldGridList == null) {
                return;
            }

            if (evt.phase == TickEvent.Phase.START) {
                worldGridList.tickStart();
            } else {
                worldGridList.tickEnd();
            }
        }
    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload evt) {

        World world = evt.getWorld();

        if (world.isRemote) {
            return;
        }

        synchronized (handlers) {
            handlers.remove(world);
            handlers.isEmpty();
        }

        synchronized (multiBlocksToCalculate) {
            if (!multiBlocksToCalculate.isEmpty()) {
                Iterator<WeakReference<IMultiBlock>> iterator = multiBlocksToCalculate.iterator();
                while (iterator.hasNext()) {
                    IMultiBlock multiBlock = iterator.next().get();
                    if (multiBlock == null || multiBlock.world() == world) {
                        iterator.remove();
                    }
                }
            }
        }
    }
}
