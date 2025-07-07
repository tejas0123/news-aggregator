package dao;

import model.Server;
import util.DBConnection;
import java.sql.*;
import java.util.*;

import exception.DAOException;

public class ServerDAOImpl implements ServerDAO {

    @Override
    public List<Server> getAllServers() {
        String getServersQuery = "SELECT * FROM servers";
        List<Server> servers = new ArrayList<>();
        
        try {
        	Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(getServersQuery);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Server server = new Server();
                server.setServerId(resultSet.getInt("server_id"));
                server.setName(resultSet.getString("name"));
                server.setApiKey(resultSet.getString("api_key"));
                server.setActive(resultSet.getBoolean("is_active"));
                servers.add(server);
            }
        } catch (SQLException sqlException) {
            throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }
        return servers;
    }

    @Override
    public boolean updateApiKey(int serverId, String newApiKey) {
        String updateServerDetialsQuery = "UPDATE servers SET api_key = ? WHERE server_id = ?";
        
        try {
        	Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(updateServerDetialsQuery);
            statement.setString(1, newApiKey);   
            statement.setInt(2, serverId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected >= 1;
        } catch (SQLException sqlException) {
            throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }
    }
}
