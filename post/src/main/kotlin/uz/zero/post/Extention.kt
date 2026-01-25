package uz.zero.post

fun Post.toResponse(): PostResponse {
    val postId = this.id ?: throw PostNotFoundException()

    return PostResponse(
        id = postId,
        userId = this.userId,
        content = this.content,
        mediaUrls = this.mediaUrls.toList(),
        likesCount = this.likedUserIds.size,
        comments = try { this.comments.map { it.toResponse() } } catch (e: Exception) { emptyList() },
        parentId = this.parent?.id,
        rootId = this.rootId,
        createdDate = this.createdDate
    )
}

fun Comment.toResponse() = CommentResponse(
    id = this.id ?: 0L, 
    userId = this.userId,
    text = this.text,
    postId = this.post.id ?: 0L,
    createdDate = this.createdDate
)