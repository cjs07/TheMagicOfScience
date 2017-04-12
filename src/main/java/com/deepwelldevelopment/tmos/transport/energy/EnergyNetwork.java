package com.deepwelldevelopment.tmos.transport.energy;

import cofh.api.energy.IEnergyReceiver;
import com.deepwelldevelopment.tmos.api.energy.EnergyStack;
import com.deepwelldevelopment.tmos.lib.util.Coord4D;
import com.deepwelldevelopment.tmos.transport.core.transmitter.DynamicNetwork;
import com.deepwelldevelopment.tmos.transport.core.transmitter.IGridTransmitter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class EnergyNetwork extends DynamicNetwork<IEnergyReceiver, EnergyNetwork> {

    private double lastPowerScale = 0;
    private double joulesTransmitted = 0;
    private double jouleBufferLastTick = 0;

    public double clientEnergyScale = 0;

    public EnergyStack buffer = new EnergyStack(0);

    public EnergyNetwork() {
    }

    public EnergyNetwork(Collection<EnergyNetwork> networks) {
        for (EnergyNetwork net : networks) {
            if (net != null) {
                if (net.jouleBufferLastTick > jouleBufferLastTick || net.clientEnergyScale > clientEnergyScale) {
                    clientEnergyScale = net.clientEnergyScale;
                    jouleBufferLastTick = net.jouleBufferLastTick;
                    joulesTransmitted = net.joulesTransmitted;
                    lastPowerScale = net.lastPowerScale;
                }

                buffer.amount += net.buffer.amount;

                adoptTransmittersAndAcceptorsFrom(net);
                net.deregister();
            }
        }

        register();
    }

    public static double round(double d) {
        return Math.round(d * 10000) / 10000;
    }

    @Override
    public void absorbBuffer(IGridTransmitter<IEnergyReceiver, EnergyNetwork> transmitter) {
        EnergyStack energy = (EnergyStack) transmitter.getBuffer();
        buffer.amount += energy.amount;
        energy.amount = 0;
    }

    @Override
    public void clampBuffer() {
        if (buffer.amount > getCapacity()) {
            buffer.amount = getCapacity();
        }

        if (buffer.amount < 0) {
            buffer.amount = 0;
        }
    }

    @Override
    protected void updateMeanCapacity() {
        int numCables = transmitters.size();
        double reciprocalSum = 0;

        for (IGridTransmitter<IEnergyReceiver, EnergyNetwork> cable : transmitters) {
            reciprocalSum += 1.0 / (double) cable.getCapacity();
        }

        meanCapacity = (double) numCables / reciprocalSum;
    }

    public double getEnergyNeeded() {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            return 0;
        }

        return getCapacity() - buffer.amount;
    }

    public double tickEmit(double energyToSend) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            return 0;
        }

        double sent = 0;
        boolean tryAgain;
        int i = 0;

        do {
            double prev = sent;
            sent += doEmit(energyToSend - sent);

            tryAgain = energyToSend - sent > 0 && sent - prev > 0 && i < 100;

            i++;
        } while (tryAgain);

        joulesTransmitted = sent;

        return sent;
    }

    public double emit(double energyToSend, boolean doEmit) {
        double toUse = Math.min(getEnergyNeeded(), energyToSend);

        if (doEmit) {
            buffer.amount += toUse;
        }

        return energyToSend - toUse;
    }

    /**
     * @return sent
     */
    public double doEmit(double energyToSend) {
        double sent = 0;

        List<Pair<Coord4D, IEnergyReceiver>> availableAcceptors = new ArrayList<>();
        availableAcceptors.addAll(getAcceptors(null));

        Collections.shuffle(availableAcceptors);

        if (!availableAcceptors.isEmpty()) {
            int divider = availableAcceptors.size();
            double remaining = energyToSend % divider;
            double sending = (energyToSend - remaining) / divider;

            for (Pair<Coord4D, IEnergyReceiver> pair : availableAcceptors) {
                IEnergyReceiver acceptor = pair.getRight();
                double currentSending = sending + remaining;
                EnumSet<EnumFacing> sides = acceptorDirections.get(pair.getLeft());

                if (sides == null || sides.isEmpty()) {
                    continue;
                }

                for (EnumFacing side : sides) {
                    double prev = sent;

                    sent += acceptor.receiveEnergy(side, (int) currentSending, false);

                    if (sent > prev) {
                        break;
                    }
                }
            }
        }

        return sent;
    }

    @Override
    public Set<Pair<Coord4D, IEnergyReceiver>> getAcceptors(Object data) {
        Set<Pair<Coord4D, IEnergyReceiver>> toReturn = new HashSet<>();

        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            return toReturn;
        }

        for (Coord4D coord : possibleAcceptors.keySet()) {
            EnumSet<EnumFacing> sides = acceptorDirections.get(coord);

            if (sides == null || sides.isEmpty()) {
                continue;
            }

            TileEntity tile = coord.getTileEntity(getWorld());

            for (EnumFacing side : sides) {
                IEnergyReceiver acceptor = null;
                if (tile instanceof IEnergyReceiver) {
                    acceptor = ((IEnergyReceiver) tile);
                }

                if (acceptor != null) {
                    if (acceptor.receiveEnergy(side, 1, true) > 0) {
                        toReturn.add(Pair.of(coord, acceptor));
                        break;
                    }
                }
            }
        }

        return toReturn;
    }

    public static class EnergyTransferEvent extends Event {
        public final EnergyNetwork energyNetwork;

        public final double power;

        public EnergyTransferEvent(EnergyNetwork network, double currentPower) {
            energyNetwork = network;
            power = currentPower;
        }
    }

    @Override
    public String toString() {
        return "[EnergyNetwork] " + transmitters.size() + " transmitters, " + possibleAcceptors.size() + " acceptors.";
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        clearJoulesTransmitted();

        double currentPowerScale = getPowerScale();

        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            if (Math.abs(currentPowerScale - lastPowerScale) > 0.01 || (currentPowerScale != lastPowerScale && (currentPowerScale == 0 || currentPowerScale == 1))) {
                needsUpdate = true;
            }

            if (needsUpdate) {
                MinecraftForge.EVENT_BUS.post(new EnergyTransferEvent(this, currentPowerScale));
                lastPowerScale = currentPowerScale;
                needsUpdate = false;
            }

            if (buffer.amount > 0) {
                buffer.amount -= tickEmit(buffer.amount);
            }
        }
    }

    public double getPowerScale() {
        return Math.max(jouleBufferLastTick == 0 ? 0 : Math.min(Math.ceil(Math.log10(getPower()) * 2) / 10, 1), getCapacity() == 0 ? 0 : buffer.amount / getCapacity());
    }

    public void clearJoulesTransmitted() {
        jouleBufferLastTick = buffer.amount;
        joulesTransmitted = 0;
    }

    public double getPower() {
        return jouleBufferLastTick * 20;
    }
}