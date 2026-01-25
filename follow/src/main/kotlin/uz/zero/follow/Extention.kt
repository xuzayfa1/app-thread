package uz.zero.follow

fun Follow.toResponse() = FollowResponse(
    id = this.id!!,
    followerId = this.followerId,
    followingId = this.followingId,
    followedAt = this.createdDate
)