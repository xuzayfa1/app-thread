package uz.zero.follow

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig {
    @Bean
    fun errorDecoder(): ErrorDecoder {
        return FeignErrorDecoder()
    }
}

class FeignErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception {
        return when (response.status()) {
            404 -> UserNotFoundException()
            else -> Exception("Tizimlararo aloqada xatolik")
        }
    }
}