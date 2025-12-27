package net.oussama.accountservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.oussama.accountservice.entities.Account;
import net.oussama.accountservice.entities.AccountTransaction;
import net.oussama.accountservice.entities.TransactionType;
import net.oussama.accountservice.repositories.AccountRepository;
import net.oussama.accountservice.repositories.AccountTransactionRepository;
import net.oussama.coreapi.events.*;
import net.oussama.coreapi.queries.GetAccountQuery;
import net.oussama.coreapi.queries.GetAllAccountsQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountEventHandler {
    private final AccountRepository accountRepository;
    private final AccountTransactionRepository transactionRepository;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("Projection: AccountCreatedEvent: {}", event.getId());
        Account account = new Account(
                event.getId(),
                event.getInitialBalance(),
                event.getCurrency(),
                event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event) {
        log.info("Projection: AccountActivatedEvent: {}", event.getId());
        Account account = accountRepository.findById(event.getId()).orElseThrow();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent event) {
        log.info("Projection: AccountCreditedEvent: {}", event.getId());
        Account account = accountRepository.findById(event.getId()).orElseThrow();
        account.setBalance(account.getBalance().add(event.getAmount()));
        accountRepository.save(account);

        AccountTransaction transaction = new AccountTransaction(
                null,
                Instant.now(),
                event.getAmount(),
                event.getCurrency(),
                TransactionType.CREDIT,
                account);
        transactionRepository.save(transaction);
    }

    @EventHandler
    public void on(AccountDebitedEvent event) {
        log.info("Projection: AccountDebitedEvent: {}", event.getId());
        Account account = accountRepository.findById(event.getId()).orElseThrow();
        account.setBalance(account.getBalance().subtract(event.getAmount()));
        accountRepository.save(account);

        AccountTransaction transaction = new AccountTransaction(
                null,
                Instant.now(),
                event.getAmount(),
                event.getCurrency(),
                TransactionType.DEBIT,
                account);
        transactionRepository.save(transaction);
    }

    @QueryHandler
    public Account on(GetAccountQuery query) {
        return accountRepository.findById(query.getId()).orElse(null);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }
}
