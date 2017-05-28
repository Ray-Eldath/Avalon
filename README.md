# Avalon v0.0.1b    [![Build Status](https://travis-ci.org/Ray-Eldath/Avalon.svg?branch=master)](https://travis-ci.org/Ray-Eldath/Avalon)    [![Build status](https://ci.appveyor.com/api/projects/status/wathx1whvj24y44p?style=flat-square)](https://ci.appveyor.com/project/Ray-Eldath/avalon)    [![](https://jitpack.io/v/Ray-Eldath/Avalon.svg?style=flat-square)](https://jitpack.io/#Ray-Eldath/Avalon)    [![GNU General Public License, version 2](https://img.shields.io/badge/license-GNU%202.0-yellow.svg?style=flat-square)](https://www.gnu.org/licenses/gpl-2.0.html)    [![Dependency Status](https://www.versioneye.com/user/projects/58f4645d9f10f8003f885743/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58f4645d9f10f8003f885743)    [![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-ProgramLeague-blue.svg?style=flat-square)](https://jq.qq.com/?_wv=1027&k=46GveNI)
一个基于[Mojo-Webqq](https://github.com/sjdy521/Mojo-Webqq)的QQ群机器人。

**[当前最新RELEASE](https://github.com/Ray-Eldath/Avalon/releases)**

**通知：游戏模块已作为独立项目移出，请见[Avalon-Game](https://github.com/Ray-Eldath/Avalon-Game)**

## 介绍
这是一个基于[Mojo-Webqq](https://github.com/sjdy521/Mojo-Webqq)的QQ群机器人。

### 目前主要功能：
 - **提供11个全方面（服务、管理、娱乐）的预定义指令响应器，同时允许通过MessageHook及插件方式自行扩展。**
 - **通过[Mojo-Weixin](https://github.com/sjdy521/Mojo-Weixin)和[微软小冰](http://www.msxiaoice.com)提供具有“相当智能度”的智能回复功能**
 - **基于RESTful API的控制、管理系统（正在完善）**
 - **自带Perl运行环境、[Mojo-Webqq](https://github.com/sjdy521/Mojo-Webqq)及[Mojo-Weixin](https://github.com/sjdy521/Mojo-Weixin)的安装检测及内置`exec`的脚本运行方式**
## 维护须知

1. 所有```api```目录下的类必须```implements BaseGroupMessageResponder```（``FriendMessage``API已有开发计划，请见``avalon.friend``包）；
2. ​

## RELEASE运行步骤

请见[Avalon 快速食用指南](http://ray-eldath.tech/2017/05/28/avalon-quick-start-guide/)。

关于配置Avalon，请见[Avalon 配置文件说明](http://ray-eldath.tech/2017/05/28/avalon-profile-description/)。