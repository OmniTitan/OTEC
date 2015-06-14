package com.omnititan.shaan.OTEggControl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shaan on 13/06/2015.
 */
public class Main extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        System.out.println("OT Egg Control Initiated");
        getConfig().options().copyDefaults(true);
        saveConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.sendMessage("hello from join");
        List<OTPlayer> players = new ArrayList<OTPlayer>();
        OTPlayer p = new OTPlayer(e.getPlayer());
        players.add(p);
    }
    @EventHandler
    public void onPlayerThrow(PlayerEggThrowEvent c){

        OTPlayer p = new OTPlayer(c.getPlayer());
        p.setDate();
        c.getPlayer().sendMessage("Hello from Event");
        p.getDateDiff();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        //TODO Add in check for who spawns egg so can send them a message.
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e){
        //TODO Make this only work in Plotworld
        //TODO Seperate into its own class for readability and expandability
        double x = getConfig().getDouble("StopEggSpawnArea.x");
        double y = getConfig().getDouble("StopEggSpawnArea.y");
        double z = getConfig().getDouble("StopEggSpawnArea.z");
        if (e.getEntity().getNearbyEntities(x, y, z).size() > getConfig().getInt("StopEggSpawnAmount")){
            e.setCancelled(true);
            return;
        }
        return;
    }


}
// Test for Git Commit