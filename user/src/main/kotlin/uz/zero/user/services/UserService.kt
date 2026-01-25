package uz.zero.user.services

import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import uz.zero.user.*

interface UserService {
    fun register(dto: UserRegistrationDto): UserResponseDto
    fun getById(id: Long): UserResponseDto
    fun getAllActive(): List<UserResponseDto>
    fun update(id: Long, dto: UserUpdateDto): UserResponseDto
    fun delete(id: Long)
    fun login(dto: LoginRequestDto): LoginResponseDto
}


@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
) : UserService {

    @Transactional
    override fun register(dto: UserRegistrationDto): UserResponseDto {
        if (userRepository.findByUsernameAndDeletedFalse(dto.username) != null) {
            throw UsernameAlreadyExsistsException()
        }

        if (userRepository.existsByEmailAndDeletedFalse(dto.email)) {
            throw EmailAlreadyExistsException()
        }

        val user = User(
            username = dto.username,
            email = dto.email,
            passwordHash = dto.password // Hozircha oddiy saqlaymiz
        ).apply {
            fullName = dto.fullName
        }

        val savedUser = userRepository.save(user)

        // Event yuborish (role olingan)
        eventPublisher.publishEvent(
            UserCreatedEvent(
                userId = savedUser.id!!,
                username = savedUser.username
            )
        )

        return UserResponseDto.fromEntity(savedUser)
    }
    override fun getById(id: Long): UserResponseDto {
        return userRepository.findByIdAndDeletedFalse(id)
            ?.let { UserResponseDto.fromEntity(it) }
            ?: throw UserNotFoundException()
    }

    override fun getAllActive(): List<UserResponseDto> {
        return userRepository.findAllNotDeleted().map { UserResponseDto.fromEntity(it) }
    }

    @Transactional
    override fun update(id: Long, dto: UserUpdateDto): UserResponseDto {
        val user = userRepository.findByIdAndDeletedFalse(id)
            ?: throw UserNotFoundException()

        // Faqat yuborilgan maydonlarni yangilaymiz
        dto.fullName?.let { user.fullName = it }
        dto.email?.let { user.email = it }

        return UserResponseDto.fromEntity(userRepository.save(user))
    }

    @Transactional
    override fun delete(id: Long) {
        // BaseRepository'dagi trash funksiyasi deleted = true qiladi
        userRepository.trash(id) ?: throw UserNotFoundOrAlreadyExsistsException()
    }

    @Transactional
    override fun login(dto: LoginRequestDto): LoginResponseDto {
        val user = userRepository.findByUsernameAndDeletedFalse(dto.username)
            ?: throw UserNotFoundException()

        // PAROL TEKSHIRISH: Agar mos kelmasa xato otish
        if (dto.password != user.passwordHash) {
            throw InvalidPasswordException()
        }

        return LoginResponseDto(
            user = UserResponseDto.fromEntity(user)
        )
    }
}