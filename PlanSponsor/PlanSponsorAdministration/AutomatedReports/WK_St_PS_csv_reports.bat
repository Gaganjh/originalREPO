@echo off


echo %classpath%
if EXIST *.csv del *.csv

java com.manulife.pension.ps.administration.RegisteredUsersReport
if NOT ERRORLEVEL 0 exit %ERRORLEVEL%
java com.manulife.pension.ps.administration.DeletedUsersReport
if NOT ERRORLEVEL 0 exit %ERRORLEVEL%


REM email attachments 
call email.bat 

exit %ERRORLEVEL%