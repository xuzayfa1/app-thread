package uz.zero.follow

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "user-service",
    url = "http://localhost:8082"
)
interface UserClient {
    @GetMapping("/api/v1/users/{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<Any>
}