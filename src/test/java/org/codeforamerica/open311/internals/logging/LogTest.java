package org.codeforamerica.open311.internals.logging;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.codeforamerica.open311.facade.APIWrapper;
import org.codeforamerica.open311.facade.APIWrapperFactory;
import org.codeforamerica.open311.facade.City;
import org.codeforamerica.open311.facade.Format;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.internals.caching.NoCache;
import org.codeforamerica.open311.internals.network.MockNetworkManager;
import org.codeforamerica.open311.internals.platform.PlatformManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogTest {
	private ByteArrayOutputStream outContent;
	private ByteArrayOutputStream errContent;
	private static APIWrapper wrapper;
	private final static String LOG_HEAD = "[open311_java]: (https://open311.sfgov.org/v2 - PRODUCTION) --> ";

	@BeforeClass
	public static void setUpWrapper() throws APIWrapperException {
		wrapper = new APIWrapperFactory(City.SAN_FRANCISCO)
				.setNetworkManager(new MockNetworkManager())
				.setCache(new NoCache()).withLogs().build();
	}

	@Before
	public void setUpStreams() {
		outContent = new ByteArrayOutputStream();
		errContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(System.out);
		System.setErr(System.err);
	}

	@Test
	public void setFormatTest() {
		wrapper.setFormat(Format.JSON);
		assertEquals(outContent.toString(), LOG_HEAD
				+ "Changing wrapper format to json\n");
		wrapper.setFormat(Format.XML);
		assertEquals(outContent.toString(), LOG_HEAD
				+ "Changing wrapper format to json\n" + LOG_HEAD
				+ "Changing wrapper format to xml\n");
	}

	@Test
	public void getServiceTest() throws APIWrapperException {
		wrapper.getServiceList();
		assertEquals(
				outContent.toString(),
				LOG_HEAD
						+ "GET Service List\n"
						+ LOG_HEAD
						+ "GET Service List is not cached, asking endpoint.\n"
						+ LOG_HEAD
						+ "HTTP GET https://open311.sfgov.org/v2/services.xml?jurisdiction_id=sfgov.org\n");
	}

	@Test
	public void getServiceWithNoLoggingTest() throws APIWrapperException {
		LogManager.getInstance().disable(wrapper);
		wrapper.getServiceList();
		assertEquals(outContent.toString(), "");
		PlatformManager.getInstance().buildCache().deleteCache();
		LogManager.getInstance().activate(wrapper);
	}

	@Test
	public void getServiceDefinitionTest() throws APIWrapperException {
		wrapper.getServiceDefinition("001");
		assertEquals(
				outContent.toString(),
				LOG_HEAD
						+ "GET Service Definition (service_code: 001)\n"
						+ LOG_HEAD
						+ "GET Service Definition (service_code: 001) is not cached, asking endpoint.\n"
						+ LOG_HEAD
						+ "HTTP GET https://open311.sfgov.org/v2/services/001.xml?jurisdiction_id=sfgov.org\n");
	}

}
