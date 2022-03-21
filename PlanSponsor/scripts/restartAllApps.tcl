#foreach application [EnterpriseApp list] {
#    puts "Stopping $application ..."
#    EnterpriseApp stop $application
#    puts "Waiting 30 seconds for Enterprise App Stop to finish ..."
#    after 30000
#    puts "Starting $application ..."
#    EnterpriseApp start $application
#}
puts Need to update to 6.0