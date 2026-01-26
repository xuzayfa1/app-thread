package uz.zero.follow

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

interface FollowService {
    fun follow(followerId: Long, followingId: Long): FollowResponse
    fun unfollow(followerId: Long, followingId: Long): FollowResponse
    fun getFollowers(userId: Long): List<Long>
    fun getFollowing(userId: Long): List<Long>
}

@Service
class FollowServiceImpl(
    private val followRepository: FollowRepository,
    private val userClient: UserClient,
) : FollowService {

    @Transactional
    override fun follow(followerId: Long, followingId: Long): FollowResponse {
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
        return FollowResponse(
            message = "siz ushbu foydalanuvchiga muvaffaqiyatli obuna bo'ldingiz",
        )
    }

    @Transactional
    override fun unfollow(followerId: Long, followingId: Long): FollowResponse {
        try {
            userClient.getUserById(followerId)
            userClient.getUserById(followingId)
        } catch (e: Exception) {
            throw UserNotFoundException()
        }
        val follow = followRepository.findByFollowerIdAndFollowingIdAndDeletedFalse(followerId, followingId)
            ?: throw FollowNotFoundException()
        followRepository.trash(follow.id!!)
        return FollowResponse(
            message = "siz ushbu foydalanuvchidan muvaffaqiyatli chiqdingiz",
        )
    }

    override fun getFollowers(userId: Long): List<Long> {
        try {
            userClient.getUserById(userId)
        } catch (e: Exception) {
            throw UserNotFoundException()
        }
        return followRepository.findAllByFollowingIdAndDeletedFalse(userId).map { it.followerId }
    }

    override fun getFollowing(userId: Long): List<Long> {
        try {
            userClient.getUserById(userId)
        } catch (e: Exception) {
            throw UserNotFoundException()
        }
        return followRepository.findAllByFollowerIdAndDeletedFalse(userId).map { it.followingId }
    }
}