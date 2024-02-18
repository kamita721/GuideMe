pushd dependencies/
    SWT_VERSION="R-4.30-202312010110"
    SWT_DOWNLOAD_BASE="https://download.eclipse.org/eclipse/downloads/drops4/${SWT_VERSION}/download.php?dropFile=swt"
    SWT_VERSION="4.30.0"
    getJar(){
        outFile="swt-${SWT_VERSION}-${1}.zip"
        if [ ! -f "$outFile" ]
        then
            curl "${SWT_DOWNLOAD_BASE}-${SWT_VERSION}-${1}.zip" -o ${outFile}
        fi
 #       mvn install:install-file -Dfile=${outFile} -DgroupId="org.eclipse.swt" -DartifactId="org.eclipse.swt.gtk.linux.x86_64" -Dversion="${SWT_VERSION}" -Dpackaging="jar"

    }
    getJar win32-win32-x86_64
    #wget "${SWT_DOWNLOAD_BASE}-${SWT_VERSION}-win32-win32-x86_64.zip"
    #wget "${SWT_DOWNLOAD_BASE}-${SWT_VERSION}-gtk-linux-x86_64.zip"
    #wget "${SWT_DOWNLOAD_BASE}-${SWT_VERSION}-gtk-linux-ppc64le.zip"
    #wget "${SWT_DOWNLOAD_BASE}-${SWT_VERSION}-gtk-linux-aarch64.zip"
    #wget "${SWT_DOWNLOAD_BASE}-${SWT_VERSION}-cocoa-macosx-x86_64.zip"
    #wget "${SWT_DOWNLOAD_BASE}-${SWT_VERSION}-cocoa-macosx-aarch64.zip"
popd
