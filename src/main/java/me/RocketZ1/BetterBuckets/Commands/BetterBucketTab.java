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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BetterBucketTab implements TabCompleter {

    private Main plugin;

    public BetterBucketTab(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("betterbuckets").setTabCompleter(this);
    }

    List<String> arguments2 = new ArrayList<String>();
    List<String> arguments3 = new ArrayList<String>();
    List<String> arguments4 = new ArrayList<String>();

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return null;
        if (!sender.hasPermission("betterbuckets.give")) return null;
        ///betterbuckets (user) (type) (liquid amount) (bucket capacity)
        if (args.length == 1) {
            return null;
        } else if (args.length == 2) {
            if (arguments2.isEmpty()) {
                for(BucketType bucketType : BucketType.values()){
                    arguments2.add(bucketType.toString().toLowerCase());
                }
            }
            List<String> result = new ArrayList<String>();
            if (args.length == 2) {
                for (String a : arguments2) {
                    if (a.toLowerCase().startsWith(args[1].toLowerCase()))
                        result.add(a);
                }
                return result;
            }
        } else if (args.length == 3) {
            if (arguments3.isEmpty()) {
                arguments3.add("1");
                arguments3.add("10");
                arguments3.add("100");
            }
            List<String> result = new ArrayList<String>();
            if (args.length == 3) {
                for (String a : arguments3) {
                    if (a.toLowerCase().startsWith(args[2].toLowerCase()))
                        result.add(a);
                }
                return result;
            }
        } else if (args.length == 4) {
            if (arguments4.isEmpty()) {
                arguments4.add("1");
                arguments4.add("10");
                arguments4.add("100");
            }
            List<String> result = new ArrayList<String>();
            if (args.length == 4) {
                for (String a : arguments4) {
                    if (a.toLowerCase().startsWith(args[3].toLowerCase()))
                        result.add(a);
                }
                return result;
            }
        }
        return null;
    }
}

