package net.adamsanchez.discord.seriouskeybot.util;

import net.adamsanchez.discord.seriouskeybot.Bot;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Adam Sanchez on 3/23/2018.
 */
public class U {

    public static void space(){
        Bot.getInstance().getLogger().info("______________________________" + CC.RESET);
    }

    public static void info(String info){
        Bot.getInstance().getLogger().info(info + CC.RESET);
    }

    public static void debug(String debug){
        if(Bot.getInstance().isDebugging()){
            Bot.getInstance().getLogger().info("[Debug]: " + debug + CC.RESET);
        }
    }
    public static void warn(String warn){
        Bot.getInstance().getLogger().error(warn + CC.RESET);
    }
    public static void error(String error){
        Bot.getInstance().getLogger().error(error + CC.RESET);
    }

    public static void error(String error, Exception e){
        Bot.getInstance().getLogger().error(error + CC.RESET);
        Bot.getInstance().getLogger().error(e.getCause() + CC.RESET);
    }

    public static void debugMap(Map<String,String> map){
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            U.debug(pair.getKey().toString() + " ::: " + pair.getValue().toString() + CC.RESET);
        }
    }

    public static void debugSet(Set<String> set) {
        for (String string : set) {
            U.debug(string + CC.RESET);
        }
    }

}
