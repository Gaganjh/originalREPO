@echo off
echo.
echo GenerateBindings v1.1  (15MAY2013)
setlocal
set directives=JMSdirectives.txt
set qexpiry=120001
set CCSID=819
set host=mqdevhub.manulife.com
set port=garbage
set channel=SYSTEM.ADMIN.SVRCONN
set clientmode=Y
set cicsregion=%1

if /i "%cicsregion%"=="d0cics" (
	set requestq=MLI.Q.LP.ESB.PRICING.EVENT
	set port=1414
	set channel=CLI.RPS.D01
	set qmanager=AIX1DEV1.WAT.MLICDN
)else if /i "%cicsregion%"=="d1cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V1
	set port=1414
	set channel=CLI.RPS.D01
	set qmanager=AIX1DEV1.WAT.MLICDN
)else if /i "%cicsregion%"=="d2cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V2
	set port=1414
	set channel=CLI.RPS.D01
	set qmanager=AIX1DEV1.WAT.MLICDN
)else if /i "%cicsregion%"=="d3cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V3
	set port=1414
	set channel=CLI.RPS.D01
	set qmanager=AIX1DEV1.WAT.MLICDN
)else if /i "%cicsregion%"=="d7cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V7
	set port=1414
	set channel=CLI.RPS.D01
	set qmanager=AIX1DEV1.WAT.MLICDN
)else if /i "%cicsregion%"=="d8cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V8
	set port=1414
	set channel=CLI.RPS.D01
	set qmanager=AIX1DEV1.WAT.MLICDN
)else if /i "%cicsregion%"=="d9cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V9
	set port=1414
	set channel=CLI.RPS.D01
	set qmanager=AIX1DEV1.WAT.MLICDN
)else if /i "%cicsregion%"=="i0cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT
	set port=1412
	set channel=CLI.RPS.I01
	set qmanager=MLIQICAON00
)else if /i "%cicsregion%"=="i1cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V1
	set port=1412
	set channel=CLI.RPS.I01
	set qmanager=MLIQICAON00
)else if /i "%cicsregion%"=="i2cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V2
	set port=1412
	set channel=CLI.RPS.I01
	set qmanager=MLIQICAON00
)else if /i "%cicsregion%"=="i3cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V3
	set port=1412
	set channel=CLI.RPS.I01
	set qmanager=MLIQICAON00
)else if /i "%cicsregion%"=="i7cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V7
	set port=1412
	set channel=CLI.RPS.I01
	set qmanager=MLIQICAON00
)else if /i "%cicsregion%"=="i8cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V8
	set port=1412
	set channel=CLI.RPS.I01
	set qmanager=MLIQICAON00
)else if /i "%cicsregion%"=="i9cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V9
	set port=1412
	set channel=CLI.RPS.I01
	set qmanager=MLIQICAON00
)else if /i "%cicsregion%"=="t1cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V1
	set port=1415
	set channel=CLI.RPS.T01
	set qmanager=AIX1TST1.WAT.MLICDN
)else if /i "%cicsregion%"=="t2cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V2
	set port=1415
	set channel=CLI.RPS.T01
	set qmanager=AIX1TST1.WAT.MLICDN
)else if /i "%cicsregion%"=="t3cics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V3
	set port=1415
	set channel=CLI.RPS.T01
	set qmanager=AIX1TST1.WAT.MLICDN
)else if /i "%cicsregion%"=="axcics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT
	set port=1416
	set channel=CLI.RPS.A01
	set qmanager=AIX1ACP1.WAT.MLICDN
)else if /i "%cicsregion%"=="aycics" (
	set requestq=WMLI.Q.LP.ESB.PROPOSAL.EVENT.V1
	set port=1416
	set channel=CLI.RPS.A01
	set qmanager=AIX1ACP1.WAT.MLICDN
)else if /i "%cicsregion%"=="fxcics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT
	set port=1411
	set channel=CLI.RPS.F01
	set qmanager=MLIQFCAON00
)else if /i "%cicsregion%"=="trcics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT
	set port=1413
	set channel=CLI.RPS.N01
	set qmanager=MLIQNCAON00
)else if /i "%cicsregion%"=="lpacics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V1
	set port=1413
	set channel=CLI.RPS.N01
	set qmanager=MLIQNCAON00
)else if /i "%cicsregion%"=="lpdcics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V2
	set port=1413
	set channel=CLI.RPS.N01
	set qmanager=MLIQNCAON00
)else if /i "%cicsregion%"=="lpicics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V3
	set port=1413
	set channel=CLI.RPS.N01
	set qmanager=MLIQNCAON00
)else if /i "%cicsregion%"=="lptcics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V4
	set port=1413
	set channel=CLI.RPS.N01
	set qmanager=MLIQNCAON00
)else if /i "%cicsregion%"=="lpucics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V5
	set port=1413
	set channel=CLI.RPS.N01
	set qmanager=MLIQNCAON00
)else if /i "%cicsregion%"=="lpxcics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V6
	set port=1413
	set channel=CLI.RPS.N01
	set qmanager=MLIQNCAON00
)else if /i "%cicsregion%"=="dcics" (
	set requestq=MLI.Q.LP.ESB.PROPOSAL.EVENT.V7
	set port=1413
	set channel=CLI.RPS.N01
	set qmanager=MLIQNCAON00
)else if /i "%cicsregion%"=="p2cics" (
	set requestq=MLI.Q.LP.ESB.PRICING.EVENT
	set port=1417
	set channel=CLI.RPS.P01
	set qmanager=AIX1PRD1.WAT.MLICDN
	set host=mqprodhub.manulife.com
)else (
	echo.
	echo !!!!! ERROR: Unknown CICS region name
	goto usage
)

echo.
echo About to generate a .bindings file with the following configuration ...
echo.
echo    Queue Manager = %qmanager%
echo    CICS Region   = %cicsregion%
echo    Client Mode   = %clientmode%
if "%clientmode%"=="Y" (
	echo    Hostname      = %host%
	echo    Port          = %port%
	echo    Channel       = %channel%
)
echo    Request Queue = %requestq%
set commonhome=D:\CVS\common
set classpath=%commonhome%\weblib\fscontext.jar
set classpath=%classpath%;%commonhome%\weblib\providerutil.jar
set classpath=%classpath%;%commonhome%\lib\com.ibm.mq.jar
set classpath=%classpath%;%commonhome%\lib\com.ibm.mqjms.jar
set classpath=%classpath%;%commonhome%\lib\jms.jar
set classpath=%classpath%;%commonhome%\lib\connector.jar
echo DELETE Q(requestQueue)     >%directives%
echo DELETE QCF(javax.jms.QueueConnectionFactory) >>%directives%
if %clientmode%==Y echo DEFINE QCF(javax.jms.QueueConnectionFactory) QMANAGER(%qmanager%) TRANSPORT(CLIENT) HOSTNAME(%host%) PORT(%port%) CHANNEL(%channel%) CCSID(%CCSID%) USECONNPOOLING(YES) >>%directives%
if %clientmode%==N echo DEFINE QCF(javax.jms.QueueConnectionFactory) QMANAGER(%qmanager%) USECONNPOOLING(YES) >>%directives%
echo DEFINE Q(requestQueue) QUEUE(%requestq%) QMANAGER(%qmanager%) CCSID(%CCSID%) PERSISTENCE(NON) EXPIRY(%qexpiry%) ENCODING(NATIVE) PRIORITY(APP) >>%directives%
echo END >>%directives%
java com.ibm.mq.jms.admin.JMSAdmin -cfg .\JMSAdmin.config <%directives%
goto end
:usage
echo.
echo This batch file will generate a JMS ".bindings" file with all of the
echo appropriate settings for using MQ Series to communicate with the specified
echo Apollo CICS region from the specified MQ Queue Manager.
echo.
echo Usage:  GenerateBindings_pricing CicsRegion
echo where
echo         CicsRegion is then name of the CICS region you are communicating with

:end
endlocal
