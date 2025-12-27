package net.oussama.coreapi.events;

import lombok.Getter;
import net.oussama.coreapi.enums.AccountStatus;

public class AccountActivatedEvent extends BaseEvent<String> {
    @Getter
    private final AccountStatus status;

    public AccountActivatedEvent(String id, AccountStatus status) {
        super(id);
        this.status = status;
    }
}
