# Avalon v1.1.0    [![GNU Affero General Public License, version 3](https://www.gnu.org/graphics/agplv3-155x51.png)](https://www.gnu.org/licenses/lgpl.html) [![GitHub stars](https://img.shields.io/github/stars/Ray-Eldath/Avalon.svg?style=social&label=Stars)](https://github.com/Ray-Eldath/Avalon/stargazers) [![GitHub followers](https://img.shields.io/github/followers/Ray-Eldath.svg?style=social&label=Follow)](https://github.com/Ray-Eldath)

[![Build Status](https://img.shields.io/travis/Ray-Eldath/Avalon/master.svg?style=flat-square)](https://travis-ci.org/Ray-Eldath/Avalon) [![Codeship Status for Ray-Eldath/Avalon](https://img.shields.io/codeship/28b37980-8a1d-0135-1242-62d9615dc8b0/master.svg?style=flat-square)](https://app.codeship.com/projects/248940) [![CircleCI](https://img.shields.io/circleci/project/github/Ray-Eldath/Avalon/master.svg?style=flat-square)](https://circleci.com/gh/Ray-Eldath/Avalon/tree/master) [![](https://jitpack.io/v/Ray-Eldath/Avalon.svg?style=flat-square)](https://jitpack.io/#Ray-Eldath/Avalon) [![Codacy grade](https://img.shields.io/codacy/grade/222c04e11950412599414cdb401b8367.svg?style=flat-square)](https://www.codacy.com/app/Ray-Eldath/Avalon?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Ray-Eldath/Avalon&amp;utm_campaign=Badge_Grade) [![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-ProgramLeague-blue.svg?style=flat-square)](https://jq.qq.com/?_wv=1027&k=46GveNI)

**Avalon是一个可扩展的多功能QQ群机器人。**

### 主要功能
 - **自然语言问答：** Avalon已对接[Wolfram Alpha](https://www.wolframalpha.com)提供“自然语言问答”服务。您可以在配置完Wolfram插件后通过指令`avalon tell me `使用它。
 - **每日一句：** Avalon已对接[一言](http://hitokoto.cn)提供“每日一句”服务。您可以配置Avalon每次启动时推送“每日一句”或使用指令`avalon hitokoto`
 - **智能回复：** Avalon已通过[Mojo-Weixin](https://github.com/sjdy521/Mojo-Weixin)对接[微软小冰](http://www.msxiaoice.com)提供智能回复功能，您可以通过`avalon answer me`指令使用它
 - **代码片段执行：** Avalon允许您使用`avalon execute`指令在线执行代码
 - **RSS订阅推送：** 当您在Avalon的配置文件中设置RSS插件为启用状态并添加订阅源后，Avalon将自动推送订阅源的更新
 - **扩展功能：** Avalon允许通过MessageHook、插件及源代码修改方式自行扩展
 - ...

### 对话示例

[record.jpg](https://raw.githubusercontent.com/Ray-Eldath/Avalon/master/img/record.jpg)

## 维护

1. 所有```group```目录下的类必须```implements GroupMessageResponder```（``FriendMessage``API已有开发计划，请见``avalon.friend``包）；
2. 标记为`@deprecated`的类/方法将于下一RELEASE中被正式移除。
3. 从`v0.0.1`开始，RELEASE版本号的管理将遵循[语义化版本 2.0.0](http://semver.org/lang/zh-CN/)规范。

## 配置

:no_entry: **施工中** :no_entry: