# SeriousKeyBot
Discord Bot for giving out private keys to group users.


# Requirements for running
 - mySQL Database
 - a keys.txt files full of keys, one key per line.

```
java -jar serious-key-bot-1.2-complete.jar "ownerdiscordID" "BotToken" "Bot's Status"
```
Upon running the program once it will generate a database.conf in the same folder. Update that file with your sql database information. Run the bot again and it will  be good to go as long as the startup flags were correct.
