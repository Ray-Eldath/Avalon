# Avalon    [![Build Status](https://travis-ci.org/Ray-Eldath/Avalon.svg?branch=master)](https://travis-ci.org/Ray-Eldath/Avalon)    [![Build status](https://ci.appveyor.com/api/projects/status/wathx1whvj24y44p?style=flat-square)](https://ci.appveyor.com/project/Ray-Eldath/avalon)    [![](https://jitpack.io/v/Ray-Eldath/Avalon.svg?style=flat-square)](https://jitpack.io/#Ray-Eldath/Avalon)    [![GNU General Public License, version 2](https://img.shields.io/badge/license-GNU%202.0-yellow.svg?style=flat-square)](https://www.gnu.org/licenses/gpl-2.0.html)    [![Dependency Status](https://www.versioneye.com/user/projects/58f4645d9f10f8003f885743/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58f4645d9f10f8003f885743)    [![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-ProgramLeague-blue.svg?style=flat-square)](https://jq.qq.com/?_wv=1027&k=46GveNI)
A QQ avalon.group robot which based on Mojo-Webqq.

一个基于[Mojo-Webqq](https://github.com/sjdy521/Mojo-Webqq)的QQ机器人。

**[最新RELEASE](https://github.com/Ray-Eldath/Avalon/releases/latest)**

通知：游戏模块已作为独立部分移出，请见[Avalon-Game]()
## 维护须知

1. 所有```api```目录下的类必须```implements GroupMessage```（``FriendMessage``API已有开发计划，请见``avalon.friend``包）；

## RELEASE运行步骤
1. 到[Mojo-Webqq的GitHub仓库地址](https://github.com/sjdy521/Mojo-Webqq)按README文件内说明配置Mojo-Webqq；
2. 再到[Mojo-Weixin的仓库地址](https://github.com/sjdy521/Mojo-Weixin)按README文件内说明配置Mojo-Weixin；
3. 到[GitHub - Releases](https://github.com/Ray-Eldath/Avalon/releases)下载**最新**构建版本；
4. 解压压缩包；
5. 编辑`bin\config.json`配置文件；
6. 运行``\bin``目录下的```*.pl```；
7. 执行`bin\Avalon.bat`或`bin\Avalon`。