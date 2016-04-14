/**
 * 
 */
package de.tmgdx.em;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Application;

public class MainTest {
	private final PlatformSpecificCode PS_CODE = null;
	private final int LOGLEVEL = Application.LOG_DEBUG;
	
	private Main main;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		main = new Main(PS_CODE){
			public final int LOG_LEVEL = LOGLEVEL;
		};
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link de.tmgdx.em.Main#Main(de.tmgdx.em.PlatformSpecificCode)}.
	 */
	@Test
	public final void testMain() {
		Assert.assertEquals(PS_CODE, main.getPlatSpeCode());
	}

}
