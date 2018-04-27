package net.adamsanchez.discord.seriouskeybot;

import net.adamsanchez.discord.seriouskeybot.data.Database;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 * Created by Adam Sanchez on 4/26/2018.
 */
public class TransactionHandlerTest {



    @Test
    public void test(){

        TransactionHandler th = new TransactionHandler();
        KeyManager keyManager = new KeyManager();
        for(int ix = 1500; ix > 0 ; ix--){
            if (keyManager.hasKey()) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            wait(ThreadLocalRandom.current().nextInt(0,200));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.run();

            }
        }


    }

    @Test
    public void generateKeys(){
        Path keyFile = Paths.get("keys.txt");
        try {
            FileWriter fileStream = new FileWriter(keyFile.toFile());
            BufferedWriter out = new BufferedWriter(fileStream);

            for(int x  = 0; x < 1500 ; x++){
                out.write(UUID.randomUUID().toString());
                out.newLine();
            }
            out.close();
        } catch (Exception e){
            System.out.println("Error");
        }
    }

}