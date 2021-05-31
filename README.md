# BetterBuckets
Main Github Link: https://github.com/RocketZ1/BetterBuckets

## Simple Description
This is a plugin which adds advanced buckets that can hold any size capacity of liquid.

## Commands
The only command is /betterbuckets

Command | Permissions | Description
----- | ---- | ------
/betterbuckets (user) <water/lava/empty> (liquid amount) (bucket capacity)  | betterbuckets.give | Gives the selected player the better bucket specified in the command arguments

# How to use the BetterBucket
You obtain a BetterBucket with the /betterbuckets command. The BetterBucket capacity can be set to any size from 1-2,147,483,647. The BetterBucket is used like a normal bucket. You can pickup as much liquid as the BetterBucket's capacity can hold. You can press Shift + Right click the air to toggle the bucket between place and pickup mode. 

Most default functionality works with the BetterBucket, with the exception of picking up fish in the BetterBucket, and milking a cow with the BetterBucket.

## Events
* Bucket Event 

    This class uses both PlayerBucketFillEvent and PlayerBucketEmptyEvent. This class is the main functionality of the BetterBucket picking up and placing liquid.
* Cauldron Event

    This class uses CauldronLevelChangeEvent. This class is the functionality of the BetterBucket interacting with a cauldron.
* Disable Bucket Fish

    This class uses PlayerInteractEntityEvent. This class is the functionality that disables the BetterBucket from picking up fish.
* Disable Dispensers

    This class uses BlockDispenseEvent. This class blocks the default functionality of a dispenser when a BetterBucket is dispensed, and runs the correct functionality of the BetterBucket.
* Disable Milking

    This class uses PlayerInteractEntityEvent. This class is the functionality that disables the BetterBucket from milking a cow.
* Furnace Event

    This class uses FurnaceBurnEvent. This class blocks the default functionality of a furnace when a BetterBucket is used as fuel, and runs the correct functionality of the BetterBucket.
* Toggle Mode

    This class uses PlayerInteractEvent. This class is the functionality of the BetterBucket toggling between pickup and place mode.
