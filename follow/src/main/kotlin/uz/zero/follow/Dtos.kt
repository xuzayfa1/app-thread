package uz.zero.follow

data class FollowRequest(
    val followerId: Long,
    val followingId: Long
)

data class FollowResponse(
    val message: String,
)

data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}