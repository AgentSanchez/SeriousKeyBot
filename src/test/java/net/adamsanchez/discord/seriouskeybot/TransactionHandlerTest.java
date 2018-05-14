package net.adamsanchez.discord.seriouskeybot;

import net.adamsanchez.discord.seriouskeybot.data.Database;
import net.adamsanchez.discord.seriouskeybot.util.U;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Adam Sanchez on 4/26/2018.
 */
public class TransactionHandlerTest {

    @Test
    public void testAsync() {
        generateKeys();
        Bot bot = new Bot();
        //Initiate DB
        TransactionHandler th = new TransactionHandler();
        KeyManager km = new KeyManager();
        List<Thread> threadList = new LinkedList();
        int testNumber = 0;
        testNumber = ThreadLocalRandom.current().nextInt(0, 1000);
        U.info("Attempting " + testNumber + " users");
        CountDownLatch cdl = new CountDownLatch(testNumber);
        //Initiate a random number of requests
        for (int ix = 0; ix < testNumber; ix++) {
            U.info("Making a new users");
            /////////////////////////////////////////
            Runnable r = () -> {
                System.out.println("New User Startup");
                String fakePlayerID = "";
                try {
                    fakePlayerID = ThreadLocalRandom.current().nextLong(100000000000L, 88888888888888L) + "";
                } catch (Exception e) {
                    System.out.println("Well Fuck");
                }
                System.out.println("new User ID Chosen " + fakePlayerID);
                String fakePlayerName = "FakePlayer-" + Thread.currentThread().getName();

                System.out.println("Begin Fake Player Request" + fakePlayerName);
                //have it wait an arbitrary amount of milliseconds between 0 and 5 seconds
                try {
                    long sleep = ThreadLocalRandom.current().nextLong(500L, 5000L);
                    Thread.currentThread().sleep(sleep);
                } catch (InterruptedException e) {
                    System.out.println("ERROR");

                }

                //Initiate a Request for information
                System.out.println("Finished Delay for " + fakePlayerName);
                synchronized(km) {
                    if (km.hasKey() && !th.hasUser(fakePlayerID)) {
                        try {
                            //After Information is received initiate a small random delay to simulate latency
                            long sleep = ThreadLocalRandom.current().nextLong(500L, 1500L);
                            Thread.currentThread().sleep(sleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Attempting to post transaction for " + fakePlayerName);
                        //Attempt to post the transaction
                        th.postTransaction(fakePlayerID, km.pop());


                    } else {
                        if (!km.hasKey()) {
                            U.info("No keys Left For " + fakePlayerName);
                        } else {
                            U.info("Already key");
                        }
                    }
                }
                cdl.countDown();
            };
            //////////////////////////////////////
            new Thread(r).start();
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void generateKeys() {
        Path keys = Paths.get("keys.txt");
        try {
            FileWriter fileStream = new FileWriter(keys.toFile());
            BufferedWriter out = new BufferedWriter(fileStream);
            int ix = 0;
            while (ix < 10000) {
                out.write("FAKE" + UUID.randomUUID().toString());
                out.newLine();
                ix++;
            }
            out.close();
        } catch (Exception e) {

        }


    }

    @Test
    public void syncTest() {
        Bot bot = new Bot();
        //Initiate DB
        TransactionHandler th = new TransactionHandler();
        KeyManager km = new KeyManager();
        CountDownLatch cdl = new CountDownLatch(2);
        Runnable postingProcess = () -> {
            System.out.println("TEST");
            for (int ix = 0; ix < 1; ix++) {
                System.out.println("POSTING");
                th.postTransaction(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            }
        };

        Runnable lookUpProcess = () -> {
            for (int ix = 0; ix < 1000; ix++) {
                String id = UUID.randomUUID().toString();
                th.hasUser(id);
                U.info("Player " + (ix + 1));

                System.out.println("POSTING");
                th.postTransaction(id, UUID.randomUUID().toString());

                Assert.assertNotNull(th.lookUpTransaction(id));
            }
            cdl.countDown();


        };

        Runnable lookUpProcess2 = () -> {
            for (int ix = 0; ix < 1000; ix++) {
                String id = UUID.randomUUID().toString();
                th.hasUser(id);
                U.info("2Player " + (ix + 1));

                System.out.println("2POSTING");
                th.postTransaction(id, UUID.randomUUID().toString());

                Assert.assertNotNull(th.lookUpTransaction(id));
            }
            cdl.countDown();
        };

        new Thread(lookUpProcess).start();
        new Thread(lookUpProcess2).start();
        postingProcess.run();

        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Process finished");


    }

    @Test
    public void amICrazy() {
        Runnable r = () -> {
            int test = ThreadLocalRandom.current().nextInt(0, 3000);
            System.out.println("waiting for " + test + "ms");
            try {
                Thread.currentThread().sleep(test);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Done");
        };
        new Thread(r).start();

    }


}