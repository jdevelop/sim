#!/bin/sh

SIM_HOME="your_home_here"
PIDFILE="$SIM_HOME/server.pid"
STARTFILE="$SIM_HOME/serverstart.sh";

if ! test -f $PIDFILE || ! kill -0 `cat $PIDFILE`
then
	echo "Starting server ...";
	. $STARTFILE >/dev/null 2>&1 & 
	echo $! > $PIDFILE
fi

