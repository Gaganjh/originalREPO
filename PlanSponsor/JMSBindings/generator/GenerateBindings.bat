@echo off
echo.
echo GenerateBindings v1.9  (28May2013)
setlocal
set directives=JMSdirectives.txt
set qexpiry=120001
set CCSID=819
set replyq=MLI.Q.LP.REPLY
set host=mqdevhub.manulife.com
set port=garbage
if exist c:\svn\common\lib\jms.jar (
	set commonhome=c:\svn\common
) else if exist d:\svn\common\lib\jms.jar (
	set commonhome=d:\svn\common
) else (
	echo.
	echo !!!!! ERROR: Can't find \svn\common\lib\jms.jar
	goto end
)
set cicsregion=%1
if /i "%cicsregion%"=="d1cics" (
	set requestq=WEB.LP.REQUEST
	set port=1414
	set channel=CLI.RPS.D01
	set notificationq=MLI.Q.LP.BOS.EVENT.DDEVH1
) else if /i "%cicsregion%"=="d2cics" (
	set requestq=WEB.LP.REQUEST.V2
	set port=1414
	set channel=CLI.RPS.D01
	set notificationq=MLI.Q.LP.BOS.EVENT.DDEVH2
) else if /i "%cicsregion%"=="d3cics" (
	set requestq=WEB.LP.REQUEST.V3
	set port=1414
	set channel=CLI.RPS.D01
	set notificationq=MLI.Q.LP.BOS.EVENT.DDEVH3
) else if /i "%cicsregion%"=="d7cics" (
	set requestq=WEB.LP.REQUEST.V7
	set port=1414
	set channel=CLI.RPS.D01
	set notificationq=MLI.Q.LP.BOS.EVENT.DDEVH7
) else if /i "%cicsregion%"=="d8cics" (
	set requestq=WEB.LP.REQUEST.V8
	set port=1414
	set channel=CLI.RPS.D01
	set notificationq=MLI.Q.LP.BOS.EVENT.DDEVH8
) else if /i "%cicsregion%"=="d0cics" (
	set requestq=WEB.LP.REQUEST.V0
	set port=1414
	set channel=CLI.RPS.D01
	set notificationq=MLI.Q.LP.BOS.EVENT.DDEVH0
) else if /i "%cicsregion%"=="d9cics" (
	set requestq=WEB.LP.REQUEST.V9
	set port=1414
	set channel=CLI.RPS.D01
	set notificationq=MLI.Q.LP.BOS.EVENT.DDEVH9
) else if /i "%cicsregion%"=="i1cics" (
	set requestq=WEB.LP.REQUEST
	set port=1412
	set channel=CLI.RPS.I01
	set notificationq=MLI.Q.LP.BOS.EVENT.IDEVH1
) else if /i "%cicsregion%"=="i2cics" (
	set requestq=WEB.LP.REQUEST.V2
	set port=1412
	set channel=CLI.RPS.I01
	set notificationq=MLI.Q.LP.BOS.EVENT.IDEVH2
) else if /i "%cicsregion%"=="i3cics" (
	set requestq=WEB.LP.REQUEST.V3
	set port=1412
	set channel=CLI.RPS.I01
	set notificationq=MLI.Q.LP.BOS.EVENT.IDEVH3
) else if /i "%cicsregion%"=="i7cics" (
	set requestq=WEB.LP.REQUEST.V7
	set port=1412
	set channel=CLI.RPS.I01
	set notificationq=MLI.Q.LP.BOS.EVENT.IDEVH7
) else if /i "%cicsregion%"=="i8cics" (
	set requestq=WEB.LP.REQUEST.V8
	set port=1412
	set channel=CLI.RPS.I01
	set notificationq=MLI.Q.LP.BOS.EVENT.IDEVH8
) else if /i "%cicsregion%"=="i0cics" (
	set requestq=WEB.LP.REQUEST.V0
	set port=1412
	set channel=CLI.RPS.I01
	set notificationq=MLI.Q.LP.BOS.EVENT.IDEVH0
) else if /i "%cicsregion%"=="i9cics" (
	set requestq=WEB.LP.REQUEST.V9
	set port=1412
	set channel=CLI.RPS.I01
	set notificationq=MLI.Q.LP.BOS.EVENT.IDEVH9
) else if /i "%cicsregion%"=="t1cics" (
	set requestq=WEB.LP.REQUEST
	set port=1415
	set channel=CLI.RPS.T01
	set notificationq=MLI.Q.LP.BOS.EVENT.TDEVH1
) else if /i "%cicsregion%"=="t2cics" (
	set requestq=WEB.LP.REQUEST.V2
	set port=1415
	set channel=CLI.RPS.T01
	set notificationq=MLI.Q.LP.BOS.EVENT.TDEVH2
) else if /i "%cicsregion%"=="t3cics" (
	set requestq=WEB.LP.REQUEST.V3
	set port=1415
	set channel=CLI.RPS.T01
	set notificationq=MLI.Q.LP.BOS.EVENT.TDEVH3
) else if /i "%cicsregion%"=="axcics" (
	set requestq=WEB.LP.REQUEST
	set port=1416
	set channel=CLI.RPS.A01
	set notificationq=MLI.Q.LP.BOS.EVENT.ADEVHX
) else if /i "%cicsregion%"=="aycics" (
	set requestq=WEB.LP.REQUEST.V2
	set port=1416
	set channel=CLI.RPS.A01
	set notificationq=MLI.Q.LP.BOS.EVENT.ADEVHY
) else if /i "%cicsregion%"=="p2cics" (
	set requestq=WEB.LP.REQUEST
	set notificationq=MLI.Q.LP.BOS.EVENT.ALT1
) else if /i "%cicsregion%"=="fxcics" (
	set requestq=WEB.LP.REQUEST
	set port=1411
	set channel=CLI.RPS.F01
	set notificationq=MLI.Q.LP.BOS.EVENT.FDEVH
) else if /i "%cicsregion%"=="trcics" (
	set requestq=WEB.LP.REQUEST
	set port=1413
	set channel=CLI.RPS.N01
	set notificationq=unknown
) else if /i "%cicsregion%"=="lpacics" (
	set requestq=WEB.LP.REQUEST.V2
	set port=1413
	set channel=CLI.RPS.N01
	set notificationq=MLI.Q.LP.BOS.EVENT.NDEVHA
) else if /i "%cicsregion%"=="lpdcics" (
	set requestq=WEB.LP.REQUEST.V3
	set port=1413
	set channel=CLI.RPS.N01
	set notificationq=MLI.Q.LP.BOS.EVENT.NDEVHD
) else if /i "%cicsregion%"=="lpicics" (
	set requestq=WEB.LP.REQUEST.V4
	set port=1413
	set channel=CLI.RPS.N01
	set notificationq=MLI.Q.LP.BOS.EVENT.NDEVHI
) else if /i "%cicsregion%"=="lptcics" (
	set requestq=WEB.LP.REQUEST.V5
	set port=1413
	set channel=CLI.RPS.N01
	set notificationq=MLI.Q.LP.BOS.EVENT.NDEVHT
) else if /i "%cicsregion%"=="lpucics" (
	set requestq=WEB.LP.REQUEST.V6
	set port=1413
	set channel=CLI.RPS.N01
	set notificationq=MLI.Q.LP.BOS.EVENT.NDEVHU
) else if /i "%cicsregion%"=="lpxcics" (
	set requestq=WEB.LP.REQUEST.V7
	set port=1413
	set channel=CLI.RPS.N01
	set notificationq=MLI.Q.LP.BOS.EVENT.NDEVHX
) else if /i "%cicsregion%"=="dcics" (
	set requestq=WEB.LP.REQUEST.V8
	set port=1413
	set channel=CLI.RPS.N01
	set notificationq=MLI.Q.LP.BOS.EVENT.NDEVDC
) else (
	echo.
	echo !!!!! ERROR: Unknown CICS region name
	goto usage
)
set qmanager=%2
set clientmode=N
if "%qmanager%"=="" (
	set clientmode=Y
	if %port%==1413 (
		set qmanager=MLIQNCAON00
	) else if %port%==1412 (
		set qmanager=MLIQICAON00
	) else if %port%==1411 (
		set qmanager=MLIQFCAON00
	) else if %port%==1414 (
		set qmanager=AIX1DEV1.WAT.MLICDN
	) else if %port%==1415 (
		set qmanager=AIX1TST1.WAT.MLICDN
	) else if %port%==1416 (
		set qmanager=AIX1ACP1.WAT.MLICDN
	)
) else if /i "%qmanager%"=="dev" (
	set clientmode=Y
	if %port%==1413 (
		set qmanager=MLIQNCAON00
	) else if %port%==1412 (
		set qmanager=MLIQICAON00
	) else if %port%==1411 (
		set qmanager=MLIQFCAON00
	) else if %port%==1414 (
		set qmanager=AIX1DEV1.WAT.MLICDN
	) else if %port%==1415 (
		set qmanager=AIX1TST1.WAT.MLICDN
	) else if %port%==1416 (
		set qmanager=AIX1ACP1.WAT.MLICDN
	)
) else if /i "%qmanager%"=="presit" (
	set clientmode=Y
	if %port%==1413 (
		set qmanager=MLIQNCAON00
	) else if %port%==1412 (
		set qmanager=MLIQICAON00
	) else if %port%==1411 (
		set qmanager=MLIQFCAON00
	) else if %port%==1414 (
		set qmanager=AIX1DEV1.WAT.MLICDN
	) else if %port%==1415 (
		set qmanager=AIX1TST1.WAT.MLICDN
	) else if %port%==1416 (
		set qmanager=AIX1ACP1.WAT.MLICDN
	)
) else if /i "%qmanager%"=="mlisezkscals2" (
	if %port%==1413 (
		set qmanager=MLIQNCAON3F
		set notificationq=MLI.Q.LP.BOS.EVENT.N3F
	) else if %port%==1412 (
		set qmanager=MLIQICAON3F
		set notificationq=MLI.Q.LP.BOS.EVENT.I3F
	) else if %port%==1411 (
		set qmanager=MLIQFCAON3F
		set notificationq=MLI.Q.LP.BOS.EVENT.F3F
	) else if %port%==1414 (
		set qmanager=MLIQDCAON3F
		set notificationq=MLI.Q.LP.BOS.EVENT.D3F
	) else if %port%==1415 (
		set qmanager=MLIQTCAON3F
		set notificationq=MLI.Q.LP.BOS.EVENT.T3F
	) else if %port%==1416 (
		set qmanager=MLIQACAON3F
		set notificationq=MLI.Q.LP.BOS.EVENT.A3F
	)
) else if /i "%qmanager%"=="mlisezkscals3" (
	if %port%==1413 (
		set qmanager=MLIQNCAON51
		set notificationq=MLI.Q.LP.BOS.EVENT.N22
	) else if %port%==1412 (
		set qmanager=MLIQICAON51
		set notificationq=MLI.Q.LP.BOS.EVENT.I22
	) else if %port%==1411 (
		set qmanager=MLIQFCAON51
		set notificationq=MLI.Q.LP.BOS.EVENT.F22
	) else if %port%==1414 (
		set qmanager=MLIQDCAON51
		set notificationq=MLI.Q.LP.BOS.EVENT.D22
	) else if %port%==1415 (
		set qmanager=MLIQTCAON51
		set notificationq=MLI.Q.LP.BOS.EVENT.T22
	) else if %port%==1416 (
		set qmanager=MLIQACAON51
		set notificationq=MLI.Q.LP.BOS.EVENT.A22
	)
) else if /i "%qmanager%"=="mlisezksas2" (
	if %port%==1413 (
		set qmanager=MLIQNCAON3E
	) else if %port%==1412 (
		set qmanager=MLIQICAON3E
	) else if %port%==1411 (
		set qmanager=MLIQFCAON3E
	) else if %port%==1414 (
		set qmanager=MLIQDCAON3E
	) else if %port%==1415 (
		set qmanager=MLIQTCAON3E
	) else if %port%==1416 (
		set qmanager=MLIQACAON3E
	)
) else if /i "%qmanager%"=="mlisezksas3" (
	if %port%==1413 (
		set qmanager=MLIQNCAON50
	) else if %port%==1412 (
		set qmanager=MLIQICAON50
	) else if %port%==1411 (
		set qmanager=MLIQFCAON50
	) else if %port%==1414 (
		set qmanager=MLIQDCAON50
	) else if %port%==1415 (
		set qmanager=MLIQTCAON50
	) else if %port%==1416 (
		set qmanager=MLIQACAON50
	)
) else if /i "%qmanager%"=="mlisezkucals3" (
	if %port%==1413 (
		set qmanager=MLIQNCAON40
		set notificationq=MLI.Q.LP.BOS.EVENT.N21
	) else if %port%==1412 (
		set qmanager=MLIQICAON40
		set notificationq=MLI.Q.LP.BOS.EVENT.I21
	) else if %port%==1411 (
		set qmanager=MLIQFCAON40
		set notificationq=MLI.Q.LP.BOS.EVENT.F21
	) else if %port%==1414 (
		set qmanager=MLIQDCAON40
		set notificationq=MLI.Q.LP.BOS.EVENT.D21
	) else if %port%==1415 (
		set qmanager=MLIQTCAON40
		set notificationq=MLI.Q.LP.BOS.EVENT.T21
	) else if %port%==1416 (
		set qmanager=MLIQACAON40
		set notificationq=MLI.Q.LP.BOS.EVENT.A21
	)
) else if /i "%qmanager%"=="mlisezkucals4" (
	if %port%==1413 (
		set qmanager=MLIQNCAON4G
		set notificationq=MLI.Q.LP.BOS.EVENT.N3H
	) else if %port%==1412 (
		set qmanager=MLIQICAON4G
		set notificationq=MLI.Q.LP.BOS.EVENT.I3H
	) else if %port%==1411 (
		set qmanager=MLIQFCAON4G
		set notificationq=MLI.Q.LP.BOS.EVENT.F3H
	) else if %port%==1414 (
		set qmanager=MLIQDCAON4G
		set notificationq=MLI.Q.LP.BOS.EVENT.D3H
	) else if %port%==1415 (
		set qmanager=MLIQTCAON4G
		set notificationq=MLI.Q.LP.BOS.EVENT.T3H
	) else if %port%==1416 (
		set qmanager=MLIQACAON4G
		set notificationq=MLI.Q.LP.BOS.EVENT.A3H
	)
) else if /i "%qmanager%"=="mlisezkuas3" (
	if %port%==1413 (
		set qmanager=MLIQNCAON3Z
	) else if %port%==1412 (
		set qmanager=MLIQICAON3Z
	) else if %port%==1411 (
		set qmanager=MLIQFCAON3Z
	) else if %port%==1414 (
		set qmanager=MLIQDCAON3Z
	) else if %port%==1415 (
		set qmanager=MLIQTCAON3Z
	) else if %port%==1416 (
		set qmanager=MLIQACAON3Z
	)
) else if /i "%qmanager%"=="mlisezkuas4" (
	if %port%==1413 (
		set qmanager=MLIQNCAON4F
	) else if %port%==1412 (
		set qmanager=MLIQICAON4F
	) else if %port%==1411 (
		set qmanager=MLIQFCAON4F
	) else if %port%==1414 (
		set qmanager=MLIQDCAON4F
	) else if %port%==1415 (
		set qmanager=MLIQTCAON4F
	) else if %port%==1416 (
		set qmanager=MLIQACAON4F
	)
) else if /i "%qmanager%"=="mlisezkpas9" (
	if /i "%cicsregion%"=="p2cics" (
		set qmanager=MLIQPCAON20
	) else (
		echo.
		echo !!!!! ERROR: %qmanager% can't be configured to talk to %cicsregion%
		goto end
	)
) else if /i "%qmanager%"=="mlisezkpas10" (
	if /i "%cicsregion%"=="p2cics" (
		set qmanager=MLIQPCAON21
	) else (
		echo.
		echo !!!!! ERROR: %qmanager% can't be configured to talk to %cicsregion%
		goto end
	)
) else if /i "%qmanager%"=="mlisezkpiws3" (
	if /i "%cicsregion%"=="p2cics" (
		set qmanager=MLIQPCAON17
	) else (
		echo.
		echo !!!!! ERROR: %qmanager% can't be configured to talk to %cicsregion%
		goto end
	)
) else (
	echo.
	echo. !!!!! ERROR: I don't know anything about queue manager '%qmanager%'.
	goto end
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
echo    Reply Queue   = %replyq%
echo    NotificationQ = %notificationq%
echo.
set classpath=%commonhome%\weblib\fscontext.jar
set classpath=%classpath%;%commonhome%\weblib\providerutil.jar
set classpath=%classpath%;%commonhome%\lib\com.ibm.mq.jar
set classpath=%classpath%;%commonhome%\lib\com.ibm.mqjms.jar
set classpath=%classpath%;%commonhome%\lib\jms.jar
set classpath=%classpath%;%commonhome%\lib\connector.jar
echo DELETE Q(notificationQueue) >%directives%
echo DELETE Q(responseQueue)    >>%directives%
echo DELETE Q(requestQueue)     >>%directives%
echo DELETE QCF(javax.jms.QueueConnectionFactory) >>%directives%
if %clientmode%==Y echo DEFINE QCF(javax.jms.QueueConnectionFactory) QMANAGER(%qmanager%) TRANSPORT(CLIENT) HOSTNAME(%host%) PORT(%port%) CHANNEL(%channel%) CCSID(%CCSID%) USECONNPOOLING(YES) >>%directives%
if %clientmode%==N echo DEFINE QCF(javax.jms.QueueConnectionFactory) QMANAGER(%qmanager%) USECONNPOOLING(YES) >>%directives%
echo DEFINE Q(requestQueue) QUEUE(%requestq%) QMANAGER(%qmanager%) CCSID(%CCSID%) PERSISTENCE(NON) TARGCLIENT(MQ) EXPIRY(%qexpiry%) ENCODING(NATIVE) PRIORITY(APP) >>%directives%
echo DEFINE Q(responseQueue) QUEUE(%replyq%) QMANAGER(%qmanager%) CCSID(%CCSID%) PERSISTENCE(NON) TARGCLIENT(MQ) EXPIRY(%qexpiry%) ENCODING(NATIVE) PRIORITY(APP) >>%directives%
echo DEFINE Q(notificationQueue) QUEUE(%notificationq%) QMANAGER(%qmanager%) CCSID(%CCSID%) PERSISTENCE(NON) TARGCLIENT(MQ) EXPIRY(%qexpiry%) ENCODING(NATIVE) PRIORITY(APP) >>%directives%
echo END >>%directives%
java com.ibm.mq.jms.admin.JMSAdmin -cfg .\JMSAdmin.config <%directives%
goto end
:usage
echo.
echo This batch file will generate a JMS ".bindings" file with all of the
echo appropriate settings for using MQ Series to communicate with the specified
echo Apollo CICS region from the specified MQ Queue Manager.
echo.
echo Usage:  GenerateBindings  CicsRegion  [QueueManager]
echo where
echo         CicsRegion is then name of the CICS region you are communicating with
echo         QueueManager is the name of the queue manager you are using
echo                      (if not specified, it defaults to the test MQ Hub)
echo                      you can also specify the following convenient shortforms
echo                           dev    presit   mlisezkscals4   mlisezksas4
echo                           mlisezkscals2   mlisezksas2     mlisezkscals3
echo                           mlisezksas3     mlisezkucals4   mlisezkuas4
echo                           mlisezkucals3   mlisezkuas3
echo                           mlisezkpiws3    mlisezkpas9     mlisezkpas10
:end
endlocal
