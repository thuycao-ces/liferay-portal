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

package com.liferay.batch.planner.web.internal.portlet.action;

import com.liferay.batch.planner.constants.BatchPlannerPortletKeys;
import com.liferay.batch.planner.model.BatchPlannerPlan;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseTransactionalMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Igor Beslic
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + BatchPlannerPortletKeys.BATCH_PLANNER,
		"mvc.command.name=/batch_planner/edit_export_batch_planner_plan"
	},
	service = MVCResourceCommand.class
)
public class EditExportBatchPlannerPlanMVResourceCommand
	extends BaseTransactionalMVCResourceCommand {

	@Override
	protected void doTransactionalCommand(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		EditExportBatchPlannerPlanMVCActionCommand
			editExportBatchPlannerPlanMVCActionCommand =
				(EditExportBatchPlannerPlanMVCActionCommand)_mvcActionCommand;

		BatchPlannerPlan batchPlannerPlan =
			editExportBatchPlannerPlanMVCActionCommand.addBatchPlannerPlan(
				resourceRequest);

		JSONPortletResponseUtil.writeJSON(
			resourceRequest, resourceResponse,
			JSONUtil.put(
				"batchPlannerPlanId", batchPlannerPlan.getBatchPlannerPlanId()
			).put(
				"name", batchPlannerPlan.getName()
			).put(
				"success", Boolean.TRUE
			));
	}

	@Reference(
		target = "(component.name=com.liferay.batch.planner.web.internal.portlet.action.EditExportBatchPlannerPlanMVCActionCommand)"
	)
	private MVCActionCommand _mvcActionCommand;

}