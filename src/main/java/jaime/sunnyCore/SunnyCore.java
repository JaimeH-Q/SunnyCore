package jaime.sunnyCore;

import jaime.sunnyCore.logger.DebugablePlugin;
import jaime.sunnyCore.logger.WarnLogger;
import org.bukkit.plugin.java.JavaPlugin;

import static jaime.sunnyCore.utils.messages.MessageUtils.colorize;

public final class SunnyCore extends JavaPlugin {

    @Override
    public void onEnable() {
        WarnLogger.loadPluginInstance(this);
        sendBanner();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void sendBanner(){
        getServer().getConsoleSender().sendMessage(colorize("&#FFB82A  ___                         &e;   :   ;"));
        getServer().getConsoleSender().sendMessage(colorize("&#FFB82A / __|_  _ _ _  _ _ _  _       &e\\_,!,_/   ,"));
        getServer().getConsoleSender().sendMessage(colorize("&#FFB82A \\__ \\ || | ' \\| ' \\ || |            &e`.,'"));
        getServer().getConsoleSender().sendMessage(colorize("&#FFB82A |___/\\_,_|_||_|_||_\\_, |              &e\\"));
        getServer().getConsoleSender().sendMessage(colorize("&#4AD1FD  ___ _           _ |__/                &e: -- ~"));
        getServer().getConsoleSender().sendMessage(colorize("&#4AD1FD / __| |_ _  _ __| (_)___ ___          &e/"));
        getServer().getConsoleSender().sendMessage(colorize("&#4AD1FD \\__ \\  _| || / _` | / _ (_-< &e ._   _.'`."));
        getServer().getConsoleSender().sendMessage(colorize("&#4AD1FD |___/\\__|\\_,_\\__,_|_\\___/__/    &e`!` \\   `"));
        getServer().getConsoleSender().sendMessage(colorize("                                   &e;   :   ;"));

    }


}
