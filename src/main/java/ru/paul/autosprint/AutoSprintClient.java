package ru.paul.autosprint;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public final class AutoSprintClient implements ClientModInitializer {
    private static final double MOVEMENT_EPSILON = 1.0E-6;
    private static boolean hasPreviousPosition;
    private static double lastX;
    private static double lastZ;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(AutoSprintClient::forceSprint);
    }

    private static void forceSprint(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || !player.isAlive() || player.isSpectator()) {
            hasPreviousPosition = false;
            return;
        }

        double currentX = player.getX();
        double currentZ = player.getZ();

        if (!hasPreviousPosition) {
            lastX = currentX;
            lastZ = currentZ;
            hasPreviousPosition = true;
        }

        double deltaX = currentX - lastX;
        double deltaZ = currentZ - lastZ;
        boolean isMoving = (deltaX * deltaX) + (deltaZ * deltaZ) > MOVEMENT_EPSILON;
        boolean shouldSprint = isMoving && !player.isSneaking();
        player.setSprinting(shouldSprint);

        lastX = currentX;
        lastZ = currentZ;
    }
}
