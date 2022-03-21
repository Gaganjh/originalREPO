@echo off
rem call fileArchiver.xml and the genericFileCleanup.xml

rem Name of the output log file
set LOG_FILE=GenericFileCleanup.log

rem Path in which log file resides
set LOG_DIR=d:\appLogs\manulife\logs

rem path in which the GenericFileCleanup.log file is Archived
set ARCHIVE_DIR=d:\appLogs\manulife\logs\archive	

:END
	rem Archive the previous GenericFileCleanup.log file
	call %ANT_HOME%\bin\ant -f D:\apps\manulife\scripts\fileArchiver.xml -DFILE=%LOG_FILE% -DDIR=%LOG_DIR% -DARCHIVEDIR=%ARCHIVE_DIR%

	rem Perform GenericFileCleanup and outputs the log to GenericFileCleanup.log file 
	call %ANT_HOME%\bin\ant -f D:\apps\manulife\scripts\GenericFileCleanup.xml -DARCHIVE_DIR_PATH=%ARCHIVE_DIR% %COMPUTERNAME% >> %LOG_DIR%\%LOG_FILE%
