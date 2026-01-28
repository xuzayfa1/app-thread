package uz.zero.post

enum class ErrorCode(val code: Int) {
    POST_ALREADY_LINKED(100),
    POST_NOT_FOUND(101),
    ROOT_POST_NOT_FOUND_OR_IT_DELETED(102),
    COMMENT_NOT_FOUND(103),
    USER_NOT_FOUND(104),
    FILE_IS_NOT_UPLOAD(105)
}

enum class MediaType{
    IMAGE,
    VIDEO,
    NONE,
}