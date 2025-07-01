JRE_64_ZIP=https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u265-b01/OpenJDK8U-jre_x64_windows_hotspot_8u265b01.zip
JRE_32_ZIP=https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u265-b01/OpenJDK8U-jre_x86-32_windows_hotspot_8u265b01.zip
VLC_64_ZIP=https://get.videolan.org/vlc/3.0.11/win64/vlc-3.0.11-win64.zip
VLC_32_ZIP=https://get.videolan.org/vlc/3.0.11/win32/vlc-3.0.11-win32.zip
JRE_INNER=jdk8u265-b01-jre
VLC_INNER=vlc-3.0.11

.PHONY: all jar dist clean test

all: test jar dist

#jar: target/linux32/GuideMe.jar target/linux64/GuideMe.jar target/mac64/GuideMe.jar target/win32/GuideMe.jar target/win64/GuideMe.jar
#dist:target/artifacts/GuideMe.Linux.32-bit.zip target/artifacts/GuideMe.Linux.64-bit.zip target/artifacts/GuideMe.Mac.64-bit.zip target/artifacts/GuideMe.Windows.Slim.zip target/artifacts/GuideMe.Windows.32-bit.zip target/artifacts/GuideMe.Windows.64-bit.zip
jar: target/linux64/GuideMe.jar target/mac64/GuideMe.jar target/win64/GuideMe.jar
dist:target/artifacts/GuideMe.Linux.64-bit.zip target/artifacts/GuideMe.Mac.64-bit.zip target/artifacts/GuideMe.Windows.Slim.zip target/artifacts/GuideMe.Windows.64-bit.zip

test:
	mvn -B test

target/linux32/GuideMe.jar:
	mvn -B -DskipTests package -P linux32

target/linux64/GuideMe.jar:
	mvn -B -DskipTests package -P linux64

target/mac64/GuideMe.jar:
	mvn -B -DskipTests package -P mac64

target/win32/GuideMe.jar:
	mvn -B -DskipTests package -P win32

target/win64/GuideMe.jar:
	mvn -B -DskipTests package -P win64

target/win32/java:
	mkdir -p target/win32/java
	curl -sL -o target/win32/java.zip "$(JRE_32_ZIP)"
	unzip target/win32/java.zip -d target/win32/java

target/win64/java:
	mkdir -p target/win64/java
	curl -sL -o target/win64/java.zip "$(JRE_64_ZIP)"
	unzip target/win64/java.zip -d target/win64/java

target/win32/vlc:
	mkdir -p target/win32/vlc
	curl -sL -o target/win32/vlc.zip "$(VLC_32_ZIP)"
	unzip target/win32/vlc.zip -d target/win32/vlc

target/win64/vlc:
	mkdir -p target/win64/vlc
	curl -sL -o target/win64/vlc.zip "$(VLC_64_ZIP)"
	unzip target/win64/vlc.zip -d target/win64/vlc

target/artifacts/GuideMe.Linux.32-bit.zip: target/linux32/GuideMe.jar
	mkdir -p target/dist/linux32/data target/artifacts
	cp target/linux32/GuideMe.jar target/dist/linux32/
	cp -rf target/linux32/lib target/dist/linux32/
	cp -rf userSettings target/dist/linux32/
	cp data/settings-linux.properties target/dist/linux32/data/settings.properties
	cp [dD]efault*.[tT][xX][tT] target/dist/linux32/
	cp user[sS]ettings*.xml target/dist/linux32/
	cp Welcome*.txt target/dist/linux32/
	cp start-linux.sh target/dist/linux32/start.sh
	chmod u+x target/dist/linux32/start.sh
	cd target/dist/linux32; zip -r ../../artifacts/GuideMe.Linux.32-bit.zip *

target/artifacts/GuideMe.Linux.64-bit.zip: target/linux64/GuideMe.jar
	mkdir -p target/dist/linux64/data target/artifacts
	cp target/linux64/GuideMe.jar target/dist/linux64/
	cp -rf target/linux64/lib target/dist/linux64/
	cp -rf userSettings target/dist/linux64/
	cp data/settings-linux.properties target/dist/linux64/data/settings.properties
	cp [dD]efault*.[tT][xX][tT] target/dist/linux64/
	cp user[sS]ettings*.xml target/dist/linux64/
	cp Welcome*.txt target/dist/linux64/
	cp start-linux.sh target/dist/linux64/start.sh
	chmod u+x target/dist/linux64/start.sh
	cd target/dist/linux64; zip -r ../../artifacts/GuideMe.Linux.64-bit.zip *

target/artifacts/GuideMe.Mac.64-bit.zip: target/mac64/GuideMe.jar
	mkdir -p target/dist/mac64/data target/artifacts
	cp target/mac64/GuideMe.jar target/dist/mac64/
	cp -rf target/mac64/lib target/dist/mac64/
	cp -rf userSettings target/dist/mac64/
	cp data/settings-macos.properties target/dist/mac64/data/settings.properties
	cp [dD]efault*.[tT][xX][tT] target/dist/mac64/
	cp user[sS]ettings*.xml target/dist/mac64/
	cp Welcome*.txt target/dist/mac64/
	cp start-macos.sh target/dist/mac64/start.sh
	chmod u+x target/dist/mac64/start.sh
	cd target/dist/mac64; zip -r ../../artifacts/GuideMe.Mac.64-bit.zip *

target/artifacts/GuideMe.Windows.Slim.zip: target/win64/GuideMe.jar
	mkdir -p target/dist/win/data target/artifacts
	cp target/win64/GuideMe.jar target/dist/win/
	cp -rf target/win64/lib target/dist/win/
	cp -rf userSettings target/dist/win/
	cp data/settings-windows.properties target/dist/win/data/settings.properties
	cp [dD]efault*.[tT][xX][tT] target/dist/win/
	cp user[sS]ettings*.xml target/dist/win/
	cp Welcome*.txt target/dist/win/
	cp start.bat target/dist/win/
	cd target/dist/win; zip -r ../../artifacts/GuideMe.Windows.Slim.zip *

target/artifacts/GuideMe.Windows.32-bit.zip: target/win32/GuideMe.jar target/win32/java target/win32/vlc
	mkdir -p target/dist/win32/data target/dist/win32/java target/artifacts
	cp target/win32/GuideMe.jar target/dist/win32/
	cp -rf target/win32/lib target/dist/win32/
	cp -rf userSettings target/dist/win32/
	cp data/settings-windows.properties target/dist/win32/data/settings.properties
	cp [dD]efault*.[tT][xX][tT] target/dist/win32/
	cp user[sS]ettings*.xml target/dist/win32/
	cp Welcome*.txt target/dist/win32/
	cp start.bat target/dist/win32/
	cp -rf target/win32/java/$(JRE_INNER)/* target/dist/win32/java/
	cp -rf target/win32/vlc/$(VLC_INNER)/plugins target/dist/win32/lib/
	cp target/win32/vlc/$(VLC_INNER)/libvlc.dll target/dist/win32/lib/
	cp target/win32/vlc/$(VLC_INNER)/libvlccore.dll target/dist/win32/lib/
	cd target/dist/win32; zip -r ../../artifacts/GuideMe.Windows.32-bit.zip *

target/artifacts/GuideMe.Windows.64-bit.zip: target/win64/GuideMe.jar target/win64/java target/win64/vlc
	mkdir -p target/dist/win64/data target/dist/win64/java target/artifacts
	cp target/win64/GuideMe.jar target/dist/win64/
	cp -rf target/win64/lib target/dist/win64/
	cp -rf userSettings target/dist/win64/
	cp data/settings-windows.properties target/dist/win64/data/settings.properties
	cp [dD]efault*.[tT][xX][tT] target/dist/win64/
	cp user[sS]ettings*.xml target/dist/win64/
	cp Welcome*.txt target/dist/win64/
	cp start.bat target/dist/win64/
	cp -rf target/win64/java/$(JRE_INNER)/* target/dist/win64/java/
	cp -rf target/win64/vlc/$(VLC_INNER)/plugins target/dist/win64/lib/
	cp target/win64/vlc/$(VLC_INNER)/libvlc.dll target/dist/win64/lib/
	cp target/win64/vlc/$(VLC_INNER)/libvlccore.dll target/dist/win64/lib/
	cd target/dist/win64; zip -r ../../artifacts/GuideMe.Windows.64-bit.zip *

clean:
	mvn -B clean
