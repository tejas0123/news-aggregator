package service;

import dao.ServerDAO;
import model.Server;
import java.util.List;

public class ServerServiceImpl implements ServerService {
    private final ServerDAO serverDAO;

    public ServerServiceImpl(ServerDAO serverDAO) {
        this.serverDAO = serverDAO;
    }

    @Override
    public List<Server> getAllServers() {
        return serverDAO.getAllServers();
    }

    @Override
    public boolean updateApiKey(int serverId, String newApiKey) {
        return serverDAO.updateApiKey(serverId, newApiKey);
    }
}
