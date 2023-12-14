package top.chilfish.chilpost.utils

import org.springframework.web.multipart.MultipartFile
import java.io.File

fun getStaticPath(): String {
    val classLoader = Thread.currentThread().contextClassLoader
    val resource = classLoader.getResource("static")
    return resource?.path ?: "/static"
}

fun uploadFile(file: MultipartFile, path: String, fileName: String): String {
    if (file.isEmpty) throw Exception("File is empty")

    val staticPath = getStaticPath()
//    logger.info("staticPath: $staticPath")

    val filePath = "$staticPath/$path"
    val dest = File(filePath, fileName)
    if (!dest.parentFile.exists()) {
        dest.parentFile.mkdirs()
    }
    file.transferTo(dest)
    return "/$path/$fileName"
}