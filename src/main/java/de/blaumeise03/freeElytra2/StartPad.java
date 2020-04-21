package de.blaumeise03.freeElytra2;

import de.blaumeise03.blueUtils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class StartPad {
    private static List<StartPad> startPads = new ArrayList<>();
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private int z1;
    private int z2;
    private String name;
    private World world;

    public StartPad(String name, int x1, int x2, int y1, int y2, int z1, int z2, World world) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
        this.world = world;
        this.name = name;
        startPads.add(this);
    }

    public StartPad(String name, int x1, int y1, int z1, int x2, int y2, int z2, World world, boolean createNew) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
        this.world = world;
        this.name = name;
        startPads.add(this);
        if(createNew){
            save(this, FreeElytra2.getPadConfig());
        }
    }

    public boolean isOnThisPad(Location loc){
        boolean isX;
        boolean isY;
        boolean isZ;
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        isX = (x <= x1 && x2 <= x) || (x >= x1 && x2 >= x);
        isY = (y <= y1 && y2 <= y) || (y >= y1 && y2 >= y);
        isZ = (z <= z1 && z2 <= z) || (z >= z1 && z2 >= z);
        //Bukkit.broadcastMessage(isX + " " + isY + " " + isZ);
        return  isX && isY && isZ && loc.getWorld() == world;
    }

    public static void load(Configuration config) throws CorruptedConfigurationException {
        startPads.clear();
        Set<String> padsRaw = config.getKeys(false);
        for(String padRaw : padsRaw){
            int x1 = config.getInt(padRaw + ".X1");
            int x2 = config.getInt(padRaw + ".X2");
            int y1 = config.getInt(padRaw + ".Y1");
            int y2 = config.getInt(padRaw + ".Y2");
            int z1 = config.getInt(padRaw + ".Z1");
            int z2 = config.getInt(padRaw + ".Z2");
            String worldRaw = config.getString(padRaw + ".World");
            if(worldRaw == null) throw new CorruptedConfigurationException("World for pad " + padRaw + " is missing!");
            World world = Bukkit.getWorld(UUID.fromString(worldRaw));
            if(world == null) throw new CorruptedConfigurationException("World for pad " + padRaw + " does not exist!");
            new StartPad(padRaw, x1, x2, y1, y2, z1, z2, world);
        }
        FreeElytra2.getPlugin().getLogger().warning("Loaded " + startPads.size() + " StartPads!");
    }

    public static void save(StartPad pad, Configuration config){
        config.set(pad.name + ".X1", pad.x1);
        config.set(pad.name + ".X2", pad.x2);
        config.set(pad.name + ".Y1", pad.y1);
        config.set(pad.name + ".Y2", pad.y2);
        config.set(pad.name + ".Z1", pad.z1);
        config.set(pad.name + ".Z2", pad.z2);
        config.set(pad.name + ".World", pad.world.getUID().toString());
    }

    public static boolean isOnPad(Location loc){
        for(StartPad pad : startPads){
            if(pad.isOnThisPad(loc)) return true;
        }
        return false;
    }

    public static boolean doesExists(String name){
        for(StartPad pad : startPads){
            if(pad.name.equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static void deletePad(String name, Configuration config){
        for(StartPad pad : startPads){
            if(pad.name.equalsIgnoreCase(name)){
                startPads.remove(pad);
                config.set(pad.name, null);
                break;
            }
        }
    }

    public static List<StartPad> getStartPads(){
        return startPads;
    }

    public String getName() {
        return name;
    }
}
