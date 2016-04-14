package de.tmtomcat.em;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EMServerTest {
	private EMServer server;
	private String serverResponse;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		server = new EMServer(){
			private void  setResponse(HttpServletResponse response, String string)
					throws IOException {
				serverResponse = string;
			}
			
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testDoGetHttpServletRequestHttpServletResponse() throws ServletException, IOException {
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		server.doGet(request, response);
		Assert.assertEquals("File: ", this.serverResponse);
	}

	@Test
	public final void testDoPostHttpServletRequestHttpServletResponse() {
		fail("Not yet implemented"); // TODO
	}

}
