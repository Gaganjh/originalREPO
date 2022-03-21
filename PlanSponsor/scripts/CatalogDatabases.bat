@echo off

setlocal

set dbserver=%1
set dbregion=%2
set dbname=%3
set bothlocalhost=%4


if %bothlocalhost%==true goto cataloglocalhost
goto catalog

:cataloglocalhost
echo catalogging database %dbname% from Localhost
REM<< Uncatalog Database>>
db2cmd /c /w /i db2 uncatalog database %dbname%
REM<< Catalog Database>>
db2cmd /c /w /i db2 catalog database %dbname% with 'Local database'
endlocal
exit

:catalog
REM << Verify the Service number>>
REM<< Uncatalog Database>>
db2cmd /c /w /i db2 uncatalog database %dbname%
REM<< Catalog Node & Database>>
db2cmd /c /w /i db2 catalog tcpip node %dbserver% remote %dbserver% server 60080 remote_instance %dbregion% with '%dbregion% and %dbserver%'
db2cmd /c /w /i db2 catalog database %dbname% at node %dbserver% with '%dbname% - %dbregion% and %dbserver%'
endlocal
exit
