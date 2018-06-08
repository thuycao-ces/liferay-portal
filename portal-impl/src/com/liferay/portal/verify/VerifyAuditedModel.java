/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.verify;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.db.DBInspector;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.dao.db.DBTypeToSQLMap;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.FullNameGenerator;
import com.liferay.portal.kernel.security.auth.FullNameGeneratorFactory;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.verify.model.VerifiableAuditedModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author Michael C. Han
 * @author Shinn Lok
 */
public class VerifyAuditedModel extends VerifyProcess {

	public void verify(VerifiableAuditedModel... verifiableAuditedModels)
		throws Exception {

		Set<String> unverifiedTableNames = new HashSet<>();

		for (VerifiableAuditedModel verifiableAuditedModel :
				verifiableAuditedModels) {

			unverifiedTableNames.add(verifiableAuditedModel.getTableName());
		}

		while (!unverifiedTableNames.isEmpty()) {
			List<VerifyAuditedModelCallable> verifyAuditedModelCallables =
				new ArrayList<>(unverifiedTableNames.size());

			for (VerifiableAuditedModel verifiableAuditedModel :
					verifiableAuditedModels) {

				String tableName = verifiableAuditedModel.getTableName();

				if (!unverifiedTableNames.contains(tableName)) {
					continue;
				}

				String relatedModelName =
					verifiableAuditedModel.getRelatedModelName();

				if ((relatedModelName != null) &&
					unverifiedTableNames.contains(relatedModelName)) {

					continue;
				}

				VerifyAuditedModelCallable verifyAuditedModelCallable =
					new VerifyAuditedModelCallable(verifiableAuditedModel);

				verifyAuditedModelCallables.add(verifyAuditedModelCallable);

				unverifiedTableNames.remove(tableName);
			}

			if (verifyAuditedModelCallables.isEmpty()) {
				throw new VerifyException(
					"Circular dependency detected " + unverifiedTableNames);
			}

			doVerify(verifyAuditedModelCallables);
		}
	}

	@Override
	protected void doVerify() throws Exception {
		Map<String, VerifiableAuditedModel> verifiableAuditedModelsMap =
			PortalBeanLocatorUtil.locate(VerifiableAuditedModel.class);

		Collection<VerifiableAuditedModel> verifiableAuditedModels =
			verifiableAuditedModelsMap.values();

		verify(
			verifiableAuditedModels.toArray(
				new VerifiableAuditedModel[verifiableAuditedModels.size()]));
	}

	protected Object[] getAuditedModelArray(
			Connection con, String tableName, String pkColumnName, long primKey,
			boolean allowAnonymousUser, long previousUserId)
		throws Exception {

		try (PreparedStatement ps = con.prepareStatement(
				StringBundler.concat(
					"select companyId, userId, createDate, modifiedDate from ",
					tableName, " where ", pkColumnName, " = ?"))) {

			ps.setLong(1, primKey);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					long companyId = rs.getLong("companyId");

					long userId = 0;
					String userName = null;

					if (allowAnonymousUser) {
						userId = previousUserId;
						userName = "Anonymous";
					}
					else {
						userId = rs.getLong("userId");

						userName = getUserName(con, userId);
					}

					Timestamp createDate = rs.getTimestamp("createDate");
					Timestamp modifiedDate = rs.getTimestamp("modifiedDate");

					return new Object[] {
						companyId, userId, userName, createDate, modifiedDate
					};
				}

				if (_log.isDebugEnabled()) {
					_log.debug(
						StringBundler.concat(
							"Unable to find ", tableName, " ",
							String.valueOf(primKey)));
				}

				return null;
			}
		}
	}

	protected Object[] getDefaultUserArray(Connection con, long companyId)
		throws Exception {

		try (PreparedStatement ps = con.prepareStatement(
				"select userId, firstName, middleName, lastName from User_ " +
					"where companyId = ? and defaultUser = ?")) {

			ps.setLong(1, companyId);
			ps.setBoolean(2, true);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					long userId = rs.getLong("userId");
					String firstName = rs.getString("firstName");
					String middleName = rs.getString("middleName");
					String lastName = rs.getString("lastName");

					FullNameGenerator fullNameGenerator =
						FullNameGeneratorFactory.getInstance();

					String userName = fullNameGenerator.getFullName(
						firstName, middleName, lastName);

					Timestamp createDate = new Timestamp(
						System.currentTimeMillis());

					return new Object[] {
						companyId, userId, userName, createDate, createDate
					};
				}

				return null;
			}
		}
	}

	protected String getUserName(Connection con, long userId) throws Exception {
		try (PreparedStatement ps = con.prepareStatement(
				"select firstName, middleName, lastName from User_ where " +
					"userId = ?")) {

			ps.setLong(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String firstName = rs.getString("firstName");
					String middleName = rs.getString("middleName");
					String lastName = rs.getString("lastName");

					FullNameGenerator fullNameGenerator =
						FullNameGeneratorFactory.getInstance();

					return fullNameGenerator.getFullName(
						firstName, middleName, lastName);
				}

				return StringPool.BLANK;
			}
		}
	}

	protected void verifyAuditedModel(
			Connection con, PreparedStatement ps, String tableName,
			long primKey, Object[] auditedModelArray, boolean updateDates)
		throws Exception {

		try {
			long companyId = (Long)auditedModelArray[0];

			if (auditedModelArray[2] == null) {
				auditedModelArray = getDefaultUserArray(con, companyId);

				if (auditedModelArray == null) {
					return;
				}
			}

			long userId = (Long)auditedModelArray[1];
			String userName = (String)auditedModelArray[2];

			ps.setLong(1, companyId);
			ps.setLong(2, userId);
			ps.setString(3, userName);

			if (updateDates) {
				Timestamp createDate = (Timestamp)auditedModelArray[3];

				ps.setTimestamp(4, createDate);

				Timestamp modifiedDate = (Timestamp)auditedModelArray[4];

				ps.setTimestamp(5, modifiedDate);

				ps.setLong(6, primKey);
			}
			else {
				ps.setLong(4, primKey);
			}

			ps.addBatch();
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to verify model " + tableName, e);
			}
		}
	}

	protected void verifyAuditedModel(
			VerifiableAuditedModel verifiableAuditedModel)
		throws Exception {

		try (LoggingTimer loggingTimer = new LoggingTimer(
				verifiableAuditedModel.getTableName());
			Connection con = DataAccess.getConnection()) {

			String relatedModelName =
				verifiableAuditedModel.getRelatedModelName();

			if (relatedModelName != null) {
				_verifyFromRelatedModel(
					con, verifiableAuditedModel, "companyId");

				_verifyFromRelatedModel(con, verifiableAuditedModel, "userId");

				if (verifiableAuditedModel.isUpdateDates()) {
					_verifyFromRelatedModel(
						con, verifiableAuditedModel, "createDate");

					_verifyFromRelatedModel(
						con, verifiableAuditedModel, "modifiedDate");
				}

				DBInspector dbInspector = new DBInspector(con);

				if (dbInspector.hasColumn(relatedModelName, "userName")) {
					_verifyFromRelatedModel(
						con, verifiableAuditedModel, "userName");
				}
			}

			_verifyUserNameRegularUsers(con, verifiableAuditedModel);

			_verifyUnresolvedUsers(con, verifiableAuditedModel);
		}
	}

	private void _verifyFromRelatedModel(
			Connection con, VerifiableAuditedModel verifiableAuditedModel,
			String columnName)
		throws Exception {

		String sql = StringBundler.concat(
			"update ", verifiableAuditedModel.getTableName(), " set ",
			columnName, " = (select ", columnName, " from ",
			verifiableAuditedModel.getRelatedModelName(), " where ",
			verifiableAuditedModel.getTableName(), StringPool.PERIOD,
			verifiableAuditedModel.getJoinByTableName(), " = ",
			verifiableAuditedModel.getRelatedModelName(), StringPool.PERIOD,
			verifiableAuditedModel.getRelatedPKColumnName(),
			") where userName is null");

		DBTypeToSQLMap dbTypeToSQLMap = new DBTypeToSQLMap(sql);

		sql = StringBundler.concat(
			"update ", verifiableAuditedModel.getTableName(), " inner join ",
			verifiableAuditedModel.getRelatedModelName(), " on ",
			verifiableAuditedModel.getTableName(), StringPool.PERIOD,
			verifiableAuditedModel.getJoinByTableName(), " = ",
			verifiableAuditedModel.getRelatedModelName(), StringPool.PERIOD,
			verifiableAuditedModel.getRelatedPKColumnName(), " set ",
			verifiableAuditedModel.getTableName(), StringPool.PERIOD,
			columnName, " = ", verifiableAuditedModel.getRelatedModelName(),
			StringPool.PERIOD, columnName, " where ",
			verifiableAuditedModel.getTableName(), ".userName is null");

		dbTypeToSQLMap.add(DBType.MARIADB, sql);
		dbTypeToSQLMap.add(DBType.MYSQL, sql);

		runSQL(con, dbTypeToSQLMap);
	}

	private void _verifyUnresolvedUsers(
			Connection con, VerifiableAuditedModel verifiableAuditedModel)
		throws Exception {

		String createDateSQL = StringBundler.concat(
			"update ", verifiableAuditedModel.getTableName(),
			" set createDate = ? where createDate is null");

		String modifiedDateSQL = StringBundler.concat(
			"update ", verifiableAuditedModel.getTableName(),
			" set modifiedDate = ? where modifiedDate is null");

		String setUserFields = StringPool.BLANK;
		String selectCompanyField = StringPool.BLANK;

		if (verifiableAuditedModel.isAnonymousUserAllowed()) {
			setUserFields = "set userName = ?";
		}
		else {
			setUserFields = "userId = ?, userName = ?";
			selectCompanyField = " and companyId = ?";
		}

		String userNameSQL = StringBundler.concat(
			"update ", verifiableAuditedModel.getTableName(), " set",
			setUserFields, " where userName is null", selectCompanyField);

		try (PreparedStatement ps1 = AutoBatchPreparedStatementUtil.autoBatch(
				con.prepareStatement(createDateSQL));
			PreparedStatement ps2 = AutoBatchPreparedStatementUtil.autoBatch(
				con.prepareStatement(modifiedDateSQL));
			PreparedStatement ps3 = AutoBatchPreparedStatementUtil.autoBatch(
				con.prepareStatement(userNameSQL))) {

			if (verifiableAuditedModel.isAnonymousUserAllowed()) {
				if (verifiableAuditedModel.isUpdateDates()) {
					Timestamp auditDate = new Timestamp(
						System.currentTimeMillis());

					ps1.setTimestamp(1, auditDate);

					ps1.addBatch();

					ps2.setTimestamp(1, auditDate);

					ps2.addBatch();
				}

				ps3.setString(1, "Anonymous");

				ps3.addBatch();
			}
			else {
				for (long companyId : PortalUtil.getCompanyIds()) {
					Object[] defaultUserArray = getDefaultUserArray(
						con, companyId);

					if (verifiableAuditedModel.isUpdateDates()) {
						Timestamp createDate = (Timestamp)defaultUserArray[3];

						ps1.setTimestamp(1, createDate);

						ps1.addBatch();

						Timestamp modifiedDate = (Timestamp)defaultUserArray[4];

						ps2.setTimestamp(1, modifiedDate);

						ps2.addBatch();
					}

					long userId = (Long)defaultUserArray[1];
					String defaultUserFullName = (String)defaultUserArray[2];

					ps3.setLong(1, userId);
					ps3.setString(2, defaultUserFullName);
					ps3.setLong(3, companyId);

					ps3.addBatch();
				}
			}

			ps1.executeBatch();
			ps2.executeBatch();
			ps3.executeBatch();
		}
	}

	private void _verifyUserNameRegularUsers(
			Connection con, VerifiableAuditedModel verifiableAuditedModel)
		throws Exception {

		String selectQuery = StringBundler.concat(
			"select companyId, userId, firstName, middleName, lastName from ",
			"User_ where exists (select 1 from ",
			verifiableAuditedModel.getTableName(), " where ",
			verifiableAuditedModel.getTableName(), " = User_.userId and ",
			verifiableAuditedModel.getTableName(), ".userName is null)");

		String updateQuery = StringBundler.concat(
			"update ", verifiableAuditedModel.getTableName(),
			" set companyId = ?, userName = ? where userId = ?");

		FullNameGenerator fullNameGenerator =
			FullNameGeneratorFactory.getInstance();

		try (PreparedStatement ps1 = con.prepareStatement(selectQuery);
			ResultSet rs = ps1.executeQuery();
			PreparedStatement ps2 = AutoBatchPreparedStatementUtil.autoBatch(
				con.prepareStatement(updateQuery))) {

			while (rs.next()) {
				long companyId = rs.getLong("companyId");
				long userId = rs.getLong("userId");

				String firstName = rs.getString("firstName");
				String middleName = rs.getString("middleName");
				String lastName = rs.getString("lastName");

				String userName = fullNameGenerator.getFullName(
					firstName, middleName, lastName);

				ps2.setLong(1, companyId);
				ps2.setString(2, userName);
				ps2.setLong(3, userId);

				ps2.addBatch();
			}

			ps2.executeBatch();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		VerifyAuditedModel.class);

	private class VerifyAuditedModelCallable implements Callable<Void> {

		@Override
		public Void call() throws Exception {
			verifyAuditedModel(_verifiableAuditedModel);

			return null;
		}

		private VerifyAuditedModelCallable(
			VerifiableAuditedModel verifiableAuditedModel) {

			_verifiableAuditedModel = verifiableAuditedModel;
		}

		private final VerifiableAuditedModel _verifiableAuditedModel;

	}

}