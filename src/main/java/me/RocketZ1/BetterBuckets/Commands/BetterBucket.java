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
package me.RocketZ1.BetterBuckets.Commands;

import me.RocketZ1.BetterBuckets.Main;
import me.RocketZ1.BetterBuckets.Other.BucketType;
import me.RocketZ1.BetterBuckets.Other.BucketUpdater;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BetterBucket implements CommandExecutor {

    private Main plugin;

    public BetterBucket(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("betterbuckets").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender.hasPermission("betterbuckets.give"))){
            sender.sendMessage(plugin.format("&cYou do not have permission to execute this command!"));
            return true;
        }
        if (1 > args.length) {
            sender.sendMessage(plugin.format("&cInvalid usage, correct usage: /betterbuckets (user) (type) (liquid amount) (bucket capacity)"));
            return true;
        }
        if (4 != args.length) {
            sender.sendMessage(plugin.format("&cInvalid usage, correct usage: /betterbuckets (user) (type) (liquid amount) (bucket capacity)"));
            return true;
        }
        Player p = null;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase((args[0]))) {
                p = player;
                break;
            }
        }
        if (p == null) {
            sender.sendMessage(plugin.format("&cPlayer \"" + args[0] + "\" is not online!"));
            return true;
        }
        BucketType bucketType = null;
        for(BucketType type : BucketType.values()){
            if(args[1].equalsIgnoreCase(type.toString())){
                bucketType = type;
                break;
            }
        }
        if(bucketType == null){
            sender.sendMessage(plugin.format("&cInvalid bucket type!"));
            return true;
        }
        int amountInBucket;
        int bucketCapacity;
        try{
            amountInBucket = Integer.parseInt(args[2]);
            bucketCapacity = Integer.parseInt(args[3]);
        }catch (Exception e){
            sender.sendMessage(plugin.format("&cPlease input whole numbers for the liquid amount and bucket capacity!"));
            return true;
        }
        if(bucketType == BucketType.EMPTY) {
            if (amountInBucket != 0) {
                sender.sendMessage(plugin.format("&cCannot put liquid in an empty bucket!"));
                return true;
            }
        }
        if(bucketType != BucketType.EMPTY) {
            if (amountInBucket == 0) {
                sender.sendMessage(plugin.format("&cCannot put no liquid in a liquid bucket!"));
                return true;
            }
        }
        if(amountInBucket > bucketCapacity){
            sender.sendMessage(plugin.format("&cCannot put more liquid in the bucket than the bucket capacity!"));
            return true;
        }
        BucketUpdater bucketUpdater = new BucketUpdater(plugin);
        ItemStack bucket = bucketUpdater.createBucket(bucketType, amountInBucket, bucketCapacity);
        if(p.getInventory().firstEmpty() == -1){
            p.getWorld().dropItemNaturally(p.getLocation(), bucket);
            sender.sendMessage(plugin.format("&a" + p.getName() + " inventory was full, dropped item on ground."));
        }else{
            p.getInventory().addItem(bucket);
        }
        return false;
    }
}
