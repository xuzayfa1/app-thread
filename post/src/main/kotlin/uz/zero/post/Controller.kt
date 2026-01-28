package uz.zero.post

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/posts")
class PostController(private val postService: PostService) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun create(
        @RequestParam userId: Long,
        @RequestParam content: String,
        @RequestParam(required = false) parentId: Long?,
        @RequestParam(required = false) files: List<MultipartFile>?
    ): PostResponse{
        return postService.createPost(userId, content, parentId, files)
    }

    @PostMapping("/{postId}/like")
    fun like(@PathVariable postId: Long, @RequestParam userId: Long): LikeResponse {
        val result = postService.toggleLike(postId, userId)
        return result
    }

    @PostMapping("/{postId}/comment")
    fun comment(@PathVariable postId: Long, @RequestBody request: CommentRequest): CommentResponse {
        return postService.addComment(postId, request.userId, request.text)
    }

    @GetMapping("/thread/{postId}")
    fun getThread(@PathVariable postId: Long): List<PostResponse> {
        return postService.getFullThread(postId)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        postService.deletePost(id)
        return ResponseEntity.noContent().build()
    }
}