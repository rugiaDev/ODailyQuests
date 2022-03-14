package com.ordwen.odailyquests.quests.player.progression.storage.mysql;

import com.ordwen.odailyquests.enums.QuestsMessages;
import com.ordwen.odailyquests.quests.LoadQuests;
import com.ordwen.odailyquests.quests.Quest;
import com.ordwen.odailyquests.quests.player.PlayerQuests;
import com.ordwen.odailyquests.quests.player.QuestsManager;
import com.ordwen.odailyquests.quests.player.progression.Progression;
import com.ordwen.odailyquests.quests.player.progression.storage.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadProgressionSQL {

    /* instance of SQLManager */
    private final MySQLManager mySqlManager;

    /**
     * Constructor.
     *
     * @param mySqlManager SQLManager instance.
     */
    public LoadProgressionSQL(MySQLManager mySqlManager) {
        this.mySqlManager = mySqlManager;
    }

    /* init variables */
    private static final Logger logger = PluginLogger.getLogger("O'DailyQuests");

    /**
     * Load player quests progression.
     *
     * @param playerName name of the player.
     */
    public void loadProgression(String playerName, HashMap<String, PlayerQuests> activeQuests, int questsConfigMode, int timestampConfigMode) {

        HashMap<Quest, Progression> quests = new HashMap<>();
        long timestamp = 0;
        boolean hasStoredData = false;

        try {
            Connection connection = mySqlManager.getConnection();
            String getTimestampQuery = "SELECT playerTimestamp FROM Player WHERE playerName = '";
            PreparedStatement preparedStatement = connection.prepareStatement(getTimestampQuery + playerName + "'");

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                hasStoredData = true;
                timestamp = resultSet.getLong("playerTimestamp");
            }

            connection.close();
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (hasStoredData) {
            if (Utils.checkTimestamp(timestampConfigMode, timestamp)) {
                Utils.loadNewPlayerQuests(playerName, activeQuests, timestampConfigMode, quests);
            }
            else {
                loadPlayerQuests(playerName, questsConfigMode, quests);

                PlayerQuests playerQuests = new PlayerQuests(timestamp, quests);
                activeQuests.put(playerName, playerQuests);

                logger.info(ChatColor.GOLD + playerName + ChatColor.YELLOW + "'s quests have been loaded.");
                Bukkit.getPlayer(playerName).sendMessage(QuestsMessages.QUESTS_IN_PROGRESS.toString());
            }
        } else {
            Utils.loadNewPlayerQuests(playerName, activeQuests, timestampConfigMode, quests);
        }
    }

    /**
     * Load player quests.
     *
     * @param playerName player.
     * @param questsConfigMode configuration mode.
     * @param quests list of player quests.
     */
    private void loadPlayerQuests(String playerName, int questsConfigMode, HashMap<Quest, Progression> quests) {

        try {
            Connection connection = mySqlManager.getConnection();
            String getQuestProgressionQuery = "SELECT * FROM Progression WHERE playerName = ";
            PreparedStatement preparedStatement = connection.prepareStatement(getQuestProgressionQuery + playerName);
            ResultSet resultSet = preparedStatement.executeQuery();

            int id = 0;

            do {
                int questIndex = resultSet.getInt("questId1");
                int advancement = resultSet.getInt("advancement1");
                boolean isAchieved = resultSet.getBoolean("isAchieved1");

                Progression progression = new Progression(advancement, isAchieved);
                Quest quest = Utils.findQuest(playerName, questsConfigMode, questIndex, id);

                quests.put(quest, progression);

                id++;
            } while (resultSet.next());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}