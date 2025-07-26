package service;

import model.Server;
import java.util.List;

public interface ServerService {
    List<Server> getAllServers();
    boolean updateApiKey(int serverId, String newApiKey);
}
