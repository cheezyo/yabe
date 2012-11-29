import com.avaje.ebean.Ebean;
import helper.YamlJodaTimeConstructor;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Loads application dummy data.
 */
public class Global extends GlobalSettings {

    public void onStart(Application app) {
        InitialData.insert(app);
    }

    static class InitialData {

        public static void insert(Application app) {
            if(Ebean.find(User.class).findRowCount() == 0) {

                InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("testdata.yaml");
                //Using Yaml instance with custom constructor here in order to support JodaTime dates with Yaml testing..
                org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml(new YamlJodaTimeConstructor());
                Map data = (Map) yaml.load(input);
                Ebean.save((Collection) (data.get("users")));
                Ebean.save((Collection) (data.get("categories")));
                Ebean.save((Collection) (data.get("tags")));

            }
        }

    }
}
