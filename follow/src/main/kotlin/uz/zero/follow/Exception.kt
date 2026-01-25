package uz.zero.follow

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource,
) {
    @ExceptionHandler(Exception::class)
    fun handleExceptions(exception: Exception): ResponseEntity<Any> {
        return when (exception) {
            is MyException -> {
                ResponseEntity
                    .badRequest()
                    .body(exception.getErrorMessage(messageSource))
            }

            is DataIntegrityViolationException -> {
                ResponseEntity
                    .badRequest()
                    .body(BaseMessage(103, "Ma'lumotlar bazasida ziddiyat: bu ma'lumot allaqachon mavjud."))
            }

            is MethodArgumentNotValidException -> {
                val errors =
                    exception.bindingResult?.fieldErrors?.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
                ResponseEntity
                    .badRequest()
                    .body(BaseMessage(400, "Validatsiya xatosi: $errors"))
            }

            else -> {
                exception.printStackTrace()
                ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseMessage(100, "Iltimos support bilan bog'laning. Xatolik: ${exception.message}"))
            }
        }
    }
}


sealed class MyException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorCode
    protected open fun getErrorMessageArguments(): Array<Any?>? = null

    fun getErrorMessage(errorMessageSource: MessageSource): BaseMessage {
        val message = try {
            errorMessageSource.getMessage(
                errorType().toString(),
                getErrorMessageArguments(),
                LocaleContextHolder.getLocale()
            )
        } catch (e: Exception) {
            errorType().toString().replace("_", " ")
        }
        return BaseMessage(errorType().code, message)
    }
}

class YouAreNotFollowToYourselfException : MyException() {
    override fun errorType() = ErrorCode.YOU_ARE_NOT_FOLLOW_TO_YOURSELF
}

class FollowNotFoundException : MyException() {
    override fun errorType() = ErrorCode.FOLLOW_NOT_FOUND
}

class UserNotFoundException : MyException() {
    override fun errorType() = ErrorCode.USER_NOT_FOUND
}


