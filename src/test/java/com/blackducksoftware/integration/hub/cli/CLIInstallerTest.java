/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
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
 *******************************************************************************/
package com.blackducksoftware.integration.hub.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.blackducksoftware.integration.hub.util.TestLogger;

public class CLIInstallerTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testCustomInstall_2_4_2() throws Exception {
		final File directoryToInstallTo = folder.newFolder();
		final CLILocation cliLocation = new CLILocation(directoryToInstallTo);

		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final URL cliZip = classLoader.getResource("scan.cli-2.4.2.zip");
		final TestLogger logger = new TestLogger();

		final CLIInstaller installer = new CLIInstaller(cliLocation);
		installer.customInstall(cliZip, "2.4.2", "localHost", logger);

		assertNotNull(cliLocation.getCLIHome());
		assertNull(cliLocation.getProvidedJavaExec());
		assertTrue(cliLocation.getCLIExists(logger));
		assertNotNull(cliLocation.getCLI(logger));
		assertNotNull(cliLocation.getOneJarFile());

		assertTrue(logger.getErrorList().isEmpty());
		assertTrue(logger.getOutputString(), logger.getOutputString().contains("Unpacking file:"));
	}

	@Test
	public void testCustomInstall_2_4_2_To_3_1_0() throws Exception {
		final File directoryToInstallTo = folder.newFolder();
		final CLILocation cliLocation = new CLILocation(directoryToInstallTo);

		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL cliZip = classLoader.getResource("scan.cli-2.4.2.zip");
		TestLogger logger = new TestLogger();

		final CLIInstaller installer = new CLIInstaller(cliLocation);
		installer.customInstall(cliZip, "2.4.2", "localHost", logger);

		assertNotNull(cliLocation.getCLIHome());
		assertNull(cliLocation.getProvidedJavaExec());
		assertTrue(cliLocation.getCLIExists(logger));
		assertNotNull(cliLocation.getCLI(logger));
		assertNotNull(cliLocation.getOneJarFile());

		assertTrue(logger.getErrorList().isEmpty());
		assertTrue(logger.getOutputString(), logger.getOutputString().contains("Unpacking file:"));

		cliZip = classLoader.getResource("scan.cli-3.1.0.zip");
		logger = new TestLogger();

		installer.customInstall(cliZip, "3.1.0", "localHost", logger);

		assertNotNull(cliLocation.getCLIHome());
		assertNull(cliLocation.getProvidedJavaExec());
		assertTrue(cliLocation.getCLIExists(logger));
		assertNotNull(cliLocation.getCLI(logger));
		assertNotNull(cliLocation.getOneJarFile());

		assertTrue(logger.getErrorList().isEmpty());
		assertTrue(logger.getOutputString(), logger.getOutputString().contains("Unpacking file:"));
	}

	@Test
	public void testCustomInstall_3_1_0() throws Exception {
		final File directoryToInstallTo = folder.newFolder();
		final CLILocation cliLocation = new CLILocation(directoryToInstallTo);

		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final URL cliZip = classLoader.getResource("scan.cli-3.1.0.zip");
		final TestLogger logger = new TestLogger();

		final CLIInstaller installer = new CLIInstaller(cliLocation);
		installer.customInstall(cliZip, "3.1.0", "localHost", logger);

		assertNotNull(cliLocation.getCLIHome());
		assertTrue(cliLocation.getCLIExists(logger));
		assertNotNull(cliLocation.getCLI(logger));
		assertNotNull(cliLocation.getOneJarFile());

		assertTrue(logger.getErrorList().isEmpty());
		assertTrue(logger.getOutputString(), logger.getOutputString().contains("Unpacking file:"));
	}

	@Test
	public void testCustomInstall_3_1_0_Modified() throws Exception {
		final File directoryToInstallTo = folder.newFolder();
		final CLILocation cliLocation = new CLILocation(directoryToInstallTo);

		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final URL cliZip = classLoader.getResource("scan.cli-3.1.0.zip");
		TestLogger logger = new TestLogger();

		final CLIInstaller installer = new CLIInstaller(cliLocation);
		installer.customInstall(cliZip, "3.1.0", "localHost", logger);

		assertNotNull(cliLocation.getCLIHome());
		assertTrue(cliLocation.getCLIExists(logger));
		assertNotNull(cliLocation.getCLI(logger));
		assertNotNull(cliLocation.getOneJarFile());

		assertTrue(logger.getErrorList().isEmpty());
		assertTrue(logger.getOutputString(), logger.getOutputString().contains("Unpacking file:"));

		final File hubVersionFile = new File(directoryToInstallTo, CLILocation.VERSION_FILE_NAME);
		hubVersionFile.setLastModified(0L);

		logger = new TestLogger();

		installer.customInstall(cliZip, "3.1.0", "localHost", logger);

		assertNotNull(cliLocation.getCLIHome());
		assertNull(cliLocation.getProvidedJavaExec());
		assertTrue(cliLocation.getCLIExists(logger));
		assertNotNull(cliLocation.getCLI(logger));
		assertNotNull(cliLocation.getOneJarFile());

		assertTrue(logger.getErrorList().isEmpty());
		assertTrue(logger.getOutputString(), logger.getOutputString().contains("Unpacking file:"));
	}

	@Test
	public void testCustomInstall_3_1_0_NotModified() throws Exception {
		final File directoryToInstallTo = folder.newFolder();
		final CLILocation cliLocation = new CLILocation(directoryToInstallTo);

		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final URL cliZip = classLoader.getResource("scan.cli-3.1.0.zip");
		TestLogger logger = new TestLogger();

		final CLIInstaller installer = new CLIInstaller(cliLocation);
		installer.customInstall(cliZip, "3.1.0", "localHost", logger);

		assertNotNull(cliLocation.getCLIHome());
		assertTrue(cliLocation.getCLIExists(logger));
		assertNotNull(cliLocation.getCLI(logger));
		assertNotNull(cliLocation.getOneJarFile());

		assertTrue(logger.getErrorList().isEmpty());
		assertTrue(logger.getOutputString(), logger.getOutputString().contains("Unpacking file:"));

		logger = new TestLogger();

		installer.customInstall(cliZip, "3.1.0", "localHost", logger);

		assertNotNull(cliLocation.getCLIHome());
		assertNull(cliLocation.getProvidedJavaExec());
		assertTrue(cliLocation.getCLIExists(logger));
		assertNotNull(cliLocation.getCLI(logger));
		assertNotNull(cliLocation.getOneJarFile());

		assertTrue(logger.getErrorList().isEmpty());
		assertTrue(logger.getOutputString(), !logger.getOutputString().contains("Unpacking file:"));
	}

}
