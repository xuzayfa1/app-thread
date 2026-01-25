package uz.zero.post

enum class ErrorCode(val code: Int) {
    POST_ALREADY_LINKED(100),
    POST_NOT_FOUND(101),
    ROOT_POST_NOT_FOUND_OR_IT_DELETED(102),
    COMMENT_NOT_FOUND(103),
}

enum class MediaType{
    IMAGE,
    VIDEO,
    NONE,
}