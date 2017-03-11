# Avalon
A QQ group robot which based on Mojo-Webqq.
一个基于Mojo-Webqq的QQ机器人。

## 维护须知

1. 所有```api```目录下的类必须```implements GroupMessage```（``FriendMessage``API暂无开发计划）；

## 构建/维护步骤

1. 请使用文本编辑器在每个文件中搜索``CUSTOM``并按各处的提示自定义机器人；
2. 请先到[Mojo-Webqq的GitHub仓库地址](https://github.com/sjdy521/Mojo-Webqq)按README文件内说明配置Mojo-Webqq；
3. 再到[Mojo-Weixin的仓库地址](https://github.com/sjdy521/Mojo-Weixin)按README文件内说明配置Mojo-Weixin；
4. 运行``\bin``目录下的```*.pl```；
5. 将``\res``目录下的``black_record.db``文件重命名为``record.db``；
6. 运行提供的```.jar```文件。