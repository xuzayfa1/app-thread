package uz.zero.follow

enum class ErrorCode(val code: Int) {
    YOU_ARE_NOT_FOLLOW_TO_YOURSELF(100),
    FOLLOW_NOT_FOUND(101),
    USER_NOT_FOUND(102),
}