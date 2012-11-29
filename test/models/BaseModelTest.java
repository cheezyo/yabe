package models;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import play.test.Helpers;

/**
 * This test takes care of boilerplate code needed when testing models, like starting a fake app context etc.
 * Inspired by http://blog.matthieuguillermin.fr/2012/03/unit-testing-tricks-for-play-2-0-and-ebean/
 * 
 * @author aksel@agresvig.com
 *
 */
public class BaseModelTest {
	public static play.test.FakeApplication app;
	
	@BeforeClass
	public static void startFakeApp() {
		app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
		Helpers.start(app);
	}
	
	@AfterClass
	public static void stopApp() {
		Helpers.stop(app);
	}
}
