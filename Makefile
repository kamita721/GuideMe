JRE_64_ZIP=https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u265-b01/OpenJDK8U-jre_x64_windows_hotspot_8u265b01.zip
JRE_32_ZIP=https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u265-b01/OpenJDK8U-jre_x86-32_windows_hotspot_8u265b01.zip
VLC_64_ZIP=https://get.videolan.org/vlc/3.0.11/win64/vlc-3.0.11-win64.zip
VLC_32_ZIP=https://get.videolan.org/vlc/3.0.11/win32/vlc-3.0.11-win32.zip
JRE_INNER=jdk8u265-b01-jre
VLC_INNER=vlc-3.0.11

.PHONY: all jar dist clean test

all: jar dist

jar: target/GuideMe.jar
dist: target/artifacts/GuideMe.Slim.zip target/artifacts/GuideMe.32-bit.zip target/artifacts/GuideMe.64-bit.zip

test:
	mvn test

target/GuideMe.jar:
	mvn install

target/vendor/java32:
	mkdir -p target/vendor/java32
	curl -L -o target/vendor/java32.zip "$(JRE_32_ZIP)"
	unzip target/vendor/java32.zip -d target/vendor/java32

target/vendor/java64:
	mkdir -p target/vendor/java64
	curl -L -o target/vendor/java64.zip "$(JRE_64_ZIP)"
	unzip target/vendor/java64.zip -d target/vendor/java64

target/vendor/vlc32:
	mkdir -p target/vendor/vlc32
	curl -L -o target/vendor/vlc32.zip "$(VLC_32_ZIP)"
	unzip target/vendor/vlc32.zip -d target/vendor/vlc32

target/vendor/vlc64:
	mkdir -p target/vendor/vlc64
	curl -L -o target/vendor/vlc64.zip "$(VLC_32_ZIP)"
	unzip target/vendor/vlc64.zip -d target/vendor/vlc64

target/dist/GuideMe.jar: target/GuideMe.jar
	mkdir -p target/dist/data
	cp target/GuideMe.jar target/dist/
	cp -rf target/lib target/dist/
	cp -rf userSettings target/dist/
	cp data/settings.properties target/dist/data/settings.properties
	cp [dD]efault*.[tT][xX][tT] target/dist/
	cp user[sS]ettings*.xml target/dist/
	cp Welcome*.txt target/dist/

target/artifacts/GuideMe.Slim.zip: target/dist/GuideMe.jar
	cp -rf target/dist target/dist_slim
	cp start.bat target/dist_slim/
	cp start.sh target/dist_slim/
	mkdir -p target/artifacts
	cd target/dist_slim;	zip -r ../artifacts/GuideMe.Slim.zip *

target/artifacts/GuideMe.32-bit.zip: target/dist/GuideMe.jar target/vendor/java32 target/vendor/vlc32
	cp -rf target/dist target/dist_32
	cp start.bat target/dist_32/
	mkdir -p target/dist_32/java
	cp -rf target/vendor/java32/$(JRE_INNER)/* target/dist_32/java/
	cp -rf target/vendor/vlc32/$(VLC_INNER)/plugins target/dist_32/lib/
	cp target/vendor/vlc32/$(VLC_INNER)/libvlc.dll target/dist_32/lib/
	cp target/vendor/vlc32/$(VLC_INNER)/libvlccore.dll target/dist_32/lib/
	mkdir -p target/artifacts
	cd target/dist_32; zip -r ../artifacts/GuideMe.32-bit.zip *

target/artifacts/GuideMe.64-bit.zip: target/dist/GuideMe.jar target/vendor/java64 target/vendor/vlc64
	cp -rf target/dist target/dist_64
	cp start.bat target/dist_64/
	mkdir -p target/dist_64/java
	cp -rf target/vendor/java64/$(JRE_INNER)/* target/dist_64/java/
	cp -rf target/vendor/vlc64/$(VLC_INNER)/plugins target/dist_64/lib/
	cp target/vendor/vlc64/$(VLC_INNER)/libvlc.dll target/dist_64/lib/
	cp target/vendor/vlc64/$(VLC_INNER)/libvlccore.dll target/dist_64/lib/
	mkdir -p target/artifacts
	cd target/dist_64; zip -r ../artifacts/GuideMe.64-bit.zip *

clean:
	mvn clean