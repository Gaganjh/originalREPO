@echo off
setlocal
db2cmd /c /w /i db2 uncatalog database xx_pdb2c
db2cmd /c /w /i db2 uncatalog node n0bdbc
db2cmd /c /w /i db2 uncatalog database xx_pdb2b
db2cmd /c /w /i db2 uncatalog node n0bdbb
db2cmd /c /w /i db2 uncatalog database srs_u3d1
db2cmd /c /w /i db2 uncatalog node srs_u3
db2cmd /c /w /i db2 uncatalog database mrl_p1d2
db2cmd /c /w /i db2 uncatalog node mrl_p1
db2cmd /c /w /i db2 uncatalog database ezcsipd1
db2cmd /c /w /i db2 uncatalog node ezcsip
db2cmd /c /w /i db2 uncatalog database srplp2d1
db2cmd /c /w /i db2 uncatalog node srplp2
endlocal