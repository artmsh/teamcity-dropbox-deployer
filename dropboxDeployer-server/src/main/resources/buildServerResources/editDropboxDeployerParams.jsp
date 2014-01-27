<%@ page import="com.gaytech.teamcity.dropboxDeployer.common.DropboxDeployerParams" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<l:settingsGroup title="Deployment Credentials">
    <tr>
        <th><label for="jetbrains.buildServer.dropbox.accessToken">Access token: <l:star/></label></th>
        <td><props:textProperty name="<%=DropboxDeployerParams.PARAM_ACCESSTOKEN%>"  className="longField" maxlength="256"/>
            <span class="smallNote">Enter access token</span><span class="error" id="error_jetbrains.buildServer.dropbox.accessToken"></span>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Deployment Source">
    <tr>
        <th><label for="jetbrains.buildServer.dropbox.sourcePath">Artifacts path: </label></th>
        <td>
            <props:multilineProperty name="<%=DropboxDeployerParams.PARAM_SOURCEPATH%>" className="longField" cols="30" rows="4" expanded="true" linkTitle="Enter artifacts paths"/>
            <span class="smallNote">New line or comma separated paths to build artifacts. Ant-style wildcards like dir/**/*.zip and target directories like *.zip => winFiles,unix/distro.tgz => linuxFiles, where winFiles and linuxFiles are target directories are supported.
            <bs:help file="Configuring+General+Settings" anchor="artifactPaths"/></span>
        </td>
    </tr>
</l:settingsGroup>