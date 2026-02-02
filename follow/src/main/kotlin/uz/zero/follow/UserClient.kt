package uz.zero.follow

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "user-service",url = "\${services.hosts.user}", configuration = [FeignOAuth2TokenConfig::class])
interface UserClient {
    @GetMapping("/api/v1/users/{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<Any>
}