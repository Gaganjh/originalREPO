@echo off

title GenerateCSSfromSCSS utility

rem This utility generate the css from the scss 
rem of a mapped drive for the specified file type.

rem Use NA (Not Applicable) for arguments that are not relevant.


rem args:

rem 1.	jar -source path Jar
rem 2.	scss -source file scss
rem 3.  css   -final destination path


:start
set jar_source=%1
set scss_source=%2
set css_path=%3
set task=%4


echo.
echo Parameters used:
echo 1. jar_source=%jar_source%
echo 2. scss_source=%scss_source%
echo 3. css_path=%css_path%
echo 4. task=%css_path%
echo.

if "%task%" == "CREATE"      goto create
if "%task%" == "COMPILE"      goto compile
if "%task%" == "WATCH"      goto watch
goto End

:create
java -jar %jar_source%/jcompass.jar -S compass create --sass-dir %scss_source% --css-dir %css_path% --trace
goto End

:compile
java -jar %jar_source%/jcompass.jar -S compass compile --sass-dir %scss_source% --css-dir %css_path% --trace
goto End


:watch
java -jar %jar_source%/jcompass.jar -S compass watch --sass-dir %scss_source% --css-dir %css_path% --trace
goto End


:End

