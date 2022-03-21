SET HOME=d:\apps\plansponsor\AutomatedReports
cd %HOME%
set CLASSPATH=%CLASSPATH%;D:\webSphere9\AppServer\runtimes\com.ibm.ws.admin.client_8.0.0.jar;D:\WebSphere9\AppServer\java\8.0\jre\lib\VM.jar;D:\webSphere9\AppServer\profiles\PlanSponsor\installedApps\MLIW3S6Z2B1Node02Cell\PlanSponsor.ear\commons-lang-2.0.jar;D:\webSphere9\AppServer\profiles\PlanSponsor\installedApps\MLIW3S6Z2B1Node02Cell\PlanSponsor.ear\USGPSUtilities.jar;D:\webSphere9\AppServer\profiles\PlanSponsor\installedApps\MLIW3S6Z2B1Node02Cell\PlanSponsor.ear\PlanSponsorEJB.jar;PlanSponsorAdministration.jar
echo %classpath%

if EXIST *.csv del *.csv

D:\WebSphere9\AppServer\java\8.0\jre\bin\java.exe com.manulife.pension.ps.administration.SearchInternalUsers
if NOT ERRORLEVEL 0 exit %ERRORLEVEL%

REM email attachments 
call emailInternalUsersReport.bat 

exit %ERRORLEVEL%