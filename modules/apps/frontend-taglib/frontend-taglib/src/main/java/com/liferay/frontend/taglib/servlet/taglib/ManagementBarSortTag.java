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

package com.liferay.frontend.taglib.servlet.taglib;

import com.liferay.frontend.taglib.internal.servlet.ServletContextUtil;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.taglib.util.IncludeTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;

/**
 * @author Eudaldo Alonso
 */
public class ManagementBarSortTag extends IncludeTag implements BodyTag {

	@Override
	public int doStartTag() throws JspException {
		setAttributeNamespace(_ATTRIBUTE_NAMESPACE);

		return super.doStartTag();
	}

	public String getOrderByCol() {
		return _orderByCol;
	}

	public String getOrderByType() {
		return _orderByType;
	}

	public Map<String, String> getOrderColumns() {
		return _orderColumns;
	}

	public PortletURL getPortletURL() {
		return _portletURL;
	}

	public void setDisabled(boolean disabled) {
		_disabled = disabled;
	}

	public void setOrderByCol(String orderByCol) {
		_orderByCol = orderByCol;
	}

	public void setOrderByType(String orderByType) {
		_orderByType = orderByType;
	}

	public void setOrderColumns(Map<String, String> orderColumns) {
		_orderColumns = orderColumns;
	}

	public void setOrderColumns(String[] orderColumns) {
		for (String orderColumn : orderColumns) {
			_orderColumns.put(orderColumn, orderColumn);
		}
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		setServletContext(ServletContextUtil.getServletContext());
	}

	public void setPortletURL(PortletURL portletURL) {
		_portletURL = portletURL;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_disabled = null;
		_orderByCol = StringPool.BLANK;
		_orderByType = StringPool.BLANK;
		_orderColumns = new HashMap<>();
		_portletURL = null;
	}

	protected List<ManagementBarFilterItem> getManagementBarFilterItems() {
		HttpServletRequest httpServletRequest = getRequest();

		PortletResponse portletResponse =
			(PortletResponse)httpServletRequest.getAttribute(
				JavaConstants.JAVAX_PORTLET_RESPONSE);

		LiferayPortletResponse liferayPortletResponse =
			PortalUtil.getLiferayPortletResponse(portletResponse);

		List<ManagementBarFilterItem> managementBarFilterItems =
			new ArrayList<>();

		try {
			PortletURL orderByColURL = PortletURLBuilder.create(
				PortletURLUtil.clone(_portletURL, liferayPortletResponse)
			).setParameter(
				"orderByType", _orderByType
			).buildPortletURL();

			for (Map.Entry<String, String> entry : _orderColumns.entrySet()) {
				String orderColumn = entry.getKey();

				orderByColURL.setParameter("orderByCol", orderColumn);

				managementBarFilterItems.add(
					new ManagementBarFilterItem(
						_orderByCol.equals(orderColumn), entry.getValue(),
						orderByColURL.toString()));
			}
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception, exception);
			}
		}

		return managementBarFilterItems;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected boolean isCleanUpSetAttributes() {
		return _CLEAN_UP_SET_ATTRIBUTES;
	}

	protected boolean isDisabled() {
		ManagementBarTag managementBarTag =
			(ManagementBarTag)findAncestorWithClass(
				this, ManagementBarTag.class);

		boolean disabled = false;

		if (_disabled != null) {
			disabled = _disabled;
		}
		else if (managementBarTag != null) {
			disabled = managementBarTag.isDisabled();
		}

		return disabled;
	}

	@Override
	protected int processStartTag() throws Exception {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	protected void setAttributes(HttpServletRequest httpServletRequest) {
		httpServletRequest.setAttribute(
			"liferay-frontend:management-bar-sort:disabled", isDisabled());
		httpServletRequest.setAttribute(
			"liferay-frontend:management-bar-sort:managementBarFilterItems",
			getManagementBarFilterItems());
		httpServletRequest.setAttribute(
			"liferay-frontend:management-bar-sort:orderByCol", _orderByCol);
		httpServletRequest.setAttribute(
			"liferay-frontend:management-bar-sort:orderByColLabel",
			_orderColumns.get(_orderByCol));
		httpServletRequest.setAttribute(
			"liferay-frontend:management-bar-sort:orderByType", _orderByType);
		httpServletRequest.setAttribute(
			"liferay-frontend:management-bar-sort:portletURL", _portletURL);
	}

	private static final String _ATTRIBUTE_NAMESPACE =
		"liferay-frontend:management-bar-sort:";

	private static final boolean _CLEAN_UP_SET_ATTRIBUTES = true;

	private static final String _PAGE = "/management_bar_sort/page.jsp";

	private static final Log _log = LogFactoryUtil.getLog(
		ManagementBarSortTag.class);

	private Boolean _disabled;
	private String _orderByCol = StringPool.BLANK;
	private String _orderByType = StringPool.BLANK;
	private Map<String, String> _orderColumns = new HashMap<>();
	private PortletURL _portletURL;

}