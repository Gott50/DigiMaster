package de.tmgdx.em;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

public class AssetsTest {
	public final int LOG_LEVEL = Application.LOG_DEBUG;
	private static final AssetManager ASSET_MANAGER = new AssetManager();
	private Assets assets = Assets.instance;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
//		Gdx.app.setLogLevel(LOG_LEVEL);
//		assets.init(ASSET_MANAGER);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testDispose() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testError() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testInit() {
		fail("Not yet implemented"); // TODO
	}

}
