package edu.pitt.sis.exp.colfusion.dataverse;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
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

	private final DataverseContext dataverseContext;

	public DataverseClientImpl(final DataverseContext dataverseContext) {
		this.dataverseContext = dataverseContext;
	}

	@Override
	public InputStream getDatafile(final String fileId) throws FileNotFoundException, AccessDeniedException {

		final Escaper urlEscaper = UrlEscapers.urlPathSegmentEscaper();

		final String restResource = String.format("/access/datafile/%s?format=original&key=%s",
				urlEscaper.escape(fileId), urlEscaper.escape(this.dataverseContext.getTokenKey()));

		final Response response = JerseyClientUtil.doGet(this.dataverseContext.getRestApiBase(), restResource,
				MediaType.APPLICATION_OCTET_STREAM_TYPE, MediaType.WILDCARD_TYPE);

		if (response.getStatus() == 403) {
			final String message = String.format("Access to the specified data file '%s' has been forbidden.", fileId);
			this.logger.info(message);
			//TODO: maybe custom exception is better.
			throw new AccessDeniedException(message);
		}

		if (response.getStatus() != 200) {
			final String message = String.format("getDatafile got response with status '%d' and message '%s' for fileid '%s'",
					response.getStatus(), response.readEntity(String.class), fileId);

			this.logger.info(message);

			throw new FileNotFoundException(message);
		}

		final InputStream entity = response.readEntity(InputStream.class);

		return entity;
	}

	@Override
	public List<DataverseFileInfo> searchForFile(final String fileName, final String dataverseName, final String datasetName) {
		final Escaper urlEscaper = UrlEscapers.urlPathSegmentEscaper();

		String restResource = String.format("/search?q=%s&type=file&key=%s",
				urlEscaper.escape(fileName), urlEscaper.escape(this.dataverseContext.getTokenKey()));

		if (dataverseName != null && !dataverseName.isEmpty()) {
			restResource = String.format("%s&subtree=%s", restResource, urlEscaper.escape(dataverseName));
		}

		final Response response = JerseyClientUtil.doGet(this.dataverseContext.getRestApiBase(), restResource);
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

				if (datasetName != null && !datasetName.equals("") && !file.getString("dataset_citation").contains(datasetName)) {
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
		return new DataverseFileInfo(item.getString("file_id"),
				item.getString("name"),
				item.getInt("size_in_bytes"),
				item.getString("dataset_citation"),
				item.getString("published_at"));
	}
}
