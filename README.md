# Avalon v1.1.0    [![GNU Affero General Public License, version 3](https://www.gnu.org/graphics/agplv3-155x51.png)](https://www.gnu.org/licenses/lgpl.html) [![GitHub stars](https://img.shields.io/github/stars/Ray-Eldath/Avalon.svg?style=social&label=Stars)]() [![GitHub followers](https://img.shields.io/github/followers/Ray-Eldath.svg?style=social&label=Follow)]()

[![Build Status](https://img.shields.io/travis/Ray-Eldath/Avalon/master.svg?style=flat-square)](https://travis-ci.org/Ray-Eldath/Avalon) [![Codeship Status for Ray-Eldath/Avalon](https://img.shields.io/codeship/28b37980-8a1d-0135-1242-62d9615dc8b0/master.svg?style=flat-square)](https://app.codeship.com/projects/248940) [![CircleCI](https://img.shields.io/circleci/project/github/Ray-Eldath/Avalon/master.svg?style=flat-square)](https://circleci.com/gh/Ray-Eldath/Avalon/tree/master) [![](https://jitpack.io/v/Ray-Eldath/Avalon.svg?style=flat-square)](https://jitpack.io/#Ray-Eldath/Avalon) [![Codacy grade](https://img.shields.io/codacy/grade/222c04e11950412599414cdb401b8367.svg?style=flat-square)](https://www.codacy.com/app/Ray-Eldath/Avalon?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Ray-Eldath/Avalon&amp;utm_campaign=Badge_Grade) [![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-ProgramLeague-blue.svg?style=flat-square)](https://jq.qq.com/?_wv=1027&k=46GveNI)

![](https://raw.githubusercontent.com/Ray-Eldath/Avalon/master/img/console.png)

一个可扩展、可部署的QQ群机器人。

**游戏模块已作为独立项目移出，请见[Avalon-Game](https://github.com/Ray-Eldath/Avalon-Game)**

## 介绍
这是一个基于[Mojo-Webqq](https://github.com/sjdy521/Mojo-Webqq)或[CoolQ](https://cqp.cc)的QQ群机器人。

**部分对话示例请见：[record.jpg](https://raw.githubusercontent.com/Ray-Eldath/Avalon/master/img/record.jpg)**

### 主要功能
 - **提供15个全方面（服务、管理、娱乐）的预定义指令响应器，同时允许通过MessageHook、插件及源代码修改方式自行扩展**
 - **对接[Wolfram Alpha](https://www.wolframalpha.com)提供“自然语言问答”服务**
 - **对接[一言](http://hitokoto.cn)提供“每日一句”服务**
 - **通过[Mojo-Weixin](https://github.com/sjdy521/Mojo-Weixin)对接[微软小冰](http://www.msxiaoice.com)提供具有“相当智能度”的智能回复功能**
 - **提供基于[Glot-Run](https://github.com/prasmussen/glot-run)等`Executive`的安全程序执行指令响应器**
 - **RSS订阅推送功能**
 - ...

## 维护须知

1. 所有```group```目录下的类必须```implements GroupMessageResponder```（``FriendMessage``API已有开发计划，请见``avalon.friend``包）；
2. 标记为`@deprecated`的类/方法将于下一RELEASE中被正式移除。
3. 从`v0.0.1`开始，RELEASE版本号的管理将遵循[语义化版本 2.0.0](http://semver.org/lang/zh-CN/)规范。

## RELEASE运行步骤

**- 以下指南已经过时 -**

 - 食用Avalon：[Avalon 快速食用指南](http://ray-eldath.tech/2017/05/28/avalon-quick-start-guide/)

 - 配置Avalon：[Avalon 配置文件说明](http://ray-eldath.tech/2017/05/28/avalon-profile-description/)
