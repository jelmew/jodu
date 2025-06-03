package nl.jelmervanamen

import io.quarkus.logging.Log
import jakarta.enterprise.context.Dependent
import org.apache.commons.io.FileUtils
import java.nio.file.Path
import kotlin.io.path.*

@Dependent
class OduService {
    data class FileDetail(val path: Path, val size: Long)

    fun listDirectoryContents(dir: Path = Path("")) {
        dir.listDirectoryEntries().map {
            analyzeFileSize(it)
        }.sortedBy { it.size }.forEach {
            println("${String.format("%-20s", FileUtils.byteCountToDisplaySize(it.size))} ${it.path}")
        }
    }

    private fun analyzeFileSize(path: Path): FileDetail {
        return try {
            if (path.isSymbolicLink()) {
                FileDetail(path.fileName, 0)
            } else if (path.isDirectory()) {
                FileDetail(path.fileName, path.listDirectoryEntries().sumOf { file -> analyzeFileSize(file).size })
            } else {
                FileDetail(path.fileName, path.fileSize())
            }
        } catch (e: java.nio.file.AccessDeniedException) {
            Log.warn("While trying to  list a directory or get afileSize, an exception was thrown: ", e)
            FileDetail(path.fileName, 0)
        }
    }
}