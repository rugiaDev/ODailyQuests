package com.ordwen.odailyquests.quests.player.progression.listeners;

import com.ordwen.odailyquests.configuration.functionalities.GlobalReward;
import com.ordwen.odailyquests.api.events.AllQuestsCompletedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AllQuestsCompletedListener implements Listener {

    @EventHandler
    public void onAllQuestsCompletedEvent(AllQuestsCompletedEvent event) {
        System.out.println("Oui3");
        GlobalReward.sendGlobalReward(event.getPlayer().getName());
    }
}
