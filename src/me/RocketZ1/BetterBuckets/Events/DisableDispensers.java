package me.RocketZ1.BetterBuckets.Events;

import me.RocketZ1.BetterBuckets.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class DisableDispensers implements Listener {

    private Main plugin;

    public DisableDispensers(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDispenserFire(BlockDispenseEvent e) {
        if (e.isCancelled()) return;
        if (e.getBlock().getType() != Material.DISPENSER) return;
        if (!e.getItem().hasItemMeta()) return;
        ItemStack bucket = e.getItem();
        ItemMeta bucket_meta = bucket.getItemMeta();
        PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
        NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
        if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
        NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
        NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
        NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
        String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
        int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
        int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
        Dispenser dispenser_dataType = (Dispenser) e.getBlock().getBlockData();
        org.bukkit.block.Dispenser dispenser = (org.bukkit.block.Dispenser) e.getBlock().getState();
        int slotNum = 0;
        ItemStack newBucket = null;
        for (ItemStack dispenserItems : dispenser.getInventory()) {
            slotNum++;
            if (dispenserItems == null) continue;
            if (dispenserItems.equals(bucket)) {
                newBucket = dispenserItems;
                slotNum--;
                break;
            }
        }
        if (newBucket == null) {
            return;
        }
        ItemMeta newBucket_meta = newBucket.getItemMeta();
        PersistentDataContainer NewItem = newBucket_meta.getPersistentDataContainer();
        if (BucketType.equalsIgnoreCase("Lava") && bucket.getType() == Material.LAVA_BUCKET) {
            LiquidAmt--;
            if (LiquidAmt == 0 && BucketType.equalsIgnoreCase("Lava")) {
                e.setCancelled(true);
                NewItem.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                NewItem.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                BucketType = "Empty";
                newBucket_meta.setDisplayName(plugin.format("&7Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&7Filled: " + NewItem.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Capacity: " + NewItem.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Type: " + BucketType));
                newBucket_meta.setLore(bucket_lore);
                newBucket.setType(Material.BUCKET);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                e.getBlock().getRelative(dispenser_dataType.getFacing()).setType(Material.LAVA);
                newBucket.setItemMeta(newBucket_meta);
                dispenser.getInventory().setItem(slotNum, newBucket);
                dispenser.update();
            } else if (LiquidAmt > 0 && BucketType.equalsIgnoreCase("Lava")) {
                e.setCancelled(true);
                NewItem.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                NewItem.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                newBucket_meta.setDisplayName(plugin.format("&6Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&6Filled: " + NewItem.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Capacity: " + NewItem.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Type: " + BucketType));
                newBucket_meta.setLore(bucket_lore);
                newBucket.setType(Material.BUCKET);
                e.getBlock().getRelative(dispenser_dataType.getFacing()).setType(Material.LAVA);
                newBucket.setItemMeta(newBucket_meta);
                dispenser.getInventory().setItem(slotNum, newBucket);
                dispenser.update();
            }
        }else if (BucketType.equalsIgnoreCase("Lava") && bucket.getType() == Material.BUCKET) {
            if (e.getBlock().getRelative(dispenser_dataType.getFacing()).getType() == Material.LAVA) {
                LiquidAmt++;
                e.setCancelled(true);
                NewItem.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                NewItem.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                newBucket_meta.setDisplayName(plugin.format("&6Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&6Filled: " + NewItem.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Capacity: " + NewItem.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Type: " + BucketType));
                newBucket_meta.setLore(bucket_lore);
                newBucket.setType(Material.LAVA_BUCKET);
                e.getBlock().getRelative(dispenser_dataType.getFacing()).setType(Material.AIR);
                newBucket.setItemMeta(newBucket_meta);
                dispenser.getInventory().setItem(slotNum, newBucket);
                dispenser.update();
            }
        }

        if (BucketType.equalsIgnoreCase("Water") && bucket.getType() == Material.WATER_BUCKET) {
            LiquidAmt--;
            if (LiquidAmt == 0 && BucketType.equalsIgnoreCase("Water")) {
                e.setCancelled(true);
                NewItem.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                NewItem.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                BucketType = "Empty";
                newBucket_meta.setDisplayName(plugin.format("&7Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&7Filled: " + NewItem.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Capacity: " + NewItem.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Type: " + BucketType));
                newBucket_meta.setLore(bucket_lore);
                newBucket.setType(Material.BUCKET);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                e.getBlock().getRelative(dispenser_dataType.getFacing()).setType(Material.WATER);
                newBucket.setItemMeta(newBucket_meta);
                dispenser.getInventory().setItem(slotNum, newBucket);
                dispenser.update();
            } else if (LiquidAmt > 0 && BucketType.equalsIgnoreCase("Water")) {
                e.setCancelled(true);
                NewItem.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                NewItem.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                newBucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&9Filled: " + NewItem.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Capacity: " + NewItem.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Type: " + BucketType));
                newBucket_meta.setLore(bucket_lore);
                newBucket.setType(Material.BUCKET);
                e.getBlock().getRelative(dispenser_dataType.getFacing()).setType(Material.WATER);
                newBucket.setItemMeta(newBucket_meta);
                dispenser.getInventory().setItem(slotNum, newBucket);
                dispenser.update();
            }
        } else if (BucketType.equalsIgnoreCase("Water") && bucket.getType() == Material.BUCKET) {
            if (e.getBlock().getRelative(dispenser_dataType.getFacing()).getType() == Material.WATER) {
                LiquidAmt++;
                e.setCancelled(true);
                NewItem.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                NewItem.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                newBucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&9Filled: " + NewItem.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Capacity: " + NewItem.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Type: " + BucketType));
                newBucket_meta.setLore(bucket_lore);
                newBucket.setType(Material.WATER_BUCKET);
                e.getBlock().getRelative(dispenser_dataType.getFacing()).setType(Material.AIR);
                newBucket.setItemMeta(newBucket_meta);
                dispenser.getInventory().setItem(slotNum, newBucket);
                dispenser.update();
            }
        }
        if (BucketType.equalsIgnoreCase("Empty") && bucket.getType() == Material.BUCKET) {
            if (e.getBlock().getRelative(dispenser_dataType.getFacing()).getType() == Material.WATER) {
                LiquidAmt++;
                e.setCancelled(true);
                NewItem.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                NewItem.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                BucketType = "Water";
                newBucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&9Filled: " + NewItem.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Capacity: " + NewItem.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Type: " + BucketType));
                newBucket_meta.setLore(bucket_lore);
                newBucket.setType(Material.WATER_BUCKET);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                e.getBlock().getRelative(dispenser_dataType.getFacing()).setType(Material.AIR);
                newBucket.setItemMeta(newBucket_meta);
                dispenser.getInventory().setItem(slotNum, newBucket);
                dispenser.update();
            } else if (e.getBlock().getRelative(dispenser_dataType.getFacing()).getType() == Material.LAVA) {
                LiquidAmt++;
                e.setCancelled(true);
                NewItem.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                NewItem.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                BucketType = "Lava";
                newBucket_meta.setDisplayName(plugin.format("&6Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&6Filled: " + NewItem.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Capacity: " + NewItem.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Type: " + BucketType));
                newBucket_meta.setLore(bucket_lore);
                newBucket.setType(Material.LAVA_BUCKET);
                NewItem.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                e.getBlock().getRelative(dispenser_dataType.getFacing()).setType(Material.AIR);
                newBucket.setItemMeta(newBucket_meta);
                dispenser.getInventory().setItem(slotNum, newBucket);
                dispenser.update();
            }
        }
    }
}
