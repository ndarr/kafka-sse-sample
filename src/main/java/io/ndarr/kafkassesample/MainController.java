package io.ndarr.kafkassesample;

import io.ndarr.kafkassesample.model.ROIban;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/sse")
@Slf4j
public class MainController {

    private final IbanNotificationService ibanNotificationService;

    public MainController(IbanNotificationService ibanNotificationService) {
        this.ibanNotificationService = ibanNotificationService;
    }

    @GetMapping(path = "iban", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ROIban> getIban(@RequestParam String customerNumber) {
        log.info("{} waiting for IBAN!", customerNumber);
        Flux<ROIban> ibanStream = this.ibanNotificationService.getEventPublisher()
            .map(roIban -> {
                log.info("Here: {}", roIban);
                return roIban;
            })
            .filter(roIban -> this.checkMessage(roIban, customerNumber))
            .doOnSubscribe(subscription -> log.info("Subscriber {}", customerNumber));
        return Mono.from(ibanStream);
    }

    private boolean checkMessage(ROIban iban, String customerNumber) {
        log.info("Checking: {}", iban);
        return customerNumber.equals(iban.customerNumber());
    }

}
