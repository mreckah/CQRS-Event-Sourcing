package net.oussama.accountservice.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.oussama.coreapi.commands.CreateAccountCommand;
import net.oussama.coreapi.commands.CreditAccountCommand;
import net.oussama.coreapi.commands.DebitAccountCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountCommandController {
    private final CommandGateway commandGateway;

    @PostMapping
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequest request) {
        return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()));
    }

    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequest request) {
        return commandGateway.send(new CreditAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()));
    }

    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequest request) {
        return commandGateway.send(new DebitAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()));
    }

    @Data
    public static class CreateAccountRequest {
        private BigDecimal initialBalance;
        private String currency;
    }

    @Data
    public static class CreditAccountRequest {
        private String accountId;
        private BigDecimal amount;
        private String currency;
    }

    @Data
    public static class DebitAccountRequest {
        private String accountId;
        private BigDecimal amount;
        private String currency;
    }
}
