package uz.zero.post

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException
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

class PostNotFoundException : MyException() {
    override fun errorType() = ErrorCode.POST_NOT_FOUND
}

class UserNotFoundException : MyException() {
    override fun errorType() = ErrorCode.USER_NOT_FOUND
}

class PostAlreadyLinkedException : MyException() {
    override fun errorType() = ErrorCode.POST_ALREADY_LINKED
}

class RootPostNotFoundOrItDeletedException : MyException() {
    override fun errorType() = ErrorCode.ROOT_POST_NOT_FOUND_OR_IT_DELETED
}

class CommentNotFoundException : MyException() {
    override fun errorType() = ErrorCode.COMMENT_NOT_FOUND
}