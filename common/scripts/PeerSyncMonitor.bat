@echo off

if "%1" == "TEST" goto useTEST
if "%1" == "PROD" goto usePROD

rem Error message when syntax is not given properly
echo "Syntax : PeerSync.bat TEST/PROD
Goto End

:useTEST
set LOG_FILE="\\USGPSPRESIT17\db\STP\data\SystemData\PeerSync_monitor.txt"
goto Create_File

:usePROD
set LOG_FILE="\\MLISUSGPPROD2\stp\SystemData\PeerSync_monitor.txt"
goto Create_File

rem Creation of the file PeerSync_monitor.txt with Timestamp
:Create_File
DEL %LOG_FILE%
for /f "tokens=1-5 delims=/ " %%a in ("%date%") do set date_str=%%b/%%c/%%d
for /f "tokens=1-5 delims=:." %%a in ("%time%") do set time_str=%%a:%%b:%%c
echo Date: %date_str% >>%LOG_FILE%
echo Time: %time_str% >>%LOG_FILE%

:End


