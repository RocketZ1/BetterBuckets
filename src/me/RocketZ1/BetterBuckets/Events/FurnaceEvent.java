package me.RocketZ1.BetterBuckets.Events;

import me.RocketZ1.BetterBuckets.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class FurnaceEvent implements Listener {

    private Main plugin;
    public FurnaceEvent(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBurn(FurnaceBurnEvent e) {
        if (e.isCancelled()) return;
        if (!e.getFuel().hasItemMeta()) return;
        ItemStack bucket = e.getFuel();
        ItemMeta bucket_meta = bucket.getItemMeta();
        PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
        NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
        if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
        NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
        NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
        NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
        String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
        if (BucketType.equalsIgnoreCase("Lava") && bucket.getType() == Material.LAVA_BUCKET) {
            if(e.getFuel().getAmount() > 1){
                e.setCancelled(true);
                return;
            }
            int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
            int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
            LiquidAmt--;
            if (LiquidAmt == 0 && BucketType.equalsIgnoreCase("Lava")) {
                item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                BucketType = "Empty";
                bucket_meta.setDisplayName(plugin.format("&7Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&7Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                bucket.setType(Material.BUCKET);
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                e.setBurning(false);
                e.setBurnTime(1000 * 20);
            }else if (LiquidAmt > 0 && BucketType.equalsIgnoreCase("Lava")) {
                item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                bucket_meta.setDisplayName(plugin.format("&6Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&6Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.setBurning(false);
                e.setBurnTime(1000 * 20);
            }
            bucket.setItemMeta(bucket_meta);
        }
    }
}
