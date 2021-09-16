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
import me.RocketZ1.BetterBuckets.Other.BucketType;
import me.RocketZ1.BetterBuckets.Other.BucketUpdater;
import org.bukkit.entity.Pose;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            BucketUpdater bucketUpdater = new BucketUpdater(plugin);
            if (!bucketUpdater.isPossibleFilledBetterBucket(e.getPlayer().getInventory().getItemInMainHand())) return;
            ItemStack bucket = e.getPlayer().getInventory().getItemInMainHand();
            ItemMeta bucket_meta = bucket.getItemMeta();
            if (!bucketUpdater.isBetterBucket(bucket_meta)) return;
            if(bucketUpdater.getBucketType(bucket_meta) == BucketType.EMPTY) return;
            if(bucket.getAmount() > 1){
                e.getPlayer().sendMessage(plugin.format("&cYou cannot toggle this bucket while stacked!"));
                return;
            }
            BucketType bucketType = bucketUpdater.getBucketType(bucket_meta);
            bucket.setType(bucketUpdater.toggleBucket(bucket, bucketType));
            e.getPlayer().sendMessage(plugin.format("&aBucket Toggled!"));
        }
    }
}