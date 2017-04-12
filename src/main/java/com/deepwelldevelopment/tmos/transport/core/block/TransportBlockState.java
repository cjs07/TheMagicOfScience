package com.deepwelldevelopment.tmos.transport.core.block;

import com.deepwelldevelopment.tmos.transport.core.transmitter.TransmissionType;
import net.minecraft.util.IStringSerializable;

public class TransportBlockState {

    public enum TransmitterType implements IStringSerializable {

        POWER_CABLE("PowerCable", Size.SMALL, TransmissionType.ENERGY, false, true),
        FLUID_PIPE("FluidPipe", Size.LARGE, TransmissionType.FLUID, false, true),
        TRANSFER_PIPE("TransferPipe", Size.LARGE, TransmissionType.ITEM, true, true);

        private String unlocalizedName;
        private Size size;
        private TransmissionType transmissionType;
        private boolean transparencyRender;
        private boolean tiers;

        private TransmitterType(String name, Size s, TransmissionType type, boolean transparency, boolean b) {
            unlocalizedName = name;
            size = s;
            transmissionType = type;
            transparencyRender = transparency;
            tiers = b;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }

        public String getUnlocalizedName() {
            return unlocalizedName;
        }

        public Size getSize() {
            return size;
        }

        public boolean hasTransparency() {
            return transparencyRender;
        }

        public TransmissionType getTransmission() {
            return transmissionType;
        }

        public boolean hasTiers() {
            return tiers;
        }

        public static enum Size {
            SMALL(6),
            LARGE(8);

            public int centerSize;

            private Size(int size) {
                centerSize = size;
            }
        }

        public static TransmitterType get(int meta) {
            return TransmitterType.values()[meta];
        }
    }
}
