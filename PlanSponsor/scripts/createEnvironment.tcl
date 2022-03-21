####################################################
#
#  Virtual Hosts
#     PlanSponsorUSA_host
#     PlanSponsorNY_host
#     BrokerDealer_host
#     PlanSponsorCMA_host
#
#  Server Group
#     PlanSponsor Server Group
#
#  Application Servers
#     PlanSponsor_Clone1 (if using server group)
#     PlanSponsor_Clone2 (if using server group)
#     PlanSponsor Server (if not using server group)
#     PlanSponsorCMA Server
#
#  JDBC Providers
#     IBM DB2 XA Provider
#         Datasources
#              CustomerService Database
#              MRL System Database
#              PlanSponsorContent Database
#              Apollo/DB2Connect
#
####################################################
# Node settings
set node [Node list]

# Create VirtualHosts
puts "Removing any existing virtual host: PlanSponsorUSA_host ..."
catch {VirtualHost remove /VirtualHost:PlanSponsorUSA_host/}
puts "Creating virtual host: PlanSponsorUSA_host ..."
set aliaslist     [list *:80]
set aliasattr     [list AliasList $aliaslist]
set attributelist [list $aliasattr]
VirtualHost create /VirtualHost:PlanSponsorUSA_host/ -attribute $attributelist

puts "Removing any existing virtual host: PlanSponsorNY_host ..."
catch {VirtualHost remove /VirtualHost:PlanSponsorNY_host/}
puts "Creating virtual host: PlanSponsorNY_host ..."
set aliaslist     [list *:81]
set aliasattr     [list AliasList $aliaslist]
set attributelist [list $aliasattr]
VirtualHost create /VirtualHost:PlanSponsorNY_host/ -attribute $attributelist

puts "Removing any existing virtual host: BrokerDealer_host ..."
catch {VirtualHost remove /VirtualHost:BrokerDealer_host/}
puts "Creating virtual host: BrokerDealer_host ..."
set aliaslist     [list *:82]
set aliasattr     [list AliasList $aliaslist]
set attributelist [list $aliasattr]
VirtualHost create /VirtualHost:BrokerDealer_host/ -attribute $attributelist

puts "Removing any existing virtual host: PlanSponsorCMA_host ..."
catch {VirtualHost remove /VirtualHost:PlanSponsorCMA_host/}
puts "Creating virtual host: PlanSponsorCMA_host ..."
set aliaslist     [list *:9090]
set aliasattr     [list AliasList $aliaslist]
set attributelist [list $aliasattr]
VirtualHost create /VirtualHost:PlanSponsorCMA_host/ -attribute $attributelist

puts "Removing any existing virtual host: Pilot_host ..."
catch {VirtualHost remove /VirtualHost:Pilot_host/}
puts "Creating virtual host: Pilot_host ..."
set aliaslist     [list *:8090]
set aliasattr     [list AliasList $aliaslist]
set attributelist [list $aliasattr]
VirtualHost create /VirtualHost:Pilot_host/ -attribute $attributelist

# Creating JDBCDriver
set db2jdbcdriver "IBM DB2 XA Provider"
set implclassattr   [list ImplClass com.ibm.db2.jcc.DB2XADataSource]
set attributelist   [list $implclassattr]
puts "Removing any existing definition of $db2jdbcdriver ..."
catch {JDBCDriver remove /JDBCDriver:$db2jdbcdriver/ -recursive}
puts "Creating JDBC driver: $db2jdbcdriver ..."
JDBCDriver create /JDBCDriver:$db2jdbcdriver/ -attribute $attributelist
puts "Installing JDBC driver: $db2jdbcdriver on $node ..."
JDBCDriver install /JDBCDriver:$db2jdbcdriver/ -node $node -jarFile "C:\\Program Files\\IBM\\SQLLIB\\java\\db2jcc.jar"

# Create CustomerService Data Source
puts "Removing any current definition for Customer Service Data Source ..."
catch {DataSource remove {/JDBCDriver:$db2jdbcdriver/DataSource:Customer Service Database/}}
puts "Creating Customer Service Data Source..."
set dbnameattr    [list DatabaseName srzkd1d1]
set dbuserattr    [list DefaultUser db2admin]
set dbpasswdattr  [list DefaultPassword db2admin]
set jndinameattr  [list JNDIName jdbc/employeeProfile]
set dsdescattr    [list Description "Customer Service Database"]
set attributelist [list $dbnameattr $dbuserattr $dbpasswdattr $jndinameattr $dsdescattr]
DataSource create "/JDBCDriver:$db2jdbcdriver/DataSource:Customer Service Database/" -attribute $attributelist

# Create MRL Data Source
puts "Removing any current definition for MRL Data Source ..."
catch {DataSource remove {/JDBCDriver:$db2jdbcdriver/DataSource:MRL System Database/}}
puts "Creating MRL Data Source..."
set dbnameattr    [list DatabaseName mrl_ezk]
set dbuserattr    [list DefaultUser db2admin]
set dbpasswdattr  [list DefaultPassword db2admin]
set jndinameattr  [list JNDIName jdbc/mrl]
set dsdescattr    [list Description "MRL System Database"]
set attributelist [list $dbnameattr $dbuserattr $dbpasswdattr $jndinameattr $dsdescattr]
DataSource create "/JDBCDriver:$db2jdbcdriver/DataSource:MRL System Database/" -attribute $attributelist

# Create PlanSponsor Content Data Source
puts "Removing any current definition for PlanSponsor Content Data Source ..."
catch {DataSource remove {/JDBCDriver:$db2jdbcdriver/DataSource:PlanSponsor Content Database/}}
puts "Creating PlanSponsor Content Data Source..."
set dbnameattr    [list DatabaseName srpld1cm]
set dbuserattr    [list DefaultUser db2admin]
set dbpasswdattr  [list DefaultPassword db2admin]
set jndinameattr  [list JNDIName java:comp/env/jdbc/content]
set dsdescattr    [list Description "PlanSponsor Content Database"]
set attributelist [list $dbnameattr $dbuserattr $dbpasswdattr $jndinameattr $dsdescattr]
DataSource create "/JDBCDriver:$db2jdbcdriver/DataSource:PlanSponsor Content Database/" -attribute $attributelist

# Create Apollo/DB2Connect Data Source
puts "Removing any current definition for Apollo DB2Connect Data Source ..."
catch {DataSource remove {/JDBCDriver:$db2jdbcdriver/DataSource:Apollo DB2Connect/}}
puts "Creating Apollo DB2Connect Data Source..."
set dbnameattr    [list DatabaseName HODB2GW]
set dbuserattr    [list DefaultUser lpngdev]
set dbpasswdattr  [list DefaultPassword lptest01]
set jndinameattr  [list JNDIName jdbc/apollo]
set dsdescattr    [list Description "Apollo via DB2Connect"]
set attributelist [list $dbnameattr $dbuserattr $dbpasswdattr $jndinameattr $dsdescattr]
DataSource create "/JDBCDriver:$db2jdbcdriver/DataSource:Apollo DB2Connect/" -attribute $attributelist

# Creating ApplicationServer
puts "Removing any current definition for ApplicationServer PlanSponsor Server ..."
catch {ApplicationServer remove "${node}ApplicationServer:PlanSponsor Server/" -recursive}
set stdoutattr    [list Stdout "c:\\WebSphere\\AppServer\\logs\\PlanSponsor_stdout.log"]
set stderrattr    [list Stderr "c:\\WebSphere\\AppServer\\logs\\PlanSponsor_stderr.log"]
set minheapattr   [list InitialHeapSize 128]
set maxheapattr   [list MaxHeapSize 384]
set heapattr      [list $minheapattr $maxheapattr]
set jvmconfigattr [list JVMConfig $heapattr]
set workdirattr   [list WorkingDirectory "c:\\WebSphere\\AppServer\\bin"]
set moduleattr    [list ModuleVisibility 1]
#set stateattr     [list DesiredState Running]
set attributelist [list $workdirattr $moduleattr $stdoutattr $stderrattr $jvmconfigattr]
puts "Creating application server: PlanSponsor Server ..."
ApplicationServer create "${node}ApplicationServer:PlanSponsor Server/" -attribute $attributelist
exit


# WebContainer transport settings
set httpport1 9090
set httpport2 9091

# DataSource settings
set ejbds Webbank
set ejbdsjndiname jdbc/webbank
set ejbdsdesc "DataSource for CMP EJBs"
set sessionsds WebbankSessions
set sessdsjndiname jdbc/webbanksessions
set sessdsdesc "DataSource for session persistence"
set appdbname WEBBANK
set sessdbname SESSIONS
set dbuser db2admin
set dbpasswd db2admin

# ApplicationServer settings
set appserver1 WebbankServer01
set appserver2 WebbankServer02
set minjvmheap 64
set maxjvmheap 128
set stdout "logs/stdout.log"
set stderr "logs/stderr.log"

set workdir "D:/WebSphere/AppServer/bin"

# ServerGroup settings
set sgname WebbankSG

# EnterpriseApp settiongs
set entappname Webbank
set instdir "D:/WebSphere/AppServer/installableApps"
set earfile webbank_deployed.ear

# Security settings
set localuid wasadmin
set localpwd wasadmin

####################################################################



# Creating DataSource for session persistence
set dbnameattr    [list DatabaseName $sessdbname]
set dbuserattr    [list DefaultUser $dbuser]
set dbpasswdattr  [list DefaultPassword $dbpasswd]
set jndinameattr  [list JNDIName $sessdsjndiname]
set dsdescattr    [list Description $sessdsdesc]
set attributelist [list $dbnameattr $dbuserattr $dbpasswdattr $jndinameattr $dsdescattr]
puts "Creating data source: $sessionsds ..."
DataSource create /JDBCDriver:$db2jdbcdriver/DataSource:$sessionsds/ -attribute $attributelist


# Configuring SessionManager for session persistence
#     - enable session persistence
#     - sepcify datasource
#     - performance settings
#           - very High (optimize for performance) see admin console
#

set perenat [list EnablePersistentSessions true]
set perdsat [list PersistentDatasourceName $sessionsds]
set siat    [list TuningScheduleInvalidation true]
set titoat  [list TuningInvalidationTimeout 30]
set wcontat [list TuningWriteContents 0]
set wfreqat [list TuningWriteFrequency 2]
set wintat  [list TuningWriteInterval 300]
set sessmanlist [list $perenat $perdsat $siat $titoat $wcontat $wfreqat $wintat]
set sessmanattr [list SessionManagerConfig $sessmanlist]
set webcontainerattr  [list WebContainerConfig $sessmanattr]
set attributelist [list $webcontainerattr]
puts "Modifying application server session persistence for: $appserver1 ..."
ApplicationServer modify /Node:$node/ApplicationServer:$appserver1/ -attribute $attributelist

# Installing the enterprise application
#
# first, construct -moduleappservers option
#     module installation destination
#     it is possible to spread modules over multiple servers
#     for instance to separate web modules and ejb modules
set warmodserv1 [list webbankWeb.war /Node:$node/ApplicationServer:$appserver1/]
set ejbmodserv [list webbankEJBs.jar /Node:$node/ApplicationServer:$appserver1/]
set modappservers [list $ejbmodserv $warmodserv1]

# second, construct -ejbreferences option
#     this can also be defined in the deployment descriptor using the AAT
set ejbref1 [list webbankEJBs.jar::Transfer::ejb/BranchAccount webbank/BranchAccount]
set ejbref2 [list webbankEJBs.jar::Transfer::ejb/CustomerAccount webbank/CustomerAccount]
set ejbref3 [list webbankEJBs.jar::Consultation::ejb/BranchAccount webbank/BranchAccount]
set ejbref4 [list webbankEJBs.jar::Consultation::ejb/CustomerAccount webbank/CustomerAccount]
set ejbref5 [list webbankWeb.war::ejb/Transfer webbank/Transfer]
set ejbrefs [list $ejbref1 $ejbref2 $ejbref3 $ejbref4 $ejbref5]

# third, construct -ejbnames option
#     this can be also defined in the deployment descriptor using the AAT
set ejbname1 [list webbankEJBs.jar::BranchAccount webbank/BranchAccount]
set ejbname2 [list webbankEJBs.jar::CustomerAccount webbank/CustomerAccount]
set ejbname3 [list webbankEJBs.jar::Transfer webbank/Transfer]
set ejbname4 [list webbankEJBs.jar::Consultation webbank/Consultation]
set ejbnames [list $ejbname1 $ejbname2 $ejbname3 $ejbname4]

# fourth, construct -cmpdatasources option
#     this can also be defined in the deployment descriptor using the AAT
set cmpdatasource1 [list webbankEJBs.jar::BranchAccount jdbc/webbank]
set cmpdatasource2 [list webbankEJBs.jar::CustomerAccount jdbc/webbank]
set cmpdatasources [list $cmpdatasource1 $cmpdatasource2]

# define the datasource for the ejb module can be used instead specifying
# the datasource for each bean
set ejbdatasource1 [list webbankEJBs.jar jdbc/webbank]
set ejbdatasources [list $ejbdatasource1]

# fifth, construct -modvirtualhosts option
set modvirthost1 [list webbankWeb.war $vhostname]
set modvirthosts [list $modvirthost1]

# installing the EnterpriseApp
puts "Installing enterprise application: $entappname ..."
EnterpriseApp install /Node:$node/ $instdir/$earfile -appname $entappname -modvirtualhosts $modvirthosts -ejbreferences $ejbrefs -ejbnames $ejbnames -cmpdatasources $cmpdatasources -moduleappservers $modappservers

# creating ServerGroup from the existing application server
puts "Creating server group: $sgname ..."
ServerGroup create /ServerGroup:$sgname/ -baseInstance /Node:$node/ApplicationServer:$appserver1/

# creating a second clone
set clonenameattr [list Name $appserver2]
set attributelist [list $clonenameattr]
puts "Creating clone: $appserver2 ..."
ServerGroup clone /ServerGroup:$sgname/ -node /Node:$node/ -cloneAttrs $attributelist

# Changing the working dir and http port for clone 1
set workdir "D:/webbank/${appserver1}"
set workdirattr   [list WorkingDirectory $workdir]
set portattr [list Port $httpport1]
set httptransattr [list $portattr]
set transportattr [list Transports $httptransattr]
set webcontainerattr [list WebContainerConfig $transportattr]
set attributelist [list $workdirattr $webcontainerattr]
puts "Modifying working dir and http port for: $appserver1 ..."
ApplicationServer modify /Node:$node/ApplicationServer:$appserver1/ -attribute $attributelist

# Changing the working dir and http port for clone 2
set workdir "D:/webbank/${appserver2}"
set workdirattr   [list WorkingDirectory $workdir]
set portattr [list Port $httpport2]
set httptransattr [list $portattr]
set transportattr [list Transports $httptransattr]
set webcontainerattr [list WebContainerConfig $transportattr]
set attributelist [list $workdirattr $webcontainerattr]
puts "Modifying working dir and http port for: $appserver2 ..."
ApplicationServer modify /Node:$node/ApplicationServer:$appserver2/ -attribute $attributelist

# specifing the user role mappings
# construct -specialroles option
set specialrole1 [list Manager AllAuthenticatedUsers]
set specialrole2 [list Employee AllAuthenticatedUsers]
set specialrole3 [list AllAuthenticated AllAuthenticatedUsers]
set specialrole4 [list Everyone AllAuthenticatedUsers]
set specialroles [list $specialrole1 $specialrole2 $specialrole3 $specialrole4]
set specialrolesattr [list $specialroles]
puts "Mapping user roles ..."
SecurityRoleAssignment addSpecialRoleMapping /EnterpriseApp:$entappname/ -specialroles $specialroles

# configuring the global security with LOCALOS
### set secattr [list $localuid $localpwd]
### puts "Configuring global security using: LOCALOS ..."
### SecurityConfig setAuthenticationMechanism LOCALOS -userid $secattr

# enabling global security
### puts "Enabling global security (node restart necessary!) ..."
### SecurityConfig enableSecurity

# Regenerate Web server plugin
puts "Regenerating plugin config for node: $node ..."
Node regenPluginCfg /Node:$node/
