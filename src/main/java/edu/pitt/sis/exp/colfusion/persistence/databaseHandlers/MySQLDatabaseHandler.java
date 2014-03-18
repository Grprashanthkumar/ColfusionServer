/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.databaseHandlers;

/**
 * @author Evgeny
 *
 */
public class MySQLDatabaseHandler extends DatabaseHandlerBase {

	public MySQLDatabaseHandler(String host, int port, String user,
			String password, String database,
			DatabaseHanderType databaseHanderType) {
		super(host, port, user, password, database, databaseHanderType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createDatabaseIfNotExist() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createTableIfNotExist(String tableName) {
		// TODO Auto-generated method stub
		
	}

}
