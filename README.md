<a name="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]
[![Discord][discord-shield]][discord-url]
[![Modrinth][modrinth-shield]][modrinth-url]

<br />
<div align="center">

[//]: # (  <a href="https://github.com/syorito-hatsuki/yet-another-air-drop">)

[//]: # (    <img src="https://github.com/syorito-hatsuki/yet-another-air-drop/blob/master/src/main/resources/assets/yet-another-air-drop/icon.png" alt="Logo" width="80" height="80">)

[//]: # (  </a>)

<h3 align="center">Yet Another Air Drop</h3>

  <p align="center">
    Goodies from the sky
    <br />
    <a href="https://discord.gg/pbwnMwnUD6">Support</a>
    ·
    <a href="https://github.com/syorito-hatsuki/yet-another-air-drop/issues">Report Bug</a>
    ·
    <a href="https://github.com/syorito-hatsuki/yet-another-air-drop/issues">Request Feature</a>
  </p>
</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>

## About The Project

[//]: # (![In-Game ScreenShot][screenshot])

Adds Air Drops to Minecraft! Random loot crates fall from the sky, giving you rare items, gear, and resources. Keep an eye out for these surprise gifts!

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* ![Fabric][fabric]
* ![Fabric-Language-Kotlin][fabric-language-kotlin]
* ![ModMenu Badges Lib][modmenu-badges-lib]
* ![Ducky-Updater-Lib][ducky-updater-lib]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Usage

> Mod require some setup from **server admins** or **modpack makers**!!!
<details>

<summary>Config</summary>

```json5
{
  // Spawn delay between drops. By default, 24,000 (20-minute or 1 minecraft day)
  "delay": 24000
}
```

</details>

<details>

<summary>Data pack</summary>

To add the air drop to the world you need, add a data pack file.
Datapack path is `.minecraft/saves/<world>/datapacks/yet-another-air-drop/<modid>/<dimension>`

Minecraft values are

| Mod ID    | Dimension  |
|-----------|------------|
| minecraft | overworld  |
| minecraft | the_nether |
| minecraft | the_end    |

```json5
{
  // Air Drop name (Required)
  "name": "First Aid",
  // Chat message when a drop landed (Optional)
  "message": "First Aid kit landed",
  // Sound when a drop landed (Optional)
  "sound": "minecraft:entity.firework_rocket.blast",
  // Safe platform for safe from destruction in void. Good for The End (Default: false)
  "safePlatform": false,
  // Loot Tables that will be used for a drop. Will select random from a list (Required)
  "lootTables": [
    "minecraft:chests/underwater_ruin_big",
    "modid:any_other_loot_table"
  ]
}
```

</details>
<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Roadmap

- [ ] Implement [PlaceholderAPI](https://placeholders.pb4.eu) by Patbox

See the [open issues](https://github.com/syorito-hatsuki/yet-another-air-drop/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

[contributors-shield]: https://img.shields.io/github/contributors/syorito-hatsuki/yet-another-air-drop.svg?style=for-the-badge
[contributors-url]: https://github.com/syorito-hatsuki/yet-another-air-drop/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/syorito-hatsuki/yet-another-air-drop.svg?style=for-the-badge
[forks-url]: https://github.com/syorito-hatsuki/yet-another-air-drop/network/members

[stars-shield]: https://img.shields.io/github/stars/syorito-hatsuki/yet-another-air-drop.svg?style=for-the-badge
[stars-url]: https://github.com/syorito-hatsuki/yet-another-air-drop/stargazers

[issues-shield]: https://img.shields.io/github/issues/syorito-hatsuki/yet-another-air-drop.svg?style=for-the-badge
[issues-url]: https://github.com/syorito-hatsuki/yet-another-air-drop/issues

[license-shield]: https://img.shields.io/github/license/syorito-hatsuki/yet-another-air-drop.svg?style=for-the-badge
[license-url]: https://github.com/syorito-hatsuki/yet-another-air-drop/blob/master/LICENSE.txt

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/kit-lehto

[discord-shield]: https://img.shields.io/discord/1032138561618726952?logo=discord&logoColor=white&style=for-the-badge&label=Discord
[discord-url]: https://discord.gg/pbwnMwnUD6

[modrinth-shield]: https://img.shields.io/modrinth/v/yet-another-air-drop?label=Modrinth&style=for-the-badge
[modrinth-url]: https://modrinth.com/mod/yet-another-air-drop

[screenshot]: <image-url>

[fabric]: https://img.shields.io/badge/fabric%20api-DBD0B4?style=for-the-badge
[fabric-language-kotlin]: https://img.shields.io/badge/fabric%20language%20kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white
[modmenu-badges-lib]: https://img.shields.io/badge/modmenu%20badges%20lib-434956?style=for-the-badge
[ducky-updater-lib]: https://img.shields.io/badge/ducky%20updater%20lib-1bd96a?style=for-the-badge
