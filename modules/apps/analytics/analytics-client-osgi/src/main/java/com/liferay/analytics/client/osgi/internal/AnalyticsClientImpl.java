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

package com.liferay.analytics.client.osgi.internal;

import com.liferay.analytics.client.AnalyticsClient;
import com.liferay.analytics.client.IdentityClient;
import com.liferay.analytics.client.osgi.internal.configuration.AnalyticsClientConfiguration;
import com.liferay.analytics.data.binding.JSONObjectMapper;
import com.liferay.analytics.model.AnalyticsEventsMessage;
import com.liferay.analytics.model.IdentityContextMessage;
import com.liferay.petra.json.web.service.client.JSONWebServiceClient;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.TimeZoneThreadLocal;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo García
 */
@Component(
	configurationPid = "com.liferay.analytics.client.osgi.internal.configuration.AnalyticsClientConfiguration",
	service = AnalyticsClient.class
)
public class AnalyticsClientImpl implements AnalyticsClient {

	@Override
	public String sendAnalytics(AnalyticsEventsMessage analyticsEventsMessage)
		throws Exception {

		if (analyticsEventsMessage.getUserId() == null) {
			String userId = getUserId(analyticsEventsMessage.getDataSourceId());

			AnalyticsEventsMessage.Builder builder =
				AnalyticsEventsMessage.builder(analyticsEventsMessage);

			analyticsEventsMessage = builder.userId(
				userId
			).build();
		}

		String jsonAnalyticsEventsMessage = _jsonObjectMapper.map(
			analyticsEventsMessage);

		if (_log.isDebugEnabled()) {
			_log.debug(
				String.format(
					"Sending analytics message %s to destination %s//%s:%s%s",
					jsonAnalyticsEventsMessage,
					_analyticsClientConfiguration.analyticsGatewayProtocol(),
					_analyticsClientConfiguration.analyticsGatewayHost(),
					_analyticsClientConfiguration.analyticsGatewayPort(),
					_analyticsClientConfiguration.analyticsGatewayPath()));
		}

		return _jsonWebServiceClient.doPostAsJSON(
			_analyticsClientConfiguration.analyticsGatewayPath(),
			jsonAnalyticsEventsMessage);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_analyticsClientConfiguration = ConfigurableUtil.createConfigurable(
			AnalyticsClientConfiguration.class, properties);

		ComponentInstance componentInstance = _componentFactory.newInstance(
			HashMapDictionaryBuilder.put(
				"hostName", _analyticsClientConfiguration.analyticsGatewayHost()
			).put(
				"hostPort", _analyticsClientConfiguration.analyticsGatewayPort()
			).put(
				"protocol",
				_analyticsClientConfiguration.analyticsGatewayProtocol()
			).build());

		_jsonWebServiceClient =
			(JSONWebServiceClient)componentInstance.getInstance();
	}

	protected String getUserId(String dataSourceId) throws Exception {
		HttpSession httpSession = PortalSessionThreadLocal.getHttpSession();

		if (httpSession != null) {
			Object userIdObject = httpSession.getAttribute(
				_REQUEST_ATTRIBUTE_NAME_ANALYTICS_USER_ID);

			if (userIdObject != null) {
				return (String)userIdObject;
			}
		}

		IdentityContextMessage.Builder identityContextMessageBuilder =
			IdentityContextMessage.builder(dataSourceId);

		identityContextMessageBuilder.protocolVersion("1.0");

		// User profile

		User user = _userLocalService.fetchUser(
			PrincipalThreadLocal.getUserId());

		if (user != null) {
			identityContextMessageBuilder.dataSourceIndividualIdentifier(
				GetterUtil.getString(user.getUserId()));

			identityContextMessageBuilder.identityFieldsProperty(
				"email", user.getEmailAddress());
			identityContextMessageBuilder.identityFieldsProperty(
				"name", user.getFullName());
		}

		// User session info

		Locale locale = LocaleThreadLocal.getThemeDisplayLocale();

		if (locale != null) {
			identityContextMessageBuilder.language(locale.getLanguage());
		}

		TimeZone timeZone = TimeZoneThreadLocal.getThemeDisplayTimeZone();

		if (timeZone != null) {
			identityContextMessageBuilder.timezone(timeZone.getDisplayName());
		}

		String userId = _identityClient.getUserId(
			identityContextMessageBuilder.build());

		if (httpSession != null) {
			httpSession.setAttribute(
				_REQUEST_ATTRIBUTE_NAME_ANALYTICS_USER_ID, userId);
		}

		return userId;
	}

	private static final String _REQUEST_ATTRIBUTE_NAME_ANALYTICS_USER_ID =
		"ANALYTICS_USER_ID";

	private static final Log _log = LogFactoryUtil.getLog(
		AnalyticsClientImpl.class);

	private volatile AnalyticsClientConfiguration _analyticsClientConfiguration;

	@Reference(
		target = "(component.factory=com.liferay.petra.json.web.service.client.JSONWebServiceClient)"
	)
	private ComponentFactory _componentFactory;

	@Reference
	private IdentityClient _identityClient;

	@Reference(
		target = "(model=com.liferay.analytics.model.AnalyticsEventsMessage)"
	)
	private JSONObjectMapper<AnalyticsEventsMessage> _jsonObjectMapper;

	private volatile JSONWebServiceClient _jsonWebServiceClient;

	@Reference
	private UserLocalService _userLocalService;

}