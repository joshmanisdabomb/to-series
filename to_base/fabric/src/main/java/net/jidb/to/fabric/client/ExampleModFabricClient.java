package net.jidb.to.fabric.client;

import net.fabricmc.api.ClientModInitializer;

public final class ExampleModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("fabric client");
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }
}
