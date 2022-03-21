set dmgr_profile_name=DeploymentManager
set node1_profile_name=mlisezkucals4Node
set node2_profile_name=mlisezkuas4Node
set dmgr_cell_name=jhrpsUAT4Cell
set cluster_setup_required_flag=yes
set CICS_Name=LPXCICS


ant -f build_was6ND.xml -Droot.dir=d: -DCICS.name="%CICS_Name%" -Ddmgr.profile.name="%dmgr_profile_name%" -Dnode1.profile.name="%node1_profile_name%"  -Dnode2.profile.name="%node2_profile_name%" -Ddmgr.cell.name="%dmgr_cell_name%" -Dneed.cluster.setup.for.eventapp="%cluster_setup_required_flag%" was6.setup.eventapp > D:\Archive\EventApp_Setup.log