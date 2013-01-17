package com.iCo6.handlers;

import java.util.LinkedHashMap;

import com.iCo6.command.Handler;
import com.iCo6.command.Parser.Argument;
import com.iCo6.command.exceptions.InvalidUsage;

import com.iCo6.iConomy;
import com.iCo6.system.Account;
import com.iCo6.system.Accounts;

import com.iCo6.util.Messaging;
import com.iCo6.util.Template;

import org.bukkit.command.CommandSender;

public class Take extends Handler {
    private Accounts Accounts = new Accounts();

    public Take(iConomy plugin) {
        super(plugin, plugin.Template);
    }

    @Override
    public boolean perform(CommandSender sender, LinkedHashMap<String, Argument> arguments) throws InvalidUsage {
        if(!hasPermissions(sender, "take"))
            throw new InvalidUsage("У Вас недостаточно прав.");

        String name = arguments.get("name").getStringValue();
        String tag = template.color(Template.Node.TAG_MONEY);
        Double amount;

        if(name.equals("0"))
            throw new InvalidUsage("Параметр <white>игрок<rose> не найден: /money take <игрок> <количество>");

        if(arguments.get("amount").getStringValue().equals("empty"))
            throw new InvalidUsage("Параметр <white>количество<rose> не найден: /money take <игрок> <количество>");

        try {
            amount = arguments.get("amount").getDoubleValue();
        } catch(NumberFormatException e) {
            throw new InvalidUsage("Параметр <white>количество<rose> задан неверно. Данный параметр может принимать только числовое значение типа double.");
        }

        if(Double.isInfinite(amount) || Double.isNaN(amount))
            throw new InvalidUsage("Параметр <white>количество<rose> задан неверно. Данный параметр может принимать только числовое значение типа double.");

        if(!Accounts.exists(name)) {
            template.set(Template.Node.ERROR_ACCOUNT);
            template.add("name", name);

            Messaging.send(sender, tag + template.parse());
            return false;
        }

        Account account = new Account(name);
        account.getHoldings().subtract(amount);

        template.set(Template.Node.PLAYER_DEBIT);
        template.add("name", name);
        template.add("amount", iConomy.format(amount));

        Messaging.send(sender, tag + template.parse());
        return false;
    }
}
