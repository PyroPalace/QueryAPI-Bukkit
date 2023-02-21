package xyz.invisraidinq.queryapi.task;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.invisraidinq.queryapi.server.Server;
import xyz.invisraidinq.queryapi.server.ServerManager;
import xyz.invisraidinq.queryapi.utils.CC;
import xyz.invisraidinq.queryapi.utils.ConfigFile;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerHeartbeatTask extends BukkitRunnable {

    private final ServerManager serverManager;
    private final long updateIntervalMillis;

    /**
     * Constructor to initialise the {@link ServerHeartbeatTask}
     *
     * @param serverManager The {@link ServerManager} instance
     * @param settingsFile  The {@link ConfigFile} containing the settings
     */

    public ServerHeartbeatTask(ServerManager serverManager, ConfigFile settingsFile) {
        this.serverManager = serverManager;
        this.updateIntervalMillis = TimeUnit.SECONDS.toMillis(settingsFile.getLong("SERVER.UPDATE-INTERVAL"));
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        for (Iterator<Map.Entry<String, Server>> it = serverManager.getServerMap().entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Server> entry = it.next();
            Server server = entry.getValue();
            if (server.getLastUpdate() + (updateIntervalMillis * 2) < currentTime) {
                CC.log("Server " + server.getServerName() + " hasn't had a heartbeat for 30s, setting server state as offline");
                it.remove();
            }
        }
    }
}