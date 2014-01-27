package com.gaytech.teamcity.dropboxDeployer.agent;

import com.gaytech.teamcity.dropboxDeployer.common.DropboxDeployerParams;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import org.jetbrains.annotations.NotNull;

public class DropboxDeployerRunnerInfo implements AgentBuildRunnerInfo {
    @NotNull
    @Override
    public String getType() {
        return DropboxDeployerParams.DROPBOX_DEPLOYER_RUN_TYPE;
    }

    @Override
    public boolean canRun(@NotNull BuildAgentConfiguration buildAgentConfiguration) {
        return true;
    }
}
