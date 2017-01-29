#!/usr/bin/env perl
use Mojo::Weixin;
my ($host, $port, $post_api);

$host = "0.0.0.0"; #������Ϣ�ӿڼ�����ַ��û��������Ҫ�벻Ҫ�޸�
$port = 3500;      #������Ϣ�ӿڼ����˿ڣ��޸�Ϊ�Լ�ϣ�������Ķ˿�
#$post_api = 'http://localhost/';  #���յ�����Ϣ�ϱ��ӿڣ��������Ҫ������Ϣ�ϱ�������ɾ����ע�ʹ���

my $client = Mojo::Weixin->new(log_level => "info", http_debug => 0);
$client->load("Openwx", data => { listen => [ { host => $host, port => $port } ], post_api => $post_api });
$client->load("ShowQRcode");
$client->run();