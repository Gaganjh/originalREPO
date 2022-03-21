To run the automated weekly report, you will need a machine that has 'csvde' and DB2 client installed. Usually any Windows 2000 and Windows XP Professional will have the 'csvde' utility installed (note: for Windows XP you may have to copy the 'csvde.exe' in your SYSTEM32 folder).

The reports are using live (LDAP and CSDB) data. You may encounter problems if the machine you are running this report from does not have access to production LDAP and CSDB.

Once all these requirements are fulfilled, all you have to do is type 'runWeeklyReport.bat'.