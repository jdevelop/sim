Site Instant Messenger 4.4.1 installation instructions
====

Eugeny N Dzhurinsky, JDevelop.com, 2003-2004
----

1 Pre-requirements

You will need JDK 1.3.1 or higher to install and launch 
the IM server. You may obtain JDK from 
http://java.sun.com/downloads/ - just choose your 
operating system and click appropriate download link.

2 Package contains

1. Server package

  (a) server.jar - this file contains IM server.

  (b) mysql.jar - the JDBC driver to use MySQL database 
    with the IM (however, this should be replaced with 
    appropriate JDBC driver for database you are using 
    with your WEB-site)

  (c) xmlrpc.jar - this library allows to use XML-RPC 
    protocol to access IM server methods

  (d) runtime.props - the main server configuration file

  (e) serverstart.sh - shell script to start server

  (f) recycle.sh - cron job to auto-restart server if 
    it goes down for some reason. This script uses 
    serverstart.sh to start server

  (g) wrapper.cgi or wrapper.php - script, which wraps 
    the IM protocol in HTTP requests. This feature 
    allows to use instant messenger for customersm 
    which are located behind firewalls or proxy servers.

2. Client package

  (a) client.jar - tthis file contains the IM client applet

  (b) skin.jar or skin.zip - the skin file to use with messenger

  (c) applet.php - sample PHP page, which contains the 
    sample applet tag

  (d) banner.php - sample PHP page for banner display

3 IM Server configuration

The IM server configuration is located in the 
runtime.props file. This file has set of configuration 
options for database, timeouts etc.
There is the description of options below:

* xml-rpc.port - port, on which XML-RPC module listing 
  for the requests

* driver - name of JDBC driver (don't change if you not 
  sure what are you doing)

* dbprovider - name of using RDBMS (don't change if you 
  not sure what are you doing)

* dbhost - host of the database server

* dbport - port of database server

* dbname - name of the database to connect

* dbuser - username to access database

* dbpassword - password to access database

* AUTH_TABLE_NAME - name of table, where IM server will 
  look for username and and password

* AUTH_TABLE_LOGIN - name of field in AUTH_TABLE_NAME 
  table, which holds username

* AUTH_TABLE_PASSWORD - name of field in 
  AUTH_TABLE_NAME which holds the password of user

* AUTH_TABLE_ID - name of field, which contains unique 
  user ID (may have numeric or string type)

* PROFILE_TABLE_NAME - name of table, which holds 
  profile for users

* PROFILE_TABLE_USERNAME - name of field, which holds 
  the username to display. In this parameter you can 
  use SQL statements, like concat(firstname,' 
  ',lastname) to get complicated usernames from several fields

* PROFILE_TABLE_ID - user ID field name. This is used 
  as foreign key to table AUTH_TABLE_NAME.

* PROFILE_TABLE_SEX - name of the field, which contains 
  gender information. 1 means male, any other value - 
  female. You can use the SQL statements like 
  if(gender='male',1,2) as value for this parameter.

* PROFILE_TABLE_AGE - optional information, which is 
  used to provide additional information, like age of 
  user, or state, or something else. You can use SQL 
  statements here as well.

* PROFILE_TABLE_PAID - field name, which shows if user 
  is paid member and can start chat. Users with value 1 
  can start chat, users with any other value only can 
  accept chat, can't to initiate it by theirselves. You 
  can use the SQL statements here.

* IGNORE_TABLE_NAME - name of table, where blocked list 
  for user is stored.

* IGNORE_TABLE_UID - the ID of user. This is foreign 
  key to AUTH_TABLE_NAME.

* IGNORE_TABLE_IGNORED - name of field, which contains 
  list of IDs for ignored users, separated by commas. 
  This field must have type of VARCHAR, but MEDIUMTEXT 
  is even better.

* BUDDY_TABLE_NAME (optional) - name of table, where 
  buddy list is stored.

* BUDDY_TABLE_ID (optional) - name of field, which 
  contains ID of user. This is foreign key for AUTH_TABLE_NAME.

* BUDDY_TABLE_BUDDY (optional) - ID of buddy, foreign 
  key for AUTH_TABLE_NAME

* timeout - parameter, which defines the timeout*30 
  seconds, after which user will be logged out if he 
  didn't send any requests to a server (no requests for 
  new messages, no requests for buddy list and no 
  requests for sending new message)

* initial_timeout - timeout value which user gets once 
  he's logged in.

* acceptedHosts - parameter, which controls hosts 
  names, where applet can be integrated in the page. 
  Names of hosts separated by ',' , ' ' , ';' . This 
  will avoid usage of your messenger client on another 
  site, if you didn't specify this site in the list of 
  accepted hosts.

Now about the recycle.sh. You will need to change the 
SIM_HOME variable so it will point to directory, where 
recycle.sh is located, and PORT variable, which defines 
port on which IM server will be started.

4 IM client configuration.

Take a look at the applet.php file, parameters for Java 
applet you will find there are described below:

1. login - username of current user

2. password - password for current user

3. port - port where IM server is configured. Used when 
  direct connections client<->server are used.

4. prefix - relative URL of the wrapper script on your 
  server (it should start with slash and point to file 
  from root of your web-server, for instance - 
  /im/wrapper.php. Consider this as relative link to 
  file on your server, like http://server.comn/im/wrapper.php)

5. profileURL - absolute URL to script, which returns 
  name of user image. This script have to return single 
  line, like this
  http://yourserver.com/images/profile/userimage.jpg
  In this case applet will get the image from server 
  and include it in chat window.

6. bannersURL - absolute URL to be used for invoking 
  script to retrieve current banner to show

7. bannersTimeout - delay in seconds between rotating banners

8. bannersBackground - background color to show if no 
  banner is in the system or image failed to load.

NOTE: all the banner pictures must be on the SAME 
server, where IM server is running, because applet will 
not be able to connect to any other server - it is 
prohibited by the JVM.
Sample script for banners:

        <?php 

        header('Content-type: text/plain'); 

        // sending the URL

        echo 'http://jdevelop.com/sim'."\n"; 

        //sending the image URL

        echo 'http://simv4.priv/jdevelopbanner.jpg'."\n"; 
        ?> 


Now about skin.zip file. This file contains skin. Skin 
is set of images, sounds, and it has it's own 
configuration file called layout.conf. Most of 
parameters there are self-explaining, but several are 
not. Here is the list:

* smileys_per_row - defines how many smiles will be 
  shown per row in a dropdown list for smiles.

* buddiesPerRow - defines how many columns will have 
  buddy list

* profileURL - URL, which will be opened when user icon 
  in buddy list or user image in message window will be 
  clicked. This URL will looks like 
  http://yourserver.com/profile.php?userid=
  applet will append the user ID to this url and open 
  it in a new window

* profileURLTarget - target window where profile will 
  be opened

* client_pull_delay - delay (client_pull_delay*5 
  seconds), which sets the delay between requests to server

* client_ignore_timeout - in seconds, shows how many 
  time the messages from user, which just was "declined" 
  to start chat, will not be accepted from the current user.

* maxsessions - controls how many concurrent chat 
  windows will have user at the same time

5 Installation

Unpack ZIP file, and place the server directory 
somewhere on server, where you will use it, and move 
contents of client directory directory, accessible from 
WEB. Configure runtime.props to match your system and 
start server with recycle.sh. Then place client applet 
tag to some of your pages, create your own skin.zip and 
enjoy ;).

6 Troubleshooting

There will be no problems installing and deploying 
messenger on most systems, if you have troubles - 
please report to eugenydzh@jdevelop.com, you will get 
support within 24 hours.
