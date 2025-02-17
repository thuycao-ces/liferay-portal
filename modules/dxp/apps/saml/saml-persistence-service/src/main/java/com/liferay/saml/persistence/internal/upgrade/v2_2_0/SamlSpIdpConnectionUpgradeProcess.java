/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.saml.persistence.internal.upgrade.v2_2_0;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.saml.persistence.internal.upgrade.v2_2_0.util.SamlSpIdpConnectionTable;

/**
 * @author Stian Sigvartsen
 */
public class SamlSpIdpConnectionUpgradeProcess extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		alter(
			SamlSpIdpConnectionTable.class,
			new AlterTableAddColumn("unknownUsersAreStrangers", "BOOLEAN"));
	}

}