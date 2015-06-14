package com.omnititan.shaan.OTEggControl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalTime;
import java.util.Date;

/**
 * Created by Shaan on 13/06/2015.
 */
public class OTPlayer {
    long lastThrow;
    Player player;

    public OTPlayer (Player p){
        player = p;
    };



    public void setDate(){
        String world = player.getWorld().getName();
        if ((world.equals("world")) || (world.equals("inspectorplots"))){
            lastThrow = Bukkit.getServer().getWorld(world).getTime();
            return;
        }
        return;
    }

    public boolean getDateDiff(){
        String world = player.getWorld().getName();
        player.sendMessage("Hello");
        if ((world.equals("world")) || (world.equals("inspectorplots"))){
            String diff =String.valueOf(player.getWorld().getTime() - lastThrow);
            player.sendMessage(diff);
            return true;
        }
        return false;
    }
}
