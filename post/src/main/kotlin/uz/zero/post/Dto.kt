package uz.zero.post


import java.util.Date

data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class PostCreateRequest(
    val userId: Long,
    val description: String,
    val parentId: Long? = null,
    val mediaUrls: List<String> = emptyList(),
    val mediaType: MediaType = MediaType.IMAGE 
)

data class PostResponse(
    val id: Long,
    val userId: Long,
    val content: String,
    val mediaUrls: List<String>,
    val likesCount: Int,
    val comments: List<CommentResponse>, 
    val parentId: Long?,
    val rootId: Long?,
    val createdDate: Date?
)

data class CommentRequest(
    val userId: Long,
    val text: String
)

data class CommentResponse(
    val id: Long,
    val userId: Long,
    val text: String,
    val postId: Long, 
    val createdDate: Date?
)

data class UserResponseDto(val id: Long)

data class LikeResponse(
    val message: String,
    val postId: Long,
)