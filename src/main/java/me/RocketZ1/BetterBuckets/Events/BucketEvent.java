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
import me.RocketZ1.BetterBuckets.Other.BlockUpdater;
import me.RocketZ1.BetterBuckets.Other.BucketAction;
import me.RocketZ1.BetterBuckets.Other.BucketType;
import me.RocketZ1.BetterBuckets.Other.BucketUpdater;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class BucketEvent implements Listener {

    private Main plugin;

    public BucketEvent(Main plugin) {
        this.plugin = plugin;
    }

    private void BucketFillLiquidBlock(Player p, PlayerBucketFillEvent e, ItemStack bucket) {
        if(e.getBlockClicked().getType().toString().contains("cauldron")) return;
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        if (!bucketUpdater.isBetterBucket(bucket.getItemMeta())) return;
        if (bucket.getAmount() > 1) {
            p.sendMessage(plugin.format("&cCannot use this item while stacked!"));
            return;
        }
        ItemMeta bucket_meta = bucket.getItemMeta();
        BucketType bucketType = bucketUpdater.getBucketType(bucket_meta);
        int bucketAmount = bucketUpdater.getBucketAmount(bucket_meta);
        int bucketCapacity = bucketUpdater.getBucketCapacity(bucket_meta);
        e.setCancelled(true);
        Block clickedBlock = e.getBlockClicked();
        BlockUpdater blockUpdater = new BlockUpdater();
        Waterlogged wl = blockUpdater.getWaterLoggableBlock(clickedBlock);
        if (bucketType == BucketType.EMPTY) {
            if (clickedBlock.getType() == Material.WATER) bucketType = BucketType.WATER;
            else if (clickedBlock.getType() == Material.LAVA) bucketType = BucketType.LAVA;
            else if (clickedBlock.getType() == Material.POWDER_SNOW) bucketType = BucketType.POWDER_SNOW;
            else if(blockUpdater.isWaterLoggable(clickedBlock) && blockUpdater.isWaterLogged(clickedBlock)){
                    bucketType = BucketType.WATER;
            }
        } else {
            //Can't pickup, currently holds water
            if (bucketType == BucketType.WATER && clickedBlock.getType() == Material.LAVA) {
                p.sendMessage(plugin.format("&cCannot pickup lava! Bucket currently holds water!"));
                return;
            } else if (bucketType == BucketType.WATER && clickedBlock.getType() == Material.POWDER_SNOW) {
                p.sendMessage(plugin.format("&cCannot pickup powder snow! Bucket currently holds water!"));
                return;
                //can't pickup, currently holds lava
            } else if (bucketType == BucketType.LAVA && clickedBlock.getType() == Material.WATER) {
                p.sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds lava!"));
                return;
            } else if (bucketType == BucketType.LAVA && wl != null) {
                p.sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds lava!"));
                return;
            } else if (bucketType == BucketType.LAVA && clickedBlock.getType() == Material.POWDER_SNOW) {
                p.sendMessage(plugin.format("&cCannot pickup powder snow! Bucket currently holds lava!"));
                return;
                //can't pickup, currently holds powder snow
            } else if (bucketType == BucketType.POWDER_SNOW && clickedBlock.getType() == Material.WATER) {
                p.sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds powder snow!"));
                return;
            } else if (bucketType == BucketType.POWDER_SNOW && wl != null) {
                p.sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds powder snow!"));
                return;
            } else if (bucketType == BucketType.POWDER_SNOW && clickedBlock.getType() == Material.LAVA) {
                p.sendMessage(plugin.format("&cCannot pickup lava! Bucket currently holds lava!"));
                return;
            }else if(bucketType == BucketType.MILK && clickedBlock.getType() == Material.WATER){
                p.sendMessage(plugin.format("&cCannot pickup water! Bucket currently holds milk!"));
                return;
            }else if(bucketType == BucketType.MILK && clickedBlock.getType() == Material.LAVA){
                p.sendMessage(plugin.format("&cCannot pickup lava! Bucket currently holds milk!"));
                return;
            }else if(bucketType == BucketType.MILK && clickedBlock.getType() == Material.POWDER_SNOW){
                p.sendMessage(plugin.format("&cCannot pickup powder snow! Bucket currently holds milk!"));
                return;
            }
        }
        if (bucketAmount >= bucketCapacity) {
            p.sendMessage(plugin.format("&cYour bucket is full!"));
            return;
        }
        ItemStack newBucket = bucketUpdater.updateBucket(bucket, BucketAction.INCREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType);
        bucketAmount++;
        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                plugin.format("&" + bucketUpdater.getChatColorValue(bucketType) + bucketAmount + "/" + bucketCapacity)));
        if (wl != null) {
            wl.setWaterlogged(false);
            clickedBlock.setBlockData(wl);
        } else if (clickedBlock.getType() == Material.WATER || clickedBlock.getType() == Material.LAVA || clickedBlock.getType() == Material.POWDER_SNOW) {
            clickedBlock.setType(Material.AIR);
        }
        bucket.setItemMeta(newBucket.getItemMeta());
    }

    @EventHandler
    public void placePowderSnowEvent(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        if (!e.canBuild()) return;
        if (e.getBlock().getType() != Material.POWDER_SNOW) return;
        if (!e.getItemInHand().hasItemMeta()) return;
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        if (!bucketUpdater.isPossibleFilledBetterBucket(e.getItemInHand())) return;
        ItemStack bucket = e.getItemInHand();
        if (!bucketUpdater.isBetterBucket(bucket.getItemMeta())) return;
        if (plugin.checkRegion(e.getPlayer(), BukkitAdapter.adapt(e.getBlock().getLocation()))) return;
        Player p = e.getPlayer();
        if(!plugin.checkClaimAccess(p, e.getBlock().getLocation())) return;
        if (bucket.getAmount() > 1) {
            p.sendMessage(plugin.format("&cCannot use this item while stacked!"));
            e.setBuild(false);
            e.setCancelled(true);
            return;
        }
        ItemMeta bucket_meta = bucket.getItemMeta();
        BucketType bucketType = bucketUpdater.getBucketType(bucket_meta);
        if (bucketType != BucketType.POWDER_SNOW)
            return;//Something bugged as it's ony possible that this is the bucket type
        int bucketAmount = bucketUpdater.getBucketAmount(bucket_meta);
        int bucketCapacity = bucketUpdater.getBucketCapacity(bucket_meta);
        if (bucketAmount == 1) bucketType = BucketType.EMPTY;
        bucketUpdater.updateBucket(bucket, BucketAction.DECREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType);
        bucketAmount--;
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                plugin.format("&" + bucketUpdater.getChatColorValue(bucketType) + bucketAmount + "/" + bucketCapacity)));
        Material bucketMat = bucket.getType();
        int bucketAmt = bucket.getAmount();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                bucket.setType(bucketMat);
                bucket.setAmount(bucketAmt);
                p.getInventory().setItem(e.getHand(), bucket);
            }
        };runnable.runTaskLater(plugin, 1);
    }


    private void BucketFillMilk(Player p, PlayerBucketFillEvent e, ItemStack bucket) {
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        if (!bucketUpdater.isBetterBucket(bucket.getItemMeta())) return;
        if (bucket.getAmount() > 1) {
            p.sendMessage(plugin.format("&cCannot use this item while stacked!"));
            e.setCancelled(true);
            return;
        }
        ItemMeta bucket_meta = bucket.getItemMeta();
        BucketType bucketType = bucketUpdater.getBucketType(bucket_meta);
        if (bucketType == BucketType.EMPTY) bucketType = BucketType.MILK;
        if(bucketType != BucketType.MILK){
            p.sendMessage(plugin.format("&cCannot pickup milk! Bucket currently holds "+
                    bucketType.toString().toLowerCase().replaceAll("_", " ")+"!"));
            e.setItemStack(bucket);
            e.setCancelled(true);
            return;
        }
        int bucketAmount = bucketUpdater.getBucketAmount(bucket_meta);
        int bucketCapacity = bucketUpdater.getBucketCapacity(bucket_meta);
        if (bucketAmount >= bucketCapacity) {
            p.sendMessage(plugin.format("&cYour bucket is full!"));
            e.setItemStack(bucket);
            e.setCancelled(true);
            return;
        }
        bucket = bucketUpdater.updateBucket(bucket, BucketAction.INCREASE_AMOUNT, bucketAmount, bucketCapacity, bucketType);
        bucketAmount++;
        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                plugin.format("&" + bucketUpdater.getChatColorValue(bucketType) + bucketAmount + "/" + bucketCapacity)));
        e.setItemStack(bucket);
    }

    @EventHandler
    public void BucketFillEvent(PlayerBucketFillEvent e) {
        if (e.isCancelled()) return;
        if(plugin.checkRegion(e.getPlayer(), BukkitAdapter.adapt(e.getBlockClicked().getLocation()))) return;
        Player p = e.getPlayer();
        if(!plugin.checkClaimAccess(p, e.getBlock().getLocation())) return;
        if (p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getType() == Material.BUCKET) {
            if(e.getItemStack().getType() == Material.WATER_BUCKET || e.getItemStack().getType() == Material.LAVA_BUCKET ||
                    e.getItemStack().getType() == Material.POWDER_SNOW_BUCKET) BucketFillLiquidBlock(p, e, p.getInventory().getItemInMainHand());
            if(e.getItemStack().getType() == Material.MILK_BUCKET) BucketFillMilk(p, e, p.getInventory().getItemInMainHand());
        } else if (p.getInventory().getItemInOffHand().hasItemMeta() && p.getInventory().getItemInOffHand().getType() == Material.BUCKET) {
            if(p.getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("bucket")) return;
            if(e.getItemStack().getType() == Material.WATER_BUCKET || e.getItemStack().getType() == Material.LAVA_BUCKET ||
                    e.getItemStack().getType() == Material.POWDER_SNOW_BUCKET) BucketFillLiquidBlock(p, e, p.getInventory().getItemInOffHand());
            if(e.getItemStack().getType() == Material.MILK_BUCKET) BucketFillMilk(p, e, p.getInventory().getItemInOffHand());
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

    private void BucketEmptyLiquidBlock(Player p, PlayerBucketEmptyEvent e, ItemStack bucket, BucketUpdater bucketUpdater) {
        //Getting the item
        ItemMeta bucket_meta = bucket.getItemMeta();
        //Check if bucket is a BetterBucket
        if (!bucketUpdater.isBetterBucket(bucket.getItemMeta())) return;
        e.setCancelled(true);
        if (bucket.getAmount() > 1) {
            p.sendMessage(plugin.format("&cCannot use this item while stacked!"));
            return;
        }
        //Clicked Block Mod Face
        int x = e.getBlockClicked().getX() + e.getBlockFace().getModX();
        int y = e.getBlockClicked().getY() + e.getBlockFace().getModY();
        int z = e.getBlockClicked().getZ() + e.getBlockFace().getModZ();
        Block clickedBlock = p.getWorld().getBlockAt(x, y, z);
        //If in a worldguard region, return
        if (plugin.checkRegion(p, BukkitAdapter.adapt(e.getBlockClicked().getLocation()))) return;
        if (plugin.checkRegion(p, BukkitAdapter.adapt(clickedBlock.getLocation()))) return;
        if(!plugin.checkClaimAccess(p, e.getBlock().getLocation())) return;
        if(!plugin.checkClaimAccess(p, clickedBlock.getLocation())) return;
        //Block Updater Checker
        BlockUpdater blockUpdater = new BlockUpdater();
        //Bucket Values
        int LiquidAmt = bucketUpdater.getBucketAmount(bucket_meta);
        int BucketCapacity = bucketUpdater.getBucketCapacity(bucket_meta);
        BucketType bucketType = bucketUpdater.getBucketType(bucket_meta);
        if (bucketType == BucketType.WATER) {
            boolean ClickedBlockInstanceOfWaterLogged = false;
            boolean ClickedBlockWaterLogged = false;
            Waterlogged Clickedwl = null;
            //Check water logged state of clicked block
            if (blockUpdater.isWaterLoggable(e.getBlockClicked())) {
                ClickedBlockInstanceOfWaterLogged = true;
                Clickedwl = blockUpdater.getWaterLoggableBlock(e.getBlockClicked());
                if (blockUpdater.isWaterLogged(e.getBlockClicked())) {
                    ClickedBlockWaterLogged = true;
                }
            }
            //Check water logged state of clicked mod block
            boolean blockInstanceOfWaterLogged = false;
            boolean blockWaterLogged = false;
            Waterlogged wl = null;
            if (blockUpdater.isWaterLoggable(clickedBlock)) {
                blockInstanceOfWaterLogged = true;
                wl = blockUpdater.getWaterLoggableBlock(clickedBlock);
                if (blockUpdater.isWaterLogged(clickedBlock)) {
                    blockWaterLogged = true;
                }
            }
            if (ClickedBlockInstanceOfWaterLogged && !ClickedBlockWaterLogged) {
                Clickedwl.setWaterlogged(true);
                e.getBlockClicked().setBlockData(Clickedwl);
            } else if (blockInstanceOfWaterLogged && !blockWaterLogged) {
                wl.setWaterlogged(true);
                clickedBlock.setBlockData(wl);
            } else {
                clickedBlock.setType(Material.WATER);
            }
        } else if (bucketType == BucketType.LAVA) {
            clickedBlock.setType(Material.LAVA);
        }
        if (LiquidAmt == 1) bucketType = BucketType.EMPTY;
        bucketUpdater.updateBucket(bucket, BucketAction.DECREASE_AMOUNT, LiquidAmt, BucketCapacity, bucketType);
        LiquidAmt--;
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                plugin.format("&" + bucketUpdater.getChatColorValue(bucketType) + LiquidAmt + "/" + BucketCapacity)));
//        bucket.setType(newBucket.getType());
//        bucket.setItemMeta(newBucket.getItemMeta());
    }
        //covers water and lava, not powder snow and milk.
    @EventHandler
    public void BucketEmptyEvent(PlayerBucketEmptyEvent e) {
        if (e.isCancelled()) return;
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        //If main hand item possible betterBucket
        if (bucketUpdater.isPossibleFilledBetterBucket(e.getPlayer().getInventory().getItemInMainHand())) {
            BucketEmptyLiquidBlock(e.getPlayer(), e, e.getPlayer().getInventory().getItemInMainHand(), bucketUpdater);
        } else if (bucketUpdater.isPossibleFilledBetterBucket(e.getPlayer().getInventory().getItemInOffHand())) {
            if(bucketUpdater.isFilledBucketKind(e.getPlayer().getInventory().getItemInMainHand().getType())) return;
            BucketEmptyLiquidBlock(e.getPlayer(), e, e.getPlayer().getInventory().getItemInOffHand(), bucketUpdater);
        }
    }
}