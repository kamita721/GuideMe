for zipFile in swt-*.zip
do
    fileName=`basename $zipFile .zip`
    fileName=`echo $fileName | sed "s/^swt-//"`
    version=`echo $fileName | cut -d- -f1`
    fileName=`echo $fileName | cut -d- -f1 --complement`
    artifactName=`echo $fileName | sed "s/-/./g"`

    mkdir $fileName-$version
    pushd $fileName-$version >/dev/null
    unzip ../$zipFile
        mvn install:install-file -Dfile=swt.jar -DgroupId="org.eclipse.swt" -DartifactId="org.eclipse.swt.${artifactName}" -Dversion="${version}" -Dpackaging="jar" -Dsources="src.zip"
    popd >/dev/null
done
