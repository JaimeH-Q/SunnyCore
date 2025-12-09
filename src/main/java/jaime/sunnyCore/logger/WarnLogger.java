package jaime.sunnyCore.logger;


import jaime.sunnyCore.SunnyCore;

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
    public static void log(DebugablePlugin debuggerPlugin, String message){
        if (plugin == null) {
            throw new IllegalStateException("UNO: Warnlogger wasn't prepared yet!");
        }

        plugin.getServer().getConsoleSender().sendMessage(colorize(replaceHolders(debuggerPlugin, "%p%" + message)));
    }

    public static void debug(DebugablePlugin debuggerPlugin, String message){
        if (plugin == null) {
            throw new IllegalStateException("UNO: Warnlogger wasn't prepared yet!");
        }
        if(!debuggerPlugin.isDebug()) return;
        plugin.getServer().getConsoleSender().sendMessage(colorize(replaceHolders(debuggerPlugin, "%p%" + message)));
    }


    private static String replaceHolders(DebugablePlugin debuggerPlugin, String message){
        return message
                .replace("%p%", "&8[&#ffb82a&lS&#4ad1fd&lC&8] " + debuggerPlugin.getPrefix());
    }
}
