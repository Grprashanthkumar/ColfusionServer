package edu.pitt.sis.exp.colfusion.dal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import edu.pitt.sis.exp.colfusion.dal.infra.DatabaseUnitTestBase;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

public class DockerTest extends DatabaseUnitTestBase {
	
	@Test
	public void testSetUpTestStory() throws Exception {
		String tabelName = "test table name";
		String[] columnNames = new String[] {"Column A", "Column B", "Column C"};
		
		ColfusionSourceinfo storyExpected = setUpTestStory(tabelName, columnNames);
		
		assertNotNull("Set up story is null", storyExpected);
		
		SourceInfoManager storyMng = new SourceInfoManagerImpl();
		ColfusionSourceinfo storyActual = storyMng.findByID(storyExpected.getSid());
		
		assertNotNull("Actual story from db is null", storyActual);
		
		assertEquals("Titles are not the same",  storyExpected.getTitle(), storyActual.getTitle());
	}

	@Test
	public void testColfusionDatabaseCreated() throws ClassNotFoundException, SQLException {
		String sql = String.format("SELECT * FROM SCHEMATA WHERE SCHEMA_NAME = '%s'", 
				ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_HIBERNATE_DEFAULT_CATALOG));
		
		List<Object[]> result = executeMySQLQuery(dockerMySQLConnectionInfo.getConnectionString("information_schema"), sql);
		assertEquals(1, result.size());	
	}
}
