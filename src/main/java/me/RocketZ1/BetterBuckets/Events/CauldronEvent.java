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
package me.RocketZ1.BetterBuckets.Events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import me.RocketZ1.BetterBuckets.Main;
import me.RocketZ1.BetterBuckets.Other.BucketAction;
import me.RocketZ1.BetterBuckets.Other.BucketType;
import me.RocketZ1.BetterBuckets.Other.BucketUpdater;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CauldronEvent implements Listener {

    private Main plugin;

    public CauldronEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void CauldronChangeEvent(CauldronLevelChangeEvent e){
        if(e.isCancelled()) return;
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        if(e.getReason() == CauldronLevelChangeEvent.ChangeReason.BUCKET_FILL){
            if(bucketUpdater.isPossibleFilledBetterBucket(p.getInventory().getItemInMainHand())){
                CauldronEmpty(e, p.getInventory().getItemInMainHand(), bucketUpdater, p);
            }else if(bucketUpdater.isPossibleFilledBetterBucket(p.getInventory().getItemInOffHand())){
                CauldronEmpty(e, p.getInventory().getItemInOffHand(), bucketUpdater, p);
            }
        }else if(e.getReason() == CauldronLevelChangeEvent.ChangeReason.BUCKET_EMPTY){
            if(bucketUpdater.isPossibleFilledBetterBucket(p.getInventory().getItemInMainHand())){
                CauldronFill(e, p.getInventory().getItemInMainHand(), bucketUpdater, p);
            }else if(bucketUpdater.isPossibleFilledBetterBucket(p.getInventory().getItemInOffHand())){
                CauldronFill(e, p.getInventory().getItemInOffHand(), bucketUpdater, p);
            }
        }

    }

    private BucketType bucketMatchCauldren(Block block){
        if(block.getType() == Material.WATER_CAULDRON) return BucketType.WATER;
        if(block.getType() == Material.LAVA_CAULDRON) return BucketType.LAVA;
        if(block.getType() == Material.POWDER_SNOW_CAULDRON) return BucketType.POWDER_SNOW;
        if(block.getType() == Material.CAULDRON) return BucketType.EMPTY;
        return null;
    }

    private void CauldronEmpty(CauldronLevelChangeEvent e, ItemStack bucket, BucketUpdater bucketUpdater, Player p){
        ItemMeta bucket_meta = bucket.getItemMeta();
        if(!bucketUpdater.isBetterBucket(bucket_meta)) return;
        e.setCancelled(true);
        if(plugin.checkRegion(p, BukkitAdapter.adapt(e.getBlock().getLocation()))) return;
        if(!plugin.checkClaimAccess(p, e.getBlock().getLocation())) return;
        if (bucket.getAmount() > 1) {
            p.sendMessage(plugin.format("&cCannot use this item while stacked!"));
            return;
        }
        int bucketAmount = bucketUpdater.getBucketAmount(bucket_meta);
        int bucketCapacity = bucketUpdater.getBucketCapacity(bucket_meta);
        BucketType bucketType = bucketUpdater.getBucketType(bucket_meta);
        if(bucketType == BucketType.EMPTY){
            bucketType = bucketMatchCauldren(e.getBlock());
        }else if(bucketMatchCauldren(e.getBlock()) != bucketType){
            if(bucketMatchCauldren(e.getBlock()) == null) return; // Something bugged
            p.sendMessage(plugin.format("&cCannot pickup "+bucketMatchCauldren(e.getBlock()).toString().toLowerCase().replaceAll("_", " ")+"! " +
                    "Bucket currently holds "+bucketType.toString().toLowerCase().replaceAll("_", " "))+"!");
            return;
        }
        bucket = bucketUpdater.updateBucket(bucket, BucketAction.INCREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType);
        bucketAmount++;
        if(bucketAmount > bucketCapacity){
            p.sendMessage(plugin.format("&cYour bucket is full!"));
            return;
        }
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                plugin.format("&" + bucketUpdater.getChatColorValue(bucketType) + bucketAmount + "/" + bucketCapacity)));
        e.getNewState().update(true);
    }
    private void CauldronFill(CauldronLevelChangeEvent e, ItemStack bucket, BucketUpdater bucketUpdater, Player p){
        ItemMeta bucket_meta = bucket.getItemMeta();
        if(!bucketUpdater.isBetterBucket(bucket_meta)) return;
        e.setCancelled(true);
        if(plugin.checkRegion(p, BukkitAdapter.adapt(e.getBlock().getLocation()))) return;
        if(!plugin.checkClaimAccess(p, e.getBlock().getLocation())) return;
        if (bucket.getAmount() > 1) {
            p.sendMessage(plugin.format("&cCannot use this item while stacked!"));
            return;
        }
        int bucketAmount = bucketUpdater.getBucketAmount(bucket_meta);
        int bucketCapacity = bucketUpdater.getBucketCapacity(bucket_meta);
        BucketType bucketType = bucketUpdater.getBucketType(bucket_meta);
        if(bucketAmount == 1) bucketType = BucketType.EMPTY;
        ItemStack newBucket = bucketUpdater.updateBucket(bucket, BucketAction.DECREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType);
        bucketAmount--;
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                plugin.format("&" + bucketUpdater.getChatColorValue(bucketType) + bucketAmount + "/" + bucketCapacity)));
        bucket.setItemMeta(newBucket.getItemMeta());
        e.getNewState().update(true);
    }
}