@echo off
echo hostname is %COMPUTERNAME%
rem iisweb /createPathSiteName [/bPort] [/iIPAddress] [/dHostHeader] [/dontstart] [/sComputer [/u [Domain\]User/pPassword]]
rem cscript C:\WINDOWS\system32\iisweb.vbs /create D:\Inetpub\ezk "EZK US" /b 80 /d usgpezk4us.test-qa.net /dontstart

cscript C:\WINDOWS\system32\iisweb.vbs /create %3 %1 /b %4 /d %2 /dontstart