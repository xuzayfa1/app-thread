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
    ): ResponseEntity<PostResponse> {
        return ResponseEntity.ok(postService.createPost(userId, content, parentId, files))
    }

    @PostMapping("/{postId}/like")
    fun like(@PathVariable postId: Long, @RequestParam userId: Long): ResponseEntity<Unit> {
        postService.toggleLike(postId, userId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{postId}/comment")
    fun comment(@PathVariable postId: Long, @RequestBody request: CommentRequest): ResponseEntity<CommentResponse> {
        return ResponseEntity.ok(postService.addComment(postId, request.userId, request.text))
    }

    @GetMapping("/thread/{postId}")
    fun getThread(@PathVariable postId: Long): ResponseEntity<List<PostResponse>> {
        return ResponseEntity.ok(postService.getFullThread(postId))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        postService.deletePost(id)
        return ResponseEntity.noContent().build()
    }
}