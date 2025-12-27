package net.oussama.accountservice.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.oussama.accountservice.entities.Account;
import net.oussama.coreapi.queries.GetAccountQuery;
import net.oussama.coreapi.queries.GetAllAccountsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
@Tag(name = "Account Queries", description = "Account query operations (CQRS Read Side)")
public class AccountQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieve account details from the read model")
    public CompletableFuture<Account> getAccount(@PathVariable String id) {
        return queryGateway.query(
                new GetAccountQuery(id),
                ResponseTypes.instanceOf(Account.class));
    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieve all accounts from the read model")
    public CompletableFuture<List<Account>> getAllAccounts() {
        return queryGateway.query(
                new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(Account.class));
    }
}
