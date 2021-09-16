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

import me.RocketZ1.BetterBuckets.Main;
import me.RocketZ1.BetterBuckets.Other.BucketAction;
import me.RocketZ1.BetterBuckets.Other.BucketType;
import me.RocketZ1.BetterBuckets.Other.BucketUpdater;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class MilkBucket implements Listener{
    private Main plugin;

    public MilkBucket(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void drinkMilk(PlayerItemConsumeEvent e){
        if(e.isCancelled()) return;
        if(e.getItem().getType() != Material.MILK_BUCKET) return;
        if(!e.getItem().hasItemMeta()) return;
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        if(!bucketUpdater.isPossibleFilledBetterBucket(e.getItem())) return;
        ItemStack bucket = e.getItem();
        if(!bucketUpdater.isBetterBucket(bucket.getItemMeta())) return;
        byte slot = 0;
        if(e.getPlayer().getInventory().getItemInMainHand().isSimilar(bucket)){
            slot = 1;
        }else if(e.getPlayer().getInventory().getItemInMainHand().isSimilar(bucket)){
            slot = 2;
        }
        bucket.setItemMeta(bucket.getItemMeta());
        ItemMeta bucket_meta = bucket.getItemMeta();
        BucketType bucketType = bucketUpdater.getBucketType(bucket_meta);
        if(bucketType != BucketType.MILK) return; //Something bugged
        int bucketAmount = bucketUpdater.getBucketAmount(bucket_meta);
        int bucketCapacity = bucketUpdater.getBucketCapacity(bucket_meta);
        bucket = bucketUpdater.updateBucket(bucket, BucketAction.DECREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType);
        bucketAmount--;
        if(bucketAmount == 0) bucket.setType(Material.BUCKET);
        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                plugin.format("&" + bucketUpdater.getChatColorValue(bucketType) + bucketAmount + "/" + bucketCapacity)));
        byte finalSlot = slot;
        ItemStack finalNewBucket = bucket;
        BukkitRunnable drinkMilkRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (finalSlot == 1){
                    e.getPlayer().getInventory().setItemInMainHand(finalNewBucket); //If slot = 0, something bugged
                }
                else if (finalSlot == 2) e.getPlayer().getInventory().setItemInOffHand(finalNewBucket);
            }
        };drinkMilkRunnable.runTaskLater(plugin, 1);
    }
}
