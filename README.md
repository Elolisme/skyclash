# Skyclash
Minecraft plugin, reimagining Hypixel's old minigame "Skyclash"
<br>

### How To Use
**TODO**
***

## Skyclash's Drug Pollinated Code Plugin
### Commands
**TODO**

### Permissions
**TODO**

### Kits and Cards
**TODO**

### Saving Player, World, and Chest Loot data
When you log in, your data is saved to a file in ```<Root folder>/Players/<username>.json```
<br>
Whenever the game needs it, it will read from those files
<br>
<br>
Also, SGM will save all multiverse worlds to a JSON in ```<root>/plugins/maps.json``` which has settings for each world, which automatically updated for new worlds.
<br>
You can change the icon of a world (icon:material), whether its a playable map (ignore:boolean), and where chests/spawnpoints are.

### Voting System
Click into menu, then vote
<br>
Click on the block to vote for that map
<br>
When a game starts, if 2 maps have equal votes its a 50/50
<br>
For admins, use this command to change the votes
```/setvotes <map name> <value>```
<br>
Maps are automatically added for each multiverse world AKA USE MULTIVERSE

### Starting the game
In the menu click the wool to become ready
<br>
When 2 or more people become ready the game starts in 30s
<br>
Click the wool again to unready yourself, this will cancel the game if 2 players are no longer ready.
<br>
```/startgame``` exists for admins to start game manually
<br>

### Ending a Game
**TODO**

### Chest Loot system
**TODO**

### Custom Weapons
Get weapons by using the command 
  ```/giveitem <item>```
<br>
(use lowercase with spaces)
<br>
Example:
  ```/giveitem chicken bow```

#### Weapons:
- Chicken bow
  - This shoots a chicken jockey instead of an arrow
  - Has 10 durability
- Explosive bow
  - Shoots tnt instead of arrows
  - Has 5 durability
- Winged boots
  - Wearing these leather boots gives you jump boost and less fall damage
- Fireball
  - Launch a fireball by clicking
  - Consumes item
- Sword of justice
  - Summon lightning 2 blocks in front of you when you hit an enemy
  - Has a cooldown of 2 seconds


# TODO
Soon
- Finish readme
- balance kits
- fix ALL bugs
- add nbt to chest loot
- improve setchest for convenience


Future
- Add more kits and cards
- Upgrade kit and card system

# Changelog

## v1.1.2
Code:
- Made enderchests now spawn better loot
- Fixed minor inconveniences
- fixed nether star
- maybe fixed starting game issue
- there was once an block iterator issue

## v1.1.1
Code:
- made maps start with correct settings
- fixed /giveitem command and custom items not working
- changed /startgame to start in 5 seconds, as opposed to 30
- ingame map does not spawn mobs, set to day on start
- added /setchest [add|remove|list] command (can change where chest loot can go)
- added /gamespawn [add|remove|list] command (similar function but for spawnpoints instead)


## v1.1.0
GitHub:
- Added changelog
- changed plugin name to SDPC (skyclash drug pollinated code)
- Added todo in readme so it's easier to track progress

Code:
- refactored a lot of files and organised everything
- added chest refill system with chest loots
- Finished any loose ends with the plugin, it should be fully functional now
- Changed state value in json to hasJoined as it was no longer necessary
