<<<<<<< .working
@echo off
setlocal
call uncatalogUATDatabases
db2cmd /c /w /i db2 catalog tcpip node ezcsiu remote ibm-sp1 server 50218 remote_instance ezcsiu with 'CSDB Instance on SP1'
db2cmd /c /w /i db2 catalog database ezcsdb as ezcsdb at node ezcsiu with 'Customer Service Database'
db2cmd /c /w /i db2 catalog tcpip node izvfiu remote ibm-sp1 server 50216 remote_instance izvfiu with 'ViewFunds Instance on SP1'
db2cmd /c /w /i db2 catalog database ezvfdb as ezvfdb at node izvfiu with 'ViewFunds Database'
db2cmd /c /w /i db2 catalog tcpip node mrlezk remote ibm-sp1 server 50214 remote_instance mrlezk with 'MRL Instance on SP1'
db2cmd /c /w /i db2 catalog database mrl_ezk as mrl_ezk at node mrlezk with 'MRL Database'
db2cmd /c /w /i db2 catalog tcpip node ezcsu2 remote ibm-sp1 server 53112 remote_instance ezcsu2 with 'PSW Content Instance on SP1'
db2cmd /c /w /i db2 catalog database srplu2d1 as srplu2d1 at node ezcsu2 with 'PlanSponsor Content Database'
db2cmd /c /w /i db2 catalog tcpip node ezvsiu remote ibm-sp1 server 50204 remote_instance ezvsiu with 'Vignette Instance on SP1'
db2cmd /c /w /i db2 catalog database vgncnt as vgncnt at node ezvsiu with 'EZk Content Database'
db2cmd /c /w /i db2 catalog tcpip node srstu1 remote ibm-sp1 server 50006 remote_instance srstu1 with 'SubJournal Instance on SP1'
db2cmd /c /w /i db2 catalog database srstu1d1 as srstu1d1 at node srstu1 with 'Submission Journal Database'
db2cmd /c /w /i db2 catalog tcpip node mlisdbdv remote mlisdbdv server 3700 with 'DB2 Gateway'
db2cmd /c /w /i db2 catalog database tom00d0 as xx_ddb2c at node mlisdbdv authentication dcs with 'Apollo Database via DB2Connect'
endlocal
=======
@echo off
setlocal
call uncatalogUATDatabases
db2cmd /c /w /i db2 catalog tcpip node ezcsiu remote ibm-sp1 server 50218 remote_instance ezcsiu with 'CSDB Instance on SP1'
db2cmd /c /w /i db2 catalog database ezcsdb as ezcsdb at node ezcsiu with 'Customer Service Database'
db2cmd /c /w /i db2 catalog tcpip node mrlezk remote ibm-sp1 server 50214 remote_instance mrlezk with 'MRL Instance on SP1'
db2cmd /c /w /i db2 catalog database mrl_ezk as mrl_ezk at node mrlezk with 'MRL Database'
db2cmd /c /w /i db2 catalog tcpip node ezcsu2 remote ibm-sp1 server 53112 remote_instance ezcsu2 with 'PSW Content Instance on SP1'
db2cmd /c /w /i db2 catalog database srplu2d1 as srplu2d1 at node ezcsu2 with 'PlanSponsor Content Database'
db2cmd /c /w /i db2 catalog tcpip node ezvsiu remote ibm-sp1 server 50204 remote_instance ezvsiu with 'Vignette Instance on SP1'
db2cmd /c /w /i db2 catalog database vgncnt as vgncnt at node ezvsiu with 'EZk Content Database'
db2cmd /c /w /i db2 catalog tcpip node srstu1 remote ibm-sp1 server 50006 remote_instance srstu1 with 'SubJournal Instance on SP1'
db2cmd /c /w /i db2 catalog database srstu1d1 as srstu1d1 at node srstu1 with 'Submission Journal Database'
db2cmd /c /w /i db2 catalog tcpip node mlisdbdv remote mlisdbdv server 3700 with 'DB2 Gateway'
db2cmd /c /w /i db2 catalog database tom00d0 as xx_ddb2c at node mlisdbdv authentication dcs with 'Apollo Database via DB2Connect'
endlocal
>>>>>>> .merge-right.r383510
