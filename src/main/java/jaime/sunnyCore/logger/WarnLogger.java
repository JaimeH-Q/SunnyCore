package jaime.sunnyCore;


import static jaime.sunnyCore.utils.messages.MessageUtils.colorize;

public class WarnLogger {
    private static SunnyCore plugin;

    private WarnLogger(){}

    public static void loadPluginInstance(SunnyCore instance) {
        if (plugin != null) {
            throw new IllegalStateException("UNO: Warnlogger was already loaded!");
        }

        plugin = instance;
    }

    /***
     * Logs a message, whether debug is active or not
     * @param message
     */
    public static void log(String message){
        if (plugin == null) {
            throw new IllegalStateException("UNO: Warnlogger wasn't prepared yet!");
        }

        plugin.getServer().getConsoleSender().sendMessage(colorize(replaceHolders("%p%" + message.replace("%p%", ""))));
    }

    public static void debug(String message){
        if (plugin == null) {
            throw new IllegalStateException("UNO: Warnlogger wasn't prepared yet!");
        }

        if(!plugin.isDebug()) return;
        plugin.getServer().getConsoleSender().sendMessage(colorize(replaceHolders("%p%" + message.replace("%p%", ""))));
    }


    private static String replaceHolders(String message){
        return message
                .replace("%p%", "&#ffc540&lU&#feaa39&lN&#fd8e31&lO &8>&7 ");
    }
}
