package de.blaumeise03.freeElytra2;

import de.blaumeise03.blueUtils.AdvancedPlugin;
import de.blaumeise03.blueUtils.Configuration;
import de.blaumeise03.blueUtils.exceptions.ConfigurationNotFoundException;

public class FreeElytra2 extends AdvancedPlugin {
    private static AdvancedPlugin plugin;
    private static Configuration config;

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        Elytra.setUp();
        getLogger().info("Registering events...");
        registerEvent(new Listeners());
        getLogger().info("Adding commands...");
        Commands.addCommands();
        getLogger().info("Loading configuration...");
        config = new Configuration("config.yml", this);
        try {
            config.setup(true);
        } catch (ConfigurationNotFoundException e) {
            e.printStackTrace();
        }
        try {
            StartPad.load(config);
        } catch (CorruptedConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onReload() {
        getLogger().info("Loading configuration...");
        config.reload();
        try {
            StartPad.load(config);
        } catch (CorruptedConfigurationException e) {
            e.printStackTrace();
        }
        getLogger().info("Complete!");
        return true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info("Saving configuration...");
        config.save();
    }

    public static AdvancedPlugin getPlugin() {
        return plugin;
    }


    public static Configuration getPadConfig() {
        return config;
    }

    public static void reloadPadConfig(){
        try {
            config.setup(false);
        } catch (ConfigurationNotFoundException e) {
            e.printStackTrace();
        }
    }
}
