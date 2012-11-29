import org.junit.Test;
import org.springframework.util.Assert;
import play.db.DB;

import javax.sql.DataSource;
import java.sql.Connection;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

/**
 * Tests that the database is up and running
 *
 * Inspired by http://digitalsanctum.com/2012/05/30/play-framework-2-tutorial-database-access/
 *
 * @author aksel@agresvig.com
 */
public class DataSourceTest {

    @Test
    public void testGetDataSource() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                DataSource ds = DB.getDataSource();
                Assert.notNull(ds, "DataSource was null!");
            }
        });
    }

    @Test
    public void testGetConnection() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Connection conn = DB.getConnection();
                Assert.notNull(conn, "Connection was null!");
            }
        });
    }

}
