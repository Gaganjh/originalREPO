set earFile [lindex $argv 0]
set cellName [lindex $argv 1]
set nodeName [lindex $argv 2]
set appserverName [lindex $argv 3]
set webserverName [lindex $argv 4]
set server1 "WebSphere:cell=$cellName,node=$nodeName,server=$appserverName"
set webserver1 "WebSphere:cell=$cellName,node=$nodeName,server=$webserverName"

set USA_host [list "PlanSponsorWeb" PlanSponsorWeb.war,WEB-INF/web.xml PlanSponsorUSA_host] 
set NY_host [list "PlanSponsorWebNY" PlanSponsorWebNY.war,WEB-INF/web.xml PlanSponsorNY_host] 
set BD_host [list "BrokerDealerWeb" BrokerDealerWeb.war,WEB-INF/web.xml BrokerDealer_host] 
set web_to_vh [list $USA_host $NY_host $BD_host]

set mod_PlanSponsorEJB  [list PlanSponsorEJB PlanSponsorEJB.jar,META-INF/ejb-jar.xml $server1]
set mod_MessageService  [list MessagingService MessagingService.jar,META-INF/ejb-jar.xml $server1]
set mod_AccountService  [list AccountService AccountService.jar,META-INF/ejb-jar.xml $server1] 
set mod_searchEJB  [list searchEJB SearchService.jar,META-INF/ejb-jar.xml $server1] 
set mod_MrlLoggerService  [list MrlLoggerService MrlLoggerService.jar,META-INF/ejb-jar.xml $server1] 
set mod_Report_Service  [list ReportService ReportService.jar,META-INF/ejb-jar.xml $server1] 
set mod_SecurityService  [list SecurityService SecurityService.jar,META-INF/ejb-jar.xml $server1] 

set mod_PlanSponsorWeb  [list PlanSponsorWeb PlanSponsorWeb.war,WEB-INF/web.xml $server1+$webserver1] 
set mod_PlanSponsorWebNY  [list PlanSponsorWebNY PlanSponsorWebNY.war,WEB-INF/web.xml $server1+$webserver1] 
set mod_BrokerDealerWeb  [list BrokerDealerWeb BrokerDealerWeb.war,WEB-INF/web.xml $server1+$webserver1] 

set mod_to_servers  [list $mod_PlanSponsorEJB $mod_MessageService $mod_AccountService $mod_searchEJB $mod_MrlLoggerService $mod_Report_Service $mod_SecurityService $mod_PlanSponsorWeb $mod_PlanSponsorWebNY $mod_BrokerDealerWeb]

set attrs [list -MapWebModToVH $web_to_vh -MapModulesToServers $mod_to_servers]
puts $earFile
$AdminApp install $earFile $attrs

$AdminConfig save

set appManager [$AdminControl queryNames cell=$cellName,node=$nodeName,type=ApplicationManager,process=$appserverName,*]

$AdminControl invoke $appManager startApplication PlanSponsor

