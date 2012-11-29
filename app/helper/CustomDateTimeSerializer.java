package helper;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Serializes jodatime DateTime object to custom format.
 * @author aksel@agresvig.com
 */
public class CustomDateTimeSerializer extends JsonSerializer<DateTime> {

    public static final String SERIALIZE_FORMAT = "dd-MM-yyyy";

    private static DateTimeFormatter formatter =
            DateTimeFormat.forPattern(SERIALIZE_FORMAT);

    @Override
    public void serialize(DateTime value, JsonGenerator gen,
                          SerializerProvider arg2)
            throws IOException, JsonProcessingException {

        gen.writeString(formatter.print(value));
    }
}
