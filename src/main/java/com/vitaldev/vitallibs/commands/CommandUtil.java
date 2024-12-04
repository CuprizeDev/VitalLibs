package com.vitaldev.vitallibs.commands;

import com.vitaldev.vitallibs.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtil {

    public static boolean checkPermission(CommandSender sender, String permission, String message) {
        if (sender instanceof Player player) {
            if (!player.hasPermission(permission)) {
                sender.sendMessage(ChatUtil.color(message));
                return false;
            }
        }
        return true;
    }

    public static int parseIntArg(CommandSender sender, String[] args, int argIndex, int defaultVal, String message) {
        if (args.length > argIndex) {
            try {
                return Integer.parseInt(args[argIndex]);
            } catch (NumberFormatException exception) {
                sender.sendMessage(ChatUtil.color(message.replace("{ARG-INDEX}", String.valueOf(argIndex))));
            }
        }
        return defaultVal;
    }

    public static boolean validateArgsLength(CommandSender sender, String[] args, int required, String message) {
        if (args.length < required) {
            sender.sendMessage(ChatUtil.color(message));
            return false;
        }
        return true;
    }

    public static boolean checkPlayerOnly(CommandSender sender, String message) {
        if (sender instanceof Player) {
            return true;
        } else {
            sender.sendMessage(ChatUtil.color(message));
            return false;
        }
    }

    public static boolean isValidPlayerArgument(CommandSender sender, String[] args, int index, String message) {
        if (!(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[index])))) {
            sender.sendMessage(ChatUtil.color(message));
            return false;
        }
        return true;
    }

    public static String getArgument(String[] args, int index) {
        if (args.length > index) {
            return args[index];
        }
        return "";
    }
}
