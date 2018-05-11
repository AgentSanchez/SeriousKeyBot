package net.adamsanchez.discord.seriouskeybot;


import net.adamsanchez.discord.seriouskeybot.commands.keyRequestCommand;
import net.adamsanchez.discord.seriouskeybot.util.CC;
import net.adamsanchez.discord.seriouskeybot.util.U;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adam Sanchez on 4/11/2018.
 */
public class Bot {

    private JDA jda;
    private static Bot instance;
    private KeyManager keyManager;
    private TransactionHandler transactionHandler;
    private PreviousUsers previousUsers;
    private static boolean debug = false;
    private String ownerID = "";

    private static Logger logger;

    public static void main(String[] args){
        AnsiConsole.systemInstall();
        try {
            //First Try Accessing Database



            if(args.length > 4){
                debug =  Boolean.parseBoolean(args[4]);
            }
            if(args.length > 3){
                Bot bot = new Bot(args[0], args[1], args[2], Boolean.parseBoolean(args[3]));
            } else {
                Bot bot = new Bot(args[0], args[1], args[2]);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Make sure you are providing all 3 parameters: ownerID, token, and status");
        }
        AnsiConsole.systemUninstall();

    }
    public Bot(){
        logger = LoggerFactory.getLogger(Bot.class);
        logger.info("\n" + CC.GREEN_BRIGHT +  CC.logo() + CC.RESET);
        instance = this;
    }
    public Bot(String ownerID, String token, String status) {
        this(ownerID, token, status, false);
    }
    public Bot(String ownerID, String token, String status, boolean importOldUsers) {
        logger = LoggerFactory.getLogger(Bot.class);
        logger.info("\n" + CC.GREEN_BRIGHT +  CC.logo() + CC.RESET);
        instance = this;
        keyManager = new KeyManager();
        transactionHandler = new TransactionHandler();
        ownerID = ownerID;
        if(!transactionHandler.isWorking()){
            U.error(CC.RED + "Could not load up the database, the bot will not continue loading!!" + CC.YELLOW + " Check your database.conf file" );
            return;
        }
        if(importOldUsers){

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("There are " + jda.getUsers().size() + " users");
            TransactionImporter importer = new TransactionImporter();
        }

        EventWaiter waiter = new EventWaiter();
        CommandClientBuilder client = new CommandClientBuilder();
        client.useDefaultGame();
        client.setOwnerId(ownerID);
        client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
        client.setPrefix("!");
        client.setStatus(OnlineStatus.DO_NOT_DISTURB);
        client.setGame(Game.playing(status));
        client.addCommands(new keyRequestCommand());

        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .addEventListener(waiter)
                    .addEventListener(client.build())
                    .buildAsync();
        } catch(LoginException e) {
            System.out.println(e.getMessage());
        }





    }

    public static Bot getInstance(){
        return instance;
    }

    public KeyManager getKeyManager(){
        return keyManager;
    }

    public TransactionHandler getTransactionHandler() {
        return transactionHandler;
    }

    public boolean isDebugging(){
        return debug;
    }

    public Logger getLogger(){
        return logger;
    }

    public String getOwnerID(){
        return ownerID;
    }
}
