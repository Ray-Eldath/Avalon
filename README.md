# Avalon    [![Build Status](https://travis-ci.org/Ray-Eldath/Avalon.svg?branch=master)](https://travis-ci.org/Ray-Eldath/Avalon)    [![Build status](https://ci.appveyor.com/api/projects/status/wathx1whvj24y44p?style=flat-square)](https://ci.appveyor.com/project/Ray-Eldath/avalon)    [![](https://jitpack.io/v/Ray-Eldath/Avalon.svg?style=flat-square)](https://jitpack.io/#Ray-Eldath/Avalon)    [![GNU General Public License, version 2](https://img.shields.io/badge/license-GNU%202.0-yellow.svg?style=flat-square)](https://www.gnu.org/licenses/gpl-2.0.html)    [![Dependency Status](https://www.versioneye.com/user/projects/58f4645d9f10f8003f885743/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58f4645d9f10f8003f885743)    [![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-ProgramLeague-blue.svg?style=flat-square)](https://jq.qq.com/?_wv=1027&k=46GveNI)
A QQ avalon.group robot which based on Mojo-Webqq.

一个基于[Mojo-Webqq](https://github.com/sjdy521/Mojo-Webqq)的QQ群机器人。

**[最新RELEASE](https://github.com/Ray-Eldath/Avalon/releases/latest)**

**通知：API、游戏模块已作为独立部分移出，请见[Avalon-API](https://github.com/Ray-Eldath/Avalon-API)、[Avalon-Game]()**

## 介绍
这是一个基于[Mojo-Webqq](https://github.com/sjdy521/Mojo-Webqq)的QQ群机器人。

### 目前主要功能：
 - **提供11个全方面（服务、管理、娱乐）的预定义指令响应器，同时允许通过[Avalon-API](https://github.com/Ray-Eldath/Avalon-API)自行扩展。**
 - **通过[Mojo-Weixin](https://github.com/sjdy521/Mojo-Weixin)和[微软小冰](http://www.msxiaoice.com)提供具有“相当智能度”的智能回复功能**
 - **基于RESTful API的控制、管理系统（正在完善）**
 - **自带Perl运行环境、[Mojo-Webqq](https://github.com/sjdy521/Mojo-Webqq)及[Mojo-Weixin](https://github.com/sjdy521/Mojo-Weixin)的安装检测及内置`exec`的脚本运行方式。**
## 维护须知

1. 所有```api```目录下的类必须```implements GroupMessage```（``FriendMessage``API已有开发计划，请见``avalon.friend``包）；

## RELEASE运行步骤
1. 到[Mojo-Webqq的GitHub仓库地址](https://github.com/sjdy521/Mojo-Webqq)按README文件内说明配置Mojo-Webqq；
2. 再到[Mojo-Weixin的仓库地址](https://github.com/sjdy521/Mojo-Weixin)按README文件内说明配置Mojo-Weixin；
3. 到[GitHub - Releases](https://github.com/Ray-Eldath/Avalon/releases)下载**最新**构建版本；
4. 解压压缩包；
5. 编辑`bin\config.json`配置文件；
6. 根据实际情况，重命名`bin`目录下的`database_mysql.properties`或`database_sqlite.properties`为`database.properties`并填写其中信息；
7. **直接**执行`bin\Avalon.bat`或`bin\Avalon`。