package com.deepwelldevelopment.tmos.transport.core.transmitter;

public interface ITransmitter {
    /**
     * Get the transmitter's transmission type
     *
     * @return TransmissionType this transmitter uses
     */
    public TransmissionType getTransmissionType();
}
