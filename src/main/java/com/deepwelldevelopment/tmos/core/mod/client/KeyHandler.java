package com.deepwelldevelopment.tmos.core.mod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyHandler {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) {
            while( Keyboard.next() ) {
                KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());

                if(Keyboard.getEventKeyState() ) {
                    KeyBinding.onTick(Keyboard.getEventKey());

                    if(ClientProxy.KEY_UPDATE_GUI.isPressed() ) {

                    }
                }
            }
        }
    }
}
