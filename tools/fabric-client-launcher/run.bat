@echo off
set JPDA_OPTS=
if not ""%1"" == ""jpda"" goto noJpda
set JPDA_OPTS=-agentlib:jdwp=transport=dt_socket,address=8989,server=y,suspend=y
:noJpda

java %JPDA_OPTS% -cp lib/portal-service.jar;lib/netty-all.jar;lib/fabric-client.jar;lib com.liferay.portal.fabric.client.FabricClientLauncher