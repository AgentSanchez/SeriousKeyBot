package net.adamsanchez.discord.seriouskeybot.data;

/**
 * Created by Adam Sanchez on 4/20/2018.
 */
public class TransactionRecord {
    private String playerID;
    private String steamKey;

    public TransactionRecord(String playerID, String steamKey){
        this.playerID = playerID;
        this.steamKey = steamKey;
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getSteamKey() {
        return steamKey;
    }


}
