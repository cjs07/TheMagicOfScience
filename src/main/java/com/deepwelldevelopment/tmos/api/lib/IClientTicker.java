package com.deepwelldevelopment.tmos.api.lib;

public interface IClientTicker {
    public void clientTick();

    public boolean needsTicks();
}