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

/**
 * Created by Adam Sanchez on 4/11/2018.
 */
public class keyRequestCommand extends Command {

    public keyRequestCommand() {
        this.name = "request";
        this.aliases = new String[]{"keyRequest", "request-beta-key", "keyrequest", "canihaskey", "pleaseohpleasekeyme", "willthegodsgracemewithkeysplease"};
        this.help = "Use this to request a beta key if you don't have one already!";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
    }

    @Override
    protected void execute(CommandEvent event) {
        Bot bot = Bot.getInstance();
        User user = event.getAuthor();
        KeyManager keyManager = bot.getKeyManager();
        TransactionHandler transactionHandler = bot.getTransactionHandler();
        String mention = "<@" + user.getId() + "> ";
        U.info(CC.WHITE_UNDERLINED + user.getName() + " has requested a key.");
        if (!transactionHandler.hasUser(user.getId()) || user.getId().equals(Bot.getInstance().getOwnerID())) {
            if (keyManager.hasKey()) {
                synchronized (keyManager) {
                    event.replyInDm("Thanks for joining our community! " +
                                    "By receiving this key you agree not to sell or exchange it. " +
                                    "Here is your key!`" + keyManager.peek() + "`!",
                            success -> {
                                U.info(CC.GREEN + "+ " + CC.RESET + "Giving key - " + keyManager.peek() + " -- to " + user.getName() + " -- " + user.getId());
                                transactionHandler.postTransaction(user.getId(), keyManager.pop());
                                event.reactSuccess();
                                event.replySuccess(mention + "I've sent you your private key in a private message!");
                            }, failure -> {
                                event.reactError();
                                event.replyWarning(mention + "I can't send you a message in DM Please modify your settings :(");
                            });

                }
            } else {
                event.reply(mention + "I'm sorry, I've run out of keys :( please try again later!");
                event.reactError();
            }
        } else {
            event.reply(mention + "I'm sorry but I've already given you a key!");
            event.reactError();
        }

    }

}
