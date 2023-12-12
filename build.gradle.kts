import java.io.ByteArrayOutputStream
import java.util.stream.Stream
import kotlin.streams.asStream

plugins {
    id("java-library")
    id("application")
}

group = "de.hts"
version = "1.0.0"

repositories {
    mavenCentral()
    flatDir {
        dirs("lib")
    }
}

dependencies {
    api(files("lib/quancom.jar"))
}

application {
    mainClass.set("de.hts.FerriteMain")
}

tasks{
    jar {
        manifest.attributes(
                "Implementation-Vendor" to "luh-code, pianoman911",
                "Implementation-Version" to project.version,
                "Implementation-Title" to project.name,

                "Git-Commit" to gitRevParse("short"),
                "Git-Branch" to gitRevParse("abbrev-ref"),
                "Timestamp" to System.currentTimeMillis().toString(),

                "Multi-Release" to "true",
        )
    }
}

fun gitRevParse(arg: String): String {
    return gitCommand(rootDir, "rev-parse", "--$arg", "HEAD")
}

fun gitCommand(workDir: File, vararg args: String): String {
    val out = ByteArrayOutputStream()
    rootProject.exec {
        commandLine(Stream.concat(Stream.of("git"), args.asSequence().asStream()).toList())
        workingDir = workDir

        standardOutput = out
        errorOutput = out
    }
    return out.toString().trim()
}

