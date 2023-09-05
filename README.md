# Skyclash

<div align="center">
    <img src="https://github.com/Elolisme/skyclash/assets/118145727/3cdf733c-1466-443a-b4c2-d79f054c57db" style="height:auto;width:60%"/>
    <br>
    <br>
    <strong>Minecraft plugin, reimagining Hypixel's old minigame "Skyclash"</strong>
</div>

## Resources for development
https://docs.google.com/spreadsheets/d/19AjEcBofWj3tTlZCbzQlgGsIxJ_DaLLrYeETvVY76Nc/edit#gid=1239566438
- Docs for cards + maps

https://docs.google.com/spreadsheets/d/19AjEcBofWj3tTlZCbzQlgGsIxJ_DaLLrYeETvVY76Nc/edit#gid=1239566438
- Spreadsheet of kits

https://docs.google.com/spreadsheets/d/1B-bbUVI84wnbqoIyJHG1DLEuIvmKJaStwKW8BKYEctg/edit?usp=sharing
- Forced labour completely in vain

## How To Install
**TODO**

You need to install the multiverse plugin, this is dependant on that

You optionally would want VoidWorld plugin to generate new void worlds for new maps

### How to use multiverse:
```
/mv tp <ur username> <world name> 
    TP to a world use 
/mv list 
    See all the different worlds
/mv create <world name> normal -g VoidWorld 
    Create a new empty world
```

### Preferable settings for a new world:
- set the world ~ y=64 high up
- use /mv modify set monsters false to turn of mob spawns
- use /gamerule doDaylightCycle false,  and use /time set day
- use /mv modify set gamemode creative to set default to creative
- use /mv modify set spawn for spawn to be at ur location

## Skyclash's Drug Pollinated Code Plugin
### Commands
/setvotes <map index> <votes>
- Used to change votes for a map for admins

/startgame
- Start the game earlier (5 seconds), may result in 0 player games where u need to /endgame

/giveitem <player> <item> | /customitem <player> <item>
- lets you get custom items from this plugin

/endgame | /abort
- Ends the currently running game

/lootchest <name>
- makes a loot table file in ```<root>/LootChestst/..``` from the current chest you are looking at

/lobby | /home | /hub
- send you to the first spawn point of the default world

/setchest [add|remove|list]
- lets you add/remove a chest in a world where loot will spawn, once a game starts on that world
- Takes the current chest you are looking at in a specific world
- This technically just edits maps.json, so you can manually input coords in the file
- /setchest list will just list coords already set in the world

/gamespawn [add|remove|list]
- adds a spawnpoint for when games run, wher players start out at
- look at block under spawn to set it as one, and use list to show already set coords
- this also modifies maps.json similarly to /setchest

### Permissions
sgm.lobby - default permission for everyone, allows /lobby command
<br>
sgm.host - allows opped players access to all other commands

### Kits and Cards
Kits:
- Swordsman
- Beserker
- Assassin

Cards
- Bigger Bangs (just like ur mum)
- Creeper
- Damage Potion

### Saving Player, World
When you log in, your data is saved to a file in ```<Root folder>/Players/<username>.json```
<br>
Whenever the game needs it, it will read from those files

Also, SDPC will save all multiverse worlds to a JSON in ```<root>/plugins/maps.json``` which has settings for each world, which automatically update for new worlds. You can manually edit any setting realtime.
<br>
You can change the icon of a world (icon:material), whether its a playable map (ignore:boolean), and where chests/spawnpoints are.

### Chest Loot
The plugin will take chest loot from a folder called ```<root>/LootChests/..```, where loot tables is stored
<br>
There are a default of 2, one for spawn and one for mid. You can technically add more using /lootchest but you have to manually configure it in the plugin and recompile...

<img src="https://media.discordapp.net/attachments/1124653076205281440/1139865042397970502/image.png?width=477&height=217" />

<img src="https://media.discordapp.net/attachments/1124653076205281440/1139866651060338698/image.png?width=466&height=201"/>

### Lifetime Statistics
The current statistics saved for each player are:
- Kills
- Deaths
- Wins
- Total Games Played
- Joins

As well as storing coins, which are gained by killing (10) or winning a game (50)
* Note killing yourself will not award kill credit but counts as a death

### Voting System
Click into menu, then vote
<br>
Click on the block to vote for that map
<br>
When a game starts, if 2 maps have equal votes its a 50/50
```
/setvotes <map name> <value>
For admins, use this command to change the votes
```

Maps are automatically added for each **multiverse** world after the plugin is reloaded

### Starting the game
In the menu click the wool to become ready
<br>
When 2 or more people become ready the game starts in 30s
<br>
Click the wool again to unready yourself, this will cancel the game if 2 players are no longer ready.

```
/startgame
Exists for admins to start game manually
``` 

### Ending a Game
**TODO**

### Chest Loot system
**TODO**

https://github.com/Veraimt/CustomLootChest
- mostly taken from here, but modified for skyclash

### Custom Weapons
Get weapons by using the command 
<br>
  ```  /giveitem <item>```
<br>
(use lowercase with spaces)
<br>
Example:
  ```
  /giveitem chicken bow
  ```

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
- add lapis in chest loot
- setchest wand
- pearl cooldown
- item in menu showing stats or smthing like that

Future
- Add more kits and cards
- Upgrade kit and card system

# Changelog

## v1.2.0
Kits:
- added swordsman true damage every 10 seconds 
- added beserker resistance on low health (2.5 hearts) passives
- made bigger bangs cause nearby explosions within 25 blocks have a greater explosion radius<br><br>
<img src="https://media.discordapp.net/attachments/1124653076205281440/1146360931673186385/image.png?width=1183&height=657" style="height:auto;width:60%"/>

- made bigger bangs spawn with flint and steel and 2 tnt

Code:
- fixed xp clearing
- fixed players being able to hit each other maybe
- added grace period of 5 seconds
- make setchest list show locations that have no chests
- added lifetime stats - kills, deaths, wins, games, join amount,

GitHub:
- accidentally spammed some commits
- added more to readme

## v1.1.3
Code:
- actaully removed tags from players at start and end of game
- fixed players being able to hit each other in lobby
- fixed dc's
- fixed armour clearing
- fixed effect clearing
- fixed spectators dcing
- fixed startgame not working sometimes due to offline players being ready
- fixed /lobby for deopped players

Kits:
- swordsman strength level to 1 (from 2)
- assassin now gets 1 pearl and invis pot (from 2 each)
- beserker pots are 15 seconds (from 60)
- beserker pots are regen 2, resistance 1 (from 3, 2)

GitHub:
- Removed target cause its unnecessary
- somehow missed 1.1.2 oh well

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
