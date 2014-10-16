/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dataLoadExecutors;

import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHanderType;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.dal.managers.ExecutionInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ExecutionInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionExecuteinfo;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryTargetDBViewModel;
import edu.pitt.sis.exp.colfusion.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

/**
 * @author Evgeny
 *
 */
public class DataLoadExecutorKTRImpl extends DataLoadExecutorBaseImpl implements DataLoadExecutor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8121337577769586080L;
	
	transient Logger logger = LogManager.getLogger(DataLoadExecutorKTRImpl.class.getName());
	
	public DataLoadExecutorKTRImpl() {
	
	}
	
	public DataLoadExecutorKTRImpl(final int sid) {
		this.sid = sid;
	}
	
	@Override
	public void execute() throws Exception {
		
		logger.info(String.format("DataLoadExecutorKTR, execute: Starting for sid:", sid));
		
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		ArrayList<String> ktrLocations = storyMgr.getStoryKTRLocations(sid);
		
		logger.info(String.format("DataLoadExecutorKTR, execute: got %d ktrLocations:", ktrLocations.size()));
		
		//If there are several files, there will be several KTR files accosted with one sid, however they all will be associated whit one target database
		//therefore we need only one KTR file to extract and save target database connection info.
		boolean firstKtr = true;
		
		ExecutionInfoManager executionInfoMgr = new ExecutionInfoManagerImpl();
		
		StoryTargetDBViewModel targetDBConnectionInfo = null;
		DatabaseHandlerBase databaseHandlerBase = null;
		
		for(String ktrLocation : ktrLocations) {
			
			logger.info(String.format("DataLoadExecutorKTR, execute: Starting loading this ktr: ", ktrLocation));
			
			KTRManager ktrManager = new KTRManager(sid);
			ktrManager.loadKTR(ktrLocation);
			
			logger.info(String.format("DataLoadExecutorKTR, execute: Loaded this ktr: ", ktrLocation));
			
			ColfusionExecuteinfo executionInfo = executionInfoMgr.getExecutionInfo(sid, ktrManager.getTableName());
			
			int executionLogId = executionInfo.getEid();
			
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.IN_PROGRESS.getValue());
			
			logger.info(String.format("DataLoadExecutorKTR, execute: Set execution status to IN_PROGRESS for eid = %d", executionLogId));
			
			logger.info(String.format("DataLoadExecutorKTR, execute: Starting to change ransformation name for ktr file at %s to %s", 
					ktrLocation, String.valueOf(executionLogId)));
			
			changeTransformationName(executionInfoMgr, executionLogId, ktrManager, ktrLocation, String.valueOf(executionLogId));
			
			if (firstKtr) {
				
				logger.info(String.format("DataLoadExecutorKTR, execute: It is first ktr. Location: %s", ktrLocation));
				
				//If there are several files, there will be several KTR files accosted with one sid, however they all will be associated whit one target database
				//therefore we need only one KTR file to extract and save target database connection info.
				
				targetDBConnectionInfo = updateTargetDatabaseConnectionInfo(executionInfoMgr, executionLogId, ktrManager, ktrLocation);
				
				try {
					databaseHandlerBase = createTargetDatabase(executionInfoMgr, executionLogId, targetDBConnectionInfo);
				} catch (Exception e) {
					
					executionInfoMgr.appendLog(executionLogId, String.format("createTargetDatabase failed for %s. Error message: %s", targetDBConnectionInfo, e.toString()));
					
					databaseHandlerBase.close();
					
					throw e;
				}
				
				//TODO: uncomment if we still need Linked Servers
				//updateLinkedServerInfo(executionInfoMgr, executionLogId, targetDBConnectionInfo);
				
				firstKtr = false;
			}
		
			try {
				createTargetTable(executionInfoMgr, executionLogId, databaseHandlerBase, ktrManager);
			} catch (Exception e) {
				executionInfoMgr.appendLog(executionLogId, String.format("createTargetTable failed for %s. Error message: %s", targetDBConnectionInfo, e.toString()));
				throw e;
			}
			finally {
				databaseHandlerBase.close();
			}
			
			String command = getPentahoCarteURL(executionInfoMgr, executionLogId, ktrManager, ktrLocation);
			
			doHTTPCallToCarteServer(executionInfoMgr, executionLogId, command);
			
			//NOTE: Cannot say it is successful at this point, because the KTR transformation might be still running.
			//What happens is the following: the database has triggers on pentaho log_transformation table which updates execution info table.
			//executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.SUCCESS);
			
			executionInfoMgr.appendLog(executionLogId, "Finished Execute method, not the ktr is probably being execution by the carte server. "
					+ "The DataLoadExecutorKTRImpl proces is however done.");
		}
	}

	private void createTargetTable(final ExecutionInfoManager executionInfoMgr, final int executionLogId, final DatabaseHandlerBase databaseHandlerBase, final KTRManager ktrManager) 
			throws Exception {		
		
		String tableName = ktrManager.getTableName();
		
		executionInfoMgr.appendLog(executionLogId, String.format("Starting to create target table %s", tableName));
		
		databaseHandlerBase.createTableIfNotExist(tableName, ktrManager.getTargetTableColumns());
		
		executionInfoMgr.appendLog(executionLogId, String.format("Finished to create target table %s", tableName));
	}

	private DatabaseHandlerBase createTargetDatabase(final ExecutionInfoManager executionInfoMgr, final int executionLogId, final StoryTargetDBViewModel targetDBConnectionInfo) 
			throws Exception {
		executionInfoMgr.appendLog(executionLogId, "Starting to create target database");
		
		DatabaseHandlerBase databaseHandlerBase = DatabaseHandlerFactory.getDatabaseHandler(targetDBConnectionInfo.getServerAddress(), targetDBConnectionInfo.getPort(), 
				targetDBConnectionInfo.getUserName(), targetDBConnectionInfo.getPassword(), "", 
				DatabaseHanderType.fromString(targetDBConnectionInfo.getDriver()),
				executionInfoMgr, executionLogId);
		
		databaseHandlerBase.createDatabaseIfNotExist(targetDBConnectionInfo.getDatabaseName());
		
		executionInfoMgr.appendLog(executionLogId, "Finished to create target database");
		
		return databaseHandlerBase;
	}

	private void doHTTPCallToCarteServer(final ExecutionInfoManager executionInfoMgr, final int executionLogId, final String url) throws Exception {
		
		executionInfoMgr.appendLog(executionLogId, String.format("Starting HTTP call to Carte Server %s", url));
		
		ConfigManager configManager = ConfigManager.getInstance();
		
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(configManager.getPropertyByName(PropertyKeys.carteServer), Integer.valueOf(configManager.getPropertyByName(PropertyKeys.cartePort))),
                new UsernamePasswordCredentials(configManager.getPropertyByName(PropertyKeys.carteUser), configManager.getPropertyByName(PropertyKeys.cartePassword)));
        
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
		
		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.1) Gecko/2008070208 Firefox/3.0.1");
		
		HttpResponse response = client.execute(request);
		
		int statusCode = response.getStatusLine().getStatusCode();
		
		executionInfoMgr.appendLog(executionLogId, String.format("Got the following status code in response %d: ", statusCode));
		
		StringWriter writer = new StringWriter();
		IOUtils.copy(response.getEntity().getContent(), writer);
		String responseContent = writer.toString();
		
		client.close();
		
		executionInfoMgr.appendLog(executionLogId, String.format("Got This contect as the result of the call to carte %s: ", responseContent));
		
		if (statusCode != 200) {
			logger.error(String.format("doHTTPCallToCarteServer failed: got following status code in response", statusCode));
			
			//TODO: not sure if it is good
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.FAILED.getValue());
			
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}
				
		executionInfoMgr.appendLog(executionLogId, String.format("Finished HTTP call to Carte Server %s", url));		
	}

	private String getPentahoCarteURL(final ExecutionInfoManager executionInfoMgr, final int executionLogId, final KTRManager ktrManager, final String ktrLocation) 
			throws Exception {

		executionInfoMgr.appendLog(executionLogId, String.format("Started to prepare Carte Server Url for the %s", ktrLocation));
		
		String ktrFileURL = edu.pitt.sis.exp.colfusion.utils.IOUtils.getFileURLFromName(ktrLocation);
		
		ktrFileURL = URLEncoder.encode(ktrFileURL, "UTF-8");

		String carteServerURL = ConfigManager.getInstance().getPropertyByName(PropertyKeys.carteServerURL);
		
        String result = String.format("%s?trans=%s&Sid=%d&Eid=%d", carteServerURL, ktrFileURL, sid, executionLogId);
        
        executionInfoMgr.appendLog(executionLogId, String.format("Finished to prepare Carte Server Url for the %s file. KtrFileURL is %s", ktrLocation, ktrFileURL));

        return result;
	}

	private void changeTransformationName(final ExecutionInfoManager executionInfoMgr, final int executionLogId, final KTRManager ktrManager, final String ktrLocation,
			final String transformationName) throws Exception {
		try {
			executionInfoMgr.appendLog(executionLogId, String.format("Starting to change the name for the KTR file located at %s. "
					+ "The new name is: %s", ktrLocation, transformationName));
			
			//The transformation  should be have name that corresponds to the execution info id because execution info record needs to be updated with success or failure.
			// The update is performed via triggers in the db.
			ktrManager.changeTransformaitonName(transformationName);
			
			executionInfoMgr.appendLog(executionLogId, String.format("Finished to change the name for the KTR file located at %s", ktrLocation));
		} catch (Exception e) {
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.FAILED.getValue());
			
			throw e;
		}
	}

	private StoryTargetDBViewModel updateTargetDatabaseConnectionInfo(final ExecutionInfoManager executionInfoMgr, final int executionLogId, final KTRManager ktrManager, final String ktrLocation) throws Exception {
		try {
			executionInfoMgr.appendLog(executionLogId, String.format("Starting to read traget database info from the KTR file located at %s", ktrLocation));
			
			StoryTargetDBViewModel sourceDBInfo = ktrManager.readTargetDatabaseInfo();
			
			executionInfoMgr.appendLog(executionLogId, String.format("Finished reading traget database info from the KTR file located at %s", ktrLocation));
			
			super.updateSourceDBInfo(executionInfoMgr, executionLogId, sourceDBInfo);
			
			return sourceDBInfo;
		} catch (Exception e) {
			
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.FAILED.getValue());
			
			throw e;
		}	
	}
		
	@Override
	protected Runnable getRunnable() {
		return this;
	}
}
