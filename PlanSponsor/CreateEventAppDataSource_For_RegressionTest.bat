
set node1_profile_name=mlisezkucals4Node
set was_datasource_name=Event Message Engine Data source
set dmgr_cell_name=jhrpsUAT4Cell
set non_xa_db2_jdbc_driver=DB2 Universal JDBC Driver Provider
set was_security_alias=jhrpsUAT4CellManager/jaas/event
set was_database_name=R2SESSDB


ant -f build_was6ND.xml -Droot.dir=D: -Dnode1.profile.name="%node1_profile_name%" -Dwas.datasource.name="%was_datasource_name%" -Ddmgr.cell.name="%dmgr_cell_name%" -Dwas.db2.jdbc.driver="%non_xa_db2_jdbc_driver%" -Dwas.security.alias="%was_security_alias%" -Dwas.database.name="%was_database_name%" was6.setup.eventapp.datasource > D:\Archive\create_event_message_engine_datasource.log
