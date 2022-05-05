package de.blaumeise03.freeElytra2;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Elytra {
    private static ItemStack elytra;
    private static Map<Player, ItemStack> playersWithElytra = new HashMap<>();
    private static Map<Player, Long> playerDamageCooldown = new HashMap<>();
    public static Map<Player, Long> playerShootCooldown = new HashMap<>();

    private static final int cooldown = 5000;

    public static void setUp(){
        elytra = new ItemStack(Material.ELYTRA);
        //elytra.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        elytra.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        elytra.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        ItemMeta meta = elytra.getItemMeta();
        assert meta != null;
        meta.setLore(Arrays.asList("§6Wird nach dem Flug automagisch zurückgegeben!"));
        meta.setDisplayName("§4Leih-Elytren");
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        elytra.setItemMeta(meta);
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(elytra);
        NBTTagCompound tag = stack.u();
        if(tag == null) tag = new NBTTagCompound();
        tag.a("freeElytra", true);
        //stack.b(tag);
        elytra = CraftItemStack.asBukkitCopy(stack);
    }

    public static boolean equals(ItemStack stack){
        //Bukkit.broadcastMessage("Test1");
        if(stack != null) {
            net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
            NBTTagCompound tag = nmsStack.u();
            if(tag != null)
                if(tag.b("freeElytra"))
                    return true;
            if (stack.getType() == elytra.getType())
                if (stack.hasItemMeta()) {
                    ItemMeta stackMeta = stack.getItemMeta();
                    assert stackMeta != null;
                    ItemMeta elytraMeta = elytra.getItemMeta();
                    assert elytraMeta != null;
                    if (stackMeta.hasDisplayName()) {
                        //Bukkit.broadcastMessage("Test2");
                        if (stackMeta.getDisplayName().equals(elytraMeta.getDisplayName())) {
                            //Bukkit.broadcastMessage("Test3");
                            if (stackMeta.hasLore()) {
                                //Bukkit.broadcastMessage("Test4");
                                for (String s : Objects.requireNonNull(stackMeta.getLore())) {
                                    boolean lineFound = false;
                                    for (String e : Objects.requireNonNull(elytraMeta.getLore()))
                                        if (s.equalsIgnoreCase(e)) {
                                            lineFound = true;
                                            //Bukkit.broadcastMessage("Test5");
                                            break;
                                        }
                                    if (!lineFound) return false;
                                }
                                FreeElytra2.getPlugin().getLogger().info("Founded Elytra from FreeElytra without tag.");
                                return true;
                            }
                        }
                    }
                }
        }
        return false;
    }

    public static void shootPlayer(Player p){
        if(playersWithElytra.containsKey(p)){
            removeElytra(p);
        }
        ItemStack chestplate = p.getInventory().getChestplate();
        if(chestplate != null){
            if(p.getInventory().firstEmpty() == -1){
                p.sendMessage("§4Dein Inventar ist voll!");
                return;
            }
            p.getInventory().addItem(chestplate);
            p.getInventory().setChestplate(new ItemStack(Material.AIR));
        }
        p.addScoreboardTag("freeElytra-Player");
        FreeElytra2.getPlugin().getLogger().info("Player " + p.getName() + " gets an elytra!");
        playerShootCooldown.put(p, System.currentTimeMillis());
        playerDamageCooldown.put(p, -1L);
        ItemStack elytraClone = getElytra();
        playersWithElytra.put(p, elytraClone);
        p.getInventory().setChestplate(elytraClone);
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 4, 5, false,false,false));
        Vector vec = new Vector(1.5, 10, 1.5);
        p.setVelocity(vec);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                p.setGliding(true);
            }
        };
        runnable.runTaskLater(FreeElytra2.getPlugin(), 5);
        p.sendMessage("§aViel Spaß mit den Elytren!");
    }

    public static void removeElytra(Player p){
        removeElytra(p, false);
    }

    public static void removeElytra(Player p, boolean force){
        if((playersWithElytra.containsKey(p) && !hasPlayerShootCooldown(p)) || force ) {
            if(p.getInventory().getChestplate() != null){
                if(p.getInventory().getChestplate().getType() == Material.ELYTRA){
                    if(Elytra.equals(p.getInventory().getChestplate())){
                        FreeElytra2.getPlugin().getLogger().info("Removing elytra from " + p.getName() + "!");
                        p.getInventory().setChestplate(new ItemStack(Material.AIR));
                        playersWithElytra.remove(p);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 5, false, false, false));
                    }
                }
            }
            if(p.getInventory().contains(Material.ELYTRA)){
                for(ItemStack stack : p.getInventory()) {
                    if(Elytra.equals(stack)) {
                        FreeElytra2.getPlugin().getLogger().info("Removing elytra from " + p.getName() + "!");
                        p.getInventory().remove(stack);
                        playersWithElytra.remove(p);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 5, false, false, false));
                    }
                }
            }
        }
    }

    public static void setDamageProtection(Player p, long time) {
        playerDamageCooldown.put(p, time);
    }

    public static boolean hasPlayerDamageProtection(Player p){
        long time = playerDamageCooldown.getOrDefault(p, 0L);
        if(System.currentTimeMillis() - time > cooldown){
            playerDamageCooldown.remove(p);
            return false;
        }
        return time == -1 || time - System.currentTimeMillis() <= cooldown;
    }

    public static boolean hasPlayerElytra(Player p){
        return playersWithElytra.containsKey(p);
    }

    public static ItemStack getElytra(){
        return elytra.clone();
    }

    public static boolean hasPlayerShootCooldown(Player p){
        long last = playerShootCooldown.getOrDefault(p, (long) -1);
        /*Bukkit.broadcastMessage(last + " " + System.currentTimeMillis() + "  "
                + (System.currentTimeMillis() - last)
                + " " + ((System.currentTimeMillis() - last) <= 1000));*/
        return (System.currentTimeMillis() - last) <= 1000;
    }
}
