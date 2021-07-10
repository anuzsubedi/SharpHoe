package me.anuzsubedi.sharphoe;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new AnvilFunction(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
