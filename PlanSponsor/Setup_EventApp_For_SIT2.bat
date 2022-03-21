
Rem to Create EventMessage Engine Datasource


set node1_profile_name=mlisezkscals2Node
set was_datasource_name=Event Message Engine Data source
set dmgr_cell_name=jhrpsSIT2Cell
set non_xa_db2_jdbc_driver =DB2 Universal JDBC Driver Provider
set was_security_alias =jhrpsSIT2CellManager/jaas/pswwss
set was_database_name=S2PSWWSS

cd \cvs\PlanSponsor\
ant -f build_was6ND.xml -Droot.dir=c: -Dnode1.profile.name="%node1_profile_name%" -Dwas.datasource.name="%was_datasource_name%" -Ddmgr.cell.name="%dmgr_cell_name%" -Dwas.db2.jdbc.driver="%non_xa_db2_jdbc_driver%" -Dwas.security.alias="%was_security_alias%" -Dwas.database.name="%was_database_name%" was6.setup.eventapp.datasource 

Rem to setup EventApp for SIT2 we need to run 


Rem - Set the ant parameter values for SIT2 environment

set dmgr_profile_name=DeploymentManager
set node1_profile_name=mlisezkscals2Node
set node2_profile_name=mlisezksas2Node  
set dmgr_cell_name=jhrpsSIT2Cell
set cluster_setup_required_flag=yes

cd \cvs\PlanSponsor\
ant -f build_was6ND.xml -Droot.dir=d: -Ddmgr.profile.name="%dmgr_profile_name%" -Dnode1.profile.name="%node1_profile_name%"  -Dnode2.profile.name="%node2_profile_name%" -Ddmgr.cell.name="%dmgr_cell_name%" -Dneed.cluster.setup.for.eventapp="%cluster_setup_required_flag%" was6.setup.eventapp > D:\Archive\EventAppSetupLogs\OCT14_full_eventapp_setup.log





 
	