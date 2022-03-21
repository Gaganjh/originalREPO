set appNodeAttribute "/ServerGroup:PlanSponsor Server Group/"
puts "Stopping $appNodeAttribute ..."
catch { ServerGroup stop $appNodeAttribute -force -wait 40 }
