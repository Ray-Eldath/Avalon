# Avalon v1.2.1    [![GNU Affero General Public License, version 3](https://www.gnu.org/graphics/agplv3-155x51.png)](https://www.gnu.org/licenses/agpl.html) [![GitHub stars](https://img.shields.io/github/stars/Ray-Eldath/Avalon.svg?style=social&label=Stars)](https://github.com/Ray-Eldath/Avalon/stargazers) [![GitHub followers](https://img.shields.io/github/followers/Ray-Eldath.svg?style=social&label=Follow)](https://github.com/Ray-Eldath)

[![Build Status](https://img.shields.io/travis/Ray-Eldath/Avalon/master.svg?style=flat-square)](https://travis-ci.org/Ray-Eldath/Avalon) [![Build status](https://img.shields.io/appveyor/ci/RayEldath/avalon/master.svg?style=flat-square)](https://ci.appveyor.com/project/RayEldath/avalon/branch/master) [![Codeship Status for Ray-Eldath/Avalon](https://img.shields.io/codeship/28b37980-8a1d-0135-1242-62d9615dc8b0/master.svg?style=flat-square)](https://app.codeship.com/projects/248940) [![CircleCI](https://img.shields.io/circleci/project/github/Ray-Eldath/Avalon/master.svg?style=flat-square)](https://circleci.com/gh/Ray-Eldath/Avalon/tree/master) [![](https://jitpack.io/v/Ray-Eldath/Avalon.svg?style=flat-square)](https://jitpack.io/#Ray-Eldath/Avalon) [![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-ProgramLeague-blue.svg?style=flat-square)](https://jq.qq.com/?_wv=1027&k=46GveNI)

**Avalon是一个可扩展的多功能QQ群机器人。**

## 功能
### 简介
 - **自然语言问答：** Avalon已对接[Wolfram Alpha](https://www.wolframalpha.com)提供“自然语言问答”服务。您可以在配置完Wolfram插件后通过指令`avalon tell me `使用它
 - **每日一句：** Avalon已对接[一言](http://hitokoto.cn)提供“每日一句”服务。您可以配置Avalon每次启动时推送“每日一句”或使用指令`avalon hitokoto`
 - **智能回复：** Avalon已通过[Mojo-Weixin](https://github.com/sjdy521/Mojo-Weixin)对接[微软小冰](http://www.msxiaoice.com)提供智能回复功能，您可以通过`avalon answer me`指令使用它
 - **代码片段执行：** Avalon允许您使用`avalon execute`指令在线执行代码
 - **RSS订阅推送：** 当您在Avalon的配置文件中设置RSS插件为启用状态并添加订阅源后，Avalon将自动推送订阅源的更新
 - **扩展功能：** Avalon允许通过MessageHook、插件及源代码修改方式自行扩展
 - ...

### 完整指令列表

以下列出的指令均可在Avalon中使用特定语句触发。

:grey_exclamation:：管理员权限指令。只有在`group.json`中被指定为管理员或拥有对应权限组权限的用户才可执行。

:exclamation:：所有者权限指令。只有在`group.json`中被指定为所有者的用户才可执行。

:construction:：被保护指令。被保护的指令不可使用Manager指令关闭，也不可在`config.json`配置文件中禁用。

| 名称          | 触发语句                                     | 功能描述                                     |
| ----------- | ---------------------------------------- | ---------------------------------------- |
| AnswerMe    | `avalon answer me OR 阿瓦隆回答我`                | 使用[微软小冰](http://www.msxiaoice.com)提供的智能回复 |
| Blacklist   | `avalon blacklist (list OR add OR remove)`     | :grey_exclamation: 查看黑名单；将指定的QQ号添加至黑名单或从黑名单移除 |
| Echo        | `avalon (echo OR repeat) OR 阿瓦隆跟我说`            | 让阿瓦隆重复给定语句                               |
| Execute     | `avalon execute <语言>{换行}<代码>`            | 执行给定代码并回显输出                              |
| ExecuteInfo | `avalon execute info`                    | 输出代码执行器信息                                |
| Flush       | `avalon flush`                           | :grey_exclamation: 刷新缓存并清除临时文件           |
| Help        | `avalon (help OR 帮助)`                        | :construction: 显示帮助文本                    |
| Hitokoto    | `avalon (hitokoto OR 一言)`                   | 获取一条一言                                   |
| Manager     | `avalon manager (start OR stop) <指令响应器触发语句>` | :construction: :grey_exclamation: 打开或关闭指定的指令响应器 |
| Mo          | ` - -`                                   | 随机触发膜*语句                                 |
| Quote       | `avalon quote <发言者> <语录内容>`              | :grey_exclamation: 记录语录到Avalon数据库。       |
| ShowAdmin   | `avalon (whoisadmin OR 谁是管理员)`              | 显示管理员列表                                  |
| Shutdown    | `avalon (shutdown OR exit)`                 | :exclamation: 退出Avalon                   |
| Version     | `avalon (version OR about OR 版本)`              | :construction: 显示版本及相关信息                 |

### 完整插件列表

以下插件均为推送型任务，监测到状态更新会自动推送，**不能**使用语句触发。

| 名称          | 功能描述                                   |
| ----------- | -------------------------------------- |
| BuildStatus | 推送指定项目CI的构建状态 </br> 现支持TravisCI和AppVeyor |
| RSSFeeder   | 推送指定RSS的更新                             |
| ShowMsg     | 每日推送“历史上的今天”（如果有）                      |

**若有希望增加的功能，欢迎提出[issue](https://github.com/Ray-Eldath/Avalon/issues)。**

### 对话示例

[record.jpg](https://raw.githubusercontent.com/Ray-Eldath/Avalon/master/img/record.jpg)

## 维护

1. 所有```group```目录下的类必须```implements GroupMessageResponder```（``FriendMessage``API已有开发计划，请见``avalon.friend``包）；
2. 标记为`@deprecated`的类/方法将于下一RELEASE中被正式移除。
3. 从`v0.0.1`开始，RELEASE版本号的管理将遵循[语义化版本 2.0.0](http://semver.org/lang/zh-CN/)规范。

## 配置

:no_entry: **施工中** :no_entry: