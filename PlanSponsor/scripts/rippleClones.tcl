#foreach server [ServerGroup listClones {/ServerGroup:PlanSponsor Server Group/}] {
#    puts "Stopping clone $server ..."
#    catch {ApplicationServer stop $server}
#    puts "Restarting clone $server ..."
#    catch {ApplicationServer start $server}
#}
puts Need to update to 6.0