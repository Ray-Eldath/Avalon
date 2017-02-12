#!/usr/bin/env perl
use Mojo::Webqq;
my ($host, $port, $post_api);

$host = "0.0.0.0"; #发送消息接口监听地址，没有特殊需要请不要修改
$port = 5000;      #发送消息接口监听端口，修改为自己希望监听的端口
$post_api = 'http://127.0.0.1:8088';  #接收到的消息上报接口，如果不需要接收消息上报，可以删除或注释此行

my $client = Mojo::Webqq->new();

$client->load("ShowQRcode");
$client->load("Openqq", data => { listen => [ { host => $host, port => $port } ], post_api => $post_api });
$client->load("Qiandao", data => {
        allow_group         => [ 617118724 ], #可选，允许插件的群，可以是群名称或群号码
        # ban_group   => ["私人群",123456], #可选，禁用该插件的群，可以是群名称或群号码
        is_qiandao_on_login => 0,        #可选，是否登录时进行签到，默认值为0
        # qiandao_time => "09:30" ,         #可选，每日签到的时间，默认是 09:30
    });
$client->load("KnowledgeBase", data => {
        allow_group     => [ 617118724 ], #可选，允许插件的群，可以是群名称或群号码
        # ban_group   => ["私人群",123456], #可选，禁用该插件的群，可以是群名称或群号码
        file            => './KnowledgeBase.txt', #数据库保存路径，纯文本形式，可以编辑
        learn_command   => 'learn', #可选，自定义学习指令关键字
        delete_command  => 'del', #可选，自定义删除指令关键字
        learn_operator  => [ 1464443139, 2276768747, 951394653, 360736041, 704639565 ], #允许学习权限的操作人qq号
        delete_operator => [ 1464443139, 951394653 ], #允许删除权限的操作人qq号
        mode            => 'fuzzy', # fuzzy|regex|exact 分别表示模糊|正则|精确, 默认模糊
        check_time      => 60, #默认10秒检查一次文件变更
        show_keyword    => 1, #消息是否包含触发关键字信息，默认为0
    });
$client->load("FuckDaShen");
$client->load("GroupManage", data => {
        allow_group       => [ 617118724 ], #可选，允许插件的群，可以是群名称或群号码
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
            keyword      => [ qw(fuck 傻逼 你妹 滚 操) ],
            warn_limit   => 3,
            warn_message => '@%s 警告, 您侮辱次数过多，可能会被禁言或踢出本群',
            shutup_limit => 5,
            #kick_limit=>undef,
        },
    });
$client->run();