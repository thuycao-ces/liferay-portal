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

package com.liferay.commerce.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.ShardedModel;

import java.util.Date;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the CommerceOrderNote service. Represents a row in the &quot;CommerceOrderNote&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.commerce.model.impl.CommerceOrderNoteModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.commerce.model.impl.CommerceOrderNoteImpl</code>.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see CommerceOrderNote
 * @generated
 */
@ProviderType
public interface CommerceOrderNoteModel
	extends BaseModel<CommerceOrderNote>, GroupedModel, ShardedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a commerce order note model instance should use the {@link CommerceOrderNote} interface instead.
	 */

	/**
	 * Returns the primary key of this commerce order note.
	 *
	 * @return the primary key of this commerce order note
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this commerce order note.
	 *
	 * @param primaryKey the primary key of this commerce order note
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the external reference code of this commerce order note.
	 *
	 * @return the external reference code of this commerce order note
	 */
	@AutoEscape
	public String getExternalReferenceCode();

	/**
	 * Sets the external reference code of this commerce order note.
	 *
	 * @param externalReferenceCode the external reference code of this commerce order note
	 */
	public void setExternalReferenceCode(String externalReferenceCode);

	/**
	 * Returns the commerce order note ID of this commerce order note.
	 *
	 * @return the commerce order note ID of this commerce order note
	 */
	public long getCommerceOrderNoteId();

	/**
	 * Sets the commerce order note ID of this commerce order note.
	 *
	 * @param commerceOrderNoteId the commerce order note ID of this commerce order note
	 */
	public void setCommerceOrderNoteId(long commerceOrderNoteId);

	/**
	 * Returns the group ID of this commerce order note.
	 *
	 * @return the group ID of this commerce order note
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this commerce order note.
	 *
	 * @param groupId the group ID of this commerce order note
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this commerce order note.
	 *
	 * @return the company ID of this commerce order note
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this commerce order note.
	 *
	 * @param companyId the company ID of this commerce order note
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this commerce order note.
	 *
	 * @return the user ID of this commerce order note
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this commerce order note.
	 *
	 * @param userId the user ID of this commerce order note
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this commerce order note.
	 *
	 * @return the user uuid of this commerce order note
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this commerce order note.
	 *
	 * @param userUuid the user uuid of this commerce order note
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this commerce order note.
	 *
	 * @return the user name of this commerce order note
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this commerce order note.
	 *
	 * @param userName the user name of this commerce order note
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this commerce order note.
	 *
	 * @return the create date of this commerce order note
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this commerce order note.
	 *
	 * @param createDate the create date of this commerce order note
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this commerce order note.
	 *
	 * @return the modified date of this commerce order note
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this commerce order note.
	 *
	 * @param modifiedDate the modified date of this commerce order note
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the commerce order ID of this commerce order note.
	 *
	 * @return the commerce order ID of this commerce order note
	 */
	public long getCommerceOrderId();

	/**
	 * Sets the commerce order ID of this commerce order note.
	 *
	 * @param commerceOrderId the commerce order ID of this commerce order note
	 */
	public void setCommerceOrderId(long commerceOrderId);

	/**
	 * Returns the content of this commerce order note.
	 *
	 * @return the content of this commerce order note
	 */
	@AutoEscape
	public String getContent();

	/**
	 * Sets the content of this commerce order note.
	 *
	 * @param content the content of this commerce order note
	 */
	public void setContent(String content);

	/**
	 * Returns the restricted of this commerce order note.
	 *
	 * @return the restricted of this commerce order note
	 */
	public boolean getRestricted();

	/**
	 * Returns <code>true</code> if this commerce order note is restricted.
	 *
	 * @return <code>true</code> if this commerce order note is restricted; <code>false</code> otherwise
	 */
	public boolean isRestricted();

	/**
	 * Sets whether this commerce order note is restricted.
	 *
	 * @param restricted the restricted of this commerce order note
	 */
	public void setRestricted(boolean restricted);

	@Override
	public CommerceOrderNote cloneWithOriginalValues();

}