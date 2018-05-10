package net.adamsanchez.discord.seriouskeybot;

import net.adamsanchez.discord.seriouskeybot.data.Database;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Adam Sanchez on 4/26/2018.
 */
public class TransactionHandlerTest {

    @Test
    public void testAsync() {
        //Initiate DB
        TransactionHandler th = new TransactionHandler();
        KeyManager km = new KeyManager();

        //Initiate a random number of requests
        for (int ix = 0; ix < ThreadLocalRandom.current().nextInt(0, 40); ix++) {
            Runnable task = () -> {
                //have it wait an arbitrary amount of milliseconds between 0 and 5 seconds
                try {
                    Thread.currentThread().wait(ThreadLocalRandom.current().nextLong(0, 5000000000L));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Initiate a Request for information

                String fakePlayerID = UUID.randomUUID().toString();
                String fakePlayerName = "FakePlayer-" + Thread.currentThread().getName();
                if(km.hasKey() && !th.hasUser(fakePlayerID)){
                    try {
                        //After Information is received initiate a small random delay to simulate latency
                        Thread.currentThread().wait(ThreadLocalRandom.current().nextLong(50000000L,150000000L));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Attempt to post the transaction
                    th.postTransaction(fakePlayerID, km.pop());

                }

            };
        }


    }


}