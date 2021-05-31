package me.RocketZ1.BetterBuckets.Events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import me.RocketZ1.BetterBuckets.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class CauldronEvent implements Listener {

    private Main plugin;

    public CauldronEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void Cauldron(CauldronLevelChangeEvent e) {
        if(e.isCancelled()) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if(plugin.checkRegion(p, BukkitAdapter.adapt(e.getBlock().getLocation()))) return;
        if (e.getReason() == CauldronLevelChangeEvent.ChangeReason.BUCKET_FILL) {
            if (p.getInventory().getItemInMainHand().hasItemMeta()) {
                ItemStack bucket = p.getInventory().getItemInMainHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
                e.setCancelled(true);
                if (bucket.getAmount() > 1) {
                    p.sendMessage(plugin.format("&cCannot use this item while stacked!"));
                    return;
                }
                NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
                NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
                NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
                int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
                int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
                String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
                if(BucketType.equalsIgnoreCase("Lava")){
                    p.sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds lava!"));
                    return;
                }
                if (LiquidAmt >= BucketCapacity) {
                    p.sendMessage(plugin.format("&cYour bucket is full!"));
                    return;
                }
                LiquidAmt++;
                item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                if (BucketType.equalsIgnoreCase("Water")) {
                    bucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                    ArrayList<String> bucket_lore = new ArrayList<>();
                    bucket_lore.add("");
                    bucket_lore.add(plugin.format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Type: " + BucketType));
                    bucket_meta.setLore(bucket_lore);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            plugin.format("&9" + LiquidAmt + "/" + BucketCapacity)));
                } else if (BucketType.equalsIgnoreCase("Empty")) {
                    BucketType = "Water";
                    item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                    bucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                    ArrayList<String> bucket_lore = new ArrayList<>();
                    bucket_lore.add("");
                    bucket_lore.add(plugin.format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Type: " + BucketType));
                    bucket_meta.setLore(bucket_lore);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            plugin.format("&9" + LiquidAmt + "/" + BucketCapacity)));
                }
                bucket.setItemMeta(bucket_meta);
                Block yourCauldron = e.getBlock();
                Levelled cauldronData = (Levelled) yourCauldron.getBlockData();
                cauldronData.setLevel(0);
                yourCauldron.setBlockData(cauldronData);
            } else if (p.getInventory().getItemInOffHand().hasItemMeta()) {
                ItemStack bucket = p.getInventory().getItemInOffHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
                e.setCancelled(true);
                if (bucket.getAmount() > 1) {
                    p.sendMessage(plugin.format("&cCannot use this item while stacked!"));
                    return;
                }
                NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
                NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
                NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
                int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
                int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
                String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
                if(BucketType.equalsIgnoreCase("Lava")){
                    p.sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds lava!"));
                    return;
                }
                if (LiquidAmt >= BucketCapacity) {
                    p.sendMessage(plugin.format("&cYour bucket is full!"));
                    return;
                }
                LiquidAmt++;
                item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                if (BucketType.equalsIgnoreCase("Water")) {
                    bucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                    ArrayList<String> bucket_lore = new ArrayList<>();
                    bucket_lore.add("");
                    bucket_lore.add(plugin.format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Type: " + BucketType));
                    bucket_meta.setLore(bucket_lore);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            plugin.format("&9" + LiquidAmt + "/" + BucketCapacity)));
                } else if (BucketType.equalsIgnoreCase("Empty")) {
                    BucketType = "Water";
                    item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                    bucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                    ArrayList<String> bucket_lore = new ArrayList<>();
                    bucket_lore.add("");
                    bucket_lore.add(plugin.format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Type: " + BucketType));
                    bucket_meta.setLore(bucket_lore);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            plugin.format("&9" + LiquidAmt + "/" + BucketCapacity)));
                }
                bucket.setItemMeta(bucket_meta);
                Block yourCauldron = e.getBlock();
                Levelled cauldronData = (Levelled) yourCauldron.getBlockData();
                cauldronData.setLevel(0);
                yourCauldron.setBlockData(cauldronData);
            }
        }else if(e.getReason() == CauldronLevelChangeEvent.ChangeReason.BUCKET_EMPTY) {
            if (p.getInventory().getItemInMainHand().hasItemMeta()) {
                ItemStack bucket = p.getInventory().getItemInMainHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
                e.setCancelled(true);
                if (bucket.getAmount() > 1) {
                    p.sendMessage(plugin.format("&cCannot use this item while stacked!"));
                    return;
                }
                NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
                NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
                NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
                int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
                int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
                String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
                LiquidAmt--;
                item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                if (BucketType.equalsIgnoreCase("Water") && LiquidAmt > 0) {
                    bucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                    ArrayList<String> bucket_lore = new ArrayList<>();
                    bucket_lore.add("");
                    bucket_lore.add(plugin.format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Type: " + BucketType));
                    bucket_meta.setLore(bucket_lore);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            plugin.format("&9" + LiquidAmt + "/" + BucketCapacity)));
                } else if (BucketType.equalsIgnoreCase("Water") && LiquidAmt == 0) {
                    BucketType = "Empty";
                    item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                    bucket_meta.setDisplayName(plugin.format("&7Big Bucket"));
                    ArrayList<String> bucket_lore = new ArrayList<>();
                    bucket_lore.add("");
                    bucket_lore.add(plugin.format("&7Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&7Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&7Type: " + BucketType));
                    bucket_meta.setLore(bucket_lore);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            plugin.format("&7" + LiquidAmt + "/" + BucketCapacity)));
                    bucket.setType(Material.BUCKET);
                }
                bucket.setItemMeta(bucket_meta);
                Block yourCauldron = e.getBlock();
                Levelled cauldronData = (Levelled) yourCauldron.getBlockData();
                cauldronData.setLevel(3);
                yourCauldron.setBlockData(cauldronData);
            } else if(p.getInventory().getItemInOffHand().hasItemMeta()) {
                ItemStack bucket = p.getInventory().getItemInOffHand();
                ItemMeta bucket_meta = bucket.getItemMeta();
                PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
                NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
                if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
                e.setCancelled(true);
                if (bucket.getAmount() > 1) {
                    p.sendMessage(plugin.format("&cCannot use this item while stacked!"));
                    return;
                }
                NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
                NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
                NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
                int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
                int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
                String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
                LiquidAmt--;
                item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
                item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
                if (BucketType.equalsIgnoreCase("Water") && LiquidAmt > 0) {
                    bucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                    ArrayList<String> bucket_lore = new ArrayList<>();
                    bucket_lore.add("");
                    bucket_lore.add(plugin.format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&9Type: " + BucketType));
                    bucket_meta.setLore(bucket_lore);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            plugin.format("&9" + LiquidAmt + "/" + BucketCapacity)));
                } else if (BucketType.equalsIgnoreCase("Water") && LiquidAmt == 0) {
                    BucketType = "Empty";
                    item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
                    bucket_meta.setDisplayName(plugin.format("&7Big Bucket"));
                    ArrayList<String> bucket_lore = new ArrayList<>();
                    bucket_lore.add("");
                    bucket_lore.add(plugin.format("&7Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&7Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                    bucket_lore.add(plugin.format("&7Type: " + BucketType));
                    bucket_meta.setLore(bucket_lore);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            plugin.format("&7" + LiquidAmt + "/" + BucketCapacity)));
                    bucket.setType(Material.BUCKET);
                }
                bucket.setItemMeta(bucket_meta);
                Block yourCauldron = e.getBlock();
                Levelled cauldronData = (Levelled) yourCauldron.getBlockData();
                cauldronData.setLevel(3);
                yourCauldron.setBlockData(cauldronData);
            }
        }
    }
}