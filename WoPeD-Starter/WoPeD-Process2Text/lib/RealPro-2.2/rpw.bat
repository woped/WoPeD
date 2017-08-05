@ECHO OFF

REM
REM  Start RealPro Workshop
REM

java -classpath .;./lib;./lib/rpw.jar;./lib/rpw-lkb.jar;./lib/xalan.jar;./lib/xml-apis.jar;./lib/xercesImpl.jar com.cogentex.real.rpw.RPW  -e Config.realization_english.realpro_workshop


pause