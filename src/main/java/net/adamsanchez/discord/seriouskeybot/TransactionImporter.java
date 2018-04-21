package net.adamsanchez.discord.seriouskeybot;

import net.adamsanchez.discord.seriouskeybot.util.CC;
import net.adamsanchez.discord.seriouskeybot.util.U;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Adam Sanchez on 4/11/2018.
 */
public class TransactionImporter {
    private Map<String, String> records;
    private Path storage;

    public TransactionImporter(){
        records = new HashMap<>();
        storage = Paths.get("history.jobj");
        System.out.println("Attempting to load up transactions from object file.... || " + storage.toString());
        if (Files.notExists(storage)){
            U.warn(CC.YELLOW + "NO history.jobj file available");
        } else {
            try {
                loadTransactions();
                System.out.println("Transactions loaded successfully!! Attempting to update the database");
                Iterator it = records.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    Bot.getInstance().getTransactionHandler().postTransaction(pair.getKey().toString(), pair.getValue().toString());
                }
                U.info(CC.GREEN + "Finished migrating all data ^.^");
            } catch (IOException e) {
                System.out.println(CC.RED + "ahahahahaha We Couldn't load up the transactions.");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println(CC.RED + "Well crap that is noooot a hash map! GO slap the dev!");
            }
        }

    }


    public boolean hasUser(String userID){
        return records.containsKey(userID);
    }


    private void saveTransactions() throws IOException {
        System.out.println("Saving Transaction");
        FileOutputStream fileOutputStream = new FileOutputStream(storage.toFile());
        ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(records);
        objectOutputStream.close();

    }

    private void loadTransactions() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream  = new FileInputStream(storage.toFile());
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        records = (HashMap<String, String>) objectInputStream.readObject();
        objectInputStream.close();
    }

}
