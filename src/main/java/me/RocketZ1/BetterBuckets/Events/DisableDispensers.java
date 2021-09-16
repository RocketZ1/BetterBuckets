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
import me.RocketZ1.BetterBuckets.Other.BucketUpdater;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

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
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        if (!bucketUpdater.isPossibleFilledBetterBucket(e.getItem())) return;
        ItemStack bucket = e.getItem();
        if (!bucketUpdater.isBetterBucket(bucket.getItemMeta())) return;
        e.setCancelled(true);
    }

    /*private ItemStack changeDispensedItem(ItemStack bucket, BucketAction BucketAction, int bucketAmount, int bucketCapacity, BucketType bucketType, Block blockInFrontOfDispenser) {
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        setBlockInFrontOfDispenser(blockInFrontOfDispenser, bucketType, bucket.getType());
        if (bucketType == BucketType.EMPTY) bucketType = getLiquidInFront(blockInFrontOfDispenser);
        Bukkit.broadcastMessage(bucket.getType().toString());
        bucket.setType(bucketUpdater.toggleBucket(bucket, bucketType));
        bucket = bucketUpdater.updateBucket(bucket, BucketAction, bucketAmount, bucketCapacity, bucketType);
        Bukkit.broadcastMessage("Change Dispensed Item");
        Bukkit.broadcastMessage(bucket.getType().toString());
        return bucket;
    }

    private void setBlockInFrontOfDispenser(Block block, BucketType bucketType, Material material){
        if(material == Material.BUCKET) block.setType(Material.AIR);
        else if(bucketType == BucketType.EMPTY) block.setType(Material.AIR);
        else if(bucketType == BucketType.WATER) block.setType(Material.WATER);
        else if(bucketType == BucketType.LAVA) block.setType(Material.LAVA);
        else if(bucketType == BucketType.POWDER_SNOW) block.setType(Material.POWDER_SNOW);
    }

    private BucketType getLiquidInFront(Block block) {
        Bukkit.broadcastMessage("get Liquid front");
        if (block.getType() == Material.WATER) return BucketType.WATER;
        if (block.getType() == Material.LAVA) return BucketType.LAVA;
        if (block.getType() == Material.POWDER_SNOW) return BucketType.POWDER_SNOW;
        return BucketType.EMPTY;
    }*/
}


     /*
        //Broken Dispenser Code

        if (e.isCancelled()) return;
        if (e.getBlock().getType() != Material.DISPENSER) return;
        if (!e.getItem().hasItemMeta()) return;
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        if (!bucketUpdater.isPossibleFilledBetterBucket(e.getItem())) return;
        ItemStack bucket = e.getItem();
        if (!bucketUpdater.isBetterBucket(bucket.getItemMeta())) return;
        Block blockInFrontOfDispenser = e.getBlock().getRelative(((Dispenser) e.getBlock().getBlockData()).getFacing());
        if (blockInFrontOfDispenser.isLiquid() || blockInFrontOfDispenser.getType().isAir() || blockInFrontOfDispenser.getType() == Material.POWDER_SNOW) {
            if(bucket.getType() == Material.BUCKET && blockInFrontOfDispenser.getType().isAir()) return;
            org.bukkit.block.Dispenser dispenser = (org.bukkit.block.Dispenser) e.getBlock().getState();
            //Container dispenserContainer = (Container) e.getBlock().getState();
            //Slot num
            int slotNum = dispenser.getSnapshotInventory().first(e.getItem());
            ItemMeta bucket_meta = bucket.getItemMeta();
            BucketType bucketType = bucketUpdater.getBucketType(bucket_meta);
            int bucketAmount = bucketUpdater.getBucketAmount(bucket_meta);
            int bucketCapacity = bucketUpdater.getBucketCapacity(bucket_meta);
            ItemStack newBucket = new ItemStack(Material.STONE); //placeholder
            if (bucketType == BucketType.LAVA && bucket.getType() == Material.LAVA_BUCKET) { //place lava
                bucket = changeDispensedItem(bucket, BucketAction.DECREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType, blockInFrontOfDispenser);
            } else if (bucketType == BucketType.LAVA && bucket.getType() == Material.BUCKET) { // pickup lava
                bucket = changeDispensedItem(bucket, BucketAction.INCREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType, blockInFrontOfDispenser);
            } else if (bucketType == BucketType.WATER && bucket.getType() == Material.WATER_BUCKET) {//place water
                bucket = changeDispensedItem(bucket, BucketAction.DECREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType, blockInFrontOfDispenser);
            } else if (bucketType == BucketType.WATER && bucket.getType() == Material.BUCKET) {//pickup water
                bucket = changeDispensedItem(bucket, BucketAction.INCREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType, blockInFrontOfDispenser);
            } else if (bucketType == BucketType.POWDER_SNOW && bucket.getType() == Material.POWDER_SNOW_BUCKET) { //place powder snow
                bucket = changeDispensedItem(bucket, BucketAction.DECREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType, blockInFrontOfDispenser);
            } else if (bucketType == BucketType.POWDER_SNOW && bucket.getType() == Material.BUCKET) {//pickup powder snow
                bucket = changeDispensedItem(bucket, BucketAction.INCREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType, blockInFrontOfDispenser);
            } else if (bucketType == BucketType.EMPTY && bucket.getType() == Material.BUCKET) { // pickup liquid infront
                bucket = changeDispensedItem(bucket, BucketAction.INCREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType, blockInFrontOfDispenser);
            }
            newBucket.setType(bucket.getType());
            newBucket.setItemMeta(bucket.getItemMeta());
            Bukkit.broadcastMessage(bucket.getType().toString());
            dispenser.getInventory().setItem(0, new ItemStack(Material.DIAMOND));
            dispenser.getInventory().setItem(1 , newBucket);
            dispenser.getInventory().setItem(2, new ItemStack(newBucket.getType()));
            e.setCancelled(true);
        }*/