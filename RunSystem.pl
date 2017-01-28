#!/usr/bin/env perl
use Mojo::Webqq;
my ($host, $port, $post_api);

$host = "0.0.0.0"; #发送消息接口监听地址，没有特殊需要请不要修改
$port = 5000;      #发送消息接口监听端口，修改为自己希望监听的端口
$post_api = 'http://localhost:8088/post_api';  #接收到的消息上报接口，如果不需要接收消息上报，可以删除或注释此行

my $client = Mojo::Webqq->new();

$client->load("Openqq", data => { listen => [ { host => $host, port => $port } ], post_api => $post_api });
$client->run();
$client->load("Qiandao", data => {
        allow_group         => [ 399863405 ], #可选，允许插件的群，可以是群名称或群号码
        # ban_group   => ["私人群",123456], #可选，禁用该插件的群，可以是群名称或群号码
        is_qiandao_on_login => 0,        #可选，是否登录时进行签到，默认值为0
        # qiandao_time => "09:30" ,         #可选，每日签到的时间，默认是 09:30
    });

$client->load("SmartReply", data => {
        apikey       => 'ae575d0269d7449d8ec4133158bea002', #可选，参考http://www.tuling123.com/html/doc/apikey.html
        allow_group  => [ 399863405 ], #可选，允许插件的群，可以是群名称或群号码
        # ban_group       => ["私人群",123456], #可选，禁用该插件的群，可以是群名称或群号码
        # ban_user        => ["坏蛋",123456], #可选，禁用该插件的用户，可以是用户的显示名称或qq号码
        notice_reply => [ "诶呀，不要了老是艾特Avalon嘛... ...", "老是艾特我，烦死了... ..." ], #可选，提醒时用语
        notice_limit => 8, #可选，达到该次数提醒对话次数太多，提醒语来自默认或 notice_reply
        warn_limit   => 10, #可选,达到该次数，会被警告
        ban_limit    => 12, #可选,达到该次数会被列入黑名单不再进行回复
        ban_time     => 1200, #可选，拉入黑名单时间，默认1200秒
        period       => 600, #可选，限制周期，单位 秒
        is_need_at   => 1, #默认是1 是否需要艾特才触发回复
        keyword      => [ qw(Avalon 阿瓦隆) ], #触发智能回复的关键字，使用时请设置is_need_at=>0
    });
$client->load("Translation");
$client->load("FuckDaShen");
$client->load("GroupManage", data => {
        allow_group       => [ 399863405 ], #可选，允许插件的群，可以是群名称或群号码
        # ban_group   => ["私人群",123456], #可选，禁用该插件的群，可以是群名称或群号码
        new_group_member  => 'Avalon欢迎新成员 @%s 入群', #新成员入群欢迎语，%s会被替换成群成员名称
        lose_group_member => 'Avalon对 @%s 表示遗憾', #成员离群提醒
        speak_limit       => { #发送消息频率限制
            period       => 10, #统计周期，单位是秒
            warn_limit   => 8, #统计周期内达到该次数，发送警告信息
            warn_message => '@%s 警告, 您发言过于频繁，可能会被禁言或踢出本群', #警告内容
            shutup_limit => 10, #统计周期内达到该次数，成员会被禁言
            shutup_time  => 600, #禁言时长
            kick_limit   => 15,   #统计周期内达到该次数，成员会被踢出本群
        },
        pic_limit         => { #发图频率限制
            period       => 600,
            warn_limit   => 6,
            warn_message => '@%s 警告, 您发图过多，可能会被禁言或踢出本群',
            shutup_limit => 8,
            kick_limit   => 10,
        },
        keyword_limit     => {
            period       => 600,
            keyword      => [ qw(fuck 傻逼 你妹 滚) ],
            warn_limit   => 3,
            shutup_limit => 5,
            #kick_limit=>undef,
        },
    });