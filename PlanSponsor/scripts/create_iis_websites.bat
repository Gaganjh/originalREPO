@echo off

if not exist d:\Inetpub\ezk     mkdir d:\Inetpub\ezk
if not exist d:\Inetpub\ezkCMA  mkdir d:\Inetpub\ezkCMA
if not exist d:\Inetpub\plansponsor     mkdir d:\Inetpub\plansponsor
if not exist d:\Inetpub\plansponsorCMA  mkdir d:\Inetpub\plansponsorCMA
if not exist d:\Inetpub\BrokerDealer    mkdir d:\Inetpub\BrokerDealer
if not exist d:\Inetpub\onlineorder     mkdir d:\Inetpub\onlineorder
if not exist d:\Inetpub\ACR     mkdir d:\Inetpub\ACR
if not exist d:\Inetpub\SupportCentral     mkdir d:\Inetpub\SupportCentral

if %computername%==USGPEZK2 set n=2
if %computername%==USGPEZK3 set n=3
if %computername%==USGPPRESIT4 set n=4
if %computername%==USGPPRESIT5 set n=5
if %computername%==USGPPRESIT6 set n=6
if %computername%==USGPPRESIT7 set n=7
if %computername%==USGPPRESIT8 set n=8
if %computername%==USGPPRESIT9 set n=9
if %computername%==USGPPRESIT10 set n=10
if %computername%==USGPPRESIT11 set n=11
if %computername%==USGPPRESIT12 set n=12
if %computername%==USGPPRESIT13 set n=13
if %computername%==USGPPRESIT14 set n=14
if %computername%==USGPPRESIT15 set n=15
if %computername%==USGPPRESIT16 set n=16
if %computername%==USGPPRESIT17 set n=17
if %computername%==USGPPRESIT18 set n=18
if %computername%==USGPPRESIT20 set n=20
if %computername%==USGPPRESIT21 set n=21
if %computername%==USGPPRESIT23 set n=23
if %computername%==USGPTR1 set n=tr

set default_port=80

call create_iis "EZk US" "usgpezk%n%us.test-qa.net" "D:\Inetpub\ezk" %default_port%
call create_iis "EZk NY" "usgpezk%n%ny.test-qa02.net" "D:\Inetpub\ezk" %default_port%
call create_iis "EZk CMA" "usgpezk%n%cma.manulife.com" "D:\Inetpub\ezkcma" %default_port%
call create_iis "PlanSponsor USA" "usgpps%n%us.test-qa.net" "D:\Inetpub\plansponsor" %default_port%
call create_iis "PlanSponsor NY" "usgpps%n%ny.test-qa02.net" "D:\Inetpub\plansponsor" %default_port%
call create_iis "PlanSponsor CMA" "usgpps%n%cma.manulife.com" "D:\Inetpub\plansponsorcma" %default_port%
call create_iis "BrokerDealer" "usgpbd%n%.test-qa.net" "D:\Inetpub\BrokerDealer" %default_port%
call create_iis "Online Order" "usgpoos%n%.manulife.com" "D:\Inetpub\onlineorder" %default_port%
call create_iis "ACR" %computername% "D:\Inetpub\acr" %default_port%
call create_iis "SupportCentral" "nslocal.test-qa.net" "D:\Inetpub\SupportCentral" %default_port%

call create_sePlugins.bat
