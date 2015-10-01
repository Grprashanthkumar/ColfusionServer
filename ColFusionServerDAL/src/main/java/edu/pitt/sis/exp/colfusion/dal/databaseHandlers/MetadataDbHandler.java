/*

Copyright 2014, xxl
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

 * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
 * Neither the name of Google Inc. nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package edu.pitt.sis.exp.colfusion.dal.databaseHandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;


/**
 * This class is used to read from colfusion main metadata database
 *
 * @author xxl
 *
 */
public class MetadataDbHandler {

	private DatabaseHandlerBase dbHandler;

	final static Logger logger = LogManager.getLogger(MetadataDbHandler.class.getName());

	public MetadataDbHandler(final DatabaseHandlerBase dbHandler) {
		this.dbHandler = dbHandler;
	}
	public MetadataDbHandler(final DatabaseConnectionInfo connectionInfo) {

	}
	public DatabaseConnectionInfo getTargetDbConnectionInfo(final int sid)
			throws SQLException {
		logger.info(String.format("Getting target database connectio info for sid %d", sid));

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "SELECT * FROM colfusion_sourceinfo_DB WHERE sid = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, sid);

				final ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					return new DatabaseConnectionInfo(rs.getString("server_address"), rs.getInt("port"),
							rs.getString("user_name"), rs.getString("password"), rs.getString("source_database"));
				}

				final String message = String.format("Select * from sourceinfo_db returned 0 rows for sid %d", sid);

				logger.error(message);
				throw new RuntimeException(message);
			} catch (final SQLException e) {
				logger.error(String.format("Select * from sourceinfo_db FAILED for sid %d", sid), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting target database connectio info for sid %d. "
					+ "Something was wrong when opening connection", sid));
			throw e;
		}
	}

	/**
	 *
	 * @param sid
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public int getOperatingUserId(final int sid, final RelationKey tableInfo)
			throws SQLException {
		logger.info(String.format("Getting operating user id for sid %d and table name %s", sid, tableInfo.getDbTableName()));

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "SELECT operatedUser FROM colfusion_table_change_log WHERE endChangeTime IS NULL AND sid = ? AND tableName = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, sid);
				statement.setString(2, tableInfo.getDbTableName());

				final ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					return rs.getInt("operatedUser");
				}

				final String message = String.format(
						"Getting operating user id for sid %d and table name %s returned 0 rows", sid, tableInfo.getDbTableName());

				logger.error(message);
				throw new RuntimeException(message);
			} catch (final SQLException e) {
				logger.error(
						String.format("Getting operating user id for sid %d and table name %s FAILED", sid, tableInfo.getDbTableName()),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting operating user id for sid %d and table name %s ", sid,
					tableInfo.getDbTableName()));
			throw e;
		}
	}

	public String getUserLoginById(final int userId)
			throws SQLException {

		logger.info(String.format("Getting user login for userId %d", userId));
		String userLogin = "";
		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "SELECT user_login FROM colfusion_users WHERE user_id = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, userId);

				final ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					userLogin = rs.getString("user_login");
				}

				return userLogin;

			} catch (final SQLException e) {
				logger.error(String.format("Getting user login for userId %d FAILED", userId), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting user login for userId %d ", userId));
			throw e;
		}

	}

	public boolean isBeingEditedByCurrentUser(final int sid, final RelationKey tableInfo, final int userId)
			throws SQLException {

		logger.info(String.format("Getting if current table is being edited by user %d", userId));
		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "SELECT endChangeTime FROM colfusion_table_change_log WHERE sid = ? AND tableName = ? AND operatedUser = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, sid);
				statement.setString(2, tableInfo.getDbTableName());
				statement.setInt(3, userId);

				final ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					if (rs.getDate("endChangeTime") == null) {
						return true;
					}
				}

				return false;

			} catch (final SQLException e) {
				logger.error(String.format("Getting if current table is being edited by user %d FAILED", userId), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting if current table is being edited by user %d ", userId));
			throw e;
		}
	}

	public void releaseTableLock(final int sid, final RelationKey tableInfo)
			throws SQLException {
		logger.info(String.format("Releasing table for sid %d and table %s", sid, tableInfo.getDbTableName()));

		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String currentTime = format.format(new Date());

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = String
					.format("UPDATE colfusion_table_change_log SET endChangeTime = '%s' WHERE endChangeTime IS NULL AND sid = %d  AND tableName = '%s'",
							currentTime, sid, tableInfo.getDbTableName());

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(sql);

			} catch (final SQLException e) {
				logger.error(String.format("Releasing table for sid %d and table %s FAILED", sid, tableInfo.getDbTableName()), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to releasing table for sid %d and table %s ", sid, tableInfo.getDbTableName()));
			throw e;
		}
	}

	public boolean isTimeOut(final int sid, final RelationKey tableInfo, final int allowedTime)
			throws SQLException {
		logger.info(String.format("Getting if it is timeout for sid %d and table %s", sid, tableInfo.getDbTableName()));

		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String currentTime = format.format(new Date());

		String startTime = "";

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "SELECT startChangeTime FROM colfusion_table_change_log WHERE endChangeTime is NULL and sid = ? AND tableName = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {

				statement.setInt(1, sid);
				statement.setString(2, tableInfo.getDbTableName());

				final ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					startTime = rs.getString("startChangeTime");
				}
				if(startTime == "") {
					return false;
				} else {
					return isTimeOutHelper(currentTime, startTime, allowedTime);
				}

			} catch (final SQLException e) {
				logger.error(String.format("Getting if it is timeout for sid %d and table %s FAILED", sid, tableInfo.getDbTableName()),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting if it is timeout for sid %d and table %s ", sid, tableInfo.getDbTableName()));
			throw e;
		}
	}

	public boolean isTimeOutForCurrentUser(final int sid, final RelationKey table, final int colfusionUserId, final int allowedTime)
			throws SQLException {
		logger.info(String.format("Getting if it is timeout for sid %d and table %s", sid, table.getDbTableName()));

		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String currentTime = format.format(new Date());

		String startTime = "";

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "SELECT startChangeTime FROM colfusion_table_change_log WHERE endChangeTime is NULL and sid = ? AND tableName = ? AND operatedUser = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {

				statement.setInt(1, sid);
				statement.setString(2, table.getDbTableName());
				statement.setInt(3, colfusionUserId);

				final ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					startTime = rs.getString("startChangeTime");
					return isTimeOutHelper(currentTime, startTime, allowedTime);
				}
				return true;

			} catch (final SQLException e) {
				logger.error(String.format("Getting if it is timeout for sid %d and table %s FAILED", sid, table.getDbTableName()),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting if it is timeout for sid %d and table %s ", sid, table.getDbTableName()));
			throw e;
		}
	}


	public boolean isTimeOutHelper(final String currentTime, String startTime, final int allowedTime) {
		if (!currentTime.split(" ")[0].equals(startTime.split(" ")[0])) {
			return true;
		}
		startTime = startTime.substring(0, currentTime.length() - 1);
		final String[] arr1 = currentTime.split(" ")[1].split(":");
		final String[] arr2 = startTime.split(" ")[1].split(":");

		final int[] intArr1 = new int[arr1.length];
		final int[] intArr2 = new int[arr2.length];

		for (int i = 0; i < arr1.length; i++) {
			intArr1[i] = Integer.valueOf(arr1[i]);
		}
		for (int j = 0; j < arr2.length; j++) {
			intArr2[j] = Integer.valueOf(arr2[j]);
		}

		if (intArr1[0] - intArr2[0] > 1) {
			return true;
		} else if (intArr1[0] - intArr2[0] == 1) {
			if (intArr1[1] + 60 - intArr2[1] > allowedTime) {
				return true;
			} else {
				return false;
			}
		} else if (intArr1[0] - intArr2[0] == 0) {
			if (intArr1[1] - intArr2[1] > allowedTime) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public void createEditLog(final int sid, final RelationKey tableInfo, final int userId)
			throws SQLException {
		logger.info(String.format("Creating edit log for sid %d, table %s and user %d", sid, tableInfo.getDbTableName(), userId));

		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String startEditTime = format.format(new Date());

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = String.format("INSERT INTO colfusion_table_change_log VALUES(%d, '%s', '%s', NULL, %d)", sid,
					tableInfo.getDbTableName(), startEditTime, userId);

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(sql);

			} catch (final SQLException e) {
				logger.error(String.format("Creating edit log for sid %d, table %s and user %d FAILED", sid, tableInfo.getDbTableName(),
						userId), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to creating edit log for sid %d, table %s and user %d", sid, tableInfo.getDbTableName(),
					userId));
			throw e;
		}
	}

	public boolean isTableLocked(final int sid, final RelationKey tableInfo)
			throws SQLException {

		logger.info(String.format("Getting if table locked for sid %d, table %s", sid, tableInfo.getDbTableName()));
		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "SELECT endChangeTime FROM colfusion_table_change_log WHERE sid = ?  AND tableName = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {

				statement.setInt(1, sid);
				statement.setString(2, tableInfo.getDbTableName());

				final ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					if (rs.getDate("endChangeTime") == null) {
						return true;
					}
				}
				return false;

			} catch (final SQLException e) {
				logger.error(String.format("Getting if table locked for sid %d, table %s FAILED", sid, tableInfo.getDbTableName()), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting if table locked for sid %d, table %s", sid, tableInfo.getDbTableName()));
			throw e;
		}
	}

	public boolean isMapExist(final int sid, final RelationKey table)
			throws SQLException {
		logger.info(String.format("Getting if map exists for sid %d, table %s", sid, table.getDbTableName()));
		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "select * from colfusion_openrefine_project_map where sid = ?  and tableName = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {

				statement.setInt(1, sid);
				statement.setString(2, table.getDbTableName());

				final ResultSet rs = statement.executeQuery();

				while (rs.next()) {
					return true;
				}
				return false;

			} catch (final SQLException e) {
				logger.error(String.format("Getting if map exists for sid %d, table %s FAILED", sid, table.getDbTableName()), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting if map exists for sid %d, table %s", sid, table.getDbTableName()));
			throw e;
		}
	}

	public String getProjectId(final int sid, final RelationKey table)
			throws SQLException {
		logger.info(String.format("Getting projectId for sid %d, table %s", sid, table.getDbTableName()));
		String projectId = "";
		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "select projectId from colfusion_openrefine_project_map where sid = ? and tableName = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {

				statement.setInt(1, sid);
				statement.setString(2, table.getDbTableName());

				final ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					projectId = rs.getString(1);
				}
				return projectId;

			} catch (final SQLException e) {
				logger.error(String.format("Getting projectId for sid %d, table %s FAILED", sid, table.getDbTableName()), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting projectId for sid %d, table %s", sid, table.getDbTableName()));
			throw e;
		}
	}

	public boolean isTableBeingEditing(final int sid, final String tableName)
			throws SQLException {
		logger.info(String.format("Getting if table is being edited for sid %d, table %s", sid, tableName));
		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "SELECT endChangeTime FROM colfusion_table_change_log WHERE sid = ? AND tableName = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {

				statement.setInt(1, sid);
				statement.setString(2, tableName);

				final ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					if (rs.getDate("endChangeTime") == null) {
						return true;
					}
				}
				return false;

			} catch (final SQLException e) {
				logger.error(
						String.format("Getting if table is being edited for sid %d, table %s FAILED", sid, tableName),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String
					.format("FAILED to getting if table is being edited for sid %d, table %s", sid, tableName));
			throw e;
		}
	}

	public void saveRelation(final long projectId, final int sid, final RelationKey table)
			throws SQLException {
		logger.info(String.format("Saving relation for project %d, sid %d and tablename %s", projectId, sid, table.getDbTableName()));

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = String.format("insert into colfusion_openrefine_project_map (`projectId`, `sid`, `tableName`) values('%d', %d, '%s')",
					projectId, sid, table.getDbTableName());

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(sql);

			} catch (final SQLException e) {
				logger.error(String.format("Saving relation for project %d, sid %d and tablename %s FAILED", projectId, sid, table.getDbTableName()),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to saving relation for project %d, sid %d and tablename %s", projectId, sid,
					table.getDbTableName()));
			throw e;
		}
	}

	public ArrayList<String> getColumnNames(final int sid)
			throws SQLException {
		logger.info(String.format("Getting column names for sid %d", sid));
		final ArrayList<String> columnNames = new ArrayList<String>();
		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "select dname_chosen from colfusion_dnameinfo where sid = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, sid);

				final ResultSet rs = statement.executeQuery();

				while (rs.next()) {
					columnNames.add(rs.getString("dname_chosen"));
				}
				return columnNames;
			} catch (final SQLException e) {
				logger.error(String.format("Getting column names for sid %d FAILED", sid), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Getting column names for sid %d", sid));
			throw e;
		}
	}

	//	public String getTableName(final int sid)
	//			throws SQLException {
	//		logger.info(String.format("Getting tableName for sid %d", sid));
	//
	//		try (Connection connection = this.dbHandler.getConnection()) {
	//
	//			final String sql = "select tableName from colfusion_columntableinfo where cid = (select cid from colfusion_dnameinfo where sid = ?  limit 1)";
	//
	//			try (PreparedStatement statement = connection.prepareStatement(sql)) {
	//				statement.setInt(1, sid);
	//
	//				final ResultSet rs = statement.executeQuery();
	//				String tableName = "";
	//				while (rs.next()) {
	//					tableName = rs.getString("tableName");
	//				}
	//				return tableName;
	//			} catch (final SQLException e) {
	//				logger.error(String.format("Getting tableName for sid %d FAILED", sid), e);
	//
	//				throw e;
	//			}
	//		} catch (final SQLException e) {
	//			logger.info(String.format("FAILED to getting tableName for sid %d", sid));
	//			throw e;
	//		}
	//	}

	public boolean isInCurrentUserSession(final int sid, final RelationKey tableInfo, final int colfusionUserId)
			throws SQLException {
		logger.info(String.format("Getting if still in the session for sid %d, tableName %s and user %d", sid, tableInfo.getDbTableName(), colfusionUserId));

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "SELECT * FROM colfusion_table_change_log WHERE endChangeTime is NULL and sid = ? and tableName = ? and operatedUser = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, sid);
				statement.setString(2, tableInfo.getDbTableName());
				statement.setInt(3, colfusionUserId);

				final ResultSet rs = statement.executeQuery();


				while (rs.next()) {
					return true;
				}
				return false;
			} catch (final SQLException e) {
				logger.error(String.format("Getting if still in the session for sid %d, tableName %s and user %d FAILED", sid, tableInfo.getDbTableName(), colfusionUserId), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting if still in the session for sid %d, tableName %s and user %d", sid, tableInfo.getDbTableName(), colfusionUserId));
			throw e;
		}
	}

	public int getSid(final String projectId) throws SQLException {
		logger.info(String.format("Getting sid for project %s", projectId));

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "select sid from colfusion_openrefine_project_map where projectId = ?";

			String sid = "";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, projectId);

				final ResultSet rs = statement.executeQuery();


				while (rs.next()) {
					sid = rs.getString(1);
				}
				return Integer.valueOf(sid == "" ? "0" : sid);
			} catch (final SQLException e) {
				logger.error(String.format("Getting sid for project %s FAILED", projectId), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting sid for project %s", projectId));
			throw e;
		}
	}

	public RelationKey getTableNameByProjectId(final String projectId) throws SQLException {
		logger.info(String.format("Getting tableName for project %s", projectId));

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "select tableName from colfusion_openrefine_project_map where projectId = ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, projectId);

				final ResultSet rs = statement.executeQuery();
				String tableName = "";
				while (rs.next()) {
					tableName = rs.getString("tableName");
				}
				return new RelationKey("", tableName); //TODO: FIXME
			} catch (final SQLException e) {
				logger.error(String.format("Getting tableName for project %s FAILED", projectId), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to getting tableName for project %s", projectId));
			throw e;
		}
	}

	public void refreshStartTime(final int sid, final String tableName)
			throws SQLException {
		logger.info(String.format("Refreshing startTime for sid %d and tablename %s", sid, tableName));

		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String currentTime = format.format(new Date());

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = String.format("UPDATE colfusion_table_change_log SET startChangeTime = '%s' WHERE endChangeTime IS NULL and sid = %d and tableName = '%s'", currentTime, sid, tableName);

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(sql);

			} catch (final SQLException e) {
				logger.error(
						String.format("Refreshing startTime for sid %d and tablename %s FAILED", sid, tableName),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Refreshing startTime for sid %d and tablename %s", sid,
					tableName));
			throw e;
		}
	}

	public ArrayList<Integer> getCidsBySid(final int sid) throws SQLException {
		logger.info(String.format("Getting cids for sid %d", sid));
		final ArrayList<Integer> cids = new ArrayList<>();

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = String.format("SELECT cid FROM colfusion_dnameinfo WHERE sid = %d", sid);

			try (Statement statement = connection.createStatement()) {

				final ResultSet rs = statement.executeQuery(sql);
				while(rs.next()) {
					cids.add(rs.getInt("cid"));
				}
				return cids;
			} catch (final SQLException e) {
				logger.error(
						String.format("Getting cids for sid %d FAILED", sid),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Getting cids for sid %d", sid));
			throw e;
		}
	}

	public void deleteDnameinfoRowsBySid(final int sid)
			throws SQLException {
		logger.info(String.format("Deleting dnameinfo rows by sid %d", sid));

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = String.format("DELETE FROM colfusion_dnameinfo WHERE sid = %d", sid);

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(sql);

			} catch (final SQLException e) {
				logger.error(
						String.format("Deleting dnameinfo rows by sid %d FAILED", sid),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Deleting dnameinfo rows by sid %d", sid));
			throw e;
		}
	}

	public void insertIntoDnameinfo(final String query, final int sid)
			throws SQLException {
		logger.info(String.format("Inserting into dnameinfo by sid %d", sid));

		try {
			final Connection connection = this.dbHandler.getConnection();

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(query);

			} catch (final SQLException e) {
				logger.error(
						String.format("Inserting into dnameinfo by sid %d FAILED", sid),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Inserting into dnameinfo by sid %d", sid));
			throw e;
		}
	}

	public void deleteColumninfoRowsByCid(final String query, final int cid)
			throws SQLException {
		logger.info(String.format("Deleting row from columninfo for cid %d", cid));

		try {
			final Connection connection = this.dbHandler.getConnection();

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(query);

			} catch (final SQLException e) {
				logger.error(
						String.format("Deleting row from columninfo for cid %d FAILED", cid),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Deleting row from columninfo for cid %d", cid));
			throw e;
		}
	}

	public void insertIntoColumninfo(final String query, final String tableName)
			throws SQLException {
		logger.info(String.format("Inserting into columninfo by table %s", tableName));

		try {
			final Connection connection = this.dbHandler.getConnection();

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(query);

			} catch (final SQLException e) {
				logger.error(
						String.format("Inserting into columninfo by table %s FAILED", tableName),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Inserting into columninfo by table %s", tableName));
			throw e;
		}
	}

	// additin here
	public void insertIntoOpenRefineHistoryHelper(final int sid, final RelationKey tableInfo)
			throws SQLException {
		logger.info(String.format("Inserting into OpenRefineHistoryHelper for sid %d, table %s", sid, tableInfo.getDbTableName()));

		try {
			final Connection connection = this.dbHandler.getConnection();

			//TODO:replace with prepared statement
			final String sql = String.format("INSERT INTO colfusion_openrefine_history_helper (`sid`, `tableName`, `count`, `isSaved`) VALUES(%d, '%s', 0, 1)",
					sid, tableInfo.getDbTableName());

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(sql);

			} catch (final SQLException e) {
				logger.error(
						String.format("Inserting into OpenRefineHistoryHelper for sid %d, table %s FAILED", sid, tableInfo.getDbTableName()),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Inserting into OpenRefineHistoryHelper for sid %d, table %s", sid, tableInfo.getDbTableName()));
			throw e;
		}
	}

	public void updateOpenRefineHistoryHelper(final int sid, final RelationKey tableInfo, final int count, final int isSaved)
			throws SQLException {
		logger.info(String.format("Updating OpenRefineHistoryHelper for sid %d, table %s set count = %d", sid, tableInfo.getDbTableName(), count));

		try {
			final Connection connection = this.dbHandler.getConnection();

			//TODO: use prepared statement
			final String sql = String.format("UPDATE colfusion_openrefine_history_helper SET count = %d, isSaved = %d WHERE sid = %d and tableName = '%s'",
					count, isSaved, sid, tableInfo.getDbTableName());

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(sql);

			} catch (final SQLException e) {
				logger.error(
						String.format("Updating OpenRefineHistoryHelper for sid %d, table %s set count = %d FAILED", sid, tableInfo.getDbTableName(), count),
						e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Updating OpenRefineHistoryHelper for sid %d, table %s set count = %d", sid, tableInfo.getDbTableName(), count));
			throw e;
		}
	}

	public int getCountFromOpenRefineHistoryHelper(final int sid, final RelationKey tableInfo) throws SQLException {
		logger.info(String.format("Getting count for sid %d table %s", sid, tableInfo.getDbTableName()));

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "select count from colfusion_openrefine_history_helper where sid = ? and tableName = ?";

			int count = -1;

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, sid);
				statement.setString(2, tableInfo.getDbTableName());

				final ResultSet rs = statement.executeQuery();


				while (rs.next()) {
					count = Integer.valueOf(rs.getString(1));
				}
				return count;
			} catch (final SQLException e) {
				logger.error(String.format("Getting count for sid %d table %s FAILED", sid, tableInfo.getDbTableName()), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Getting count for sid %d table %s", sid, tableInfo.getDbTableName()));
			throw e;
		}
	}

	public int getIsSavedFromOpenRefineHistoryHelper(final int sid, final RelationKey tableInfo) throws SQLException {
		logger.info(String.format("Getting isSaved for sid %d table %s", sid, tableInfo.getDbTableName()));

		try {
			final Connection connection = this.dbHandler.getConnection();

			final String sql = "select isSaved from colfusion_openrefine_history_helper where sid = ? and tableName = ?";

			int isSaved = -1;

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, sid);
				statement.setString(2, tableInfo.getDbTableName());

				final ResultSet rs = statement.executeQuery();


				while (rs.next()) {
					isSaved = Integer.valueOf(rs.getString(1));
				}
				return isSaved;
			} catch (final SQLException e) {
				logger.error(String.format("Getting isSaved for sid %d table %s FAILED", sid, tableInfo.getDbTableName()), e);

				throw e;
			}
		} catch (final SQLException e) {
			logger.info(String.format("FAILED to Getting isSaved for sid %d table %s", sid, tableInfo.getDbTableName()));
			throw e;
		}
	}
}
