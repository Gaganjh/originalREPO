@echo off

set STP_APP_HOME=c:\apps\stp
set MRL_APP_HOME=c:\apps\mrl
set STP_LOGS_HOME=c:\Db\stp\SystemData\logs
set STPRUNTIMEPARMS_PROPERTIES=STPRuntimeParms_Presit.properties

set ORIGINAL_CLASSPATH=%CLASSPATH%
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\STPDataChecker.jar
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\STPFileProcessor.jar
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\STPUtil.jar
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\db2jcc.jar
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\db2jcc_license_cu.jar
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\jakarta-regexp-1.2.jar
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\dj750ec.jar
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\activation.jar
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\mail.jar
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\xerces.jar
set CLASSPATH=%CLASSPATH%;%MRL_APP_HOME%\lib\mrl1.5.7.jar
set CLASSPATH=%CLASSPATH%;%STP_APP_HOME%\lib\PSUtils.jar

start "STP File Processor" java -Xrs -Djava.security.policy=%STP_APP_HOME%\properties\STP.policy -Djava.rmi.server.codebase=file:///%STP_APP_HOME%\lib\STPFileProcessor.jar -Djava.rmi.server.hostname=localhost -Dfileproperty.filename=%STP_APP_HOME%\properties\%STPRUNTIMEPARMS_PROPERTIES% -Dmonitor.filename=%STP_LOGS_HOME%\Monitor_FileProcessor.log com.manulife.pension.stp.processor.STPFileProcessorAISSystem
start "STP Data Checker" java -Xrs -Djava.security.policy=%STP_APP_HOME%\properties\STP.policy -Djava.rmi.server.codebase=file:///%STP_APP_HOME%\lib\STPDataChecker.jar -Djava.rmi.server.hostname=localhost -Dfileproperty.filename=%STP_APP_HOME%\properties\%STPRUNTIMEPARMS_PROPERTIES% -Dmonitor.filename=%STP_LOGS_HOME%\Monitor_DataChecker.log com.manulife.pension.stp.datachecker.STPDataCheckerAISSystem

set CLASSPATH=%ORIGINAL_CLASSPATH%
