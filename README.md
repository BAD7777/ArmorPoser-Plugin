[![](http://cf.way2muchnoise.eu/versions/1078242.svg)](https://www.curseforge.com/minecraft/bukkit-plugins/daily-dad-plugin)

# Armor Poser - Plugin #

## About ##
This paper plugin adds compatibility for the mod by the same name [Armor Poser](https://www.curseforge.com/minecraft/mc-mods/armor-poser).

## Permissions ##
When the `requirePermissions` config option is set to `true`, these permissions can be used

| Permission        | Description                               |
|-------------------|-------------------------------------------|
| armorposer.use    | Allows a player to open the GUI           |
| armorposer.resize | Allows a player to resize the Armor Stand |
| armorposer.lock   | Allows a player to lock Armor Stands      |
| armorposer.reload | Allows reloading the plugin config        |

## Command ##

| Command             | Description             |
|---------------------|-------------------------|
| /armorposer reload  | Reloads plugin config   |

## Config ##

| Option               | Default | Description |
|----------------------|---------|-------------|
| enableConfigGui      | true    | Allows opening the Armor Poser GUI |
| requirePermissions   | false   | Requires permission nodes for plugin actions |
| restrictResizeToOP   | false   | Restricts resize to OP (or whitelist) when permissions are not required |
| resizeWhitelist      | [""]    | Players that can resize when OP restriction is enabled |
| minArmorStandScale   | 0.25    | Minimum allowed Armor Stand scale (`Scale` values below this are ignored) |
| maxArmorStandScale   | 4.0     | Maximum allowed Armor Stand scale (`Scale` values above this are ignored) |

## License ##
* Armor Poser - Plugin licensed under the MIT license
  - (c) 2024 Mrbysco
  - [![License](https://img.shields.io/badge/License-MIT-red.svg?style=flat)](http://opensource.org/licenses/MIT)

## Downloads ##
Downloads will be available on [Curseforge](https://www.curseforge.com/minecraft/bukkit-plugins/armor-poser)
