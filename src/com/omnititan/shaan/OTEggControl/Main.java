package com.omnititan.shaan.OTEggControl;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;



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
    public void onCreatureSpawn(CreatureSpawnEvent e){
        //Create plotme IWorld instances for world check
        IWorld world = new BukkitWorld(e.getEntity().getWorld());
        //If in a plotworld then do eggcontrol
        if (PlotMeCoreManager.getInstance().isPlotWorld(world) && e.getSpawnReason().name().equals("SPAWNER_EGG")) {
            //Save location to Plotme usable location class
            ILocation loc = new BukkitLocation(e.getLocation());
            //Get the plots ID for later use
            String plotID = PlotMeCoreManager.getInstance().getPlotId(loc);
            Bukkit.getServer().broadcastMessage(plotID);
            //Check if there is actually a plot at this location, getPlotId returns an empty string if nothing is found.
            if (plotID.equals("")) {
                e.setCancelled(true);
                return;
            }

            //Set loc equal to plot location
            loc = PlotMeCoreManager.getInstance().getPlotBottomLoc(world, plotID);
            // Get Plot Size from the config
            int configSize = getConfig().getInt("worlds." + world.getName() + ".size");
            // Create modifier, which is the halfway point of x and z of the plot
            double modifier = (double) (configSize / 2 + 1);
            //Calculate centre point of the plot for entity spawn location variable
            Location centrePoint = new Location(e.getEntity().getWorld(), loc.getX() + modifier, loc.getY(), loc.getZ() + modifier);
            EntityType orb = EntityType.EXPERIENCE_ORB;
            //Spawn an experience orb at the centre point of the plot
            org.bukkit.entity.Entity tempOrb = Bukkit.getServer().getWorld(e.getEntity().getWorld().getName()).spawnEntity(centrePoint, orb);
            //Number of entities within the specified plot
            int pets = tempOrb.getNearbyEntities(modifier, 124, modifier).size();
            //If too many entities then stop the spawning of any more mobs.
            //TODO VERY IMPORTANT - Add in check for entities being mobs otherwise experience orbs and carts/boats are included in the count, possibly use a stream to check against all elements in the nearby entities.
            if (pets >= getConfig().getInt("worlds." + world.getName() + ".allowed-entities")) {
                Bukkit.getServer().broadcastMessage(Integer.toString(getConfig().getInt("worlds." + world.getName() + ".allowed-entities")));
                Bukkit.getServer().broadcastMessage("Cancelled due to " + pets);
                e.setCancelled(true);
                return;
            }
        }
    }
}
// Test for Git Commit