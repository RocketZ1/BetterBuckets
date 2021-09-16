/*
   Copyright 2021 RocketZ1

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

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
import me.RocketZ1.BetterBuckets.Commands.ReloadConfigCmd;
import me.RocketZ1.BetterBuckets.Events.*;
import me.RocketZ1.BetterBuckets.Files.ConfigManager;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public ConfigManager config;
    public WorldGuardPlugin worldGuardPlugin;
    public WorldEditPlugin worldEditPlugin;
    public WorldGuard worldGuard;
    public WorldEdit worldEdit;
    public GriefPrevention griefPrevention;

    public String bucketName = "null";

    @Override
    public void onEnable(){
        worldGuardPlugin = getWorldGuardPlugin();
        worldEditPlugin = getWorldEditPlugin();
        griefPrevention = getGriefPreventionPlugin();
        PluginManager pm = getServer().getPluginManager();
        this.config = new ConfigManager(this);
        new BetterBucket(this);
        new ReloadConfigCmd(this);
        new BetterBucketTab(this);
        pm.registerEvents(new BucketEvent(this), this);
        pm.registerEvents(new ToggleMode(this), this);
        pm.registerEvents(new DisableDispensers(this), this);
        pm.registerEvents(new FurnaceEvent(this), this);
        pm.registerEvents(new CauldronEvent(this), this);
        pm.registerEvents(new DisableBucketFish(this), this);
        pm.registerEvents(new MilkBucket(this), this);
        setupConfigFiles();
    }

    public void setupConfigFiles(){
        if(config.getConfig().contains("bucket_name")){
            bucketName = config.getConfig().getString("bucket_name");
        }
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
                if(region.getOwners().contains(localPlayer.getUniqueId())) return false;
                if(region.getMembers().contains(localPlayer.getUniqueId())) return false;
                return true;
            }
        }
        return false;
    }

    public boolean checkClaimAccess(Player p, org.bukkit.Location loc){
        if(griefPrevention.dataStore.getPlayerData(p.getUniqueId()).ignoreClaims) return true;
        for(Claim claim : griefPrevention.dataStore.getClaims()){
            if(claim.contains(loc, true, true)){
                String msg = claim.allowAccess(p);
                if(msg == null){
                    return true;
                }
                p.sendMessage(format("&c")+msg);
                return false;
            }
        }
        return true;
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

    private GriefPrevention getGriefPreventionPlugin(){
        Plugin plugin = this.getServer().getPluginManager().getPlugin("GriefPrevention");
        // May not be loaded
        if(plugin == null || !(plugin instanceof GriefPrevention)){
            return null;
        }
        return (GriefPrevention) plugin;
    }

    public String format(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
