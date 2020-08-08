## CommonCapabilities

[![Build Status](https://travis-ci.org/CyclopsMC/CommonCapabilities.svg)](https://travis-ci.org/CyclopsMC/CommonCapabilities)
[![Coverage Status](https://coveralls.io/repos/github/CyclopsMC/CommonCapabilities/badge.svg)](https://coveralls.io/github/CyclopsMC/CommonCapabilities)
[![Download](https://img.shields.io/maven-metadata/v/https/oss.jfrog.org/artifactory/simple/libs-release/org/cyclops/commoncapabilities/CommonCapabilities/maven-metadata.xml.svg) ](https://oss.jfrog.org/artifactory/simple/libs-release/org/cyclops/commoncapabilities/CommonCapabilities/)
[![CurseForge](http://cf.way2muchnoise.eu/full_247007_downloads.svg)](http://minecraft.curseforge.com/projects/247007)
[![Discord](https://img.shields.io/discord/386052815128100865.svg?colorB=7289DA&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHYAAABWAgMAAABnZYq0AAAACVBMVEUAAB38%2FPz%2F%2F%2F%2Bm8P%2F9AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhBxwQJhxy2iqrAAABoElEQVRIx7WWzdGEIAyGgcMeKMESrMJ6rILZCiiBg4eYKr%2Fd1ZAfgXFm98sJfAyGNwno3G9sLucgYGpQ4OGVRxQTREMDZjF7ILSWjoiHo1n%2BE03Aw8p7CNY5IhkYd%2F%2F6MtO3f8BNhR1QWnarCH4tr6myl0cWgUVNcfMcXACP1hKrGMt8wcAyxide7Ymcgqale7hN6846uJCkQxw6GG7h2MH4Czz3cLqD1zHu0VOXMfZjHLoYvsdd0Q7ZvsOkafJ1P4QXxrWFd14wMc60h8JKCbyQvImzlFjyGoZTKzohwWR2UzSONHhYXBQOaKKsySsahwGGDnb%2FiYPJw22sCqzirSULYy1qtHhXGbtgrM0oagBV4XiTJok3GoLoDNH8ooTmBm7ZMsbpFzi2bgPGoXWXME6XT%2BRJ4GLddxJ4PpQy7tmfoU2HPN6cKg%2BledKHBKlF8oNSt5w5g5o8eXhu1IOlpl5kGerDxIVT%2BztzKepulD8utXqpChamkzzuo7xYGk%2FkpSYuviLXun5bzdRf0Krejzqyz7Z3p0I1v2d6HmA07dofmS48njAiuMgAAAAASUVORK5CYII%3D)](https://discord.gg/9yDxubB)

Forge Capabilities that can be shared by multiple mods.
This mod is responsible for registering the capabilities from the [API](https://github.com/CyclopsMC/CommonCapabilitiesAPI),
it also provides default implementations of these capabilities for various mods.

All stable releases (including deobfuscated builds) can be found on [CurseForge](http://minecraft.curseforge.com/projects/247007/files).

[Development builds](https://oss.jfrog.org/artifactory/simple/libs-release/org/cyclops/commoncapabilities/CommonCapabilities/) are hosted by [JFrog Artifactory](https://www.jfrog.com/artifactory/).

### Contributing
* Before submitting a pull request containing a new feature, please discuss this first with one of the lead developers.
* When fixing an accepted bug, make sure to declare this in the issue so that no duplicate fixes exist.
* All code must comply to our coding conventions, be clean and must be well documented.
* All capabilities for mods must be tested in a separate test mod, in the `src/test/java/` directory. 

### Dependency

Instead of adding the [CommonCapabilities API as a git submodule](https://github.com/CyclopsMC/CommonCapabilitiesAPI#using-the-api), you can also add this mod as a dependency to your `build.gradle` file:

```gradle
repositories {
  maven {
    name "Cyclops Repo"
    url "https://oss.jfrog.org/artifactory/simple/libs-release/"
  }
}

dependencies {
  deobfCompile "org.cyclops.commoncapabilities:CommonCapabilities:${mc_version}-${mod_version}"
}
```

All available versions can be found at the [Maven repo](https://dl.bintray.com/cyclopsmc/dev/org/cyclops/commoncapabilities/CommonCapabilities/).

### Issues
* All bug reports and other issues are appreciated. If the issue is a crash, please include the FULL Forge log.
* Before submission, first check for duplicates, including already closed issues since those can then be re-opened.

### Branching Strategy

For every major Minecraft version, two branches exist:

* `master-{mc_version}`: Latest (potentially unstable) development.
* `release-{mc_version}`: Latest stable release for that Minecraft version. This is also tagged with all mod releases.

### License
All code and images are licenced under the [MIT License](https://github.com/CyclopsMC/CommonCapabilities/blob/master-1.8/LICENSE.txt)
