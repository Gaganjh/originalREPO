<<<<<<< .working
@echo off
setlocal
db2cmd /c /w /i db2 catalog tcpip node n0bdbc remote n0bdbc.manulife.com server 3700 with 'Main DB2 Gateway'
db2cmd /c /w /i db2 catalog database tom00p0 as xx_pdb2c at node n0bdbc with 'Apollo DB2 via Main Gateway'
db2cmd /c /w /i db2 catalog tcpip node n0bdbb remote n0bdbb.manulife.com server 3700 with 'Secondary DB2 Gateway'
db2cmd /c /w /i db2 catalog database tom00p0 as xx_pdb2b at node n0bdbb with 'Apollo DB2 via Second Gateway'
db2cmd /c /w /i db2 catalog tcpip node srs_u3 remote ibm-mlistorngebp1.manulife.com server 50008 remote_instance srs_u3 with 'ViewFunds Instance on BP1'
db2cmd /c /w /i db2 catalog database srs_u3d1 as srs_u3d1 at node srs_u3 with 'ViewFunds Database'
db2cmd /c /w /i db2 catalog tcpip node mrl_p1 remote mlistorngebp4.manulife.com server 50220 remote_instance mrl_p1 with 'MRL Instance on BP4'
db2cmd /c /w /i db2 catalog database mrl_p1d2 as mrl_p1d2 at node mrl_p1 with 'MRL Database'
db2cmd /c /w /i db2 catalog tcpip node ezcsip remote mlistorngebp4.manulife.com server 50318 remote_instance ezcsip with 'CSDB Instance on BP4'
db2cmd /c /w /i db2 catalog database ezcsipd1 as ezcsipd1 at node ezcsip with 'Customer Service Database'
db2cmd /c /w /i db2 catalog tcpip node srplp2 remote mlistorngebp4.manulife.com server 50134 remote_instance srplp2 with 'PSW Content Instance on BP4'
db2cmd /c /w /i db2 catalog database srplp2d1 as srplp2d1 at node srplp2 with 'PlanSponsor Content Database'
=======
@echo off
setlocal
db2cmd /c /w /i db2 catalog tcpip node n0bdbc remote n0bdbc.manulife.com server 3700 with 'Main DB2 Gateway'
db2cmd /c /w /i db2 catalog database tom00p0 as xx_pdb2c at node n0bdbc with 'Apollo DB2 via Main Gateway'
db2cmd /c /w /i db2 catalog tcpip node n0bdbb remote n0bdbb.manulife.com server 3700 with 'Secondary DB2 Gateway'
db2cmd /c /w /i db2 catalog database tom00p0 as xx_pdb2b at node n0bdbb with 'Apollo DB2 via Second Gateway'
db2cmd /c /w /i db2 catalog tcpip node mrl_p1 remote mlistorngebp4.manulife.com server 50220 remote_instance mrl_p1 with 'MRL Instance on BP4'
db2cmd /c /w /i db2 catalog database mrl_p1d2 as mrl_p1d2 at node mrl_p1 with 'MRL Database'
db2cmd /c /w /i db2 catalog tcpip node ezcsip remote mlistorngebp4.manulife.com server 50318 remote_instance ezcsip with 'CSDB Instance on BP4'
db2cmd /c /w /i db2 catalog database ezcsipd1 as ezcsipd1 at node ezcsip with 'Customer Service Database'
db2cmd /c /w /i db2 catalog tcpip node srplp2 remote mlistorngebp4.manulife.com server 50134 remote_instance srplp2 with 'PSW Content Instance on BP4'
db2cmd /c /w /i db2 catalog database srplp2d1 as srplp2d1 at node srplp2 with 'PlanSponsor Content Database'
>>>>>>> .merge-right.r383510
endlocal