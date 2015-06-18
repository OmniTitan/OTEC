package com.omnititan.shaan.OTECPlotMe;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Shaan on 13/06/2015.
 */
public class Main extends JavaPlugin implements Listener {

    List<String> entityWatchList = new ArrayList<String>();


    @Override
    public void onEnable() {
        System.out.println("OT Egg Control Initiated");

        //List of all entities to count when checking for nearby entities.
        entityWatchList.add("BAT");
        entityWatchList.add("BLAZE");
        entityWatchList.add("CAVE_SPIDER");
        entityWatchList.add("CHICKEN");
        entityWatchList.add("COW");
        entityWatchList.add("CREEPER");
        entityWatchList.add("ENDER_DRAGON");
        entityWatchList.add("ENDERMITE");
        entityWatchList.add("GHAST");
        entityWatchList.add("GIANT");
        entityWatchList.add("GUARDIAN");
        entityWatchList.add("HORSE");
        entityWatchList.add("IRON_GOLEM");
        entityWatchList.add("MAGMA_CUBE");
        entityWatchList.add("MINECART");
        entityWatchList.add("MUSHROOM_COW");
        entityWatchList.add("OCELOT");
        entityWatchList.add("PIG");
        entityWatchList.add("PIG_ZOMBIE");
        entityWatchList.add("RABBIT");
        entityWatchList.add("SHEEP");
        entityWatchList.add("SILVERFISH");
        entityWatchList.add("SKELETON");
        entityWatchList.add("SLIME");
        entityWatchList.add("SNOWMAN");
        entityWatchList.add("SPIDER");
        entityWatchList.add("SQUID");
        entityWatchList.add("VILLAGER");
        entityWatchList.add("WITCH");
        entityWatchList.add("WITHER");
        entityWatchList.add("WOLF");
        entityWatchList.add("ZOMBIE");

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
            List entities = tempOrb.getNearbyEntities(modifier, 124, modifier);
            tempOrb.remove();
            //Check to see if entity is a mob that is dissalowed to be over spawned or to be measured against
            List<Entity> pets = new ArrayList<>();
            Iterator iterator = entities.iterator();
            while (iterator.hasNext()) {
                Entity tempEnt = (Entity) iterator.next();
                if (entityWatchList.contains(tempEnt.getType().name())) {
                    pets.add(tempEnt);
                }
            }

//            pets.get(1).
            //If too many entities then stop the spawning of any more mobs.

            if (pets.size() >= getConfig().getInt("worlds." + world.getName() + ".allowed-entities")) {
                e.setCancelled(true);
                return;
            }
        }
    }

}