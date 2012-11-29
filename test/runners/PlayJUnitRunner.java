package runners;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;
import play.test.FakeApplication;
import play.test.Helpers;

import java.util.Date;

/**
* Takes care of boilerplate code needed when testing models, like starting a fake app context etc.
* Inspired by http://ostia.be/blog/2012/10/20/play-2-test-runner/
*
*/
public class PlayJUnitRunner extends BlockJUnit4ClassRunner {

	public PlayJUnitRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }
	
	public void run(final RunNotifier notifier) {
        FakeApplication app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
        Helpers.start(app);
        super.run(notifier);
        Helpers.stop(app);
    }

    class JodaPropertyConstructor extends Constructor {
        public JodaPropertyConstructor() {
            yamlClassConstructors.put(NodeId.scalar, new TimeStampConstruct());
        }

        class TimeStampConstruct extends Constructor.ConstructScalar {
            @Override
            public Object construct(Node nnode) {
                if (nnode.getTag().equals("tag:yaml.org,2002:timestamp")) {
                    Construct dateConstructor = yamlConstructors.get(Tag.TIMESTAMP);
                    Date date = (Date) dateConstructor.construct(nnode);
                    return new DateTime(date, DateTimeZone.UTC);
                } else {
                    return super.construct(nnode);
                }
            }
        }
    }
}
