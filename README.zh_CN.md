# Avalon v1.3.1    [![GNU Affero General Public License, version 3](https://www.gnu.org/graphics/agplv3-155x51.png)](https://www.gnu.org/licenses/agpl.html) [![GitHub stars](https://img.shields.io/github/stars/Ray-Eldath/Avalon.svg?style=social&label=Stars)](https://github.com/Ray-Eldath/Avalon/stargazers) [![GitHub followers](https://img.shields.io/github/followers/Ray-Eldath.svg?style=social&label=Follow)](https://github.com/Ray-Eldath)

[![Build Status](https://img.shields.io/travis/Ray-Eldath/Avalon/master.svg?style=flat-square)](https://travis-ci.org/Ray-Eldath/Avalon) [![Build status](https://img.shields.io/appveyor/ci/RayEldath/avalon/master.svg?style=flat-square)](https://ci.appveyor.com/project/RayEldath/avalon/branch/master) [![CircleCI](https://img.shields.io/circleci/project/github/Ray-Eldath/Avalon/master.svg?style=flat-square)](https://circleci.com/gh/Ray-Eldath/Avalon/tree/master) [![](https://jitpack.io/v/Ray-Eldath/Avalon.svg?style=flat-square)](https://jitpack.io/#Ray-Eldath/Avalon) [![Known Vulnerabilities](https://snyk.io/test/github/ray-eldath/avalon/badge.svg?style=flat-square)](https://snyk.io/test/github/ray-eldath/avalon) [![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-ProgramLeague-blue.svg?style=flat-square)](https://jq.qq.com/?_wv=1027&k=46GveNI)

**Avalon是一个多功能、可扩展的群机器人，支持QQ和Discord。**

对QQ的支持基于[CoolQ-HTTP-API](https://github.com/richardchien/coolq-http-api)。对Discord的支持基于[JDA](https://github.com/DV8FromTheWorld/JDA/)。与微软小冰的对接基于[Mojo-Weixin](https://github.com/sjdy521/Mojo-Weixin)。

## 功能
### 简介
 - **自然语言问答：** Avalon已对接[Wolfram Alpha](https://www.wolframalpha.com)提供“自然语言问答”服务。您可以通过指令`avalon tell me `使用它
 - **每日一句：** Avalon已对接[一言](http://hitokoto.cn)提供“每日一句”服务。您可以配置Avalon每次启动时推送“每日一句”或使用指令`avalon hitokoto`
 - **智能回复：** Avalon已通过[Mojo-Weixin](https://github.com/sjdy521/Mojo-Weixin)对接[微软小冰](http://www.msxiaoice.com)提供智能回复功能，您可以通过`avalon answer me`指令使用它
 - **代码片段执行：** Avalon允许您使用`avalon execute`指令在线执行代码
 - **RSS订阅推送：** 当您在Avalon的配置文件中设置RSS插件为启用状态并添加订阅源后，Avalon将自动推送订阅源的更新
 - **扩展功能：** Avalon允许通过MessageHook、插件及源代码修改方式自行扩展
 - ...

### 完整指令列表

以下列出的指令均可在Avalon中使用特定语句触发，只要用户具有要求的权限。

:grey_exclamation:：管理员权限指令。只有在`group.json`中被指定为管理员或拥有对应权限组权限的用户才可执行。

:exclamation:：所有者权限指令。只有在`group.json`中被指定为所有者的用户才可执行。

:white_circle:：不可管理指令。被保护的指令不可使用Manager指令管理，但**可以**在`config.json`配置文件中禁用。

:red_circle:：基础指令。这些指令不可使用Manager指令关闭，也**不可**在`config.json`配置文件中禁用。

| 名称        | 触发语句                                              | 功能描述                                                     |
| ----------- | ----------------------------------------------------- | ------------------------------------------------------------ |
| AnswerMe    | `avalon answer me`                                    | 使用[微软小冰](http://www.msxiaoice.com)提供的智能回复       |
| Blacklist   | `avalon blacklist (list OR add OR remove)`            | :white_circle: :grey_exclamation: 查看黑名单；将指定的帐号添加至黑名单或从黑名单移除 |
| Echo        | `avalon (echo OR repeat`                              | 让阿瓦隆重复给定语句                                         |
| Execute     | `avalon (ex OR execute) <语言>{换行}<代码>`           | 执行给定代码并回显输出                                       |
| ExecuteInfo | `avalon(exi OR execute info)`                         | 输出代码执行器信息                                           |
| Flush       | `avalon flush`                                        | :white_circle: :grey_exclamation: 刷新缓存并清除临时文件     |
| Heartbeat | `avalon (hb OR heartbeat)` |Avalon存活检测|
| Help        | `avalon (hp OR help)`                                 | :red_circle: 显示帮助文本                                    |
| Hitokoto    | `avalon (hitokoto OR 一言)`                           | 获取一条一言                                                 |
| Manager     | `avalon manager (start OR stop) <指令响应器触发语句>` | :white_circle: :grey_exclamation: 打开或关闭指定的指令响应器 |
| Mo          | ` - -`                                                | 随机触发膜*语句                                              |
| Quote       | `avalon quote <发言者> <语录内容>`                    | :grey_exclamation: 记录语录到Avalon数据库。                  |
| Reboot      | `avalon reboot`                                       | :white_circle: :exclamation: 重启Avalon                      |
| ShowAdmin   | `avalon (wia OR whoisadmin)`                          | 显示管理员列表                                               |
| Shutdown    | `avalon (shutdown OR exit)`                           | :white_circle: :exclamation: 退出Avalon                      |
| Version     | `avalon (ver OR about)`                               | :red_circle: 显示版本及相关信息                              |
| Wolfram | `avalon (tm OR tell me) <your question>` |(Only English) Send your question to Wolfram Alpha and echo the return.|

部分指令仅对某个版本号以上的Avalon有效。具体请见各 [RELEASE](https://github.com/Ray-Eldath/Avalon/releases)的RELEASE Notes。

### 完整扩展功能列表

以下扩展功能均为推送型任务，监测到状态更新会自动推送，**不能**使用语句触发。

| 名称        | 功能描述                                                     |
| ----------- | ------------------------------------------------------------ |
| BuildStatus | 推送指定项目CI的构建状态。现支持[TravisCI](https://travis-ci.org)和[AppVeyor](https://ci.appveyor.com) |
| RSSFeeder   | 推送指定RSS的更新                                            |
| ShowMsg     | 每日推送“历史上的今天”（如果有）                             |

**若有希望增加的功能，欢迎提出[issue](https://github.com/Ray-Eldath/Avalon/issues)。**

### 对话示例

[record.zh_CN.jpg](https://raw.githubusercontent.com/Ray-Eldath/Avalon/master/img/record.zh_CN.jpg)

## 维护 [![Open Source Helpers](https://www.codetriage.com/ray-eldath/avalon/badges/users.svg?style=flat-square)](https://www.codetriage.com/ray-eldath/avalon)

### 须知

1. **请确保您的编辑器支持[EditorConfig](http://editorconfig.org)！**
2. 所有```group```目录下的类必须```implements GroupMessageResponder```（``FriendMessage``API已有开发计划，请见``avalon.friend``包）；
3. 标记为`@deprecated`的类/方法将于下一RELEASE中被正式移除。
4. 从`v0.0.1`开始，RELEASE版本号的管理将遵循[语义化版本 2.0.0](http://semver.org/lang/zh-CN/)规范。

### Todo

见[Projects / Avalon](https://github.com/Ray-Eldath/Avalon/projects/1)。

## 配置

请见[Avalon.wiki](https://github.com/Ray-Eldath/Avalon/wiki)。

## 致谢

本项目使用 IntelliJ IDEA Ultimate 开发，且许可由 [Jetbrains 开源支持计划](https://www.jetbrains.com/community/opensource/) 免费提供。感谢 Jetbrains 对开源软件的支持！

[![Jetbrains logo](/img/jetbrains-variant-4.jpg)](https://www.jetbrains.com/?from=Avalon)