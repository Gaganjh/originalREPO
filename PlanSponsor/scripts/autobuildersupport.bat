@echo off

REM need to stop the services

setlocal
set call=%1
set timestamp=%date:~10,4%-%date:~4,2%-%date:~7,2%_%time:~0,2%-%time:~3,2%-%time:~6,2%
set timestamp=%timestamp: =%
if not exist c:\apps\archive mkdir c:\apps\archive
if not exist \apps\archive mkdir \apps\archive
if not exist \db\archive mkdir \db\archive

if %call%==common_directories goto common_directories
if %call%==stp_deploy_dirs goto stp_deploy_dirs
if %call%==gft_deploy_dirs goto gft_deploy_dirs
if %call%==sj_deploy_dirs goto sj_deploy_dirs
if %call%==al_deploy_dirs goto al_deploy_dirs
if %call%==er_deploy_dirs goto er_deploy_dirs
endlocal
exit

:common_directories
if exist c:\apps\manulife move c:\apps\manulife c:\apps\archive\manulife%timestamp% 
sleep 2
if exist \apps\manulife move \apps\manulife \apps\archive\manulife%timestamp%
if exist \apps\mrl move \apps\mrl \apps\archive\mrl%timestamp%
if exist \db\mrl move \db\mrl \db\archive\mrl%timestamp%
if not exist \db\mrl\logger mkdir \db\mrl\logger
if not exist \db\mrl\logs mkdir \db\mrl\logs
endlocal
exit

:al_deploy_dirs
if exist \apps\Autoloader move \apps\Autoloader \apps\archive\Autoloader%timestamp% 
if exist \db\Autoloader move \db\Autoloader \db\archive\Autoloader%timestamp%
endlocal
exit

:stp_deploy_dirs
if exist \apps\stp move \apps\stp \apps\archive\stp%timestamp% 
if exist \db\stp move \db\stp \db\archive\stp%timestamp%
endlocal
exit

:gft_deploy_dirs
if exist \apps\gft move \apps\gft \apps\archive\gft%timestamp% 
if exist \db\gft move \db\gft \db\archive\gft%timestamp%
endlocal
exit

:sj_deploy_dirs
if exist \apps\SubJournal move \apps\SubJournal \apps\archive\SubJournal%timestamp%
if exist \db\SubJournal move \db\SubJournal \db\archive\SubJournal%timestamp% 
endlocal
exit

:er_deploy_dirs
if exist \apps\ereports move \apps\ereports \apps\archive\ereports%timestamp% 
if exist \apps\d2e move \apps\d2e \apps\archive\d2e%timestamp%
if exist \db\ereports move \db\ereports \db\archive\ereports%timestamp% 
endlocal
exit
