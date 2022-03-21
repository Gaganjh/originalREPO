set earFile [lindex $argv 0]
set cellName [lindex $argv 1]
set nodeName [lindex $argv 2]
set webserverName [lindex $argv 3]
set server1 "WebSphere:cell=$cellName,node=$nodeName,server=server1"
set webserver1Part1 "WebSphere:cell=$cellName,node=$webserverName"
set webserver1Part2 "_node,server=$webserverName"
set webserver1 $webserver1Part1$webserver1Part2

set CMA_host [list PlanSponsorCMAWeb PlanSponsorCMAWeb.war,WEB-INF/web.xml PlanSponsorCMA_host] 
set web_to_vh [list $CMA_host]

set mod_CMAEJB  [list CMAEJB CMAService.jar,META-INF/ejb-jar.xml $server1]
set mod_PlanSponsorCMAWeb  [list PlanSponsorCMAWeb PlanSponsorCMAWeb.war,WEB-INF/web.xml $server1+$webserver1]

set mod_to_servers  [list $mod_CMAEJB $mod_PlanSponsorCMAWeb ]

set attrs [list -MapWebModToVH $web_to_vh -MapModulesToServers $mod_to_servers]
puts $earFile
$AdminApp install $earFile $attrs
$AdminConfig save

