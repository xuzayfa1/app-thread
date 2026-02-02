package uz.zero.user

import org.springframework.web.bind.annotation.*
import uz.zero.user.services.UserService

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@RequestBody dto: UserRegistrationDto): UserResponseDto {
        return userService.register(dto)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): UserResponseDto {
        return userService.getById(id)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: UserUpdateDto
    ): UserResponseDto {
        return userService.update(id, dto)
    }

    @GetMapping
    fun getAllActive(): List<UserResponseDto> {
        return userService.getAllActive()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Unit {
        userService.delete(id)
        return
    }

    @PostMapping("/login")
    fun login(@RequestBody dto: LoginRequestDto): LoginResponseDto {
        return userService.login(dto)
    }
}