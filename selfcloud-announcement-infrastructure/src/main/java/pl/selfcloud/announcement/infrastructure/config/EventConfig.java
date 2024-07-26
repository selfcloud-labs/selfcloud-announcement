package pl.selfcloud.announcement.infrastructure.config;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.selfcloud.announcement.domain.service.publisher.AnnouncementDomainEventPublisher;

@Configuration
@Import({
    TramEventsPublisherConfiguration.class,
    TramJdbcKafkaConfiguration.class,
    TramMessageProducerJdbcConfiguration.class,
    OptimisticLockingDecoratorConfiguration.class,
    TramMessageProducerJdbcConfiguration.class})
public class EventConfig {

  @Bean
  public AnnouncementDomainEventPublisher orderAggregateEventPublisher(DomainEventPublisher eventPublisher) {
    return new AnnouncementDomainEventPublisher(eventPublisher);
  }
}

