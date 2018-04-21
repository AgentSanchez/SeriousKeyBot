
# SeriousKeyBot
Discord Bot for giving out private keys to group users.

Latest Build : [Download](https://github.com/AgentSanchez/SeriousKeyBot/blob/master/build/libs/serious-keybot-1.2.jar?raw=true)


# Requirements for running
 - mySQL Database
 - a keys.txt files full of keys, one key per line.
 - a bot in the Discord Dev website. [Discord Dev Link](https://discordapp.com/developers/applications/me)
 - Invite the bot to your Discord server using this link
	 -  `https://discordapp.com/oauth2/authorize?client_id=CLIENTID&scope=bot`
	 -  Replace the client_id with your client id from your dev portal [Example](https://i.imgur.com/PGWxbAr.png)
 
---
To run just execute the jar with the following arguments

```
java -jar serious-key-bot-1.2-complete.jar "ownerdiscordID" "BotToken" "Bot's Status"
```
Upon running the program once it will generate a database.conf in the same folder. Update that file with your sql database information. Run the bot again and it will  be good to go as long as the startup flags were correct.

This will also generate a log.log file.

# Build requirements
 - Import the `build.gradle` file and everything should work out fine. Just do a gradle build and it should generate fine. It will generate a shadowed jar _-complete.jar_ with all dependencies included.
