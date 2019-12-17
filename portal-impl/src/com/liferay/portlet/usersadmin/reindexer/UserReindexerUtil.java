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

import com.liferay.petra.lang.SafeClosable;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.proxy.ProxyModeThreadLocal;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * @author Minhchau Dang
 */
public class UserReindexerUtil {

	public static void reindex(final List<User> users) throws SearchException {
		long[] userIds = ListUtil.toLongArray(users, User.USER_ID_ACCESSOR);

		reindex(userIds);
	}

	public static void reindex(long... userIds) throws SearchException {
		boolean forceSync = ProxyModeThreadLocal.isForceSync();

		List<UserIndexerRequest> newUserIndexerRequests = new ArrayList<>();

		Indexer<User> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
			User.class);

		for (long userId : userIds) {
			UserIndexerRequest userIndexerRequest = new UserIndexerRequest(
				userId, indexer);

			UserIndexerRequest newUserIndexerRequest =
				_queuedIndexerRequests.computeIfAbsent(
					userIndexerRequest, Function.identity());

			if (forceSync || (newUserIndexerRequest == userIndexerRequest)) {
				newUserIndexerRequests.add(newUserIndexerRequest);
			}
		}

		if (forceSync) {
			_reindex(newUserIndexerRequests);
		}
		else {
			_queuedIndexerRequestExecutorService.submit(
				() -> _syncReindex(newUserIndexerRequests));
		}
	}

	public static void reindex(final User user) throws SearchException {
		reindex(user.getUserId());
	}

	private static void _reindex(List<UserIndexerRequest> userIndexerRequests) {
		int i = 0;

		for (UserIndexerRequest userIndexerRequest : userIndexerRequests) {
			i++;

			if (userIndexerRequest.isExecuting()) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						StringBundler.concat(
							"Currently executing indexer request ", i,
							" in another thread: ", userIndexerRequest));
				}

				continue;
			}

			synchronized (userIndexerRequest) {
				if (userIndexerRequest.isExecuted()) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							StringBundler.concat(
								"Already executed indexer request", i,
								"in another thread: ", userIndexerRequest));
					}

					continue;
				}

				if (_log.isDebugEnabled()) {
					_log.debug(
						StringBundler.concat(
							"Executing indexer request ", i, ": ",
							userIndexerRequest));
				}

				try {
					userIndexerRequest.execute();
				}
				finally {
					_queuedIndexerRequests.remove(userIndexerRequest);
				}
			}
		}
	}

	private static void _syncReindex(
		List<UserIndexerRequest> userIndexerRequests) {

		try (SafeClosable safeClosable =
				ProxyModeThreadLocal.setWithSafeClosable(true)) {

			_reindex(userIndexerRequests);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UserReindexerUtil.class);

	private static final ExecutorService _queuedIndexerRequestExecutorService =
		Executors.newSingleThreadExecutor();
	private static final ConcurrentMap<UserIndexerRequest, UserIndexerRequest>
		_queuedIndexerRequests = new ConcurrentHashMap<>();

}