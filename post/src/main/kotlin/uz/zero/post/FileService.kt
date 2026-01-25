package uz.zero.post



import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileService {
    fun upload(file: MultipartFile): String {
        return "temp_file_name.jpg"
    }
}