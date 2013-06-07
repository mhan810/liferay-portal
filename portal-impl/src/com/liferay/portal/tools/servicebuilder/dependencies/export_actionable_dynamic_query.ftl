package ${packagePath}.service.persistence;

import ${packagePath}.model.${entity.name};
import ${packagePath}.service.${entity.name}LocalServiceUtil;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.ManifestSummary;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerRegistryUtil;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.model.SystemEventConstants;
import com.liferay.portal.service.persistence.SystemEventEntryActionableDynamicQuery;
import com.liferay.portal.util.PortalUtil;

import java.util.Date;

/**
 * @author ${author}
 * @generated
 */
public class ${entity.name}ExportActionableDynamicQuery extends ${entity.name}ActionableDynamicQuery {

	public ${entity.name}ExportActionableDynamicQuery(PortletDataContext portletDataContext) throws SystemException {
		_portletDataContext = portletDataContext;

		<#if entity.isStagedGroupedModel()>
			setGroupId(_portletDataContext.getScopeGroupId());
		</#if>
	}

	@Override
	public long performCount() throws PortalException, SystemException {
		long count = super.performCount();

		ManifestSummary manifestSummary = _portletDataContext.getManifestSummary();

		manifestSummary.addModelCount(getManifestSummaryKey(), count);

		long deletionCount = getDeletionCount();

		manifestSummary.addDeletionCount(getManifestSummaryKey(), deletionCount);

		return count;
	}

	@Override
	protected void addCriteria(DynamicQuery dynamicQuery) {
		_portletDataContext.addDateRangeCriteria(dynamicQuery, "modifiedDate");
	}

	protected long getDeletionCount()
		throws PortalException, SystemException {

		ActionableDynamicQuery actionableDynamicQuery =
			new SystemEventEntryActionableDynamicQuery() {

				@Override
				protected void addCriteria(DynamicQuery dynamicQuery) {
					Property groupProperty = PropertyFactoryUtil.forName("groupId");

					dynamicQuery.add(groupProperty.eq(_portletDataContext.getScopeGroupId()));

					Property typeProperty = PropertyFactoryUtil.forName("type");

					dynamicQuery.add(typeProperty.eq(SystemEventConstants.TYPE_DELETE));

					Property classNameIdProperty = PropertyFactoryUtil.forName("classNameId");

					dynamicQuery.add(
						classNameIdProperty.eq(
							PortalUtil.getClassNameId(
								${entity.name}.class.getName())));

					if (!_portletDataContext.hasDateRange()) {
						return;
					}

					Property modifiedDateProperty = PropertyFactoryUtil.forName("createDate");

					Date startDate = _portletDataContext.getStartDate();
					Date endDate = _portletDataContext.getEndDate();

					dynamicQuery.add(modifiedDateProperty.ge(startDate.getTime()));
					dynamicQuery.add(modifiedDateProperty.le(endDate.getTime()));
				}

				@Override
				protected void performAction(Object object)
					throws PortalException, SystemException {
				}
			};

		return actionableDynamicQuery.performCount();
	}

	protected String getManifestSummaryKey() {
		StagedModelDataHandler<?> stagedModelDataHandler = StagedModelDataHandlerRegistryUtil.getStagedModelDataHandler(${entity.name}.class.getName());

		return stagedModelDataHandler.getManifestSummaryKey(null);
	}

	@Override
	@SuppressWarnings("unused")
	protected void performAction(Object object) throws PortalException, SystemException {
		${entity.name} stagedModel = (${entity.name})object;

		StagedModelDataHandlerUtil.exportStagedModel(_portletDataContext, stagedModel);
	}

	private PortletDataContext _portletDataContext;

}