package com.gaytech.teamcity.dropboxDeployer.agent;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.DbxException;
import com.gaytech.teamcity.dropboxDeployer.common.DropboxDeployerParams;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProcessAdapter;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.impl.artifacts.ArtifactsCollection;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.Date;

public class DropboxBuildProcessAdapter extends BuildProcessAdapter implements Callable<BuildFinishedStatus> {
    public static final String DROPBOX_CLIENT_IDENTIFIER = "TeamcityDropboxDeployer/1.0";

    private BuildProgressLogger buildProcessLogger;
    private Logger logger = Logger.getLogger(getClass());

    private String accessToken;
    private List<ArtifactsCollection> artifacts;

    private Future<BuildFinishedStatus> completionStatus;

    public DropboxBuildProcessAdapter(@NotNull BuildRunnerContext context,
                                      @NotNull List<ArtifactsCollection> artifactsCollections) {
        this.buildProcessLogger = context.getBuild().getBuildLogger();
        this.artifacts = artifactsCollections;
        this.accessToken = context.getRunnerParameters().get(DropboxDeployerParams.PARAM_ACCESSTOKEN);
    }

    @Override
    public void start() throws RunBuildException {
        try {
            completionStatus = Executors.newSingleThreadExecutor().submit(this);
            logger.info("Build process submitted for execution");
        } catch (RejectedExecutionException e) {
            logger.error("Build process failed to start", e);
            throw new RunBuildException(e);
        }
    }

    @Override
    public boolean isFinished() {
        return completionStatus.isDone();
    }

    @Override
    public boolean isInterrupted() {
        return completionStatus.isCancelled();
    }

    @Override
    public void interrupt() {
        logger.info("Trying to cancel build process");
        completionStatus.cancel(true);
    }

    @NotNull
    @Override
    public BuildFinishedStatus waitFor() throws RunBuildException {
        try {
            BuildFinishedStatus status = completionStatus.get();
            logger.info("Build process have finished with status " + status);

            return status;
        } catch (InterruptedException e) {
            throw new RunBuildException(e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RunBuildException) {
                throw (RunBuildException) e.getCause();
            } else {
                throw new RunBuildException(e);
            }
        } catch (CancellationException e) {
            logger.info("Build process was cancelled", e);
            return BuildFinishedStatus.INTERRUPTED;
        }
    }

    @Override
    public BuildFinishedStatus call() throws Exception {
        try {
            DbxRequestConfig requestConfig = new DbxRequestConfig(DROPBOX_CLIENT_IDENTIFIER,
                    Locale.getDefault().toString());
            DbxClientV2 dbxClient = new DbxClientV2(requestConfig, accessToken);

            buildProcessLogger.message("Starting upload via Dropbox");
            int processedFiles = 0;
            for (ArtifactsCollection artifact : artifacts) {
                for (File file : artifact.getFilePathMap().keySet()) {
                    String destinationDir = "/" + artifact.getFilePathMap().get(file);
                    FileInputStream inputStream = new FileInputStream(file);

                    // todo implement FINISHED_WITH_PROBLEMS case
                    // todo implement progress

                    try (InputStream in = new FileInputStream(file))
                    {
                        FileMetadata metadata = dbxClient
                            .files()
                            .uploadBuilder(destinationDir)
                            .withMode(WriteMode.ADD)
                            .withClientModified(new Date(file.lastModified()))
                            .uploadAndFinish(in);
                    }
                    catch (UploadErrorException ex)
                    {
                        System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                        throw new RunBuildException(ex);
                    }
                    catch (DbxException ex)
                    {
                        System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                        throw new RunBuildException(ex);
                    }
                    catch (IOException ex)
                    {
                        System.err.println("Error reading from file \"" + file + "\": " + ex.getMessage());
                        throw new RunBuildException(ex);
                    }
                    processedFiles++;
                }
            }

            buildProcessLogger.message("Successfully uploaded " + processedFiles + " files to Dropbox");

            return BuildFinishedStatus.FINISHED_SUCCESS;
        }
        catch (IOException e) {
            throw new RunBuildException(e);
        }
    }
}
