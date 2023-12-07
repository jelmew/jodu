package nl.jelmervanamen

import jakarta.enterprise.context.Dependent
import org.apache.commons.io.FileUtils
import java.nio.file.Path
import kotlin.io.path.*

@Dependent
class OduService {
    data class FileDetail(val path: Path, val size: Long)

    fun listDirectoryContents(dir: Path = Path("")) {
        dir.listDirectoryEntries().map { path ->
            analyzeFileSize(path)
        }.sortedBy { fileDetail -> fileDetail.size }.forEach { fileDetail ->
            println("${String.format("%-20s", FileUtils.byteCountToDisplaySize(fileDetail.size))} ${fileDetail.path}")
        }
    }

    private fun analyzeFileSize(path: Path): FileDetail {
        return if (path.isSymbolicLink()) {
            FileDetail(path.fileName, 0)
        } else if (path.isDirectory()) {
            FileDetail(path.fileName, path.listDirectoryEntries().sumOf { file -> analyzeFileSize(file).size })
        } else {
            FileDetail(path.fileName, path.fileSize())
        }
    }
}