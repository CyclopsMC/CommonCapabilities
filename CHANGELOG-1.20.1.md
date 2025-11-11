# Changelog for Minecraft 1.20.1
All notable changes to this project will be documented in this file.

<a name="1.20.1-2.9.10"></a>
## [1.20.1-2.9.10](/compare/1.20.1-2.9.9...1.20.1-2.9.10) - 2025-10-17 15:12:02


### Changed
* Optimize equals and hashCode of recipes
  Related to CyclopsMC/IntegratedCrafting#156

<a name="1.20.1-2.9.9"></a>
## [1.20.1-2.9.9](/compare/1.20.1-2.9.8...1.20.1-2.9.9) - 2025-10-07 07:35:07 +0200


### Fixed
* Clear recipe handler caches on server stop
* Fix not all shapeless recipes not being exposed correctly

<a name="1.20.1-2.9.8"></a>
## [1.20.1-2.9.8](/compare/1.20.1-2.9.7...1.20.1-2.9.8) - 2025-08-15 08:35:10 +0200


### Fixed
* Remove energy from NBT equality filter config
  This fixes ID energy batteries incorrectly stacking in Integrated Terminals.
  Closes CyclopsMC/IntegratedDynamics#1542

<a name="1.20.1-2.9.7"></a>
## [1.20.1-2.9.7](/compare/1.20.1-2.9.6...1.20.1-2.9.7) - 2025-05-31 19:15:01 +0200


### Added
* Add getters in PrototypedIngredientAlternativesItemStackTag

<a name="1.20.1-2.9.6"></a>
## [1.20.1-2.9.6](/compare/1.20.1-2.9.5...1.20.1-2.9.6) - 2025-05-10 08:50:33 +0200


### Fixed
* Fix recipe simulation for smithing table
* Fix recipe simulation for stonecutter recipes

<a name="1.20.1-2.9.5"></a>
## [1.20.1-2.9.5](/compare/1.20.1-2.9.4...1.20.1-2.9.5) - 2025-05-07 17:17:22 +0200


### Fixed
* Fix extraction from non-zero tanks not working
  Closes CyclopsMC/IntegratedTunnels#335

<a name="1.20.1-2.9.4"></a>
## [1.20.1-2.9.4](/compare/1.20.1-2.9.3...1.20.1-2.9.4) - 2024-12-10 15:41:18 +0100


### Fixed
* Fix Vanilla Furnace not accepting recipes with some empty inputs
  Closes CyclopsMC/IntegratedDynamics#1432

<a name="1.20.1-2.9.3"></a>
## [1.20.1-2.9.3](/compare/1.20.1-2.9.2...1.20.1-2.9.3) - 2024-08-21 17:43:12 +0200


### Added
* Add tr_tr.json Turkish localization

### Fixed
* Fix incorrect RecipeDefinition#hashCode
  This fixes issues where some recipes could not be crafted.
  Closes CyclopsMC/IntegratedCrafting#110
* Correctly extract from slots with limit > 64
  This fixes issues with Integrated Tunnels and Crafting when interacting
  with mods such as Sophisticated Barrels.
  Closes CyclopsMC/IntegratedCrafting#106

<a name="1.20.1-2.9.2"></a>
## [1.20.1-2.9.2](/compare/1.20.1-2.9.1...1.20.1-2.9.2) - 2024-07-30 09:37:21 +0200


### Fixed
* Correctly extract from slots with limit > 64
  This fixes issues with Integrated Tunnels and Crafting when interacting
  with mods such as Sophisticated Barrels.
  Closes CyclopsMC/IntegratedCrafting#106

<a name="1.20.1-2.9.1"></a>
## [1.20.1-2.9.1](/compare/1.20.1-2.9.0...1.20.1-2.9.1) - 2023-12-27 17:11:58 +0100


### Fixed
* Fix 2x2 Integrated Crafting recipes from Machine Reader failing, Closes CyclopsMC/IntegratedDynamics#1316

<a name="1.20.1-2.9.0"></a>
## [1.20.1-2.9.0] - 2023-07-02 08:10:29 +0200


Initial 1.20.1 release
