#!/usr/bin/perl

use CGI::Carp qw(fatalsToBrowser);
use IO::Socket;
#open F,">>d:\\im.myfriendsnetwork.com\\log.txt";
#print F "Statring session \r\n";
$arr="";
read(STDIN,$arr,$ENV{"CONTENT_LENGTH"});
#print F "Got data from STDIN: ".$arr."\r\n";
my $sock = new IO::Socket::INET
(
    PeerAddr => 'localhost',
    PeerPort => '8088',
    Proto => 'tcp',
);
die "Could not create socket: $!\n" unless $sock;
binmode $sock;
print $sock $arr;
$resp = join "",<$sock>;
close $sock;
#print F "Got data from server: ".$resp."\r\n";
print "Content-type: application/octet-stream\n\n";
binmode STDOUT;
print $resp;
#close F;

