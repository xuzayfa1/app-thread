package uz.zero.user

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.zero.user.services.UserService

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@RequestBody dto: UserRegistrationDto): ResponseEntity<UserResponseDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(dto))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserResponseDto> {
        return ResponseEntity.ok(userService.getById(id))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: UserUpdateDto
    ): ResponseEntity<UserResponseDto> {
        return ResponseEntity.ok(userService.update(id, dto))
    }

    @GetMapping
    fun getAllActive(): ResponseEntity<List<UserResponseDto>> {
        return ResponseEntity.ok(userService.getAllActive())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        userService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/login")
    fun login(@RequestBody dto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        return ResponseEntity.ok(userService.login(dto))
    }
}