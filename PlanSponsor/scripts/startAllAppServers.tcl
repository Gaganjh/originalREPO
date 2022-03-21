#foreach server [ApplicationServer list] {
#    puts "Starting $server ..."
#    catch { ApplicationServer start $server }
#    after 10000
#}
#after 20000
#foreach group [ServerGroup list] {
#    puts "Starting $group ..."
#    catch { ServerGroup start $group }
#    after 5000
#}
puts Need to update to 6.0