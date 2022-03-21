# Node settings
#set node [lindex $argv 1]
#set appserver1 "EZk Server"

# VirtualHost settings
#set vhostname1 ezk_host
#set vhostname2 ezkNY_host
#set webport 8082
#set sslport 443

# EnterpriseApp settiongs
#set earfile "C:/cvs/ezk/dist/ezk.ear"

# Installing the enterprise application
#
# first, construct -moduleappservers option
#     module installation destination
#     it is possible to spread modules over multiple servers
#     for instance to separate web modules and ejb modules
#set warmodserv1 [list ezkWeb.war /Node:$node/ApplicationServer:$appserver1/]
#set warmodserv2 [list ezkWebNY.war /Node:$node/ApplicationServer:$appserver1/]
#set ejbmodserv [list ezkEJB.jar /Node:$node/ApplicationServer:$appserver1/]
#set modappservers [list $ejbmodserv $warmodserv1 $warmodserv2]

# fifth, construct -modvirtualhosts option
#set modvirthost1 [list ezkWeb.war $vhostname1]
#set modvirthost2 [list ezkWebNY.war $vhostname2]
#set modvirthosts [list $modvirthost1 $modvirthost2]

# installing the EnterpriseApp
#puts "Installing enterprise application: EZk ..."
#EnterpriseApp install /Node:$node/ $earfile -modvirtualhosts $modvirthosts -moduleappservers $modappservers
#EnterpriseApp install /Node:$node/ $earfile -modvirtualhosts $modvirthosts -defappserver /Node:$node/ApplicationServer:$appserver1/

# Regenerate Web server plugin
#puts "Regenerating plugin config for node: $node ..."
#Node regenPluginCfg /Node:$node/
puts Need to update to 6.0