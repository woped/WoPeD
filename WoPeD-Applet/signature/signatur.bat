@echo off 
rem * SignTool by L-ectron-X ( www.byte-welt.de ) 
rem * 
rem * Ablauf beim Signieren 
rem * 
rem * Erzeugen eines eigenen Schlüssels 
rem * Mit KeyTool, einem Werkzeug aus dem SDK, einen neuen Schlüssel erzeugen: 
rem * keytool -genkey -alias Signer -dname "cn=Dein Name, c=de" 
rem * Gib anschließend Dein Passwort ein. 
rem 
rem * Erzeugen eines Zertifikats 
rem * Mit KeyTool Zertifikat erzeugen: 
rem * keytool -selfcert -alias Signer -dname "cn=Dein Name, c=de" 
rem * Gib anschließend dein Passwort ein. 
rem * 
rem * Signieren des Applets 
rem * Signieren des Applets mit Hilfe des Tools jarsigner 
rem * jarsigner signed.jar Signer 
rem * Und noch einmal dein Passwort eingeben. 

echo = SignTool = 
echo Dieses Tool hilft beim Erzeugen von signierten jar-Dateien 
echo. 
echo Schritt 1: jar-Dateien erzeugen 
echo ------------------------------- 

rem Pfad zum SDK setzen 
rem --> anpassen! 
set path=.;C:\Programme\Java\jdk1.5.0_14\bin

echo Manifestdatei erzeugen... 
echo Manifest-Version: 1.0>manifest.mf 
echo Created-by: SignTool by L-ectron-X>>manifest.mf 

if exist *.jar goto key 
echo jar-Datei mit angegebenen Parametern erzeugen... 
rem --> anpassen! 
rem * In diesem Beispiel werden alle .class-Dateien und die Verzeichnisse bilder und etc 
rem * mit ins jar-Archiv gepackt. 
jar cfmv WoPeD-classes-2.0.0_alpha.jar manifest.mf *.class bilder etc 

:key 
echo. 
echo Schritt 2: Schluessel generieren 
echo -------------------------------- 
rem --> anpassen! 
keytool -genkey -alias Signer -dname "cn=WoPeD Projekt, c=de" 

echo. 
echo Schritt 3: Zertifikat erzeugen 
echo ------------------------------ 
rem --> anpassen! 
rem -validity 18250 (365 Tage x 50) erzeugt ein 50 Jahre gültiges Zertifikat 
keytool -selfcert -validity 3650 -alias Signer -dname "cn=WoPeD Projekt, c=de" 

if not exist *.jar goto error 
echo. 
echo Schritt 4: jar-Datei signieren 
echo ------------------------------ 
echo jarsigner erwartet hier nochmals dein Passwort. 
rem --> anpassen! 
jarsigner WoPeD-classes-2.0.0_alpha.jar Signer 

echo. 
echo Schritt 5: Zertifikat testen 
echo ---------------------------- 
rem --> anpassen! 
jarsigner -verify -verbose -certs WoPeD-classes-2.0.0_alpha.jar 
goto end 

:error 
echo. 
echo Es wurde keine jar-Datei zum Signieren gefunden. 
echo Die Erzeugung der jar-Datei ist moeglicherweise fehlgeschlagen. 
echo Pruefe deine Eingaben in der Batchdatei! 

:end 
if not exist manifest.mf goto console 
echo. 
rem Manifest von Festplatte löschen 
del manifest.mf 

:console 
rem Console für Ausgaben noch geöffnet lassen 
echo. 
pause