# Changelog for Minecraft 1.21.1
All notable changes to this project will be documented in this file.

<a name="1.21.1-2.10.1"></a>
## [1.21.1-2.10.1](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.10.0...1.21.1-2.10.1) - 2025-10-17 15:16:06


### Changed
* Optimize equals and hashCode of recipes
  Related to CyclopsMC/IntegratedCrafting#156

<a name="1.21.1-2.10.0"></a>
## [1.21.1-2.10.0](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.9.12...1.21.1-2.10.0) - 2025-10-07 07:44:32 +0200


### Added
* Add SizedIngredient variant for getPrototypesFromIngredient (required for Integrated Mekanism)

### Fixed
* Clear recipe handler caches on server stop
* Fix not all shapeless recipes not being exposed correctly

<a name="1.21.1-2.9.12"></a>
## [1.21.1-2.9.12](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.9.11...1.21.1-2.9.12) - 2025-08-15 08:42:31 +0200


### Added
* Add translations through Crowdin (#42)
* Update PT_BR localization (#41)

### Fixed
* Remove energy from NBT equality filter config
  This fixes ID energy batteries incorrectly stacking in Integrated Terminals.
  Closes CyclopsMC/IntegratedDynamics#1542

<a name="1.21.1-2.9.11"></a>
## [1.21.1-2.9.11](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.9.10...1.21.1-2.9.11) - 2025-05-31 19:23:33 +0200


### Added
* Add getters in PrototypedIngredientAlternativesItemStackTag

<a name="1.21.1-2.9.10"></a>
## [1.21.1-2.9.10](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.9.9...1.21.1-2.9.10) - 2025-05-10 08:50:54 +0200


### Fixed
* Fix recipe simulation for stonecutter recipes

<a name="1.21.1-2.9.9"></a>
## [1.21.1-2.9.9](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.9.8...1.21.1-2.9.9) - 2025-05-07 17:18:00 +0200


### Fixed
* Fix extraction from non-zero tanks not working
  Closes CyclopsMC/IntegratedTunnels#335

<a name="1.21.1-2.9.8"></a>
## [1.21.1-2.9.8](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.9.7...1.21.1-2.9.8) - 2025-04-04 17:13:53 +0200


### Added
* Add nl_nl translations

### Fixed
* Fix wrong worker hasWork capability on brewing stand
* Fix recipes for <3x3 grids sometimes being invalid
* Fix bundle capability storing empty stacks
* Fix bundle item handler not supporting item removals
* Fix item entity capabilities not being added correctly

<a name="1.21.1-2.9.7"></a>
## [1.21.1-2.9.7](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.9.6...1.21.1-2.9.7) - 2024-12-10 15:45:16 +0100


### Changed
* Improve performance of DataComparator, required for CyclopsMC/IntegratedTerminals#139

### Fixed
* Fix Vanilla Furnace not accepting recipes with some empty inputs
  Closes CyclopsMC/IntegratedDynamics#1432

<a name="1.21.1-2.9.6"></a>
## [1.21.1-2.9.6](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.9.5...1.21.1-2.9.6) - 2024-11-10 14:09:31 +0100


### Fixed
* Fix Integrated Dynamics batteries not being auto-craftable
  Closes CyclopsMC/IntegratedCrafting#113

<a name="1.21.1-2.9.5"></a>
## [1.21.1-2.9.5](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.9.4...1.21.1-2.9.5) - 2024-10-13 17:28:43 +0200


### Fixed
* Fix mutation when serializing items with count > 99
  This caused desync issues in mods such as Integrated Terminals.
  Closes CyclopsMC/IntegratedTerminals#126

<a name="1.21.1-2.9.4"></a>
## [1.21.1-2.9.4](https://github.com/CyclopsMC/CommonCapabilities/compare/1.21.1-2.9.3...1.21.1-2.9.4) - 2024-08-21 17:47:56 +0200


### Fixed
* Fix incorrect RecipeDefinition#hashCode
  This fixes issues where some recipes could not be crafted.
  Closes CyclopsMC/IntegratedCrafting#110
* Refer to NeoForge's updateJSONURL instead of Forge's
* Correctly extract from slots with limit > 64
  This fixes issues with Integrated Tunnels and Crafting when interacting
  with mods such as Sophisticated Barrels.
  Closes CyclopsMC/IntegratedCrafting#106

<a name="1.21.1-2.9.3"></a>
## [1.21.1-2.9.3] - 2024-08-09 21:03:16 +0200


Added:
* Add missing serialization context when decoding ingredients
  This fixes a problem in Integrated Terminals where terminals break if
  they contain enchanted items.
  Closes CyclopsMC/IntegratedDynamics#1375

### Fixed
* Fix capability getter failing with void context capabilities
  This fixes crashes with the Integrated Dynamics world item importer,
  Closes CyclopsMC/IntegratedDynamics#1376
