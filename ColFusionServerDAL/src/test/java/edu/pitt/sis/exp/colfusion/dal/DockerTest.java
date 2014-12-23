package edu.pitt.sis.exp.colfusion.dal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import edu.pitt.sis.exp.colfusion.dal.infra.DatabaseUnitTestBase;
import edu.pitt.sis.exp.colfusion.dal.managers.GeneralManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

public class DockerTest extends DatabaseUnitTestBase {
	
	@Test
	public void testSetUpTestStory() throws Exception {
		String tableName = "test table name";
		String[] columnNames = new String[] {"Column A", "Column B", "Column C"};
		
		ColfusionSourceinfo storyExpected = setUpTestStory(tableName, columnNames);
		
		assertNotNull("Set up story is null", storyExpected);
		
		SourceInfoManager storyMng = new SourceInfoManagerImpl();
		ColfusionSourceinfo storyActual = storyMng.findByID(storyExpected.getSid());
		storyActual = GeneralManagerImpl.initializeField(storyActual, "colfusionLicense", "colfusionUsers", "colfusionSourceinfoDb", "colfusionDnameinfos");
		
		assertNotNull("Actual story from db is null", storyActual);
		
		assertEquals("Titles are not the same", storyExpected.getTitle(), storyActual.getTitle());
		assertNotNull("Actual story from db license is null", storyActual.getColfusionLicense());
		assertNotNull("Actual story from db user is null", storyActual.getColfusionUsers());
		
		assertEquals("License ids are not the same", storyExpected.getColfusionLicense().getLicenseId(), storyActual.getColfusionLicense().getLicenseId());
		assertEquals("User ids are not the same", storyExpected.getColfusionUsers().getUserId(), storyActual.getColfusionUsers().getUserId());
		
		assertEquals("# of columns is not the same", columnNames.length, storyActual.getColfusionDnameinfos().size());
		
		ColfusionSourceinfoDb sourceInfoDb = storyActual.getColfusionSourceinfoDb();
		
		assertNotNull("Actual story from db sourceinfodb is null", sourceInfoDb);
		
		DockerMySQLConnectionInfo dockerMySQLConnectionInfo = new DockerMySQLConnectionInfo(sourceInfoDb.getServerAddress(), 
				sourceInfoDb.getPort(), sourceInfoDb.getUserName(), sourceInfoDb.getPassword());
		
		String connectionString = dockerMySQLConnectionInfo.getConnectionString(sourceInfoDb.getSourceDatabase());
		String query = String.format("SELECT * FROM `%s`", tableName);
		List<Object[]> result = executeMySQLQuery(connectionString, query);
		
		assertEquals("# of tuples is not the same", DEFAULT_NUM_GENERATE_TUPLES, result.size());
		assertEquals("# of columns is not the same", columnNames.length, result.get(0).length);
	}

	@Test
	public void testColfusionDatabaseCreated() throws ClassNotFoundException, SQLException {
		String sql = String.format("SELECT * FROM SCHEMATA WHERE SCHEMA_NAME = '%s'", 
				ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_HIBERNATE_DEFAULT_CATALOG));
		
		List<Object[]> result = executeMySQLQuery(dockerMySQLConnectionInfo.getConnectionString("information_schema"), sql);
		assertEquals(1, result.size());	
	}
}
