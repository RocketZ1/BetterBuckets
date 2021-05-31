package me.RocketZ1.BetterBuckets.Commands;

import me.RocketZ1.BetterBuckets.Main;
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

        // /betterbuckets (User)
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
        String bucketType = "";
        boolean bucketTypeFound = false;
        if(args[1].equalsIgnoreCase("Empty")){
            bucketType = "Empty";
            bucketTypeFound = true;
        }else if(args[1].equalsIgnoreCase("Lava")){
            bucketType = "Lava";
            bucketTypeFound = true;
        }else if(args[1].equalsIgnoreCase("Water")){
            bucketType = "Water";
            bucketTypeFound = true;
        }
        if(!bucketTypeFound){
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
        if(bucketType.equalsIgnoreCase("Empty")) {
            if (amountInBucket != 0) {
                sender.sendMessage(plugin.format("&cCannot put liquid in an empty bucket!"));
                return true;
            }
        }
        if(bucketType.equalsIgnoreCase("Lava")) {
            if (amountInBucket == 0) {
                sender.sendMessage(plugin.format("&cCannot put no liquid in a liquid bucket!"));
                return true;
            }
        }
        if(bucketType.equalsIgnoreCase("Water")){
            if (amountInBucket == 0) {
                sender.sendMessage(plugin.format("&cCannot put no liquid in a liquid bucket!"));
                return true;
            }
        }
        if(amountInBucket > bucketCapacity){
            sender.sendMessage(plugin.format("&cCannot put more liquid in the bucket than the bucket capacity!"));
            return true;
        }

        int slotNum = 0;
        boolean found = false;
        for (ItemStack slots : p.getInventory()) {
            slotNum++;
            if (slots != null) continue;
            if (slotNum > 36) break;
            found = true;
            p.getInventory().setItem(slotNum - 1, plugin.BigBucket(bucketType, amountInBucket, bucketCapacity));
            break;
        }
        if (!found) {
            p.getWorld().dropItemNaturally(p.getLocation(), plugin.BigBucket(bucketType, amountInBucket, bucketCapacity));
            sender.sendMessage(plugin.format("&a" + p.getName() + " inventory was full, dropped item on ground."));
        }
        return false;
    }
}
