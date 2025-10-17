# Changelog for Minecraft 1.19.2
All notable changes to this project will be documented in this file.

<a name="1.19.2-2.9.8"></a>
## [1.19.2-2.9.8](/compare/1.19.2-2.9.7...1.19.2-2.9.8) - 2025-10-17 15:11:00


### Changed
* Optimize equals and hashCode of recipes
  Related to CyclopsMC/IntegratedCrafting#156

<a name="1.19.2-2.9.7"></a>
## [1.19.2-2.9.7](/compare/1.19.2-2.9.6...1.19.2-2.9.7) - 2025-08-15 08:34:05 +0200


### Fixed
* Remove energy from NBT equality filter config
  This fixes ID energy batteries incorrectly stacking in Integrated Terminals.
  Closes CyclopsMC/IntegratedDynamics#1542

<a name="1.19.2-2.9.6"></a>
## [1.19.2-2.9.6](/compare/1.19.2-2.9.5...1.19.2-2.9.6) - 2025-05-31 19:13:31 +0200


### Added
* Add getters in PrototypedIngredientAlternativesItemStackTag

<a name="1.19.2-2.9.5"></a>
## [1.19.2-2.9.5](/compare/1.19.2-2.9.4...1.19.2-2.9.5) - 2025-05-07 17:16:42 +0200


### Fixed
* Fix extraction from non-zero tanks not working
  Closes CyclopsMC/IntegratedTunnels#335

<a name="1.19.2-2.9.4"></a>
## [1.19.2-2.9.4](/compare/1.19.2-2.9.3...1.19.2-2.9.4) - 2024-12-10 15:40:27 +0100


### Fixed
* Fix Vanilla Furnace not accepting recipes with some empty inputs
  Closes CyclopsMC/IntegratedDynamics#1432

<a name="1.19.2-2.9.3"></a>
## [1.19.2-2.9.3](/compare/1.19.2-2.9.2...1.19.2-2.9.3) - 2024-08-21 17:34:33 +0200


### Fixed
* Fix incorrect RecipeDefinition#hashCode
  This fixes issues where some recipes could not be crafted.
  Closes CyclopsMC/IntegratedCrafting#110

<a name="1.19.2-2.9.2"></a>
## [1.19.2-2.9.2](/compare/1.19.2-2.9.1...1.19.2-2.9.2) - 2024-07-30 09:34:18 +0200


### Fixed
* Correctly extract from slots with limit > 64
  This fixes issues with Integrated Tunnels and Crafting when interacting
  with mods such as Sophisticated Barrels.
  Closes CyclopsMC/IntegratedCrafting#106

<a name="1.19.2-2.9.1"></a>
## [1.19.2-2.9.1](/compare/1.19.2-2.9.0...1.19.2-2.9.1) - 2023-12-27 17:09:29 +0100


### Fixed
* Fix 2x2 Integrated Crafting recipes from Machine Reader failing, Closes CyclopsMC/IntegratedDynamics#1316

<a name="1.19.2-2.9.0"></a>
## [1.19.2-2.9.0](/compare/1.19.2-2.8.3...1.19.2-2.9.0) - 2023-02-11 13:36:51 +0100


### Added
* Store reusable ingredient flags in recipes
  This is required for the reusable ingredients functionality in Integrated Crafting.

<a name="1.19.2-2.8.3"></a>
## [1.19.2-2.8.3] - 2022-08-11 19:47:44 +0200


Update to MC 1.19.2
