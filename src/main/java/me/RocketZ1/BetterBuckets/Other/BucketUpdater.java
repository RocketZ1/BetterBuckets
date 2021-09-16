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
package me.RocketZ1.BetterBuckets.Other;

import me.RocketZ1.BetterBuckets.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class BucketUpdater {

    //Namespacekeys
    private final NamespacedKey BetterBucketKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets");
    private final NamespacedKey LiquidAmtKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-filled");
    private final NamespacedKey BucketCapacityKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-capacity");
    private final NamespacedKey BucketTypeKey = new NamespacedKey(Main.getPlugin(Main.class), "better-buckets-type");
    //Bucket Name
    private Main plugin;
    public BucketUpdater(Main plugin){
         this.plugin = plugin;
    }

    private Material createNewBucket(BucketType bucketType){
        if(bucketType == BucketType.WATER) return Material.WATER_BUCKET;
        if(bucketType == BucketType.LAVA) return Material.LAVA_BUCKET;
        if(bucketType == BucketType.POWDER_SNOW) return Material.POWDER_SNOW_BUCKET;
        if(bucketType == BucketType.MILK) return Material.MILK_BUCKET;
        return Material.BUCKET;
    }

    public ItemStack createBucket(BucketType bucketType, int amount, int capacity){
        //Make ItemStack
        ItemStack bucket = new ItemStack(createNewBucket(bucketType));
        ItemMeta bucket_meta = bucket.getItemMeta();
        PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
        //Set Namespacekeys
        item.set(LiquidAmtKey, PersistentDataType.INTEGER, amount);
        item.set(BucketCapacityKey, PersistentDataType.INTEGER, capacity);
        item.set(BucketTypeKey, PersistentDataType.STRING, bucketType.toString());
        item.set(BetterBucketKey, PersistentDataType.STRING, "Better Buckets");
        //Set Bucket Meta
        String chatColorValue = getChatColorValue(bucketType);
        bucket_meta.setDisplayName(format("&"+chatColorValue+plugin.bucketName));
        ArrayList<String> item_lore = new ArrayList<>();
        item_lore.add("");
        item_lore.add(format("&"+chatColorValue+"Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
        item_lore.add(format("&"+chatColorValue+"Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
        item_lore.add(format("&"+chatColorValue+"Type: " + item.get(BucketTypeKey, PersistentDataType.STRING)));
        //Set lore to meta
        bucket_meta.setLore(item_lore);
        //set meta to ItemStack
        bucket.setItemMeta(bucket_meta);
        //return bucket
        return bucket;
    }



    public String getChatColorValue(BucketType bucketType){
        if(bucketType == BucketType.EMPTY) {
           return "7";
        }else if(bucketType == BucketType.LAVA) {
            return  "6";
        }else if(bucketType == BucketType.WATER) {
            return  "9";
        }else if(bucketType == BucketType.MILK){
            return "f";
        }else if(bucketType == BucketType.POWDER_SNOW){
            return "8";
        }
        //If the bucketType is invalid in someway return empty color
        return "7";
    }

    public BucketType getBucketType(ItemMeta bucket_meta){
        PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
        String bucketType = item.get(BucketTypeKey, PersistentDataType.STRING);
        if(bucketType.equalsIgnoreCase("EMPTY")) {
            return BucketType.EMPTY;
        }else if(bucketType.equalsIgnoreCase("LAVA")) {
            return  BucketType.LAVA;
        }else if(bucketType.equalsIgnoreCase("WATER")) {
            return  BucketType.WATER;
        }else if(bucketType.equalsIgnoreCase("MILK")){
            return BucketType.MILK;
        }else if(bucketType.equalsIgnoreCase("POWDER_SNOW")){
            return BucketType.POWDER_SNOW;
        }
        return BucketType.EMPTY;
    }
    public int getBucketAmount(ItemMeta bucket_meta){
        PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
        try {
            return item.get(LiquidAmtKey, PersistentDataType.INTEGER);
        }catch (NullPointerException e){
            return 0;
        }
    }
    public int getBucketCapacity(ItemMeta bucket_meta){
        PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
        try {
            return item.get(BucketCapacityKey, PersistentDataType.INTEGER);
        }catch (NullPointerException e){
            return 0;
        }
    }

    public boolean isBetterBucket(ItemMeta bucket_meta){
        if(bucket_meta == null) return false;
        PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
        return item.has(BetterBucketKey, PersistentDataType.STRING);
    }

    public ItemStack updateBucket(ItemStack bucket, BucketAction bucketAction, int amount, int capacity, BucketType convertBucketType){
        ItemMeta bucket_meta = bucket.getItemMeta();
        PersistentDataContainer item = bucket_meta.getPersistentDataContainer();
        BucketType bucketType = getBucketType(bucket_meta);
        if(bucketAction == BucketAction.INCREASE_AMOUNT) amount++;
        else if(bucketAction == BucketAction.DECREASE_AMOUNT) amount--;
        if(amount > capacity) {
            //If bucket stays the same, amount was greater than capacity
            return bucket;
        }else if(amount == 0){
            bucketType = BucketType.EMPTY;
            //set bucket Material
            bucket.setType(getBucketMaterial(bucketType));
        }else if(convertBucketType != bucketType){
            bucketType = convertBucketType;
        }
        //Set Namespacekeys
        item.set(LiquidAmtKey, PersistentDataType.INTEGER, amount);
        item.set(BucketTypeKey, PersistentDataType.STRING, bucketType.toString());
        //Set Bucket Meta
        String chatColorValue = getChatColorValue(bucketType);
        bucket_meta.setDisplayName(format("&"+chatColorValue+plugin.bucketName));
        ArrayList<String> item_lore = new ArrayList<>();
        item_lore.add("");
        item_lore.add(format("&"+chatColorValue+"Filled: " + item.get(LiquidAmtKey, PersistentDataType.INTEGER)));
        item_lore.add(format("&"+chatColorValue+"Capacity: " + item.get(BucketCapacityKey, PersistentDataType.INTEGER)));
        item_lore.add(format("&"+chatColorValue+"Type: " + item.get(BucketTypeKey, PersistentDataType.STRING)));
        //Set lore to meta
        bucket_meta.setLore(item_lore);
        //set meta to ItemStack
        bucket.setItemMeta(bucket_meta);
        //return bucket
        return bucket;
    }

    private Material getBucketMaterial(BucketType bucketType){
        if(bucketType == BucketType.WATER) return Material.WATER_BUCKET;
        if(bucketType == BucketType.LAVA) return Material.LAVA_BUCKET;
        if(bucketType == BucketType.POWDER_SNOW) return Material.POWDER_SNOW_BUCKET;
        if(bucketType == BucketType.MILK) return Material.MILK_BUCKET;
        return Material.BUCKET;
    }

    public boolean isPossibleFilledBetterBucket(ItemStack item){
        if(!isBucketKind(item.getType())) return false;
        if(item.hasItemMeta()) return true;
        return false;
    }

    private boolean isBucketKind(Material itemType){
        if(itemType == Material.WATER_BUCKET) return true;
        if(itemType == Material.LAVA_BUCKET) return true;
        if(itemType == Material.POWDER_SNOW_BUCKET) return true;
        if(itemType == Material.MILK_BUCKET) return true;
        if(itemType == Material.BUCKET) return true;
        return false;
    }

    public boolean isFilledBucketKind(Material itemType){
        if(itemType == Material.WATER_BUCKET) return true;
        if(itemType == Material.LAVA_BUCKET) return true;
        if(itemType == Material.POWDER_SNOW_BUCKET) return true;
        return false;
    }

    public Material toggleBucket(ItemStack bucket, BucketType bucketType){
        if(bucketType == BucketType.EMPTY) return Material.BUCKET;
        if(bucketType == BucketType.WATER && bucket.getType() == Material.BUCKET) return Material.WATER_BUCKET;
        if(bucketType == BucketType.WATER && bucket.getType() == Material.WATER_BUCKET) return Material.BUCKET;
        if(bucketType == BucketType.LAVA && bucket.getType() == Material.BUCKET) return Material.LAVA_BUCKET;
        if(bucketType == BucketType.LAVA && bucket.getType() == Material.LAVA_BUCKET) return Material.BUCKET;
        if(bucketType == BucketType.POWDER_SNOW && bucket.getType() == Material.BUCKET) return Material.POWDER_SNOW_BUCKET;
        if(bucketType == BucketType.POWDER_SNOW && bucket.getType() == Material.POWDER_SNOW_BUCKET) return Material.BUCKET;
        if(bucketType == BucketType.MILK && bucket.getType() == Material.BUCKET) return Material.MILK_BUCKET;
        if(bucketType == BucketType.MILK && bucket.getType() == Material.MILK_BUCKET) return Material.BUCKET;
        return bucket.getType();
    }

    private String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
