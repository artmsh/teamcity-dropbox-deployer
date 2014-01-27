package com.gaytech.teamcity.dropboxDeployer.server;

import com.gaytech.teamcity.dropboxDeployer.common.DropboxDeployerParams;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DropboxDeployerRunType extends RunType {
    public static final String DROPBOX_DEPLOYER_DISPLAY_NAME = "Dropbox Deployer";
    public static final String DROPBOX_DEPLOYER_DESCRIPTION = "Runner able to deploy build artifacts via Dropbox";
    public static final String DROPBOX_DEPLOYER_EDIT_PARAMS_JSP = "editDropboxDeployerParams.jsp";
    public static final String DROPBOX_DEPLOYER_VIEW_PARAMS_JSP = "viewDropboxDeployerParams.jsp";

    private PluginDescriptor descriptor;

    public DropboxDeployerRunType(@NotNull RunTypeRegistry registry, @NotNull PluginDescriptor descriptor) {
        registry.registerRunType(this);
        this.descriptor = descriptor;
    }

    @NotNull
    @Override
    public String getType() {
        return DropboxDeployerParams.DROPBOX_DEPLOYER_RUN_TYPE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return DROPBOX_DEPLOYER_DISPLAY_NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return DROPBOX_DEPLOYER_DESCRIPTION;
    }

    @Nullable
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new PropertiesProcessor() {
            public Collection<InvalidProperty> process(Map<String, String> stringStringMap) {
                return Collections.emptyList();
            }
        };
    }

    @Nullable
    @Override
    public String getEditRunnerParamsJspFilePath() {
        return descriptor.getPluginResourcesPath() + DROPBOX_DEPLOYER_EDIT_PARAMS_JSP;
    }

    @Nullable
    @Override
    public String getViewRunnerParamsJspFilePath() {
        return descriptor.getPluginResourcesPath() + DROPBOX_DEPLOYER_VIEW_PARAMS_JSP;
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return new HashMap<String, String>();
    }
}
