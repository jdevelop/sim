<?php

$service_port = 8088;
$address = '127.0.0.1';

// Getting data from POST request
$buffer = $GLOBALS["HTTP_RAW_POST_DATA"];

//sending request to server
$socket = socket_create (AF_INET, SOCK_STREAM, 0);
if ($socket < 0) {
    error_log($out,"socket_create() failed: reason: " . socket_strerror ($socket) . "\n");
	exit;
}
$result = socket_connect ($socket, $address, $service_port);
if ($result < 0) {
    error_log($out,"socket_connect() failed.\nReason: ($result) " . socket_strerror($result) . "\n");
}
socket_write ($socket, $buffer, $_SERVER['CONTENT_LENGTH']);

//Reading response
$response='';
while ($tmp = socket_read ($socket, 2048)) {
    $response.=$tmp;
}
header('Content-type: application/octet-stream');
echo $response;

?>
