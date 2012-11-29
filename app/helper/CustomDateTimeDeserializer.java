package helper;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: aksel
 * Date: 11/6/12
 * Time: 11:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomDateTimeDeserializer extends JsonDeserializer<DateTime> {

    public static final String DESERIALIZE_FORMAT = "dd-MM-yyyy";

    @Override
    public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(DESERIALIZE_FORMAT);
        String date = jsonParser.getText();
        return DateTime.parse(date, formatter);
    }
}
