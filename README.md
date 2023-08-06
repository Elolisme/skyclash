# Skyclash
Minecraft plugin, reimagining Hypixel's old minigame "Skyclash"
<br>

***

## Skyclash Game Manager
### Kits and Cards


### Saving Data and Worlds
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

### Custom Weapons
#### Usage
Get weapons by using the command 
  ```/giveitem <item>```
<br>
(use lowercase with spaces)
<br>
Example:
  ```/giveitem chicken bow```

### Current Items

#### Chicken bow
This shoots a chicken jockey instead of an arrow
Has 10 durability
<br>

##### Explosive bow
Shoots tnt instead of arrows
Has 5 durability
<br>
##### Winged boots
Wearing these leather boots gives you jump boost and less fall damage
<br>
##### Fireball
Launch a fireball by clicking
Consumes item
<br>
##### Sword of justice
Summon lightning 2 blocks in front of you when you hit an enemy
Has a cooldown of 2 seconds

