package me.RocketZ1.BetterBuckets.Events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import me.RocketZ1.BetterBuckets.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class BucketEvent implements Listener {

    private Main plugin;

    public BucketEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void BucketFill(PlayerBucketFillEvent e) {
        if (e.isCancelled()) return;
        if(plugin.checkRegion(e.getPlayer(), BukkitAdapter.adapt(e.getBlockClicked().getLocation()))) return;
        if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BUCKET) {
            ItemStack bucket = e.getPlayer().getInventory().getItemInMainHand();
            ItemMeta bucket_meta = bucket.getItemMeta();
            PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
            NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
            if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
            e.setCancelled(true);
            boolean blockWaterLogged = false;
            NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
            NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
            NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
            int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
            int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
            String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
            Waterlogged wl = null;
            if(e.getBlockClicked().getBlockData() instanceof Waterlogged){
                if(((Waterlogged) e.getBlockClicked().getState().getBlockData()).isWaterlogged()){
                    blockWaterLogged = true;
                    wl = (Waterlogged) e.getBlockClicked().getBlockData();
                }
            }
            boolean blockIsLiquid = false;
            if(e.getBlockClicked().getType() == Material.LAVA || e.getBlockClicked().getType() == Material.WATER || blockWaterLogged) blockIsLiquid = true;
            if(!blockIsLiquid)return;
            if(bucket.getAmount() > 1){
                e.getPlayer().sendMessage(plugin.format("&cCannot use this item while stacked!"));
                return;
            }
            if (BucketType.equalsIgnoreCase("Water") && e.getBlockClicked().getType() == Material.LAVA) {
                e.getPlayer().sendMessage(plugin.format("&cCannot pickup lava! Bucket currently holds water!"));
                return;
            } else if (BucketType.equalsIgnoreCase("Lava") && e.getBlockClicked().getType() == Material.WATER) {
                e.getPlayer().sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds lava!"));
                return;
            } else if(BucketType.equalsIgnoreCase("Lava") && blockWaterLogged){
                e.getPlayer().sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds lava!"));
                return;
            }
            if (LiquidAmt >= BucketCapacity) {
                e.getPlayer().sendMessage(plugin.format("&cYour bucket is full!"));
                return;
            }
            LiquidAmt++;
            if (item.get(BucketTypeKey, PersistentDataType.STRING).equals("Empty")) {
                if (e.getBlockClicked().getType() == Material.LAVA) {
                    BucketType = "Lava";
                } else if (e.getBlockClicked().getType() == Material.WATER) {
                    BucketType = "Water";
                } else if(blockWaterLogged){
                    BucketType = "Water";
                }
            }
            item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
            item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
            item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
            if (BucketType.equalsIgnoreCase("Water")) {
                bucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&9" + LiquidAmt + "/" + BucketCapacity)));
            } else if (BucketType.equalsIgnoreCase("Lava")) {
                bucket_meta.setDisplayName(plugin.format("&6Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&6Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&6" + LiquidAmt + "/" + BucketCapacity)));
            }
            bucket.setItemMeta(bucket_meta);
            if(blockWaterLogged){
                wl.setWaterlogged(false);
                e.getBlockClicked().setBlockData(wl);
            }else if(e.getBlockClicked().getType() == Material.WATER){
                e.getBlockClicked().setType(Material.AIR);
            }else if(e.getBlockClicked().getType() == Material.LAVA){
                e.getBlockClicked().setType(Material.AIR);
            }
        } else if (e.getPlayer().getInventory().getItemInOffHand().hasItemMeta() && e.getPlayer().getInventory().getItemInOffHand().getType() == Material.BUCKET) {
            if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BUCKET) return;
            ItemStack bucket = e.getPlayer().getInventory().getItemInOffHand();
            ItemMeta bucket_meta = bucket.getItemMeta();
            PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
            NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
            if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
            e.setCancelled(true);
            boolean blockWaterLogged = false;
            NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
            NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
            NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
            int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
            int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
            String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
            Waterlogged wl = null;
            if (e.getBlockClicked().getBlockData() instanceof Waterlogged) {
                if (((Waterlogged) e.getBlockClicked().getState().getBlockData()).isWaterlogged()) {
                    blockWaterLogged = true;
                    wl = (Waterlogged) e.getBlockClicked().getBlockData();
                }
            }
            boolean blockIsLiquid = false;
            if (e.getBlockClicked().getType() == Material.LAVA || e.getBlockClicked().getType() == Material.WATER || blockWaterLogged)
                blockIsLiquid = true;
            if (!blockIsLiquid) return;
            if (bucket.getAmount() > 1) {
                e.getPlayer().sendMessage(plugin.format("&cCannot use this item while stacked!"));
                return;
            }
            if (BucketType.equalsIgnoreCase("Water") && e.getBlockClicked().getType() == Material.LAVA) {
                e.getPlayer().sendMessage(plugin.format("&cCannot pickup lava! Bucket currently holds water!"));
                return;
            } else if (BucketType.equalsIgnoreCase("Lava") && e.getBlockClicked().getType() == Material.WATER) {
                e.getPlayer().sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds lava!"));
                return;
            } else if (BucketType.equalsIgnoreCase("Lava") && blockWaterLogged) {
                e.getPlayer().sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds lava!"));
                return;
            }
            if (LiquidAmt >= BucketCapacity) {
                e.getPlayer().sendMessage(plugin.format("&cYour bucket is full!"));
                return;
            }
            LiquidAmt++;
            if (item.get(BucketTypeKey, PersistentDataType.STRING).equals("Empty")) {
                if (e.getBlockClicked().getType() == Material.LAVA) {
                    BucketType = "Lava";
                } else if (e.getBlockClicked().getType() == Material.WATER) {
                    BucketType = "Water";
                } else if (blockWaterLogged) {
                    BucketType = "Water";
                }
            }
            item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
            item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
            item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
            if (BucketType.equalsIgnoreCase("Water")) {
                bucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&9" + LiquidAmt + "/" + BucketCapacity)));
            } else if (BucketType.equalsIgnoreCase("Lava")) {
                bucket_meta.setDisplayName(plugin.format("&6Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&6Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&6" + LiquidAmt + "/" + BucketCapacity)));
            }
            bucket.setItemMeta(bucket_meta);
            if (blockWaterLogged) {
                wl.setWaterlogged(false);
                e.getBlockClicked().setBlockData(wl);
            } else if (e.getBlockClicked().getType() == Material.WATER) {
                e.getBlockClicked().setType(Material.AIR);
            } else if (e.getBlockClicked().getType() == Material.LAVA) {
                e.getBlockClicked().setType(Material.AIR);
            }
        }
    }


    /**
     * If Main hand has ItemMeta and item is Water Bucket
     *  //code
     * else if Main hand has itemMeta and item is Lava bucket
     *  //code
     * else if Offhand has itemMeta and item is Water Bucket
     *  //code
     *  else if Offhand has itemMeta and item is Lava Bucket
     *   //code
     *
     */

    @EventHandler
    public void BucketEmptyEvent(PlayerBucketEmptyEvent e) {
        if (e.isCancelled()) return;
        if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.WATER_BUCKET) {
            ItemStack bucket = e.getPlayer().getInventory().getItemInMainHand();
            ItemMeta bucket_meta = bucket.getItemMeta();
            PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
            NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
            if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
            e.setCancelled(true);
            if(bucket.getAmount() > 1){
                e.getPlayer().sendMessage(plugin.format("&cCannot use this item while stacked!"));
                return;
            }
            NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
            NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
            NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
            int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
            int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
            String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
            int x = e.getBlockClicked().getX() + e.getBlockFace().getModX();
            int y = e.getBlockClicked().getY() + e.getBlockFace().getModY();
            int z = e.getBlockClicked().getZ() + e.getBlockFace().getModZ();
            Block clickedBlock = e.getPlayer().getWorld().getBlockAt(x, y, z);
            boolean ClickedBlockInstanceOfWaterLogged = false;
            boolean ClickedBlockWaterLogged = false;
            Waterlogged Clickedwl = null;
            if (e.getBlockClicked().getBlockData() instanceof Waterlogged) {
                ClickedBlockInstanceOfWaterLogged = true;
                Clickedwl = (Waterlogged) e.getBlockClicked().getBlockData();
                if (((Waterlogged) e.getBlockClicked().getState().getBlockData()).isWaterlogged()) {
                    ClickedBlockWaterLogged = true;
                }
            }
            boolean blockInstanceOfWaterLogged = false;
            boolean blockWaterLogged = false;
            Waterlogged wl = null;
            if(clickedBlock.getBlockData() instanceof Waterlogged){
                blockInstanceOfWaterLogged = true;
                wl = (Waterlogged) clickedBlock.getBlockData();
                if (((Waterlogged) clickedBlock.getState().getBlockData()).isWaterlogged()) {
                    blockWaterLogged = true;
                }
            }
            if(plugin.checkRegion(e.getPlayer(), BukkitAdapter.adapt(e.getBlockClicked().getLocation()))) return;
            if(plugin.checkRegion(e.getPlayer(), BukkitAdapter.adapt(clickedBlock.getLocation()))) return;
            LiquidAmt--;
            item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
            item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
            if (LiquidAmt == 0 && BucketType.equalsIgnoreCase("Water")) {
                BucketType = "Empty";
                bucket_meta.setDisplayName(plugin.format("&7Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&7Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&7" + LiquidAmt + "/" + BucketCapacity)));
                if (ClickedBlockInstanceOfWaterLogged && !ClickedBlockWaterLogged) {
                    Clickedwl.setWaterlogged(true);
                    e.getBlockClicked().setBlockData(Clickedwl);
                }else if(blockInstanceOfWaterLogged && !blockWaterLogged) {
                    wl.setWaterlogged(true);
                    clickedBlock.setBlockData(wl);
                }else{
                    clickedBlock.setType(Material.WATER);
                }
                bucket.setType(Material.BUCKET);
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
            }
            if (LiquidAmt > 0 && BucketType.equalsIgnoreCase("Water")) {
                bucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&9" + LiquidAmt + "/" + BucketCapacity)));
                if (ClickedBlockInstanceOfWaterLogged && !ClickedBlockWaterLogged) {
                    Clickedwl.setWaterlogged(true);
                    e.getBlockClicked().setBlockData(Clickedwl);
                }else if(blockInstanceOfWaterLogged && !blockWaterLogged) {
                    wl.setWaterlogged(true);
                    clickedBlock.setBlockData(wl);
                }else{
                    clickedBlock.setType(Material.WATER);
                }
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
            }
            bucket.setItemMeta(bucket_meta);
        } else if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LAVA_BUCKET) {
            ItemStack bucket = e.getPlayer().getInventory().getItemInMainHand();
            ItemMeta bucket_meta = bucket.getItemMeta();
            PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
            NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
            if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
            e.setCancelled(true);
            if (bucket.getAmount() > 1) {
                e.getPlayer().sendMessage(plugin.format("&cCannot use this item while stacked!"));
                return;
            }
            NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
            NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
            NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
            int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
            int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
            String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
            int x = e.getBlockClicked().getX() + e.getBlockFace().getModX();
            int y = e.getBlockClicked().getY() + e.getBlockFace().getModY();
            int z = e.getBlockClicked().getZ() + e.getBlockFace().getModZ();
            Block clickedBlock = e.getPlayer().getWorld().getBlockAt(x, y, z);
            if(plugin.checkRegion(e.getPlayer(), BukkitAdapter.adapt(clickedBlock.getLocation()))) return;
            LiquidAmt--;
            item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
            item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
            if (LiquidAmt == 0 && BucketType.equalsIgnoreCase("Lava")) {
                BucketType = "Empty";
                bucket_meta.setDisplayName(plugin.format("&7Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&7Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&7" + LiquidAmt + "/" + BucketCapacity)));
                clickedBlock.setType(Material.LAVA);
                bucket.setType(Material.BUCKET);
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
            }
            if (LiquidAmt > 0 && BucketType.equalsIgnoreCase("Lava")) {
                bucket_meta.setDisplayName(plugin.format("&6Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&6Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&6" + LiquidAmt + "/" + BucketCapacity)));
                clickedBlock.setType(Material.LAVA);
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
            }
            bucket.setItemMeta(bucket_meta);
        } else if (e.getPlayer().getInventory().getItemInOffHand().hasItemMeta() && e.getPlayer().getInventory().getItemInOffHand().getType() == Material.WATER_BUCKET) {
            ItemStack bucket = e.getPlayer().getInventory().getItemInOffHand();
            ItemMeta bucket_meta = bucket.getItemMeta();
            PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
            NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
            if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
            e.setCancelled(true);
            if(bucket.getAmount() > 1){
                e.getPlayer().sendMessage(plugin.format("&cCannot use this item while stacked!"));
                return;
            }
            NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
            NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
            NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
            int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
            int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
            String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
            int x = e.getBlockClicked().getX() + e.getBlockFace().getModX();
            int y = e.getBlockClicked().getY() + e.getBlockFace().getModY();
            int z = e.getBlockClicked().getZ() + e.getBlockFace().getModZ();
            Block clickedBlock = e.getPlayer().getWorld().getBlockAt(x, y, z);
            boolean ClickedBlockInstanceOfWaterLogged = false;
            boolean ClickedBlockWaterLogged = false;
            Waterlogged Clickedwl = null;
            if (e.getBlockClicked().getBlockData() instanceof Waterlogged) {
                ClickedBlockInstanceOfWaterLogged = true;
                Clickedwl = (Waterlogged) e.getBlockClicked().getBlockData();
                if (((Waterlogged) e.getBlockClicked().getState().getBlockData()).isWaterlogged()) {
                    ClickedBlockWaterLogged = true;
                }
            }
            boolean blockInstanceOfWaterLogged = false;
            boolean blockWaterLogged = false;
            Waterlogged wl = null;
            if(clickedBlock.getBlockData() instanceof Waterlogged){
                blockInstanceOfWaterLogged = true;
                wl = (Waterlogged) clickedBlock.getBlockData();
                if (((Waterlogged) clickedBlock.getState().getBlockData()).isWaterlogged()) {
                    blockWaterLogged = true;
                }
            }
            if(plugin.checkRegion(e.getPlayer(), BukkitAdapter.adapt(e.getBlockClicked().getLocation()))) return;
            if(plugin.checkRegion(e.getPlayer(), BukkitAdapter.adapt(clickedBlock.getLocation()))) return;
            LiquidAmt--;
            item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
            item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
            if (LiquidAmt == 0 && BucketType.equalsIgnoreCase("Water")) {
                BucketType = "Empty";
                bucket_meta.setDisplayName(plugin.format("&7Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&7Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&7" + LiquidAmt + "/" + BucketCapacity)));
                if (ClickedBlockInstanceOfWaterLogged && !ClickedBlockWaterLogged) {
                    Clickedwl.setWaterlogged(true);
                    e.getBlockClicked().setBlockData(Clickedwl);
                }else if(blockInstanceOfWaterLogged && !blockWaterLogged) {
                    wl.setWaterlogged(true);
                    clickedBlock.setBlockData(wl);
                }else{
                    clickedBlock.setType(Material.WATER);
                }
                bucket.setType(Material.BUCKET);
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
            }
            if (LiquidAmt > 0 && BucketType.equalsIgnoreCase("Water")) {
                bucket_meta.setDisplayName(plugin.format("&9Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&9Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&9" + LiquidAmt + "/" + BucketCapacity)));
                if (ClickedBlockInstanceOfWaterLogged && !ClickedBlockWaterLogged) {
                    Clickedwl.setWaterlogged(true);
                    e.getBlockClicked().setBlockData(Clickedwl);
                }else if(blockInstanceOfWaterLogged && !blockWaterLogged) {
                    wl.setWaterlogged(true);
                    clickedBlock.setBlockData(wl);
                }else{
                    clickedBlock.setType(Material.WATER);
                }
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
            }
            bucket.setItemMeta(bucket_meta);
        } else if (e.getPlayer().getInventory().getItemInOffHand().hasItemMeta() && e.getPlayer().getInventory().getItemInOffHand().getType() == Material.LAVA_BUCKET) {
            ItemStack bucket = e.getPlayer().getInventory().getItemInOffHand();
            ItemMeta bucket_meta = bucket.getItemMeta();
            PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
            NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
            if (!(item.has(BetterBucketKey, PersistentDataType.STRING))) return;
            e.setCancelled(true);
            if (bucket.getAmount() > 1) {
                e.getPlayer().sendMessage(plugin.format("&cCannot use this item while stacked!"));
                return;
            }
            NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
            NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
            NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
            int LiquidAmt = item.get(LiquidAmtKey, PersistentDataType.INTEGER);
            int BucketCapacity = item.get(BucketCapacityKey, PersistentDataType.INTEGER);
            String BucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
            int x = e.getBlockClicked().getX() + e.getBlockFace().getModX();
            int y = e.getBlockClicked().getY() + e.getBlockFace().getModY();
            int z = e.getBlockClicked().getZ() + e.getBlockFace().getModZ();
            Block clickedBlock = e.getPlayer().getWorld().getBlockAt(x, y, z);
            if(plugin.checkRegion(e.getPlayer(), BukkitAdapter.adapt(clickedBlock.getLocation()))) return;
            LiquidAmt--;
            item.set(LiquidAmtKey, PersistentDataType.INTEGER, LiquidAmt);
            item.set(BucketCapacityKey, PersistentDataType.INTEGER, BucketCapacity);
            if (LiquidAmt == 0 && BucketType.equalsIgnoreCase("Lava")) {
                BucketType = "Empty";
                bucket_meta.setDisplayName(plugin.format("&7Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&7Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&7Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&7" + LiquidAmt + "/" + BucketCapacity)));
                clickedBlock.setType(Material.LAVA);
                bucket.setType(Material.BUCKET);
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
            }
            if (LiquidAmt > 0 && BucketType.equalsIgnoreCase("Lava")) {
                bucket_meta.setDisplayName(plugin.format("&6Big Bucket"));
                ArrayList<String> bucket_lore = new ArrayList<>();
                bucket_lore.add("");
                bucket_lore.add(plugin.format("&6Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
                bucket_lore.add(plugin.format("&6Type: " + BucketType));
                bucket_meta.setLore(bucket_lore);
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        plugin.format("&6" + LiquidAmt + "/" + BucketCapacity)));
                clickedBlock.setType(Material.LAVA);
                item.set(BucketTypeKey, PersistentDataType.STRING, BucketType);
            }
            bucket.setItemMeta(bucket_meta);
        }
    }
}