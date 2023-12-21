package top.chilfish.chilpost.utils

import org.springframework.core.io.ClassPathResource
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

fun getStaticPath(): String = ClassPathResource("static").file.absolutePath

const val filePath = "./files"

// mkdir for filePath
fun initFileDir() {
    val file = File(filePath)
    if (!file.exists()) {
        file.mkdirs()
    }
}

fun emptyFile(): File {
    val ba = ByteArray(0)
    val file = File.createTempFile("empty", "file")
    file.writeBytes(ba)
    return file
}

fun convertToJpg(multipartFile: MultipartFile): ByteArray {
    val inputImage = ImageIO.read(multipartFile.inputStream)
        ?: throw IllegalArgumentException("Invalid image file")

    val output = ByteArrayOutputStream()
    ImageIO.write(inputImage, "png", output)
    return output.toByteArray()
}

fun getFile(path: String, fileName: String): File {
    val filePath = "$filePath/$path"
    val dest = File(filePath, fileName)
    if (!dest.parentFile.exists()) {
        dest.parentFile.mkdirs()
    }
    return dest
}

fun uploadFile(file: MultipartFile, path: String, fileName: String): String {
    if (file.isEmpty) throw Exception("File is empty")

    val dest = getFile(path, fileName)
    file.transferTo(dest)
    return "/files/$path/$fileName"
}


fun uploadImage(imgFile: MultipartFile, path: String, fileName: String): String {
    val jpgImg = convertToJpg(imgFile)
    val dest = getFile(path, fileName)
    val fos = dest.outputStream()
    fos.write(jpgImg)
    fos.close()

    return "/files/$path/$fileName"
}