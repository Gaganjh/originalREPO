#foreach server [ApplicationServer list] {
#    #puts "Stopping $server ..."
#    catch { ApplicationServer stop $server -force -wait 40 }
#}
#foreach group [ServerGroup list] {
#    puts "Stopping $group ..."
#    catch { ServerGroup stop $group -force }
#}
puts Need to update to 6.0