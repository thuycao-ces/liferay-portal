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
import com.liferay.portal.kernel.model.StagedAuditedModel;

import java.util.Date;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the CommerceSubscriptionEntry service. Represents a row in the &quot;CommerceSubscriptionEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.commerce.model.impl.CommerceSubscriptionEntryModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.commerce.model.impl.CommerceSubscriptionEntryImpl</code>.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see CommerceSubscriptionEntry
 * @generated
 */
@ProviderType
public interface CommerceSubscriptionEntryModel
	extends BaseModel<CommerceSubscriptionEntry>, GroupedModel, ShardedModel,
			StagedAuditedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a commerce subscription entry model instance should use the {@link CommerceSubscriptionEntry} interface instead.
	 */

	/**
	 * Returns the primary key of this commerce subscription entry.
	 *
	 * @return the primary key of this commerce subscription entry
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this commerce subscription entry.
	 *
	 * @param primaryKey the primary key of this commerce subscription entry
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this commerce subscription entry.
	 *
	 * @return the uuid of this commerce subscription entry
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this commerce subscription entry.
	 *
	 * @param uuid the uuid of this commerce subscription entry
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the commerce subscription entry ID of this commerce subscription entry.
	 *
	 * @return the commerce subscription entry ID of this commerce subscription entry
	 */
	public long getCommerceSubscriptionEntryId();

	/**
	 * Sets the commerce subscription entry ID of this commerce subscription entry.
	 *
	 * @param commerceSubscriptionEntryId the commerce subscription entry ID of this commerce subscription entry
	 */
	public void setCommerceSubscriptionEntryId(
		long commerceSubscriptionEntryId);

	/**
	 * Returns the group ID of this commerce subscription entry.
	 *
	 * @return the group ID of this commerce subscription entry
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this commerce subscription entry.
	 *
	 * @param groupId the group ID of this commerce subscription entry
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this commerce subscription entry.
	 *
	 * @return the company ID of this commerce subscription entry
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this commerce subscription entry.
	 *
	 * @param companyId the company ID of this commerce subscription entry
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this commerce subscription entry.
	 *
	 * @return the user ID of this commerce subscription entry
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this commerce subscription entry.
	 *
	 * @param userId the user ID of this commerce subscription entry
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this commerce subscription entry.
	 *
	 * @return the user uuid of this commerce subscription entry
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this commerce subscription entry.
	 *
	 * @param userUuid the user uuid of this commerce subscription entry
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this commerce subscription entry.
	 *
	 * @return the user name of this commerce subscription entry
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this commerce subscription entry.
	 *
	 * @param userName the user name of this commerce subscription entry
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this commerce subscription entry.
	 *
	 * @return the create date of this commerce subscription entry
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this commerce subscription entry.
	 *
	 * @param createDate the create date of this commerce subscription entry
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this commerce subscription entry.
	 *
	 * @return the modified date of this commerce subscription entry
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this commerce subscription entry.
	 *
	 * @param modifiedDate the modified date of this commerce subscription entry
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the cp instance uuid of this commerce subscription entry.
	 *
	 * @return the cp instance uuid of this commerce subscription entry
	 */
	@AutoEscape
	public String getCPInstanceUuid();

	/**
	 * Sets the cp instance uuid of this commerce subscription entry.
	 *
	 * @param CPInstanceUuid the cp instance uuid of this commerce subscription entry
	 */
	public void setCPInstanceUuid(String CPInstanceUuid);

	/**
	 * Returns the c product ID of this commerce subscription entry.
	 *
	 * @return the c product ID of this commerce subscription entry
	 */
	public long getCProductId();

	/**
	 * Sets the c product ID of this commerce subscription entry.
	 *
	 * @param CProductId the c product ID of this commerce subscription entry
	 */
	public void setCProductId(long CProductId);

	/**
	 * Returns the commerce order item ID of this commerce subscription entry.
	 *
	 * @return the commerce order item ID of this commerce subscription entry
	 */
	public long getCommerceOrderItemId();

	/**
	 * Sets the commerce order item ID of this commerce subscription entry.
	 *
	 * @param commerceOrderItemId the commerce order item ID of this commerce subscription entry
	 */
	public void setCommerceOrderItemId(long commerceOrderItemId);

	/**
	 * Returns the subscription length of this commerce subscription entry.
	 *
	 * @return the subscription length of this commerce subscription entry
	 */
	public int getSubscriptionLength();

	/**
	 * Sets the subscription length of this commerce subscription entry.
	 *
	 * @param subscriptionLength the subscription length of this commerce subscription entry
	 */
	public void setSubscriptionLength(int subscriptionLength);

	/**
	 * Returns the subscription type of this commerce subscription entry.
	 *
	 * @return the subscription type of this commerce subscription entry
	 */
	@AutoEscape
	public String getSubscriptionType();

	/**
	 * Sets the subscription type of this commerce subscription entry.
	 *
	 * @param subscriptionType the subscription type of this commerce subscription entry
	 */
	public void setSubscriptionType(String subscriptionType);

	/**
	 * Returns the subscription type settings of this commerce subscription entry.
	 *
	 * @return the subscription type settings of this commerce subscription entry
	 */
	@AutoEscape
	public String getSubscriptionTypeSettings();

	/**
	 * Sets the subscription type settings of this commerce subscription entry.
	 *
	 * @param subscriptionTypeSettings the subscription type settings of this commerce subscription entry
	 */
	public void setSubscriptionTypeSettings(String subscriptionTypeSettings);

	/**
	 * Returns the current cycle of this commerce subscription entry.
	 *
	 * @return the current cycle of this commerce subscription entry
	 */
	public long getCurrentCycle();

	/**
	 * Sets the current cycle of this commerce subscription entry.
	 *
	 * @param currentCycle the current cycle of this commerce subscription entry
	 */
	public void setCurrentCycle(long currentCycle);

	/**
	 * Returns the max subscription cycles of this commerce subscription entry.
	 *
	 * @return the max subscription cycles of this commerce subscription entry
	 */
	public long getMaxSubscriptionCycles();

	/**
	 * Sets the max subscription cycles of this commerce subscription entry.
	 *
	 * @param maxSubscriptionCycles the max subscription cycles of this commerce subscription entry
	 */
	public void setMaxSubscriptionCycles(long maxSubscriptionCycles);

	/**
	 * Returns the subscription status of this commerce subscription entry.
	 *
	 * @return the subscription status of this commerce subscription entry
	 */
	public int getSubscriptionStatus();

	/**
	 * Sets the subscription status of this commerce subscription entry.
	 *
	 * @param subscriptionStatus the subscription status of this commerce subscription entry
	 */
	public void setSubscriptionStatus(int subscriptionStatus);

	/**
	 * Returns the last iteration date of this commerce subscription entry.
	 *
	 * @return the last iteration date of this commerce subscription entry
	 */
	public Date getLastIterationDate();

	/**
	 * Sets the last iteration date of this commerce subscription entry.
	 *
	 * @param lastIterationDate the last iteration date of this commerce subscription entry
	 */
	public void setLastIterationDate(Date lastIterationDate);

	/**
	 * Returns the next iteration date of this commerce subscription entry.
	 *
	 * @return the next iteration date of this commerce subscription entry
	 */
	public Date getNextIterationDate();

	/**
	 * Sets the next iteration date of this commerce subscription entry.
	 *
	 * @param nextIterationDate the next iteration date of this commerce subscription entry
	 */
	public void setNextIterationDate(Date nextIterationDate);

	/**
	 * Returns the start date of this commerce subscription entry.
	 *
	 * @return the start date of this commerce subscription entry
	 */
	public Date getStartDate();

	/**
	 * Sets the start date of this commerce subscription entry.
	 *
	 * @param startDate the start date of this commerce subscription entry
	 */
	public void setStartDate(Date startDate);

	/**
	 * Returns the delivery subscription length of this commerce subscription entry.
	 *
	 * @return the delivery subscription length of this commerce subscription entry
	 */
	public int getDeliverySubscriptionLength();

	/**
	 * Sets the delivery subscription length of this commerce subscription entry.
	 *
	 * @param deliverySubscriptionLength the delivery subscription length of this commerce subscription entry
	 */
	public void setDeliverySubscriptionLength(int deliverySubscriptionLength);

	/**
	 * Returns the delivery subscription type of this commerce subscription entry.
	 *
	 * @return the delivery subscription type of this commerce subscription entry
	 */
	@AutoEscape
	public String getDeliverySubscriptionType();

	/**
	 * Sets the delivery subscription type of this commerce subscription entry.
	 *
	 * @param deliverySubscriptionType the delivery subscription type of this commerce subscription entry
	 */
	public void setDeliverySubscriptionType(String deliverySubscriptionType);

	/**
	 * Returns the delivery subscription type settings of this commerce subscription entry.
	 *
	 * @return the delivery subscription type settings of this commerce subscription entry
	 */
	@AutoEscape
	public String getDeliverySubscriptionTypeSettings();

	/**
	 * Sets the delivery subscription type settings of this commerce subscription entry.
	 *
	 * @param deliverySubscriptionTypeSettings the delivery subscription type settings of this commerce subscription entry
	 */
	public void setDeliverySubscriptionTypeSettings(
		String deliverySubscriptionTypeSettings);

	/**
	 * Returns the delivery current cycle of this commerce subscription entry.
	 *
	 * @return the delivery current cycle of this commerce subscription entry
	 */
	public long getDeliveryCurrentCycle();

	/**
	 * Sets the delivery current cycle of this commerce subscription entry.
	 *
	 * @param deliveryCurrentCycle the delivery current cycle of this commerce subscription entry
	 */
	public void setDeliveryCurrentCycle(long deliveryCurrentCycle);

	/**
	 * Returns the delivery max subscription cycles of this commerce subscription entry.
	 *
	 * @return the delivery max subscription cycles of this commerce subscription entry
	 */
	public long getDeliveryMaxSubscriptionCycles();

	/**
	 * Sets the delivery max subscription cycles of this commerce subscription entry.
	 *
	 * @param deliveryMaxSubscriptionCycles the delivery max subscription cycles of this commerce subscription entry
	 */
	public void setDeliveryMaxSubscriptionCycles(
		long deliveryMaxSubscriptionCycles);

	/**
	 * Returns the delivery subscription status of this commerce subscription entry.
	 *
	 * @return the delivery subscription status of this commerce subscription entry
	 */
	public int getDeliverySubscriptionStatus();

	/**
	 * Sets the delivery subscription status of this commerce subscription entry.
	 *
	 * @param deliverySubscriptionStatus the delivery subscription status of this commerce subscription entry
	 */
	public void setDeliverySubscriptionStatus(int deliverySubscriptionStatus);

	/**
	 * Returns the delivery last iteration date of this commerce subscription entry.
	 *
	 * @return the delivery last iteration date of this commerce subscription entry
	 */
	public Date getDeliveryLastIterationDate();

	/**
	 * Sets the delivery last iteration date of this commerce subscription entry.
	 *
	 * @param deliveryLastIterationDate the delivery last iteration date of this commerce subscription entry
	 */
	public void setDeliveryLastIterationDate(Date deliveryLastIterationDate);

	/**
	 * Returns the delivery next iteration date of this commerce subscription entry.
	 *
	 * @return the delivery next iteration date of this commerce subscription entry
	 */
	public Date getDeliveryNextIterationDate();

	/**
	 * Sets the delivery next iteration date of this commerce subscription entry.
	 *
	 * @param deliveryNextIterationDate the delivery next iteration date of this commerce subscription entry
	 */
	public void setDeliveryNextIterationDate(Date deliveryNextIterationDate);

	/**
	 * Returns the delivery start date of this commerce subscription entry.
	 *
	 * @return the delivery start date of this commerce subscription entry
	 */
	public Date getDeliveryStartDate();

	/**
	 * Sets the delivery start date of this commerce subscription entry.
	 *
	 * @param deliveryStartDate the delivery start date of this commerce subscription entry
	 */
	public void setDeliveryStartDate(Date deliveryStartDate);

	@Override
	public CommerceSubscriptionEntry cloneWithOriginalValues();

}