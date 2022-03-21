@echo off

set SJ_APP_HOME=c:\apps\SubJournal
set GFT_APP_HOME=c:\apps\GFT
set MRL_APP_HOME=c:\apps\mrl

set ORIGINAL_CLASSPATH=%CLASSPATH%
set CLASSPATH=%CLASSPATH%;%MRL_APP_HOME%\lib\mrl1.5.7.jar
set CLASSPATH=%CLASSPATH%;%SJ_APP_HOME%\lib\ngec_model.jar
set CLASSPATH=%CLASSPATH%;%SJ_APP_HOME%\lib\dummy-lp-log.jar
set CLASSPATH=%CLASSPATH%;%SJ_APP_HOME%\lib\sub-journal-service.jar
set CLASSPATH=%CLASSPATH%;%GFT_APP_HOME%\lib\gft-client.jar
set CLASSPATH=%CLASSPATH%;%GFT_APP_HOME%\lib\gft-service.jar
set CLASSPATH=%CLASSPATH%;%GFT_APP_HOME%\lib\gft-tests.jar
set CLASSPATH=%CLASSPATH%;%GFT_APP_HOME%\lib\SubJournal_Client.jar

start "RMI Registry" rmiregistry

sleep 5

start "Submission Journal Service" java -Djava.security.policy=file:///c:/apps/SubJournal/properties/subJournalSystem.policy -Djava.rmi.server.hostname=localhost com.manulife.pension.lp.bos.ais.submission.journal.SubmissionJournalAISSystem

sleep 10

start "GFT Upload Service" java -Xrs -Djava.security.policy=file:///c:/apps/gft/properties/uploadServiceSystem.policy -Djava.rmi.server.codebase=file:///c:/apps/gft/lib/gft-service.jar -Djava.rmi.server.hostname=localhost com.manulife.pension.lp.bos.gft.uploadservice.UploadServiceSystem

set CLASSPATH=%ORIGINAL_CLASSPATH%
