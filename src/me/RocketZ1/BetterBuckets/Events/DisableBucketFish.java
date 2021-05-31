package me.RocketZ1.BetterBuckets.Events;

import me.RocketZ1.BetterBuckets.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DisableBucketFish implements Listener {

    private Main plugin;

    public DisableBucketFish(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void DisableFish(PlayerInteractEntityEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        if (e.getRightClicked().getType() == EntityType.PUFFERFISH) {
            if(checkForFish(p)){
                e.setCancelled(true);
                p.sendMessage(plugin.format("&cCannot pickup fish with this bucket!"));
            }
        } else if (e.getRightClicked().getType() == EntityType.TROPICAL_FISH) {
            if(checkForFish(p)){
                e.setCancelled(true);
                p.sendMessage(plugin.format("&cCannot pickup fish with this bucket!"));
            }
        } else if (e.getRightClicked().getType() == EntityType.SALMON) {
            if(checkForFish(p)){
                e.setCancelled(true);
                p.sendMessage(plugin.format("&cCannot pickup fish with this bucket!"));
            }
        } else if (e.getRightClicked().getType() == EntityType.COD) {
            if(checkForFish(p)){
                e.setCancelled(true);
                p.sendMessage(plugin.format("&cCannot pickup fish with this bucket!"));
            }
        }
    }

    public boolean checkForFish(Player p) {
        if (p.getInventory().getItemInMainHand().getType() == Material.WATER_BUCKET) {
            if (p.getInventory().getItemInMainHand().hasItemMeta()) {
                ItemStack bucket = p.getInventory().getItemInMainHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (item.has(BetterBucketKey, PersistentDataType.STRING)) return true;
            }
        } else if (p.getInventory().getItemInOffHand().getType() == Material.WATER_BUCKET) {
            if (p.getInventory().getItemInOffHand().hasItemMeta()) {
                ItemStack bucket = p.getInventory().getItemInOffHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (item.has(BetterBucketKey, PersistentDataType.STRING)) return true;
            }
        }
        return false;
    }
}