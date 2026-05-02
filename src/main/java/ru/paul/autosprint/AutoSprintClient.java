package ru.paul.autosprint;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public final class AutoSprintClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(AutoSprintClient::forceSprint);
        ClientTickEvents.END_CLIENT_TICK.register(AutoSprintClient::forceSprint);
    }

    private static void forceSprint(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || !player.isAlive() || player.isSpectator()) {
            return;
        }

        boolean shouldSprint = player.input != null && player.input.movementForward > 0.0F && !player.isSneaking();
        player.setSprinting(shouldSprint);
    }
}
