package uz.zero.follow

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/follows")
class FollowController(private val followService: FollowService) {

    @PostMapping
    fun follow(@RequestBody request: FollowRequest): FollowResponse {
        val result = followService.follow(request.followerId, request.followingId)
        return result
    }

    @PostMapping("/unfollow")
    fun unfollow(@RequestParam followerId: Long, @RequestParam followingId: Long): FollowResponse {
        val result = followService.unfollow(followerId, followingId)
        return result
    }

    @GetMapping("/{userId}/followers")
    fun getFollowers(@PathVariable userId: Long): List<Long> {
        return followService.getFollowers(userId)
    }

    @GetMapping("/{userId}/following")
    fun getFollowing(@PathVariable userId: Long): List<Long> {
        return followService.getFollowing(userId)
    }
}