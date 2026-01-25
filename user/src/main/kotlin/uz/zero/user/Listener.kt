package uz.zero.user

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class UserEventTrackingListener(
    private val rabbitTemplate: RabbitTemplate
) {

    @EventListener
    fun onUserCreated(event: UserCreatedEvent) {
        println("Log: User Created Event RabbitMQ ga yuborildi: ${event.username}")
    }
}