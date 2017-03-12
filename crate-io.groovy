@Grab(group='org.codehaus.plexus', module='plexus-archiver', version='2.10.2')
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver
import com.github.igorsuhorukov.smreed.dropship.MavenClassLoader;

@Grab(group='org.codehaus.plexus', module='plexus-container-default', version='1.6')
import org.codehaus.plexus.logging.console.ConsoleLogger;

def artifact = 'crate'
def version = '1.0.4'

def userHome= System.getProperty('user.home')
def destDir = new File("$userHome/.crate-io")

def crateIoDir= new File(destDir, "$artifact-$version");
if(!crateIoDir.exists()){
	destDir.mkdirs()
	String sourceFile = MavenClassLoader.using("https://dl.bintray.com/crate/crate/").getArtifactUrlsCollection("io.crate:$artifact:tar.gz:$version", null).get(0).getFile()
	final TarGZipUnArchiver unArchiver = new TarGZipUnArchiver()
	unArchiver.setSourceFile(new File(sourceFile))
	unArchiver.enableLogging(new ConsoleLogger(ConsoleLogger.LEVEL_DEBUG,"Logger"))
	unArchiver.setDestDirectory(destDir)
	unArchiver.extract()
}

def proc = "$crateIoDir.absolutePath/bin/crate".execute()

proc.consumeProcessOutput(System.out, System.err)
proc.waitFor()
