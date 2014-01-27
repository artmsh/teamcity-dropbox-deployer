package com.gaytech.teamcity.dropboxDeployer.agent;

import com.gaytech.teamcity.dropboxDeployer.common.DropboxDeployerParams;
import jetbrains.buildServer.ExtensionHolder;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.agent.impl.artifacts.ArtifactsBuilder;
import jetbrains.buildServer.agent.impl.artifacts.ArtifactsBuilderAdapter;
import jetbrains.buildServer.agent.impl.artifacts.ArtifactsCollection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class DropboxDeployerRunner implements AgentBuildRunner {
    private ExtensionHolder extensionHolder;

    public DropboxDeployerRunner(@NotNull ExtensionHolder extensionHolder) {
        this.extensionHolder = extensionHolder;
    }

    @NotNull
    public BuildProcess createBuildProcess(@NotNull AgentRunningBuild agentRunningBuild,
                                           @NotNull BuildRunnerContext buildRunnerContext) throws RunBuildException {
        String sourcePath = buildRunnerContext.getRunnerParameters().get(DropboxDeployerParams.PARAM_SOURCEPATH);

        Collection<ArtifactsPreprocessor> preprocessors = extensionHolder.getExtensions(ArtifactsPreprocessor.class);
        ArtifactsBuilder builder = new ArtifactsBuilder();
        builder.setPreprocessors(preprocessors);
        builder.setBaseDir(agentRunningBuild.getCheckoutDirectory());
        builder.setArtifactsPaths(sourcePath);
        builder.addListener(new ArtifactsBuilderAdapter());

        List<ArtifactsCollection> artifactsCollections = builder.build();

        return new DropboxBuildProcessAdapter(buildRunnerContext, artifactsCollections);
    }

    @NotNull
    @Override
    public AgentBuildRunnerInfo getRunnerInfo() {
        return new DropboxDeployerRunnerInfo();
    }
}
