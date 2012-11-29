package helper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.Date;

/**
 * This file makes JodaTime attributes in Model usable with Yaml testdata.
 *
 * Requires use of SnakeYaml
 *
 * This class should be used if JodaTime may appear with a tag or as a
 * JavaBean property
 *
 * Taken from SnakeYaml resources:
 * http://code.google.com/p/snakeyaml/wiki/howto#How_to_parse_JodaTime
 *
 */
public class YamlJodaTimeConstructor extends Constructor {

    public YamlJodaTimeConstructor() {
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
