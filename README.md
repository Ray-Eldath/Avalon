# Avalon
A QQ group robot which based on Mojo-Webqq.
一个基于Mojo-Webqq的QQ机器人。

## 维护须知

1. 所有```api```目录下的类必须```implements BaseAPI```；
2. 请在开发新一API之后，在```MainServlet```中按关键字/关键字表装载API；

## 构建/维护步骤

1. 请先到[Mojo-Webqq的GitHub仓库地址](https://github.com/sjdy521/Mojo-Webqq)按README文件内说明配置Mojo-Webqq；
2. 运行本目录下的```RunSystem.pl```；
3. 运行```MainServer```。

然后你会发现一直``Timeout``，这就是我说的bug。

Help-needed...