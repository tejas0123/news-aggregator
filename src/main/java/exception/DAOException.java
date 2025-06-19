package exception;

public class DAOException extends RuntimeException{
	private String sqlState;
    public DAOException(String message){
        super(message);
    }

    public DAOException(String message, Throwable cause){
        super(message, cause);
    }
    
    public DAOException(String message, String sqlState) {
    	super(message);
    	this.sqlState = sqlState;
    }
    
    public String getSqlState() {
    	return this.sqlState;
    }
}
