package uz.zero.follow

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

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

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfoResponse(
    val id: Long,
    val fullName: String,
    val username: String,
    val role: String,
)