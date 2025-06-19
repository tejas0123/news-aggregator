package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import exception.DAOException;
import dto.UserCredentials;
import dto.UserDetails;
import util.DBConnection;

public class UserAuthenticationDAOImpl implements UserAuthenticationDAO{

	private static final String FETCH_CREDENTIALS_QUERY = "SELECT user_id, email, password FROM users WHERE email = ?";
	private static final String INSERT_USER_QUERY = "INSERT INTO users (first_name, last_name, email, gender, password, phone, date_of_birth)\r\n"
			+ "VALUES (?, ?, ?, ?::gender_enum, ?, ?, ?);";
	
    public Optional<UserCredentials> fetchUserCredentials(UserCredentials loginCredentials){
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(FETCH_CREDENTIALS_QUERY);
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

	@Override
	public int addNewUser(UserDetails userDetails) {
		try {
			Connection connection = DBConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement(INSERT_USER_QUERY);
			statement = setStatementValues(statement, userDetails);
			return statement.executeUpdate();
		} catch(SQLException sqlException) {
			System.out.println(sqlException.getSQLState());
			throw new DAOException(sqlException.getMessage(), sqlException.getSQLState());
		}
	}
	
	private PreparedStatement setStatementValues(PreparedStatement statement, UserDetails details) {
		try {
			statement.setString(1, details.firstName());
			statement.setString(2, details.lastName());
			statement.setString(3, details.email());
			statement.setString(4, details.gender().name());
			statement.setString(5, details.password());
			statement.setString(6, details.phone());
			statement.setDate(7, java.sql.Date.valueOf(details.dateOfBirth()));
		} catch (SQLException sqlException) {
			throw new DAOException(sqlException.getMessage(), sqlException.getCause());
		}

        return statement;
	}
}
