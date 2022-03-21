SET HOME=C:\cvstpa\plansponsor\PlanSponsorAdministration\AutomatedReports
cd %HOME%
set CLASSPATH=%CLASSPATH%;D:\WebSphere\AppServer\lib\websphere.jar;D:\WebSphere\AppServer\java\jre\lib\rt.jar;D:\websphere\appserver\installedApps\PlanSponsor.ear\commons-lang-2.0.jar;D:\websphere\appserver\installedApps\PlanSponsor.ear\USGPSUtilities.jar;D:\websphere\appserver\installedApps\PlanSponsor.ear\PlanSponsorEJB.jar;PlanSponsorAdministration.jar
echo %classpath%

if EXIST *.csv del *.csv

C:\WebSphere\AppServer\java\jre\bin\java.exe com.manulife.pension.ps.administration.SearchInternalUsers
if NOT ERRORLEVEL 0 exit %ERRORLEVEL%

REM email attachments 
call emailInternalUsersReport.bat 

exit %ERRORLEVEL%