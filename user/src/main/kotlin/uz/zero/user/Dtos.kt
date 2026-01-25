package uz.zero.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserResponseDto(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String?
) {
    companion object {
        fun fromEntity(user: User) = UserResponseDto(
            id = user.id!!,
            username = user.username,
            email = user.email,
            fullName = user.fullName
        )
    }
}


data class UserUpdateDto(
    val fullName: String? = null,
    val email: String? = null
)

data class LoginRequestDto(
    val username: String,
    val password: String
)

data class LoginResponseDto(
    val user: UserResponseDto
)

data class UserCreatedEvent(
    val userId: Long,
    val username: String,
    val registrationDate: java.util.Date = java.util.Date()
)

data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class UserRegistrationDto(
    @field:NotBlank(message = "Username bo'sh bo'lmasligi kerak")
    val username: String,

    @field:Email(message = "Email formati noto'g'ri")
    val email: String,

    @field:Size(min = 6, message = "Parol kamida 6 belgidan iborat bo'lishi kerak")
    val password: String,

    val fullName: String? = null
)

