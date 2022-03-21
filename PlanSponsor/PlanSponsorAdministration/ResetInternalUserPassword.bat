@echo off
setlocal
call prodenv.bat
rem echo CLASSPATH=%classpath%
java com.manulife.pension.ps.administration.ResetInternalUserPassword %1 %2 %3 %4 %5 
endlocal
