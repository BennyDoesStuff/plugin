package com.danielgulic.minehutbans;

import org.bukkit.plugin.java.*;
import org.bstats.bukkit.Metrics;
import khttp.*;
import khttp.responses.*;
import lombok.Getter;

@Getter
public final class MinehutBans extends JavaPlugin {

    public static MinehutBans instance;
    public static MinehutBans get() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        Metrics metrics = new Metrics(this);

        saveDefaultConfig();

        // Check if we can access the API
        final String url = getConfig().getString("api");
        final Response res = KHttp.get(url);
        if (res.getStatusCode() == 200) {
            getLogger().info("Connected to API: " + url);
        }

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void onDisable() {
        // Plugin shutdown logic
    }

}