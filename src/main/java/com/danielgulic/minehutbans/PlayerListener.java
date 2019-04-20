package com.danielgulic.minehutbans;

import khttp.KHttp;
import khttp.responses.Response;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerListener implements Listener {

    @EventHandler
    public void beforePlayerLogin(AsyncPlayerPreLoginEvent event) {
        final String url = MinehutBans.get().getConfig().getString("api");
        final Boolean bypass = MinehutBans.get().getConfig().getBoolean("allow-ops-bypass");
        final OfflinePlayer player = Bukkit.getOfflinePlayer(event.getUniqueId());
        if (player.isOp() && bypass) return;
        final String requestUrl = url + "/api/blacklisted_players/" + event.getUniqueId().toString();
        final Response res = KHttp.get(requestUrl);
        if (res.getJsonObject().getBoolean("ok") == true && res.getJsonObject().getBoolean("blacklisted") == true) {
            final JSONObject playerResponse = res.getJsonObject().getJSONObject("player");
            final Date issued = new Date(playerResponse.getLong("createdAt"));
            final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            final String issuedFormat = df.format(issued);
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', "&7You are on the MinehutBans blacklist!\n\n&cReason: &f" + playerResponse.getString("reason") + "\n&cIssued: &f" + issuedFormat + "\n\n&fLearn more: &b&nhttps://u.jlz.fun/minehutbans"));
            MinehutBans.get().getLogger().info("Blocked " + event.getName() + " from joining");
        }
    }

}
