package net.oussama.accountservice.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Account Commands", description = "Account command operations (CQRS Write Side)")
public class AccountCommandController {
    private final CommandGateway commandGateway;

    @PostMapping
    @Operation(summary = "Create a new account", description = "Creates a new bank account with initial balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequest request) {
        return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()));
    }

    @PutMapping("/credit")
    @Operation(summary = "Credit an account", description = "Add funds to an existing account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account credited successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or account not activated")
    })
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequest request) {
        return commandGateway.send(new CreditAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()));
    }

    @PutMapping("/debit")
    @Operation(summary = "Debit an account", description = "Withdraw funds from an existing account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account debited successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request, insufficient balance, or account not activated")
    })
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
