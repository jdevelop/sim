#!/bin/sh

if (test "x$SIM_HOME" = "x") then
    SIM_HOME="/home/bofh/workspace/sim"
fi;
clear
java -cp $SIM_HOME/xmlrpc-1.2-b1.jar:$SIM_HOME/mysql.jar:$SIM_HOME/server.jar com.jdevelop.sim.server.LiveSIMServer 8088 $SIM_HOME/runtime.props
