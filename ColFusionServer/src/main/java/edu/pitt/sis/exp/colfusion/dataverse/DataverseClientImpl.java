package edu.pitt.sis.exp.colfusion.dataverse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;

import edu.pitt.sis.exp.colfusion.utils.JerseyClientUtil;

public class DataverseClientImpl implements DataverseClient {

	final Logger logger = LogManager.getLogger(DataverseClientImpl.class.getName());

	//TODO: apparently server base address should also be user provided
	// because there are a number of dataverse servers, CHIA uses harvard's server
	String apiBaseUrl = "https://dataverse.harvard.edu/api";

	String dataverseKey = "551ceb21-bd44-456a-abe0-12d7412f401b";

	@Override
	public InputStream getDatafile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DataverseFileInfo> searchForFile(final String fileName, final String dataverseName, final String datasetName) {
		final Escaper urlEscaper = UrlEscapers.urlPathSegmentEscaper();

		final String restResource = String.format("/search?q=%s&subtree=%s&type=file&key=%s",
				urlEscaper.escape(fileName), urlEscaper.escape(dataverseName), urlEscaper.escape(this.dataverseKey));

		final Response response = JerseyClientUtil.doGet(this.apiBaseUrl, restResource);
		final String entity = response.readEntity(String.class);

		if (response.getStatus() != 200) {
			this.logger.warn( String.format("searchForFile: Was trying to search for a file"
					+ " '%s' on dataverse network in dataverse '%s' in dataset '%s'. Got this as the result: '%s'",
					fileName, dataverseName, datasetName, entity));

			//TODO: alternatively we could propagate message from the dataverse server to the user to let him/her know what was wrong
			return new ArrayList<DataverseFileInfo>();
		}

		final List<DataverseFileInfo> filesArray = new ArrayList<DataverseFileInfo>();
		try {
			final JSONObject json = new JSONObject(entity);
			final JSONObject data = json.getJSONObject("data");
			final JSONArray item = data.getJSONArray("items");
			for (int i = 0; i < item.length(); i++) {
				final JSONObject file = item.getJSONObject(i);

				if (!datasetName.equals("") && !file.getString("dataset_citation").contains(datasetName)) {
					continue;
				}

				final DataverseFileInfo dataverseFileInfo = dataverSearchResultItemJsonItemToDataverseFileInfo(file);
				filesArray.add(dataverseFileInfo);
			}

			return filesArray;
		} catch (final JSONException e) {
			final String message = String.format("searchForFile: Was trying to search for a file"
					+ " '%s' on dataverse network in dataverse '%s' in dataset '%s'",
					fileName, dataverseName, datasetName);
			this.logger.error(message, e);
			throw new RuntimeException(e);
		}
	}

	static DataverseFileInfo dataverSearchResultItemJsonItemToDataverseFileInfo(final JSONObject item) throws JSONException {
		//TODO:gson could be used here.
		return new DataverseFileInfo(item.getString("file_id"), item.getString("name"),
				item.getInt("size_in_bytes"), item.getString("dataset_citation"),
				item.getString("published_at"));
	}
}
