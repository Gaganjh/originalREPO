@echo off

title TED File Operations

rem args:
rem     1. flag 
rem 	   COPY -copy to Web server
rem 	   DELETE   -Delete from Web server
rem   	   NA   -copy without mapping to a server
rem	2. drive letter 
rem	3. server name
rem	4. source path
rem	5. destination path
rem	6. login user to server
rem	7. login password to server

set operation_flag=%1
set drive_letter=%2
set login_user=%3
set login_password=%4
set server_name=%5
set source_path=%6
set destination_path=%7

if "%operation_flag%" == "MAP"       goto drive_map
if "%operation_flag%" == "DRIVEDEL"  goto delete_drive
if "%operation_flag%" == "DIR"  goto dir_list
if "%operation_flag%" == "GET"  goto get_bin_file
goto End

:drive_map
cmd.exe /c net use %drive_letter%: /delete
cmd.exe /c net use %drive_letter%: \\%server_name%\Manumerge\BU  /user:%login_user% %login_password%
goto  End

:dir_list
dir %drive_letter%:%source_path% *.zip
goto  End

:get_bin_file
type %drive_letter%:%source_path%
goto  End

:delete_drive
cmd.exe /c net use %drive_letter%: /delete
goto  End

:End
