:: Used to map the Aspose license file shared folder
:: This mapping is done only for developers system and for Test regions
:: It should never execute in Regression and Production environments

@echo off

set _computer_name=%COMPUTERNAME%
set first_four=%_computer_name:~0,4%
set first_nine=%_computer_name:~0,9%


:: Developer PCs
if "%first_four%"     == "MLIW"  goto map_shared_drive
if "%first_four%"     == "VMEX"  goto map_shared_drive

:: Test servers
if "%first_nine%" == "JHRPSTEST" goto map_shared_drive

:: If COMPUTERNAME is not one of the above, don't do any drive mappings.
goto end

:map_shared_drive
    :: \\jhrpstest25\licenses - Provide the username and password for access to this drive
	net use Y: /delete	
    cmd.exe /c net use Y: \\jhrpstest25.americas.manulife.net\licenses /user:jhrpstest25\sharedro Sharedr0$
    goto end

:donot_map
    :: don't map any drive
    goto end

:end