package me.RocketZ1.BetterBuckets.Events;

import me.RocketZ1.BetterBuckets.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DisableMilking implements Listener {

    private Main plugin;
    public DisableMilking(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void DisableMilkingCow(PlayerInteractEntityEvent e){
        if(e.isCancelled()) return;
        if(e.getRightClicked() instanceof Cow || e.getRightClicked() instanceof Cow) {
            Player p = e.getPlayer();
            if(p.getInventory().getItemInMainHand().getType() == Material.BUCKET && p.getInventory().getItemInMainHand().hasItemMeta()){
                ItemStack bucket = e.getPlayer().getInventory().getItemInMainHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
                e.setCancelled(true);
                p.sendMessage(plugin.format("&cYou cannot milk a cow with this bucket!"));
                p.getInventory().setItemInMainHand(e.getPlayer().getInventory().getItemInMainHand());
            }else if(p.getInventory().getItemInOffHand().getType() == Material.BUCKET && p.getInventory().getItemInOffHand().hasItemMeta()){
                ItemStack bucket = e.getPlayer().getInventory().getItemInOffHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
                e.setCancelled(true);
                p.sendMessage(plugin.format("&cYou cannot milk a cow with this bucket!"));
                p.getInventory().setItemInOffHand(e.getPlayer().getInventory().getItemInOffHand());
            }
        }
    }
}
