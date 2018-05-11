package net.adamsanchez.discord.seriouskeybot;

import net.adamsanchez.discord.seriouskeybot.data.Database;
import net.adamsanchez.discord.seriouskeybot.data.TransactionRecord;
import net.adamsanchez.discord.seriouskeybot.util.CC;
import net.adamsanchez.discord.seriouskeybot.util.U;

import java.nio.file.Path;

/**
 * Created by Adam Sanchez on 4/11/2018.
 */
public class TransactionHandler {
    private Path storage;
    private Database db;
    private boolean working = false;

    public TransactionHandler(){
        db = new Database();
        if(!db.testDB()) working = false;
        working = true;
        db.createUserTable();
    }

    public boolean hasUser(String playerID){
        U.info("Looking up transaction info for: " + playerID);
        TransactionRecord tr = db.getTransaction(playerID);
        if(tr != null) {
            U.info(CC.RED + "- " + CC.RESET + "Player already had a key");
            return true;
        }
        U.info("Player Not Found");
        return false;
    }

    public TransactionRecord lookUpTransaction(String playerID){
        TransactionRecord tr = db.getTransaction(playerID);
        return tr;
    }

    public void postTransaction(String userID, String gameKey){
        db.postTransaction(new TransactionRecord(userID, gameKey));
    }


    public boolean isWorking(){
        return working;
    }
}
