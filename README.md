# BetterBuckets
Main Github Link: https://github.com/RocketZ1/BetterBuckets

## Simple Description
This is a plugin which adds advanced buckets that can hold any size capacity of liquid.

## Commands
* /betterbuckets
* /betterbucketsreload

Command | Permissions | Description
----- | ---- | ------
/betterbuckets <player> <water/lava/milk/powder_snow/empty> <liquid_amount> <bucket_capacity> | betterbuckets.give | Gives the selected player the better bucket specified in the command arguments
/betterbucketsreload | betterbuckets.reload | Reloads the plugin config

# How to use the BetterBucket
You obtain a BetterBucket with the /betterbuckets command. The BetterBucket capacity can be set to any size from 1-2,147,483,647. The BetterBucket is used like a normal bucket. You can pickup as much liquid as the BetterBucket's capacity can hold. You can press Shift + Right click the air to toggle the bucket between place and pickup mode. 

Most default bucket functionality works with the BetterBucket, with the exception of picking up fish in the BetterBucket, and using dispensers with the BetterBucket.

## Events
* **Bucket Event** 

    This class uses both PlayerBucketFillEvent, PlayerBucketEmptyEvent and BlockPlaceEvent. This class is the main functionality of the BetterBucket picking up and placing liquid.
* **Cauldron Event**

    This class uses CauldronLevelChangeEvent. This class is the functionality of the BetterBucket interacting with a cauldron.
* **Disable Bucket Fish**

    This class uses PlayerInteractEntityEvent. This class is the functionality that disables the BetterBucket from picking up fish.
* **Disable Dispensers**

    This class uses BlockDispenseEvent. This class blocks the default the use of a BetterBucket in a dispenser. Might implement dispenser functionality in a later update.
* **Milk Bucket**

    This class uses PlayerItemConsumeEvent. This class adds the functionality of the BetterBucket drinking milk from a bucket.
* **Furnace Event**

    This class uses FurnaceBurnEvent. This class blocks the default functionality of a furnace when a BetterBucket is used as fuel, and runs the correct functionality of the BetterBucket.
* **Toggle Mode**

    This class uses PlayerInteractEvent. This class is the functionality of the BetterBucket toggling between pickup and place mode.

## Dependencies
This plugin supports WorldGuard and GriefPrevention

## Other Comments
This plugin does not fully support all vanilla functionality. Due to the way the Spigot API works, some features have small bugs. 

**List of known bugs**
* Dispenser functionality has currently been disabled due to how buggy it is.
* Waterlogging a block does not give a block update to the waterlogged block.
* Picking up an axolotl might cause the axolotl to not render anymore for the player until they relog (client side rendering bug).
* Trying to milk a cow with a full BetterBucket might cause a visual bug that causes the BetterBucket to appear as a normal bucket of milk (client side rendering bug).
* Toggling a BetterBucket milk bucket sometimes causes the player to drink the milk bucket.

# Offical Plugin Discord
**If you have any issues, questions, concerns, want to report a bug, or to get updates about this plugin, please join the offical discord!**
https://discord.gg/xMW5a9FtaD
