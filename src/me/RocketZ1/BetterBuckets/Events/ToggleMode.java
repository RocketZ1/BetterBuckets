package me.RocketZ1.BetterBuckets.Events;

import me.RocketZ1.BetterBuckets.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Pose;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ToggleMode implements Listener {
    private Main plugin;

    public ToggleMode(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void ToggleBucket(PlayerInteractEvent e) {
        if(e.getHand() != EquipmentSlot.HAND) return;
        if (e.getPlayer().getPose() != Pose.SNEAKING) return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BUCKET) {
                ItemStack bucket = e.getPlayer().getInventory().getItemInMainHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
                NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
                String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
                if (BucketType.equalsIgnoreCase("Water") && bucket.getType() == Material.BUCKET) {
                    bucket.setType(Material.WATER_BUCKET);
                    e.getPlayer().sendMessage(plugin.format("&aBucket Toggled!"));
                } else if (BucketType.equalsIgnoreCase("Lava") && bucket.getType() == Material.BUCKET) {
                    bucket.setType(Material.LAVA_BUCKET);
                    e.getPlayer().sendMessage(plugin.format("&aBucket Toggled!"));
                }
            } else if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.WATER_BUCKET) {
                ItemStack bucket = e.getPlayer().getInventory().getItemInMainHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
                NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
                String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
                if (BucketType.equalsIgnoreCase("Water") && bucket.getType() == Material.WATER_BUCKET) {
                    bucket.setType(Material.BUCKET);
                    e.getPlayer().sendMessage(plugin.format("&aBucket Toggled!"));
                }
            } else if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LAVA_BUCKET) {
                ItemStack bucket = e.getPlayer().getInventory().getItemInMainHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
                NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
                String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
                if (BucketType.equalsIgnoreCase("Lava") && bucket.getType() == Material.LAVA_BUCKET) {
                    bucket.setType(Material.BUCKET);
                    e.getPlayer().sendMessage(plugin.format("&aBucket Toggled!"));
                }
            }
        }
    }
}