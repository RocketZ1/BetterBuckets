package me.RocketZ1.BetterBuckets;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.RocketZ1.BetterBuckets.Commands.BetterBucket;
import me.RocketZ1.BetterBuckets.Commands.BetterBucketTab;
import me.RocketZ1.BetterBuckets.Events.*;
import me.RocketZ1.BetterBuckets.Files.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    public ConfigManager config;
    public WorldGuardPlugin worldGuardPlugin;
    public WorldEditPlugin worldEditPlugin;
    public WorldGuard worldGuard;
    public WorldEdit worldEdit;

    @Override
    public void onEnable(){
        worldGuardPlugin = getWorldGuardPlugin();
        worldEditPlugin = getWorldEditPlugin();
        PluginManager pm = getServer().getPluginManager();
        this.config = new ConfigManager(this);
        new BetterBucket(this);
        new BetterBucketTab(this);
        pm.registerEvents(new BucketEvent(this), this);
        pm.registerEvents(new ToggleMode(this), this);
        pm.registerEvents(new DisableDispensers(this), this);
        pm.registerEvents(new FurnaceEvent(this), this);
        pm.registerEvents(new CauldronEvent(this), this);
        pm.registerEvents(new DisableBucketFish(this), this);
        pm.registerEvents(new DisableMilking(this), this);
    }

    public boolean checkRegion(Player p, Location loc) {
        LocalPlayer localPlayer = worldGuardPlugin.wrapPlayer(p);
        RegionContainer container = getWorldGuard().getPlatform().getRegionContainer();
        RegionManager regions = container.get(localPlayer.getWorld());
        if(regions == null)
            return false;
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        for(ProtectedRegion region : set){
            if(region.contains(loc.toVector().toBlockPoint())){
                if(region.getMembers().contains(localPlayer.getUniqueId())) return false;
                if(region.getOwners().contains(localPlayer.getUniqueId())) return false;
                return true;
            }
        }
        return false;
    }

    public WorldGuardPlugin getWorldGuardPlugin() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    public WorldGuard getWorldGuard() {
        WorldGuard worldGuard = WorldGuard.getInstance();
        if (worldGuard == null || !(worldGuard instanceof WorldGuard)) {
            return null;
        }
        return worldGuard;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldEdit");
        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return null; // Maybe you want throw an exception instead
        }
        return (WorldEditPlugin) plugin;
    }

    public WorldEdit getWorldEdit() {
        WorldEdit worldEdit = WorldEdit.getInstance();
        if (worldEdit == null || !(worldEdit instanceof WorldEdit)) {
            return null;
        }
        return worldEdit;
    }

    public ItemStack BigBucket(String bucketType, int amount, int capacity){
        ItemStack bucket = new ItemStack(Material.BUCKET);
        ItemMeta bucket_meta = bucket.getItemMeta();
        PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
        NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
        NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
        NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
        NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");

        item.set(LiquidAmtKey, PersistentDataType.INTEGER, amount);
        item.set(BucketCapacityKey, PersistentDataType.INTEGER, capacity);
        item.set(BucketTypeKey, PersistentDataType.STRING, bucketType);
        item.set(BetterBucketKey, PersistentDataType.STRING, "Better Buckets");
        if(bucketType.equalsIgnoreCase("Empty")) {
            bucket_meta.setDisplayName(format("&7Big Bucket"));
            ArrayList<String> item_lore = new ArrayList<>();
            item_lore.add("");
            item_lore.add(format("&7Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
            item_lore.add(format("&7Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
            item_lore.add(format("&7Type: " + item.get(BucketTypeKey, PersistentDataType.STRING)));
            bucket_meta.setLore(item_lore);
            bucket.setItemMeta(bucket_meta);
        }else if(bucketType.equalsIgnoreCase("Lava")) {
            bucket_meta.setDisplayName(format("&6Big Bucket"));
            ArrayList<String> item_lore = new ArrayList<>();
            item_lore.add("");
            item_lore.add(format("&6Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
            item_lore.add(format("&6Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
            item_lore.add(format("&6Type: " + item.get(BucketTypeKey, PersistentDataType.STRING)));
            bucket_meta.setLore(item_lore);
            bucket.setItemMeta(bucket_meta);
        }else if(bucketType.equalsIgnoreCase("Water")) {
            bucket_meta.setDisplayName(format("&9Big Bucket"));
            ArrayList<String> item_lore = new ArrayList<>();
            item_lore.add("");
            item_lore.add(format("&9Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
            item_lore.add(format("&9Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
            item_lore.add(format("&9Type: " + item.get(BucketTypeKey, PersistentDataType.STRING)));
            bucket_meta.setLore(item_lore);
            bucket.setItemMeta(bucket_meta);
        }
        return bucket;
    }

    public String format(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
