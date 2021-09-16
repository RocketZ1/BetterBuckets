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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.ArrayList;

public class DisableBucketFish implements Listener {

    private Main plugin;

    public DisableBucketFish(Main plugin) {
        this.plugin = plugin;
        blackListedFish.add(EntityType.PUFFERFISH);
        blackListedFish.add(EntityType.TROPICAL_FISH);
        blackListedFish.add(EntityType.SALMON);
        blackListedFish.add(EntityType.COD);
        blackListedFish.add(EntityType.AXOLOTL);
    }

    private final ArrayList<EntityType> blackListedFish = new ArrayList<>();

    @EventHandler
    public void DisableFish(PlayerInteractEntityEvent e) {
        if (e.isCancelled()) return;
        if(blackListedFish.contains(e.getRightClicked().getType())) {
            if (checkForFish(e.getPlayer())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(plugin.format("&cCannot pickup fish with this bucket!"));
            }
        }
    }

    private boolean checkForFish(Player p) {
        if (p.getInventory().getItemInMainHand().getType() == Material.WATER_BUCKET) {
            BucketUpdater bucketUpdater = new BucketUpdater(plugin);
            if(!bucketUpdater.isPossibleFilledBetterBucket(p.getInventory().getItemInMainHand())) return false;
            if(!bucketUpdater.isBetterBucket(p.getInventory().getItemInMainHand().getItemMeta())) return false;
            return true;
        } else if (p.getInventory().getItemInOffHand().getType() == Material.WATER_BUCKET) {
            BucketUpdater bucketUpdater = new BucketUpdater(plugin);
            if(!bucketUpdater.isPossibleFilledBetterBucket(p.getInventory().getItemInOffHand())) return false;
            if(!bucketUpdater.isBetterBucket(p.getInventory().getItemInOffHand().getItemMeta())) return false;
            return true;
        }
        return false;
    }
}