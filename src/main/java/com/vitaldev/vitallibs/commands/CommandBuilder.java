package com.vitaldev.vitallibs.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;
public abstract class CommandBuilder extends BukkitCommand {
    public CommandBuilder(String command, String[] aliases, String description, String basePerm, String noPermMessage) {
        super(command);
        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);
        this.setPermission(basePerm);
        this.setPermissionMessage(noPermMessage);
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(command, this);
        } catch (NoSuchFieldException | IllegalAccessException illegalAccess) {
            illegalAccess.printStackTrace();
        }
    }
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        execute(sender, args);
        return true;
    }

    protected abstract void execute(CommandSender sender, String[] args);
}
