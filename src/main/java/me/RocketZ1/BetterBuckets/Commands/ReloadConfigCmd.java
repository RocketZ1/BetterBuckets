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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCmd implements CommandExecutor {
    private Main plugin;

    public ReloadConfigCmd(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("betterbucketsreload").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender.hasPermission("betterbuckets.reload"))) {
            sender.sendMessage(plugin.format("&cYou do not have permission to execute this command!"));
            return true;
        }
        plugin.config.reloadConfig();
        plugin.config.saveConfig();
        plugin.setupConfigFiles();
        sender.sendMessage(plugin.format("&8[BetterBuckets]&f: &aConfig reloaded!"));
        return false;
    }
}
