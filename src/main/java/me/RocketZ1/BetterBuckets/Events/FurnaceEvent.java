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
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FurnaceEvent implements Listener {

    private Main plugin;

    public FurnaceEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBurn(FurnaceBurnEvent e) {
        if (e.isCancelled()) return;
        if (!e.getFuel().hasItemMeta()) return;
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        if (!bucketUpdater.isPossibleFilledBetterBucket(e.getFuel())) return;
        ItemStack bucket = e.getFuel();
        if (!bucketUpdater.isBetterBucket(bucket.getItemMeta())) return;
        ItemMeta bucket_meta = bucket.getItemMeta();
        int bucketAmount = bucketUpdater.getBucketAmount(bucket_meta);
        int bucketCapacity = bucketUpdater.getBucketCapacity(bucket_meta);
        BucketType bucketType = bucketUpdater.getBucketType(bucket_meta);
        if (bucketType == BucketType.LAVA && bucket.getType() == Material.LAVA_BUCKET) {
            if (e.getFuel().getAmount() > 1) {
                e.setCancelled(true);
                return;
            }
            bucket = bucketUpdater.updateBucket(bucket, BucketAction.DECREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType);
            if (bucketAmount == 1) bucket.setType(Material.BUCKET);
            e.setBurning(false);
            e.setBurnTime(1000 * 20);
        }
    }
}