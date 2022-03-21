@echo off
setlocal
db2cmd /c /w /i db2 uncatalog database xx_ddb2c
db2cmd /c /w /i db2 uncatalog node n0bdbc
db2cmd /c /w /i db2 uncatalog node mlisdbdv
db2cmd /c /w /i db2 uncatalog database ezcsdb
db2cmd /c /w /i db2 uncatalog node ezcsiu
db2cmd /c /w /i db2 uncatalog database ezvfdb
db2cmd /c /w /i db2 uncatalog node izvfiu
db2cmd /c /w /i db2 uncatalog database mrl_ezk
db2cmd /c /w /i db2 uncatalog node mrlezk
db2cmd /c /w /i db2 uncatalog database srplu2d1
db2cmd /c /w /i db2 uncatalog node ezcsu2
db2cmd /c /w /i db2 uncatalog database vgncnt
db2cmd /c /w /i db2 uncatalog node ezvsiu
db2cmd /c /w /i db2 uncatalog database srstu1d1
db2cmd /c /w /i db2 uncatalog node srstu1
endlocal