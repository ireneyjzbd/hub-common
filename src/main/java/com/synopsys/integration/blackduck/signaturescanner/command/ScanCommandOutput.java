/**
 * hub-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.blackduck.signaturescanner.command;

import java.io.File;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

import com.synopsys.integration.blackduck.summary.Result;
import com.synopsys.integration.log.IntLogger;

public class ScanCommandOutput {
    public static final String DRY_RUN_RESULT_DIRECTORY = "data";
    public static final String SCAN_RESULT_DIRECTORY = "status";

    private final IntLogger logger;
    private final Result result;
    private final String errorMessage;
    private final Exception exception;
    private final ScanCommand scanCommand;
    private final Integer scanExitCode;

    public static ScanCommandOutput SUCCESS(final IntLogger logger, final ScanCommand scanCommand) {
        return new ScanCommandOutput(logger, scanCommand, Result.SUCCESS, null, null, 0);
    }

    public static ScanCommandOutput FAILURE(final IntLogger logger, final ScanCommand scanCommand, final String errorMessage, final Exception exception) {
        return new ScanCommandOutput(logger, scanCommand, Result.FAILURE, errorMessage, exception, null);
    }

    public static ScanCommandOutput FAILURE(final IntLogger logger, final ScanCommand scanCommand, final int scanExitCode) {
        final String errorMessage = String.format("The scan failed with return code: %d", scanExitCode);
        return new ScanCommandOutput(logger, scanCommand, Result.FAILURE, errorMessage, null, Integer.valueOf(scanExitCode));
    }

    private ScanCommandOutput(final IntLogger logger, final ScanCommand scanCommand, final Result result, final String errorMessage, final Exception exception, final Integer scanExitCode) {
        this.logger = logger;
        this.result = result;
        this.errorMessage = errorMessage;
        this.exception = exception;
        this.scanCommand = scanCommand;
        this.scanExitCode = scanExitCode;
    }

    private Optional<File> getResultFile(final String resultDirectoryName) {
        final File resultDirectory = new File(scanCommand.getOutputDirectory(), resultDirectoryName);
        if (null != resultDirectory && resultDirectory.exists()) {
            final File[] resultFiles = resultDirectory.listFiles((dir, name) -> FilenameUtils.wildcardMatchOnSystem(name, "*.json"));
            if (null != resultFiles && resultFiles.length == 1) {
                return Optional.of(resultFiles[0]);
            }
        }
        logger.error(String.format("Exactly 1 result file was not found in the result directory: %s", resultDirectory.getAbsolutePath()));
        return Optional.empty();
    }

    public boolean wasDryRun() {
        return scanCommand.isDryRun();
    }

    public Optional<File> getScanSummaryFile() {
        return getResultFile(SCAN_RESULT_DIRECTORY);
    }

    public Optional<File> getDryRunFile() {
        return getResultFile(DRY_RUN_RESULT_DIRECTORY);
    }

    public File getSpecificRunOutputDirectory() {
        return scanCommand.getOutputDirectory();
    }

    public Result getResult() {
        return result;
    }

    public String getScanTarget() {
        return scanCommand.getTargetPath();
    }

    public Optional<String> getErrorMessage() {
        return Optional.ofNullable(errorMessage);
    }

    public Optional<Exception> getException() {
        return Optional.ofNullable(exception);
    }

    public Optional<Integer> getScanExitCode() {
        return Optional.ofNullable(scanExitCode);
    }

}
