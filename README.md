# Avalon v0.0.1    [![GNU Affero General Public License, version 3](https://www.gnu.org/graphics/agplv3-155x51.png)](https://www.gnu.org/licenses/lgpl.html) 

[![Build Status](https://travis-ci.org/Ray-Eldath/Avalon.svg?branch=master&style=flat-square)](https://travis-ci.org/Ray-Eldath/Avalon) [![Build status](https://ci.appveyor.com/api/projects/status/wathx1whvj24y44p?style=flat-square)](https://ci.appveyor.com/project/Ray-Eldath/avalon)    [![CircleCI](https://circleci.com/gh/Ray-Eldath/Avalon/tree/master.svg?style=svg)](https://circleci.com/gh/Ray-Eldath/Avalon/tree/master) [![](https://jitpack.io/v/Ray-Eldath/Avalon.svg?style=flat-square)](https://jitpack.io/#Ray-Eldath/Avalon) [![Dependency Status](https://www.versioneye.com/user/projects/58f4645d9f10f8003f885743/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58f4645d9f10f8003f885743)    [![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-ProgramLeague-blue.svg?style=flat-square)](https://jq.qq.com/?_wv=1027&k=46GveNI)

一个可扩展、可部署的QQ群机器人。

**游戏模块已作为独立项目移出，请见[Avalon-Game](https://github.com/Ray-Eldath/Avalon-Game)**

## 介绍
这是一个基于[Mojo-Webqq](https://github.com/sjdy521/Mojo-Webqq)或[CoolQ](https://cqp.cc)的QQ群机器人。

### 主要功能
 - **提供12个全方面（服务、管理、娱乐）的预定义指令响应器，同时允许通过MessageHook、插件及源代码修改方式自行扩展。**
 - **通过[Mojo-Weixin](https://github.com/sjdy521/Mojo-Weixin)和[微软小冰](http://www.msxiaoice.com)提供具有“相当智能度”的智能回复功能**
 - **提供基于[Docker](https://www.docker.com)和[Avalon-Executive](https://github.com/ProgramLeague/Avalon-Executive)的安全程序执行指令响应器**（正在实现）
## 维护须知

1. 所有```group```目录下的类必须```implements BaseGroupMessageResponder```（``FriendMessage``API已有开发计划，请见``avalon.friend``包）；

## RELEASE运行步骤

**- 以下指南可能已经过时 -**

 - 食用Avalon：[Avalon 快速食用指南](http://ray-eldath.tech/2017/05/28/avalon-quick-start-guide/)

 - 配置Avalon：[Avalon 配置文件说明](http://ray-eldath.tech/2017/05/28/avalon-profile-description/)