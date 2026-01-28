package uz.zero.post

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

interface PostService {
    fun createPost(userId: Long, content: String, parentId: Long?, files: List<MultipartFile>?): PostResponse
    fun toggleLike(postId: Long, userId: Long): LikeResponse
    fun addComment(postId: Long, userId: Long, text: String): CommentResponse
    fun getFullThread(postId: Long): List<PostResponse>
    fun deletePost(postId: Long)
}

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val userClient: UserClient,
    private val fileClient: FileClient,
) : PostService {

    @Transactional
    override fun createPost(userId: Long, content: String, parentId: Long?, files: List<MultipartFile>?): PostResponse {
        try {
            userClient.getById(userId)
        } catch (e: Exception) {
            throw UserNotFoundException()
        }
        val newPost = Post(userId = userId, content = content)
        files?.filter { !it.isEmpty }?.forEach { file ->
            val response = fileClient.upload(file)
            val fileUrl = response["url"] ?: throw FileIsNotUploadException()
            newPost.mediaUrls.add(fileUrl)
        }

        if (parentId != null) {
            val parentPost = postRepository.findByIdAndDeletedFalse(parentId)
                ?: throw PostNotFoundException()
            if (postRepository.existsByParentIdAndDeletedFalse(parentId)) {
                throw PostAlreadyLinkedException()
            }
            newPost.parent = parentPost
            newPost.rootId = parentPost.rootId ?: parentPost.id
        }

        return postRepository.save(newPost).toResponse()
    }

    @Transactional
    override fun toggleLike(postId: Long, userId: Long): LikeResponse {
        try {
            userClient.getById(userId)
        } catch (e: Exception) {
            throw UserNotFoundException()
        }
        val post = postRepository.findByIdAndDeletedFalse(postId) ?: throw PostNotFoundException()

        val message: String

        if (post.likedUserIds.contains(userId)) {
            post.likedUserIds.remove(userId)
            message = "Siz ushbu postdan likeni olib tashladingiz"
        } else {
            post.likedUserIds.add(userId)
            message = "Siz ushbu postga like bosdingiz"
        }
        postRepository.save(post)
        return LikeResponse(
            message = message,
            postId = postId,
        )
    }

    @Transactional
    override fun addComment(postId: Long, userId: Long, text: String): CommentResponse {
        try {
            userClient.getById(userId)
        } catch (e: Exception) {
            throw UserNotFoundException()
        }

        val post = postRepository.findByIdAndDeletedFalse(postId) ?: throw PostNotFoundException()
        val comment = Comment(
            userId = userId,
            text = text,
            post = post
        )
        val savedComment = commentRepository.saveAndFlush(comment)
        return savedComment.toResponse()
    }

    override fun getFullThread(postId: Long): List<PostResponse> {

        val post = postRepository.findByIdAndDeletedFalse(postId)
            ?: throw PostNotFoundException()
        val rootId = post.rootId ?: post.id!!
        return postRepository.findFullThread(rootId)
            .map { it.toResponse() }
    }

    @Transactional
    override fun deletePost(postId: Long) {
        postRepository.trash(postId) ?: throw PostNotFoundException()
    }
}