package net.oussama.accountservice.web;

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
public class AccountQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/{id}")
    public CompletableFuture<Account> getAccount(@PathVariable String id) {
        return queryGateway.query(
                new GetAccountQuery(id),
                ResponseTypes.instanceOf(Account.class));
    }

    @GetMapping
    public CompletableFuture<List<Account>> getAllAccounts() {
        return queryGateway.query(
                new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(Account.class));
    }
}
