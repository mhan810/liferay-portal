package com.liferay.portal.ac.profile.service.configuration.configurator;

import com.liferay.portal.service.configuration.ServiceComponentConfiguration;
import com.liferay.portal.service.configuration.configurator.ServiceConfigurator;
import com.liferay.portal.spring.extender.loader.ModuleResourceLoader;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
@Component(
	immediate = true, service = ServiceACProfileConfigurator.class
)
public class ServiceACProfileConfigurator {

	@Reference(unbind = "-")
	public void setServiceConfigurator(
		ServiceConfigurator serviceConfigurator) {

		_serviceConfigurator = serviceConfigurator;
	}

	@Activate
	protected void activate() throws Exception {
		_serviceConfigurator.initServices(getConfiguration(), getClassLoader());
	}

	@Deactivate
	protected void deactivate() throws Exception {
		_serviceConfigurator.destroyServices(
			getConfiguration(), getClassLoader());
	}

	protected ClassLoader getClassLoader() {
		Class<? extends ServiceACProfileConfigurator> clazz = getClass();

		return clazz.getClassLoader();
	}

	protected ServiceComponentConfiguration getConfiguration() {
		Bundle bundle = FrameworkUtil.getBundle(getClass());

		return new ModuleResourceLoader(bundle);
	}

	private ServiceConfigurator _serviceConfigurator;

}