package dao;

import model.Server;
import java.util.List;

public interface ServerDAO {
    List<Server> getAllServers();
    boolean updateApiKey(int serverId, String newApiKey);
}
