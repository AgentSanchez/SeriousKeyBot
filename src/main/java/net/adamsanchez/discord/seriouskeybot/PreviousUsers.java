package net.adamsanchez.discord.seriouskeybot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Adam Sanchez on 4/12/2018.
 */
public class PreviousUsers {
    private Path olduserfile;
    private List<User> serverUsers;

    public PreviousUsers(JDA jda) {
        olduserfile = Paths.get("oldusers.txt");
        serverUsers = jda.getUsers();
        compare();
    }

    public void compare() {
        try {
            Scanner fileScanner = new Scanner(olduserfile);
            int count = 0;
            while (fileScanner.hasNextLine()) {
                TransactionHandler th = Bot.getInstance().getTransactionHandler();
                String name = fileScanner.nextLine();
                System.out.println("Checking for ... " + name );
                String id = hasName(name);
                if ( id !=null){
                    th.postTransaction(id, "OldUser");
                    count += 1;
                }
            }
            System.out.println("A total of " + count + " users were matched.");
            //olduserfile.toFile().delete();
        } catch (NoSuchFileException e) {
            System.out.println("YOU ARE MISSING THE oldusers.txt file!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String hasName(String name) {
        for (User user : serverUsers) {
            if (user.getName().equals(name)) {
                System.out.println("Match Found");
                return user.getId();
            }
        }
        return null;
    }
}
