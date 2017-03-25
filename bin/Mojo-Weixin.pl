#!/usr/bin/env perl
use Mojo::Weixin;
my ($host, $port, $post_api);

$host = "127.0.0.1";
$port = 3500;
#$post_api = 'http://localhost/';

my $client = Mojo::Weixin->new(log_level => "info", http_debug => 0);
$client->load("Openwx", data => { listen => [ { host => $host, port => $port } ], post_api => $post_api });
$client->load("ShowQRcode");
$client->run();