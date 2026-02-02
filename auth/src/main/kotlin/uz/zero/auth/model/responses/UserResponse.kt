package uz.zero.auth.model.responses



import uz.zero.auth.enums.UserStatus

data class UserResponse(
    val id: Long,
    val fullName: String,
    val username: String,
    val role: String,
    val status: UserStatus
)