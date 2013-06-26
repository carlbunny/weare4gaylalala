@ECHO ON
setlocal
set prog=%~f0
echo %prog%
set CURRENTDIR=%~dp0
cd /d %CURRENTDIR%
echo %CURRENTDIR%

set USER_NAME="¿ª·¢Õß"
set COMPILER_PATH="D:\aeviou\repositories\source\compiler\trunk\dist\AeviouCompiler.jar"
set XML_PATH="D:\aeviou\repositories\source\compiler\trunk\dist\XMLUtil.jar"
set ANDROID_SDK="D:\aeviou\android-sdk"
set JDK_HOME="C:\Program Files\Java\jdk1.6.0_24"
set TOOLS="D:\aeviou\android-sdk\platforms\android-7\tools"

del bin\resources.ap_
del classes_obfuscated.jar
del classes_obfuscated.dex
del unsigned.apk
del signed.apk

call java -jar -Dfile.encoding=utf-8 %COMPILER_PATH% %USER_NAME%
call java -jar -Dfile.encoding=utf-8 %XML_PATH% %USER_NAME%

xcopy /E /I /R /Y bin\com classes\com
call java -jar proguard.jar -injars classes\ -outjars classes_obfuscated.jar -libraryjars android7.jar @proguard.cfg

rmdir /S /Q classes

call %TOOLS%\aapt package -f -m -J gen -S res -I %ANDROID_SDK%\platforms\android-7\android.jar -M %CURRENTDIR%AndroidManifest.xml

call %TOOLS%\dx --dex --output=%CURRENTDIR%classes_obfuscated.dex %CURRENTDIR%classes_obfuscated.jar

call %TOOLS%\aapt package -f -M AndroidManifest.xml -S res -A assets -I %ANDROID_SDK%\platforms\android-7\android.jar -F %CURRENTDIR%bin\resources.ap_

call %ANDROID_SDK%\tools\apkbuilder %CURRENTDIR%unsigned.apk -u -f %CURRENTDIR%classes_obfuscated.dex -z %CURRENTDIR%bin\resources.ap_

call %JDK_HOME%\bin\jarsigner -keystore androidkey -storepass sjtuslideimp -keypass sjtuslideimp -signedjar signed.apk unsigned.apk aeviou

copy signed.apk dist\%USER_NAME%.apk
