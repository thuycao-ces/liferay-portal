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

package com.liferay.calendar.web.internal.display.context;

import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarResource;
import com.liferay.calendar.service.CalendarLocalService;
import com.liferay.calendar.service.CalendarService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.LogUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.util.log4j.Log4JUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Adam Brandizzi
 */
public class CalendarDisplayContext {

	public CalendarDisplayContext(
		GroupLocalService groupLocalService, CalendarService calendarService,
		CalendarLocalService calendarLocalService, ThemeDisplay themeDisplay) {

		_groupLocalService = groupLocalService;
		_calendarService = calendarService;
		_calendarLocalService = calendarLocalService;
		_themeDisplay = themeDisplay;
	}

	public List<Calendar> getOtherCalendars(long[] calendarIds)
		throws PortalException {

		List<Calendar> otherCalendars = new ArrayList<>();

		Calendar calendar = null;

		for (long calendarId : calendarIds) {

			try
			{
				calendar = _calendarService.fetchCalendar(calendarId);
			}
			catch(Exception e)
			{
				if(_log.isInfoEnabled()){
					_log.info(e.getMessage());
				}
			}

			if (calendar == null) {
				continue;
			}

			CalendarResource calendarResource = calendar.getCalendarResource();

			if (!calendarResource.isActive()) {
				continue;
			}

			Group scopeGroup = _themeDisplay.getScopeGroup();

			long scopeGroupId = scopeGroup.getGroupId();
			long scopeLiveGroupId = scopeGroup.getLiveGroupId();

			Group calendarGroup = _groupLocalService.getGroup(
				calendar.getGroupId());

			long calendarGroupId = calendarGroup.getGroupId();

			if (scopeGroup.isStagingGroup()) {
				if (calendarGroup.isStagingGroup()) {
					if (scopeGroupId != calendarGroupId) {
						calendar =
							_calendarLocalService.fetchCalendarByUuidAndGroupId(
								calendar.getUuid(),
								calendarGroup.getLiveGroupId());
					}
				}
				else if (scopeLiveGroupId == calendarGroupId) {
					Group stagingGroup = calendarGroup.getStagingGroup();

					calendar =
						_calendarLocalService.fetchCalendarByUuidAndGroupId(
							calendar.getUuid(), stagingGroup.getGroupId());
				}
			}
			else if (calendarGroup.isStagingGroup()) {
				calendar = _calendarLocalService.fetchCalendarByUuidAndGroupId(
					calendar.getUuid(), calendarGroup.getLiveGroupId());
			}

			if (calendar == null) {
				continue;
			}

			otherCalendars.add(calendar);
		}

		return otherCalendars;
	}

	private final CalendarLocalService _calendarLocalService;
	private final CalendarService _calendarService;
	private final GroupLocalService _groupLocalService;
	private final ThemeDisplay _themeDisplay;
	private static final Log _log = LogFactoryUtil.getLog(CalendarDisplayContext.class);

}