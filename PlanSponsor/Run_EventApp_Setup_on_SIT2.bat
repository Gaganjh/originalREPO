
Rem to Create EventMessage Engine Datasource on SIT2

set node1_profile_name=mlisezkscals2Node
set was_datasource_name=Event Message Engine Data source
set dmgr_cell_name=jhrpsSIT2Cell
set non_xa_db2_jdbc_driver=DB2 Universal JDBC Driver Provider
set was_security_alias=jhrpsSIT2CellManager/jaas/pswwss
set was_database_name=S2PSWWSS

cd \cvs\PlanSponsor\
db2cmd /w /c /i ant -f build_was6ND.xml -Droot.dir=D: -Dnode1.profile.name="%node1_profile_name%" -Dwas.datasource.name="%was_datasource_name%" -Ddmgr.cell.name="%dmgr_cell_name%" -Dwas.db2.jdbc.driver="%non_xa_db2_jdbc_driver%" -Dwas.security.alias="%was_security_alias%" -Dwas.database.name="%was_database_name%" was6.setup.eventapp.datasource > D:\Archive\create_event_message_engine_datasource.log

Rem to Create EventApp Setup on SIT2

set dmgr_profile_name=DeploymentManager
set node1_profile_name=mlisezkscals2Node
set node2_profile_name=mlisezksas2Node
set dmgr_cell_name=jhrpsSIT2Cell
set cluster_setup_required_flag=yes
set CICS_Name=LPXCICS

cd \cvs\PlanSponsor\
db2cmd /w /c /i ant -f build_was6ND.xml -Droot.dir=d: -DCICS.name="%CICS_Name%" -Ddmgr.profile.name="%dmgr_profile_name%" -Dnode1.profile.name="%node1_profile_name%"  -Dnode2.profile.name="%node2_profile_name%" -Ddmgr.cell.name="%dmgr_cell_name%" -Dneed.cluster.setup.for.eventapp="%cluster_setup_required_flag%" was6.setup.eventapp > D:\Archive\eventapp_setup.log