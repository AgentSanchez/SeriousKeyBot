package net.adamsanchez.discord.seriouskeybot;

import net.adamsanchez.discord.seriouskeybot.util.U;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by Adam Sanchez on 4/11/2018.
 */
public class KeyManager {
    Path keys;
    String currentKey = "";
    Scanner buffer;

    public KeyManager() {
        keys = Paths.get("keys.txt");
        updateNext();
    }

    public String peek() {
        return currentKey;
    }

    public String pop() {
        String key = currentKey;
        saveKeys(buffer);
        updateNext();
        return key;
    }

    public boolean hasKey() {
        return !currentKey.equals("") ? true : false;
    }


    public void updateNext() {
        try {
            String line;
            Scanner fileScanner = new Scanner(keys);
            if (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine();
                if (!line.equals("")) {
                    currentKey = line;
                } else {
                    currentKey = "";
                }
                buffer = fileScanner;

            } else {
                currentKey = "";
            }
        } catch (NoSuchFileException e) {
            System.out.println("YOU ARE MISSING THE keys.txt file!! No keys will be given out!");
            currentKey = "";
        } catch (IOException e) {
            e.printStackTrace();
            currentKey = "";
        }
    }

    public void saveKeys(Scanner fileScanner) {
        try {
            FileWriter fileStream = new FileWriter(keys.toFile());
            BufferedWriter out = new BufferedWriter(fileStream);
            while (fileScanner.hasNextLine()) {
                String next = fileScanner.nextLine();
                if (next.equals("\n"))
                    out.newLine();
                else
                    out.write(next);
                out.newLine();
            }
            out.close();
        } catch (Exception e) {

        }
    }

    public void returnKey(String key) {
        synchronized (this) {
            Scanner localBuffer = null;
            try {
                localBuffer = new Scanner(keys);
                FileWriter fileStream = new FileWriter(keys.toFile());
                BufferedWriter out = new BufferedWriter(fileStream);
                //First add the returned key
                out.write(key);
                //Add the rest to file
                while (localBuffer.hasNextLine()) {
                    String next = localBuffer.nextLine();
                    if (next.equals("\n"))
                        out.newLine();
                    else
                        out.write(next);
                    out.newLine();
                }
                out.close();
                U.info("Returned Key: " + key + " to File");

            } catch (NoSuchFileException e) {
                System.out.println("YOU ARE MISSING THE keys.txt file!! Cannot Return Key: " + key + " to File");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
