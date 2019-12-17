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

package com.liferay.portlet.usersadmin.reindexer;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchException;

/**
 * @author Minhchau Dang
 */
public class UserIndexerRequest {

	public UserIndexerRequest(long userId, Indexer<User> indexer) {
		_userId = userId;
		_indexer = indexer;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof UserIndexerRequest)) {
			return false;
		}

		UserIndexerRequest other = (UserIndexerRequest)o;

		return _userId.equals(other._userId);
	}

	public void execute() {
		try {
			_executing = true;

			_indexer.reindex(User.class.getName(), _userId);

			_executed = true;
		}
		catch (SearchException searchException) {
			_log.error(searchException, searchException);
		}
		finally {
			_executing = false;
		}
	}

	@Override
	public int hashCode() {
		return _userId.hashCode();
	}

	public boolean isExecuted() {
		return _executed;
	}

	public boolean isExecuting() {
		return _executing;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UserIndexerRequest.class);

	private boolean _executed;
	private boolean _executing;
	private final Indexer<User> _indexer;
	private final Long _userId;

}