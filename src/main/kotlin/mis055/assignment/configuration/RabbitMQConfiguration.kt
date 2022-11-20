package mis055.assignment.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Declarables
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.interceptor.RetryOperationsInterceptor


@Configuration
class RabbitMQConfiguration(private val cachingConnectionFactory: CachingConnectionFactory) {

    companion object {
        const val transactionQueueName: String = "q.transactions"
    }

    @Bean
    fun createTransactionQueue(): Queue {
        return QueueBuilder.durable(transactionQueueName)
            .withArgument("x-dead-letter-exchange", "x.transactions-failure")
            .withArgument("x-dead-letter-routing-key", "fall-back")
            .build()
    }

    @Bean
    fun retryInterceptor(): RetryOperationsInterceptor {
        return RetryInterceptorBuilder.stateless().maxAttempts(3)
            .backOffOptions(2000, 2.0, 100000)
            .recoverer(RejectAndDontRequeueRecoverer())
            .build()
    }


    @Bean
    fun rabbitListenerContainerFactory(
        configurer: SimpleRabbitListenerContainerFactoryConfigurer,
        @Value("\${spring.rabbitmq.autostart:false}") autoStart: Boolean
    ): SimpleRabbitListenerContainerFactory? {
        val factory = SimpleRabbitListenerContainerFactory()
        configurer.configure(factory, cachingConnectionFactory)
        factory.setAutoStartup(autoStart)
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO)
        factory.setAdviceChain(retryInterceptor())
        factory.setDefaultRequeueRejected(false)
        return factory
    }

    @Bean
    fun createDeadLetterSchema(): Declarables? {
        return Declarables(
            DirectExchange("x.transactions-failure"),
            Queue("q.fall-back-transactions"),
            Binding(
                "q.fall-back-transactions",
                Binding.DestinationType.QUEUE,
                "x.transactions-failure",
                "fall-back",
                null
            )
        )
    }

    @Bean
    fun converter(objectMapper: ObjectMapper): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }

    @Bean
    fun rabbitTemplate(converter: Jackson2JsonMessageConverter): RabbitTemplate {
        val template = RabbitTemplate(cachingConnectionFactory)
        template.messageConverter = converter
        return template
    }
}