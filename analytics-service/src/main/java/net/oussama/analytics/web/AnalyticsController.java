package net.oussama.analytics.web;

import lombok.AllArgsConstructor;
import net.oussama.analytics.model.AccountOperation;
import net.oussama.analytics.services.AnalyticsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamOperations() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        analyticsService.addEmitter(emitter);
        return emitter;
    }

    @GetMapping("/accounts/{accountId}/history")
    public List<AccountOperation> getAccountHistory(@PathVariable String accountId) {
        return analyticsService.getAccountHistory(accountId);
    }

    @GetMapping("/accounts/balances")
    public Map<String, java.math.BigDecimal> getAllBalances() {
        return analyticsService.getAccountBalances();
    }
}
