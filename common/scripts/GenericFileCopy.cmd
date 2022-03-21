@echo off

title GenericFileCopy utility

rem This utility copies all files from the source path to the destination application path
rem of a mapped drive for the specified file type.

rem Use NA (Not Applicable) for arguments that are not relevant.
rem Use * in the file type to copy all files.

rem args:
rem     1. flag 
rem 	   FROM -copy from server
rem 	   TO   -copy to server
rem   	   NA   -copy without mapping to a server
rem	2. drive letter 
rem	3. server name
rem	4. source path
rem	5. destination path
rem	6. file type 
rem	7. login user to server
rem	8. login password to server

set _computer_name=%COMPUTERNAME%
set first_four=%_computer_name:~0,4%
set first_three=%_computer_name:~0,3%
set first_nine=%_computer_name:~0,9%

if "%first_three%"    == "VMR"           goto start
if "%first_four%"     == "MLIW"          goto start
if "%first_nine%"     == "JHRPSTEST"     goto start

if "%_computer_name%" == "CPCWVJHPWASP03"   goto start
if "%_computer_name%" == "MLISUSGPERP2"   goto start

rem If COMPUTERNAME is not one of the above, don't do anything.
goto End

:start
set from_to_flag=%1
set drive_letter=%2
set server_name=%3
set source_path=%4
set destination_path=%5
set file_type=%6
set login_user=%7
set login_password=%8

echo.
echo COMPUTERNAME=%COMPUTERNAME%
echo.
echo Parameters used:
echo 1. from_to_flag=%from_to_flag%
echo 2. drive_letter=%drive_letter%
echo 3. server_name=%server_name%
echo 4. source_path=%source_path%
echo 5. destination_path=%destination_path%
echo 6. file_type=%file_type%
echo 7. login_user=%login_user%
echo.

if "%from_to_flag%" == "TO"      goto copy_files_to_server
if "%from_to_flag%" == "FROM"    goto copy_files_from_server
if "%from_to_flag%" == "NA"    	 goto copy_files
goto End

:copy_files_to_server
net use %drive_letter%: /delete
net use %drive_letter%:   %server_name%  /user:%login_user% %login_password%
copy %source_path%\*.%file_type%    %drive_letter%:%destination_path% /Y
net use %drive_letter%: /delete
goto  End

:copy_files_from_server
net use %drive_letter%: /delete
net use %drive_letter%:   %server_name%  /user:%login_user% %login_password%
copy %drive_letter%:%source_path%\*.%file_type%  %destination_path% /Y
net use %drive_letter%: /delete
goto  End

:copy_files
copy  %drive_letter%:%source_path%\*.%file_type%    %destination_path% /Y
goto  End

:End

