package ru.clevertec.ecl.util.serialization.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import ru.clevertec.ecl.util.constant.AppConstants;

public class LocalDateTimeJsonSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime dateTime, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {

        gen.writeString(
            dateTime.format(AppConstants.DATE_TIME_FORMATTER));
    }
}
