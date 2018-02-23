# Avalon v1.2.2    [![GNU Affero General Public License, version 3](https://www.gnu.org/graphics/agplv3-155x51.png)](https://www.gnu.org/licenses/agpl.html) [![GitHub stars](https://img.shields.io/github/stars/Ray-Eldath/Avalon.svg?style=social&label=Stars)](https://github.com/Ray-Eldath/Avalon/stargazers) [![GitHub followers](https://img.shields.io/github/followers/Ray-Eldath.svg?style=social&label=Follow)](https://github.com/Ray-Eldath)

[![Build Status](https://img.shields.io/travis/Ray-Eldath/Avalon/master.svg?style=flat-square)](https://travis-ci.org/Ray-Eldath/Avalon) [![Build status](https://img.shields.io/appveyor/ci/RayEldath/avalon/master.svg?style=flat-square)](https://ci.appveyor.com/project/RayEldath/avalon/branch/master) [![Codeship Status for Ray-Eldath/Avalon](https://img.shields.io/codeship/28b37980-8a1d-0135-1242-62d9615dc8b0/master.svg?style=flat-square)](https://app.codeship.com/projects/248940) [![CircleCI](https://img.shields.io/circleci/project/github/Ray-Eldath/Avalon/master.svg?style=flat-square)](https://circleci.com/gh/Ray-Eldath/Avalon/tree/master) [![](https://jitpack.io/v/Ray-Eldath/Avalon.svg?style=flat-square)](https://jitpack.io/#Ray-Eldath/Avalon) [![Known Vulnerabilities](https://snyk.io/test/github/ray-eldath/avalon/badge.svg?style=flat-square)](https://snyk.io/test/github/ray-eldath/avalon) 

[中文（简体）](https://github.com/Ray-Eldath/Avalon/blob/master/README.zh_CN.md)

**Avalon is a scalable multi-function group robot for QQ and Discord.**

The support for QQ is based on [CoolQ-HTTP-API](https://github.com/richardchien/coolq-http-api), and the support for Discord is based on [JDA](https://github.com/DV8FromTheWorld/JDA/). The connection with Microsoft XiaoIce is based on Mojo-Weixin *(may not works except in China)*.

## Functions

> **Note:** Because Avalon is designed for QQ (an IM most of which users are Chinese) initially, and therefore some commands and functions are *only* available in nations and areas using Chinese.

### Introduction

- **Natural language question answering: ** Avalon has been docked to Wolfram Alpha in order to provide natural language question answering service. You could use it by command `avalon tell me`.
- **Message record and quote: ** If configured correctly, Avalon will record every message in group(s) into a database. You can also quote message into database annually by command `avalon quote`.
- **Build status notification: **When the build status for specified project(s) update, Avalon will send notification. *([AppVeyor](https://ci.appveyor.com) and [TravisCI](https://travis-ci.org) supported.)*


 - **Execute code online: **Avalon allows you to execute a piece of code by command `avalon execute`. *35+ programming languages supported*, include Java, Rust, Ruby, JavaScript, Kotlin, C++, and so on. *(served by [glot.io](https://glot.io))*
 - **Notification sent when RSS update: **After enable RSS function and specified the URL(s), Avalon will send notification when these RSS update.
 - **Scalable: **Avalon allows you to add new function or feature by MessageHook or Plugin.
 - ***(Unavailable except in Chinese nations or areas)* Smart reply: **Avalon has been docked to [Microsoft XiaoIce](http://www.msxiaoice.com) to privide smart reply service (just like chat bot), you could use it by command `avalon answer me`.
 - ***(Unavailable except in Chinese nations or areas)* Hitokoto: **Avalon has been docked to [一言](http://hitokoto.cn) to provide "hitokoto". You could config Avalon to send one when started or use command `avalon hitokoto` to get one.
 - ...

### List of commands

All commands listed below can be triggered by some particular command as long as has required permission.

:grey_exclamation:: **Admin command.** Only users specficed as admin in `group.json` and owner could execute these commands.

:exclamation:: **Owner command.** Only user specficed as owner in `group.json` could execute these commands.

:white_circle:: **Unmanagerable command.** These command *can not* be managed by `avalon manager`, but *can* be disabled in `config.json`.

:red_circle:: **Basic command.** These command can *neither* be managerd by `avalon manager` *nor* disabled in `config.json`.

| Name        | Trigger command                                    | Description                                                  |
| ----------- | -------------------------------------------------- | ------------------------------------------------------------ |
| Blacklist   | `avalon blacklist (list OR add OR remove)`         | :white_circle: :grey_exclamation: List all accounts in blacklist. Add account to blacklist or remove from it. |
| Echo        | `avalon (echo OR repeat)`                          | Avalon will repeat the given sentence.                       |
| Execute     | `avalon execute <LANGUAGE>{ENTER}<CODE>`           | Execute given code and echo the return or the error message. |
| ExecuteInfo | `avalon execute info`                              | For info about code executor.                                |
| Flush       | `avalon flush`                                     | :white_circle: :grey_exclamation: Flush caches and delete temp files. |
| Help        | `avalon help`                                      | :red_circle: For help content.                               |
| Manager     | `avalon manager (start OR stop) <TRIGGER COMMAND>` | :white_circle: :grey_exclamation: To start or stop particular command responder. |
| Quote       | `avalon quote <SPEAKER> <CONTENT>`                 | :grey_exclamation: To quote message into Avalon's database.  |
| Reboot      | `avalon reboot`                                    | :white_circle: :exclamation: Reboot Avalon.                  |
| ShowAdmin   | `avalon whoisadmin`                                | Show list of all admins.                                     |
| Shutdown    | `avalon (shutdown OR exit)`                        | :white_circle: :exclamation: Shutdown Avalon.                |
| Version     | `avalon (version OR about)`                        | :red_circle: Show version and related info.                  |

Some commands only available in particular version of Avalon. For more details, see RELEASE Notes for every [RELEASE](https://github.com/Ray-Eldath/Avalon/releases).

### List of extension

All following extension are push-type, they will sent message automatically when state change detected and therefore **can not** be triggered by command statements.

| Name        | Description                                                  |
| ----------- | ------------------------------------------------------------ |
| BuildStatus | Notification will be sent when configed CI build state of given project update. [TravisCI](https://travis-ci.org) and [AppVeyor](https://ci.appveyor.com) now supported. |
| RSSFeeder   | Message will sent when configed RSS update.                  |

**If you want Avalon to have some new functions, welcome new [issue](https://github.com/Ray-Eldath/Avalon/issues).**

> ** Commands and extensions not work except in Chinese nations or areas may not listed. For info about these commands and extensions, see [Chinese (Simplified) version of README file]().*

### Example



## Contribute

[![Open Source Helpers](https://www.codetriage.com/ray-eldath/avalon/badges/users.svg?style=flat-square)](https://www.codetriage.com/ray-eldath/avalon)

### Notice

1. **Please make sure your editor support [EditorConfig](http://editorconfig.org)!**
2. All class under package `group` must `implements GroupMessageResponder`.
3. Class or method that tagged as `@deprecated` will removed officially in the next release.
4. From `v0.0.1`, the version number will follow [Semantic Versioning 2.0.0](http://semver.org/).

### Todo

See [Projects / Avalon](https://github.com/Ray-Eldath/Avalon/projects/1).

## Configuration

See [Avalon.wiki](https://github.com/Ray-Eldath/Avalon/wiki). (Only Chinese (Simplified) version provided yet ;-) )