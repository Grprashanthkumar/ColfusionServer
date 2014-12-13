package edu.pitt.sis.exp.colfusion.dal;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import edu.pitt.sis.exp.colfusion.dal.infra.DatabaseUnitTestBase;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

public class DockerTest extends DatabaseUnitTestBase {
	
	@Test
	public void testDocker1() throws ClassNotFoundException, SQLException {
		HibernateUtil.beginTransaction();
		System.out.println("running hibernate transaction.");
		HibernateUtil.commitTransaction();
	}
	
	private void wasDatabaseCreated() throws ClassNotFoundException, SQLException {
		String sql = String.format("SELECT * FROM SCHEMATA WHERE SCHEMA_NAME = '%s'", 
				ConfigManager.getInstance().getProperty(PropertyKeys.HIBERNATE_DEFAULT_CATALOG));
		
		List<Object[]> result = executeMySQLQuery(dbConnectionUrl + "/information_schema", sql);
		assertEquals(1, result.size());		
	}

	@Test
	public void testDocker2() throws ClassNotFoundException, SQLException {
		wasDatabaseCreated();
	}
	
	@Test
	public void testDocker3() throws ClassNotFoundException, SQLException {
		wasDatabaseCreated();
	}
	
	@Test
	public void testDocker4() throws ClassNotFoundException, SQLException {
		wasDatabaseCreated();
	}
	
	@Test
	public void testDocker5() throws ClassNotFoundException, SQLException {
		wasDatabaseCreated();
	}
}
