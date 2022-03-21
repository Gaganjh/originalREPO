# Node settings
#set node itsohost

# VirtualHost settings
#set vhostname webbank_vhost

# JDBCDriver settings
#set db2jdbcdriver WebbankDB2Driver

# DataSource settings
#set ejbds Webbank
#set sessionsds WebbankSessions

# ApplicationServer settings
#set appserver1 WebbankServer01
#set appserver2 WebbankServer02
#
# ServerGroup settings
#set sgname WebbankSG

# EnterpriseApp settiongs
#set entappname Webbank

####################################################################

#catch {EnterpriseApp remove /EnterpriseApp:$entappname/ -recursive}
#catch {ApplicationServer remove /Node:$node/ApplicationServer:$appserver1/ -recursive}
#catch {ApplicationServer remove /Node:$node/ApplicationServer:$appserver2/ -recursive}
#catch {DataSource remove /JDBCDriver:$db2jdbcdriver/DataSource:$ejbds/ -recursive}
#catch {DataSource remove /JDBCDriver:$db2jdbcdriver/DataSource:$sessionsds/ -recursive}
#catch {JDBCDriver remove /JDBCDriver:$db2jdbcdriver/ -recursive}
#catch {ServerGroup remove /ServerGroup:$sgname/ -recursive}
#catch {VirtualHost remove /VirtualHost:$vhostname/ -recursive}
