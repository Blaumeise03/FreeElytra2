package de.blaumeise03.freeElytra2;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Listeners implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(Elytra.hasPlayerDamageProtection(p) || Elytra.hasPlayerElytra(p)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if(Elytra.hasPlayerElytra(p)){
            //Bukkit.broadcastMessage("t");
            if(p.isOnGround() && !p.isGliding()){
                Elytra.removeElytra(p);
            }
        }else {
            if(StartPad.isOnPad(p.getLocation())){
                if(!Elytra.hasPlayerShootCooldown(p)) {
                    Elytra.playerShootCooldown.put(p, System.currentTimeMillis());
                    Elytra.shootPlayer(p);
                }
            }
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(e.getPlayer().getScoreboardTags().contains("elytraResi")) {
            FreeElytra2.getPlugin().getLogger().warning("Player " + e.getPlayer().getName() + " has elytra-tag! Giving effect...");
            Elytra.setDamageProtection(e.getPlayer(), System.currentTimeMillis() + 5000);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 5, false, false, false), true);
            e.getPlayer().removeScoreboardTag("elytraResi");
        }
        Elytra.removeElytra(e.getPlayer(), true);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        if(Elytra.hasPlayerElytra(e.getPlayer())){
            FreeElytra2.getPlugin().getLogger().warning("Player " + e.getPlayer().getName() + " has elytra! Adding tag...");
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 8, 5, false, false, false));
            e.getPlayer().addScoreboardTag("elytraResi");
        }
        Elytra.removeElytra(e.getPlayer(), true);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if(Elytra.hasPlayerElytra(e.getPlayer())) {
            if(e.hasItem()) {
                if(e.getItem().getType() == Material.FIREWORK_ROCKET) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
