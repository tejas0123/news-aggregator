package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import exception.DAOException;
import dto.UserCredentials;
import util.DBConnection;

public class UserAuthenticationDAOImpl implements UserAuthenticationDAO{

	private static final String fetchCredentialsQuery = "SELECT user_id, email, password FROM users WHERE email = ?";

    public Optional<UserCredentials> fetchUserCredentials(UserCredentials loginCredentials){
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(fetchCredentialsQuery);
            statement.setString(1, loginCredentials.email());

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new UserCredentials(resultSet.getString("email"), resultSet.getString("password")));
            } else{
                return Optional.empty();
            }
        } catch (SQLException sqlException) {
            throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }
    }

}
