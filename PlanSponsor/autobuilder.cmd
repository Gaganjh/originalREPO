@echo off
	 
REM<<<<<<<<< Identify the current drive >>>>>>>>>
	set CurrentDir="%cd%"
	for /F "delims=:" %%a in ( 'echo %cd%') do set Currentdrive="%%a"
	set Currentdrive=%Currentdrive:~1,-1%

		
REM<<<<<<<<< Autobuilder.xml call >>>>>>>>>

	REM db2cmd /c /w /i ant -f autobuilder.xml -Dbuild_target="combined_deploy_to_presit8"

	db2cmd /c /w /i ant -f autobuilder.xml -Dbuild_target="combined_deploy"	
	
	REM<< Ant call for USGPPRESIT4 - CVS checkout, Build & JUNIT Tasks >> 

	REM db2cmd /c /w /i ant -f autobuilder.xml usgppresit4_build


REM<<<<<<<<< Mail Notification Process in the case of Failure >>>>>>>>>

	REM<< Identify the sendemail.exe, Mail.properties, current build file location >>
	set log_loc=%Currentdrive%:\Buildlogs\index.html
	set mailexe_loc=%Currentdrive%:\cvs\PlanSponsor\scripts\sendemail.exe
	set property_loc=%Currentdrive%:\cvs\PlanSponsor\scripts\mail.properties

	REM<< Check for Build status >>
	REM FINDSTR /C:"BUILD SUCCESSFUL" %log_loc%
	FINDSTR /C:"BUILD FAILED" %log_loc%

	IF %ERRORLEVEL% neq 1 goto sendemail
	 echo Build Success
	 goto Nextstep

REM<<<<<<<<< Email Notification >>>>>>>>>
:sendemail

	echo Sending Mail for Build Failure

	REM << Get the entire line which matches with search string into variable >>

	for /F "delims=" %%a in (
	'findstr /c:"CVS tag" %log_loc%'
	 ) do set line="%%a"

	REM << Substring the Build CVS tag name >>

	set line=%line:~33,-11%

	REM << Read 'FROM, To & CC addresses from mail.properties file >>

	for /f "delims==" %%a in (%property_loc%) do (
	if %%a==TO for /f "tokens=2 delims==" %%b in ('findstr /c:"TO=" %property_loc%') do set TO="%%b"
	if %%a==CC for /f "tokens=2 delims==" %%b in ('findstr /c:"CC=" %property_loc%') do set CC="%%b"
	if %%a==FROM for /f "tokens=2 delims==" %%b in ('findstr /c:"FROM=" %property_loc%') do set FROM="%%b"
	)

	set TO=%TO:~1,-1%
	set CC=%CC:~1,-1%
	set FROM=%FROM:~1,-1%

	set Buildlog_loc="%Currentdrive%:\Buildlogs\%line%.html"

	REM -a   "Attachment Files path", -cc  cc address, -f   From address, -t   To address, -o   Message-file="File path", -m   Message, -l   "File path", -u   Subject, -s   SMTP Server

	%mailexe_loc% -f %FROM% -t %TO% -cc %CC% -s usgppresit12 -u Build Failure Notice-CVS Tag:%line% -m Build Failed. Please find the attachment for more detail. -a %Buildlog_loc%
	
	REM Shoule exit here. Because build failed
	
:Nextstep
REM<<<<<<<<< Diff report update >>>>>>>>>

	for /f %%a in ('%Currentdrive%:\cvs\PlanSponsor\scripts\doff.exe yyyy-mm-dd ') do (set D1=%%a)
	for /f %%a in ('%Currentdrive%:\cvs\PlanSponsor\scripts\doff.exe yyyy-mm-dd -1') do (set D2=%%a)
	for /f %%a in ('%Currentdrive%:\cvs\PlanSponsor\scripts\doff.exe yyyy-mm-dd_hh-mi-ss') do (set D3=%%a)
	cd "%Currentdrive%:\cvs\"
	cmd.exe /X /C cvs diff -D %D1% -D %D2% > "%Currentdrive%:\Buildlogs\diffreport_%D3%.txt"
	cd %CurrentDir%
	exit
