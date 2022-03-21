SET HOME=C:\cvs\plansponsor\PlanSponsorAdministration\AutomatedReports
cd %HOME%
set CLASSPATH=%CLASSPATH%;..\..\..\common\lib\PlanSponsorAdministration.jar
echo %classpath%
call Wk_St_PS_csv_reports.bat > weeklyReport_output.txt