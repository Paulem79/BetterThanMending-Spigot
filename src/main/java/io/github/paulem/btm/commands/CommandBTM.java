package io.github.paulem.btm.commands;

import io.github.paulem.btm.config.PlayerDataConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CommandBTM implements TabExecutor {

    public final int configVersion;
    public final PlayerDataConfig playerDataConfig;
    public final String pluginVersion;

    public CommandBTM(int configVersion, PlayerDataConfig playerDataConfig, String pluginVersion) {
        this.configVersion = configVersion;
        this.playerDataConfig = playerDataConfig;
        this.pluginVersion = pluginVersion;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if((args.length == 0 || args[0].equalsIgnoreCase("toggle")) && sender instanceof Player) {
            Player player = (Player) sender;

            boolean enabled;
            try {
                enabled = playerDataConfig.getPlayerOrCreate(player, true);

                enabled = playerDataConfig.setPlayer(player, !enabled);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            player.sendMessage("Mending's ability has been successfully " + (enabled ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled") + " !");

            return true;
        }

        sender.sendMessage(ChatColor.BLUE + "Running BetterThanMending " + pluginVersion + " with config version " + configVersion);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Arrays.asList("toggle", "version");
    }
}
