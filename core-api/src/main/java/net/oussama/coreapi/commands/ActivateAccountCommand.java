package net.oussama.coreapi.commands;

import lombok.Getter;

public class ActivateAccountCommand extends BaseCommand<String> {
    public ActivateAccountCommand(String id) {
        super(id);
    }
}
