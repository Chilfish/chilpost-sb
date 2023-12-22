package top.chilfish.chilpost.controller

import org.springframework.core.io.FileSystemResource
import org.springframework.http.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import top.chilfish.chilpost.utils.emptyFile
import java.io.File

@Controller
@RequestMapping("/files")
class FileController {
    @GetMapping("/**")
    fun getFile(): ResponseEntity<FileSystemResource> {
        val req = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val filePath = req.servletPath.substringAfter("/files/")

        var file = File("files/$filePath")
        if (!file.exists()) {
            file = emptyFile()
        }
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_OCTET_STREAM
        headers.contentDisposition = ContentDisposition.builder("attachment")
            .filename(file.name)
            .build()

        val resource = FileSystemResource(file)
        return ResponseEntity(resource, headers, HttpStatus.OK)
    }
}