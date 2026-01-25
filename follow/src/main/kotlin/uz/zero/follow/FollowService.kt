package uz.zero.follow

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

interface FollowService {
    fun follow(followerId: Long, followingId: Long)
    fun unfollow(followerId: Long, followingId: Long)
    fun getFollowers(userId: Long): List<Long>
    fun getFollowing(userId: Long): List<Long>
}

@Service
class FollowServiceImpl(
    private val followRepository: FollowRepository,
    private val userClient: UserClient
) : FollowService {

    @Transactional
    override fun follow(followerId: Long, followingId: Long) {
        try {
            userClient.getUserById(followingId)
        } catch (e: Exception) {
            throw UserNotFoundException()
        }

        if (followerId == followingId) throw YouAreNotFollowToYourselfException()
        val existingFollow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
        if (existingFollow == null) {
            followRepository.save(Follow(followerId, followingId))
        } else if (existingFollow.deleted) {

            existingFollow.deleted = false
            followRepository.save(existingFollow)
        }
    }

    @Transactional
    override fun unfollow(followerId: Long, followingId: Long) {
        val follow = followRepository.findByFollowerIdAndFollowingIdAndDeletedFalse(followerId, followingId)
            ?: throw FollowNotFoundException()
        followRepository.trash(follow.id!!)
    }

    override fun getFollowers(userId: Long): List<Long> {
        return followRepository.findAllByFollowingIdAndDeletedFalse(userId).map { it.followerId }
    }

    override fun getFollowing(userId: Long): List<Long> {
        return followRepository.findAllByFollowerIdAndDeletedFalse(userId).map { it.followingId }
    }
}