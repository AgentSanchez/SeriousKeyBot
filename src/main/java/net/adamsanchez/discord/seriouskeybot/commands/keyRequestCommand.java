package net.adamsanchez.discord.seriouskeybot.commands;


import net.adamsanchez.discord.seriouskeybot.Bot;
import net.adamsanchez.discord.seriouskeybot.TransactionHandler;
import net.adamsanchez.discord.seriouskeybot.KeyManager;
import net.adamsanchez.discord.seriouskeybot.util.CC;
import net.adamsanchez.discord.seriouskeybot.util.U;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Adam Sanchez on 4/11/2018.
 */
public class keyRequestCommand extends Command {
    private KeyManager keyManager = Bot.getInstance().getKeyManager();
    private Set activeRequests;

    public keyRequestCommand() {
        this.name = "request";
        this.aliases = new String[]{"keyRequest", "request-beta-key", "keyrequest", "canihaskey", "pleaseohpleasekeyme", "willthegodsgracemewithkeysplease"};
        this.help = "Use this to request a beta key if you don't have one already!";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        activeRequests = new HashSet();
    }

    @Override
    protected void execute(CommandEvent event) {
        Bot bot = Bot.getInstance();
        User user = event.getAuthor();
        TransactionHandler transactionHandler = bot.getTransactionHandler();
        String mention = "<@" + user.getId() + "> ";
        U.info(CC.WHITE_UNDERLINED + user.getName() + " has requested a key.");
        synchronized (activeRequests) {
            if (!activeRequests.contains(user.getId())) {
                activeRequests.add(user.getId());
                event.reply(mention + "Thanks for your request it is being processed");
                U.info("began processing for " + user.getName() + ".");
            } else {
                event.reply(mention + "I'm sorry but your key isn't ready yet, please wait while we process your request.");
                U.info(user.getName() + " requested a key before processing has terminated");
                return;
            }
        }
        if (!transactionHandler.hasUser(user.getId()) || user.getId().equals(Bot.getInstance().getOwnerID())) {
            synchronized (keyManager) {
                if (keyManager.hasKey()) {

                    String lockedKey = keyManager.pop();
                    event.replyInDm("Thanks for joining our community! " +
                                    "By receiving this key you agree not to sell or exchange it. " +
                                    "Here is your key!`" + lockedKey + "`!",
                            success -> {
                                U.info(CC.GREEN + "+ " + CC.RESET + "Giving key - " + lockedKey + " -- to " + user.getName() + " -- " + user.getId());
                                transactionHandler.postTransaction(user.getId(), lockedKey);
                                event.reactSuccess();
                                event.replySuccess(mention + "I've sent you your private key in a private message!");
                                synchronized (activeRequests) {
                                    activeRequests.remove(user.getId());
                                }
                            }, failure -> {
                                synchronized (keyManager) {
                                    keyManager.returnKey(lockedKey);
                                }
                                U.info(CC.RED + "- " + CC.RESET + "Unable to send message to user --- Key: " + lockedKey + "returning to file.");
                                event.reactError();
                                event.replyWarning(mention + "I can't send you a message in DM Please modify your settings :(");
                                synchronized (activeRequests) {
                                    activeRequests.remove(user.getId());
                                }
                            });
                    return;


                } else {
                    event.reply(mention + "I'm sorry, I've run out of keys :( please try again later!");
                    event.reactError();
                    synchronized (keyManager){
                        activeRequests.remove(user.getId());
                    }
                }
            }
        } else {
            event.reply(mention + "I'm sorry but I've already given you a key!");
            event.reactError();
            synchronized (keyManager){
                activeRequests.remove(user.getId());
            }
        }

    }

}
