<%@ page import="com.gaytech.teamcity.dropboxDeployer.common.DropboxDeployerParams" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
    Source: <strong><props:displayValue name="<%=DropboxDeployerParams.PARAM_SOURCEPATH%>" emptyValue="none"/></strong>
</div>