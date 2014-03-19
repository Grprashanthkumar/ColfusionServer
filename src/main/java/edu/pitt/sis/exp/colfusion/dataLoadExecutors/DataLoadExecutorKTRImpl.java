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

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.PropertyKeys;
import edu.pitt.sis.exp.colfusion.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHanderType;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.persistence.managers.ExecutionInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.ExecutionInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryTargetDB;

/**
 * @author Evgeny
 *
 */
public class DataLoadExecutorKTRImpl extends DataLoadExecutorBaseImpl implements
		DataLoadExecutor {
	
	Logger logger = LogManager.getLogger(DataLoadExecutorKTRImpl.class.getName());
	
	public DataLoadExecutorKTRImpl() {
	
	}
	
	public DataLoadExecutorKTRImpl(int sid) {
		this.sid = sid;
	}
	
	@Override
	public void execute() throws Exception {
		
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		ArrayList<String> ktrLocations = storyMgr.getStoryKTRLocations(sid);
		
		//If there are several files, there will be several KTR files accosted with one sid, however they all will be associated whit one target database
		//therefore we need only one KTR file to extract and save target database connection info.
		boolean firstKtr = true;
		
		ExecutionInfoManager executionInfoMgr = new ExecutionInfoManagerImpl();
		
		StoryTargetDB targetDBConnectionInfo = null;
		DatabaseHandlerBase databaseHandlerBase = null;
		
		for(String ktrLocation : ktrLocations) {
			
			KTRManager ktrManager = new KTRManager(sid);
			ktrManager.loadKTR(ktrLocation);
			
			int executionLogId = executionInfoMgr.getExecutionLogId(sid, ktrManager.getTableName());
			
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.IN_PROGRESS);
			
			changeTransformationName(executionInfoMgr, executionLogId, ktrManager, ktrLocation, String.valueOf(executionLogId));
			
			if (firstKtr) {
				
				//If there are several files, there will be several KTR files accosted with one sid, however they all will be associated whit one target database
				//therefore we need only one KTR file to extract and save target database connection info.
				
				targetDBConnectionInfo = updateTargetDatabaseConnectionInfo(executionInfoMgr, executionLogId, ktrManager, ktrLocation);
				databaseHandlerBase = createTargetDatabase(executionInfoMgr, executionLogId, targetDBConnectionInfo);
				
				firstKtr = false;
			}
		
			createTargetTable(executionInfoMgr, executionLogId, databaseHandlerBase, ktrManager);
			
			String command = getPentahoCarteURL(executionInfoMgr, executionLogId, ktrManager, ktrLocation);
			
			doHTTPCallToCarteServer(executionInfoMgr, executionLogId, command);
			
			//NOTE: Cannot say it is successful at this point, because the KTR transformation might be still running.
			//What happens is the following: the database has triggers on pentaho log_transformation table which updates execution info table.
			//executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.SUCCESS);
		}
	}

	private void createTargetTable(ExecutionInfoManager executionInfoMgr, int executionLogId, DatabaseHandlerBase databaseHandlerBase, KTRManager ktrManager) 
			throws Exception {		
		executionInfoMgr.appendLog(executionLogId, String.format("Starting to create target table %s", ktrManager));
		
		databaseHandlerBase.createTableIfNotExist(ktrManager.getTableName(), ktrManager.getTargetTableColumns());
		
		executionInfoMgr.appendLog(executionLogId, String.format("Finished to create target table %s", ktrManager));
	}

	private DatabaseHandlerBase createTargetDatabase(ExecutionInfoManager executionInfoMgr, int executionLogId, StoryTargetDB targetDBConnectionInfo) 
			throws Exception {
		executionInfoMgr.appendLog(executionLogId, "Starting to create target database");
		
		DatabaseHandlerBase databaseHandlerBase = DatabaseHandlerFactory.getDatabaseHandler(targetDBConnectionInfo.getServerAddress(), targetDBConnectionInfo.getPort(), 
				targetDBConnectionInfo.getUserName(), targetDBConnectionInfo.getPassword(), "", 
				DatabaseHanderType.fromString(targetDBConnectionInfo.getDriver()));
		
		databaseHandlerBase.createDatabaseIfNotExist(targetDBConnectionInfo.getDatabaseName());
		
		executionInfoMgr.appendLog(executionLogId, "Finished to create target database");
		
		return databaseHandlerBase;
	}

	private void doHTTPCallToCarteServer(ExecutionInfoManager executionInfoMgr, int executionLogId, String url) throws Exception {
		
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
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.FAILED);
			
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}
				
		executionInfoMgr.appendLog(executionLogId, String.format("Finished HTTP call to Carte Server %s", url));		
	}

	private String getPentahoCarteURL(ExecutionInfoManager executionInfoMgr, int executionLogId, KTRManager ktrManager, String ktrLocation) 
			throws Exception {

		executionInfoMgr.appendLog(executionLogId, String.format("Started to prepare Carte Server Url for the %s", ktrLocation));
		
		ktrLocation = URLEncoder.encode(ktrLocation, "UTF-8");

		String carteServerURL = ConfigManager.getInstance().getPropertyByName(PropertyKeys.carteServerURL);
		
        String result = String.format("%s?trans=%s&Sid=%d&Eid=%d", carteServerURL, ktrLocation, sid, executionLogId);
        
        executionInfoMgr.appendLog(executionLogId, String.format("Finished to prepare Carte Server Url for the %s", ktrLocation));

        return result;
	}

	private void changeTransformationName(ExecutionInfoManager executionInfoMgr, int executionLogId, KTRManager ktrManager, String ktrLocation,
			String transformationName) throws Exception {
		try {
			executionInfoMgr.appendLog(executionLogId, String.format("Starting to change the name for the KTR file located at %s. "
					+ "The new name is: %s", ktrLocation, transformationName));
			
			//The transformation  should be have name that corresponds to the execution info id because execution info record needs to be updated with success or failure.
			// The update is performed via triggers in the db.
			ktrManager.changeTransformaitonName(transformationName);
			
			executionInfoMgr.appendLog(executionLogId, String.format("Finished to change the name for the KTR file located at %s", ktrLocation));
		} catch (Exception e) {
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.FAILED);
			
			throw e;
		}
	}

	private StoryTargetDB updateTargetDatabaseConnectionInfo(ExecutionInfoManager executionInfoMgr, int executionLogId, KTRManager ktrManager, String ktrLocation) throws Exception {
		try {
			executionInfoMgr.appendLog(executionLogId, String.format("Starting to read traget database info from the KTR file located at %s", ktrLocation));
			
			StoryTargetDB sourceDBInfo = ktrManager.readTargetDatabaseInfo();
			
			executionInfoMgr.appendLog(executionLogId, String.format("Finished reading traget database info from the KTR file located at %s", ktrLocation));
			
			executionInfoMgr.appendLog(executionLogId, String.format("Starting to update sourceintoDB record with target database conneciton info fetched form the ktr file %s. "
					+ "Here is what connection info is: %s ", ktrLocation, sourceDBInfo.toString()));
			
			super.updateSourceDBInfo(sourceDBInfo);
			
			executionInfoMgr.appendLog(executionLogId, "Finished update sourceintoDB record with target database conneciton info");
			
			return sourceDBInfo;
		} catch (Exception e) {
			
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.FAILED);
			
			throw e;
		}
		
	}

	@Override
	public void run() {
		
		try {
			execute();
			
			this._manager.onDoneProcess(this);
		} catch (Exception e) {
			//TODO: add logger if needed here, or maybe all exceptions should be logged by process manager
			
			this._manager.onFailedProcess(this, e);
		}	
	}

	@Override
	protected Runnable getRunnable() {
		return this;
	}
}
