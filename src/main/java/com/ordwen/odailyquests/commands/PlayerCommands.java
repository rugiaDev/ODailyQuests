package com.ordwen.odailyquests.commands;

import com.ordwen.odailyquests.ODailyQuests;
import com.ordwen.odailyquests.commands.interfaces.InterfacesManager;
import com.ordwen.odailyquests.commands.interfaces.playerinterface.PlayerQuestsInterface;
import com.ordwen.odailyquests.enums.QuestsMessages;
import com.ordwen.odailyquests.enums.QuestsPermissions;
import com.ordwen.odailyquests.files.ConfigurationFiles;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class PlayerCommands implements CommandExecutor {

    private final ConfigurationFiles configurationFiles;

    /**
     * ConfigurationFiles class instance constructor.
     *
     * @param oDailyQuests main class instance.
     */
    public PlayerCommands(ODailyQuests oDailyQuests) {
        this.configurationFiles = oDailyQuests.getConfigurationFiles();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (sender.hasPermission(QuestsPermissions.QUEST_USE.getPermission())) {
                if (args.length >= 1) {
                    switch (args[0]) {
                        case "show" -> {
                            if (sender.hasPermission(QuestsPermissions.QUEST_SHOW.getPermission())) {
                                if (args.length == 2) {
                                    switch (args[1]) {
                                        case "global" -> {
                                            if (configurationFiles.getConfigFile().getInt("quests_mode") != 1) {
                                                final String msg = QuestsMessages.CATEGORIZED_ENABLED.toString();
                                                if (msg != null) sender.sendMessage(msg);

                                            } else if (sender.hasPermission(QuestsPermissions.QUESTS_SHOW_GLOBAL.getPermission())) {
                                                player.openInventory(InterfacesManager.getInterfaceFirstPage("global", player));
                                            } else {
                                                final String msg = QuestsMessages.NO_PERMISSION_CATEGORY.toString();
                                                if (msg != null) sender.sendMessage(msg);
                                            }
                                        }
                                        case "easy" -> {
                                            if (configurationFiles.getConfigFile().getInt("quests_mode") != 2) {
                                                final String msg = QuestsMessages.CATEGORIZED_DISABLED.toString();
                                                if (msg != null) sender.sendMessage(msg);
                                            } else {
                                                if (sender.hasPermission(QuestsPermissions.QUESTS_SHOW_EASY.getPermission())) {
                                                    player.openInventory(InterfacesManager.getInterfaceFirstPage("easy", player));
                                                } else {
                                                    final String msg = QuestsMessages.NO_PERMISSION_CATEGORY.toString();
                                                    if (msg != null) sender.sendMessage(msg);
                                                }
                                            }
                                        }
                                        case "medium" -> {
                                            if (configurationFiles.getConfigFile().getInt("quests_mode") != 2) {
                                                final String msg = QuestsMessages.CATEGORIZED_DISABLED.toString();
                                                if (msg != null) sender.sendMessage(msg);
                                            } else {
                                                if (sender.hasPermission(QuestsPermissions.QUESTS_SHOW_MEDIUM.getPermission())) {
                                                    player.openInventory(InterfacesManager.getInterfaceFirstPage("medium", player));
                                                } else {
                                                    final String msg = QuestsMessages.NO_PERMISSION_CATEGORY.toString();
                                                    if (msg != null) sender.sendMessage(msg);
                                                }
                                            }
                                        }
                                        case "hard" -> {
                                            if (configurationFiles.getConfigFile().getInt("quests_mode") != 2) {
                                                final String msg = QuestsMessages.CATEGORIZED_DISABLED.toString();
                                                if (msg != null) sender.sendMessage(msg);
                                            } else {
                                                if (sender.hasPermission(QuestsPermissions.QUESTS_SHOW_HARD.getPermission())) {
                                                    player.openInventory(InterfacesManager.getInterfaceFirstPage("hard", player));
                                                } else {
                                                    final String msg = QuestsMessages.NO_PERMISSION_CATEGORY.toString();
                                                    if (msg != null) sender.sendMessage(msg);
                                                }
                                            }
                                        }
                                        default -> {
                                            final String msg = QuestsMessages.INVALID_CATEGORY.toString();
                                            if (msg != null) sender.sendMessage(msg);
                                        }
                                    }
                                } else {
                                    final String msg = QuestsMessages.PLAYER_HELP.toString();
                                    if (msg != null) sender.sendMessage(msg);
                                }
                            } else {
                                final String msg = QuestsMessages.NO_PERMISSION.toString();
                                if (msg != null) sender.sendMessage(msg);
                            }
                        }
                        case "me" -> {
                            final Inventory inventory = PlayerQuestsInterface.getPlayerQuestsInterface(player);

                            if (inventory == null) {
                                player.sendMessage("§cImpossible to open the quests interface. Is the plugin still loading?");
                                player.sendMessage("§cIf the problem persists, please contact the server administrator.");
                                return true;
                            }

                            player.openInventory(inventory);
                        }
                        default -> {
                            final String msg = QuestsMessages.PLAYER_HELP.toString();
                            if (msg != null) sender.sendMessage(msg);
                        }
                    }
                } else {
                    final Inventory inventory = PlayerQuestsInterface.getPlayerQuestsInterface(player);

                    if (inventory == null) {
                        player.sendMessage("§cImpossible to open the quests interface. Is the plugin still loading?");
                        player.sendMessage("§cIf the problem persists, please contact the server administrator.");
                        return true;
                    }
                    player.openInventory(inventory);
                }
            } else {
                final String msg = QuestsMessages.NO_PERMISSION.toString();
                if (msg != null) sender.sendMessage(msg);
            }
        }
        return false;
    }
}
