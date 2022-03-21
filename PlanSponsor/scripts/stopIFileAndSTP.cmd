@echo off

set MRL_APP_HOME=c:\apps\mrl

set ORIGINAL_CLASSPATH=%CLASSPATH%
set CLASSPATH=%CLASSPATH%;%MRL_APP_HOME%\lib\mrl1.5.7.jar

java com.manulife.ais.AISShutdown STP-FP
java com.manulife.ais.AISShutdown STP-DC

java com.manulife.ais.AISShutdown LPGFT
java com.manulife.ais.AISShutdown SubJournal

taskkill /fi "windowtitle eq RMI Registry"

set CLASSPATH=%ORIGINAL_CLASSPATH%
