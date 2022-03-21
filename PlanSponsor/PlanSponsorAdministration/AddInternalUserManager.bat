@echo off
setlocal
call prodenv.bat 
rem echo CLASSPATH=%classpath%
java com.manulife.pension.ps.administration.AddInternalUserManager %1 %2 %3 %4 %5 %6 %7 %8 %9
endlocal
