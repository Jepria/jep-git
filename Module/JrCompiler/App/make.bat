@echo off
chcp 1251
rem  Environment Variable Prequisites
rem
rem JDEV_HOME: JDeveloper installation directory
rem COMMON_HOME: local directory
rem PROJECT_HOME: local directory
rem MODULE_NAME: module name (in small english letters)
rem
rem Set make message's prefix
set msPrefix=MAKE:

rem Disply current time.
echo %msPrefix% Started at: %time%

rem Initialize variables.
:init
if not exist "%cd%/setEnvironment.bat" goto checkEnvironment
call setEnvironment.bat

:checkEnvironment
if "%JDEV_HOME%" == "" (
	echo %msPrefix% The JDEV_HOME environment variable is not defined
	goto end
)
if "%JASPER_REPORTS_HOME%" == "" (
	echo %msPrefix% The JASPER_REPORTS_HOME environment variable is not defined
	goto end
)
if "%COMMON_HOME%" == "" (
	echo %msPrefix% The COMMON_HOME environment variable is not defined
	goto end
)
if "%PROJECT_HOME%" == "" (
	echo %msPrefix% The PROJECT_HOME environment variable is not defined
	goto end
)
if "%MODULE_NAME%" == "" (
	echo %msPrefix% The MODULE_NAME variable is not defined
	goto end
)

:setAction
set ACTION=%1
if not "%ACTION%" == "" goto setVariables
set ACTION=all

:setVariables
set CURRENT_DIR=%cd%
set OC4J_HOME=%JDEV_HOME%
set JAVA_HOME=%JDEV_HOME%\jdk
set J2EE_HOME=%OC4J_HOME%\j2ee\home
rem set LIBS=%CURRENT_DIR%\lib\ext\jasperreports-3.5.2.jar
set LIBS=%LIBS%;%JASPER_REPORTS_HOME%\dist\jasperreports-3.6.0.jar

set RESOURCE_HOME=%CURRENT_DIR%\src\resources
set JAVA_DOC_DIR=..\Doc\App\AutoGen
set MODULE_PACKAGE=com\technology\jep\jrcompiler

rem Create temporary directory  
mkdir temp
cd temp
mkdir classes
cd %CURRENT_DIR%
set TEMP_DIR=%CURRENT_DIR%\temp
set CLASSES=%TEMP_DIR%\classes

:showVariables
echo %msPrefix% JDEV_HOME:   %JDEV_HOME%
echo %msPrefix% JAVA_HOME:   %JAVA_HOME%
echo %msPrefix% OC4J_HOME:   %OC4J_HOME%
echo %msPrefix% J2EE_HOME:   %J2EE_HOME%
echo %msPrefix% JASPER_REPORTS_HOME:   %JASPER_REPORTS_HOME%
echo %msPrefix% MODULE_NAME: %MODULE_NAME%
echo %msPrefix% LIBS: %LIBS%
echo %msPrefix% TEMP_DIR: %TEMP_DIR%
echo %msPrefix% CLASSES: %CLASSES%
echo %msPrefix% RESOURCE_HOME: %RESOURCE_HOME%
echo %msPrefix% MODULE_PACKAGE: %MODULE_PACKAGE%
echo %msPrefix% JAVA_DOC_DIR: %JAVA_DOC_DIR%

rem Check make parameters.
:checkAction
if "%ACTION%" == "compile" goto compile
if "%ACTION%" == "encode" goto encode
if "%ACTION%" == "jar" goto jar
if "%ACTION%" == "doc" goto doc
if "%ACTION%" == "all" goto actions
echo %msPrefix% unknown action %ACTION%
goto end

:actions
:compile
echo %msPrefix% Compile started...
%JAVA_HOME%\bin\javac -classpath %CLASSES%;%LIBS% -d %CLASSES% src\java\%MODULE_PACKAGE%\JRCompiler.java
echo %msPrefix% Compile completed
if "%ACTION%" == "compile" goto end

rem ---- build jar -----
:jar
echo %msPrefix% Jar creation started...
rem Extract external libraries
cd temp/classes
%JAVA_HOME%\bin\jar xf %JASPER_REPORTS_HOME%\dist\jasperreports-3.6.0.jar
%JAVA_HOME%\bin\jar xf %JDEV_HOME%\jakarta-taglibs\commons-beanutils-1.6.1\commons-beanutils.jar
%JAVA_HOME%\bin\jar xf %JDEV_HOME%\jakarta-taglibs\commons-collections-2.1\commons-collections.jar
%JAVA_HOME%\bin\jar xf %JDEV_HOME%\jakarta-taglibs\commons-logging-1.0.3\commons-logging.jar
%JAVA_HOME%\bin\jar xf %CURRENT_DIR%\lib\ext\commons-digester-1.7.jar
 
cd %CURRENT_DIR%
rem Create result jar 
echo xcopy /Q /Y /S %RESOURCE_HOME%\META-INF\manifest.mf %TEMP_DIR%\META-INF\
xcopy /Q /Y /S %RESOURCE_HOME%\META-INF\manifest.mf %CLASSES%\META-INF\
%JAVA_HOME%\bin\jar cmf %CLASSES%\META-INF\manifest.mf %CURRENT_DIR%\lib\%MODULE_NAME%.jar -C %CLASSES% .
echo %msPrefix% Jar creation completed
if "%ACTION%" == "jar" goto end

rem ---- generate javadoc -----
:doc
rem JavaDoc Generation...
echo %msPrefix% JavaDoc generation started...
set forJavaDocFiles=
echo set forJavaDocFiles=%forJavaDocFiles% src\java\%MODULE_PACKAGE%\*.java
set forJavaDocFiles=%forJavaDocFiles% src\java\%MODULE_PACKAGE%\*.java
if not "%forJavaDocFiles%" == "" %JAVA_HOME%\bin\javadoc -sourcepath src\java -d %JAVA_DOC_DIR% -classpath %LIBS% %forJavaDocFiles%
echo %msPrefix% JavaDoc generation completed
if "%ACTION%" == "doc" goto end

rem The next options don't executed by default.
goto end

:end
rem Cleanup temporary directory.
if not "%TEMP_DIR%" == "" rmdir /S /Q %TEMP_DIR%
@echo %msPrefix% Completed at: %time%
