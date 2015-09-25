package currency_rate.converters;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lav on 25.09.15.
 */
public class StringToLocalDate implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        HashSet<ConvertiblePair> convertiblePairs = new HashSet<ConvertiblePair>();
        convertiblePairs.add(new ConvertiblePair(String.class, LocalDate.class));
        return convertiblePairs;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (targetType.getObjectType().equals(LocalDate.class))
            return convertLocalDate((String) source);
        else
            throw new RuntimeException("can't convert " + source + " to " + targetType.getObjectType().getSimpleName());
    }

    private LocalDate convertLocalDate(String source) {
        DateTimeFormatter formatter;

        if (source.contains(".")) {
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

        return LocalDate.parse(source, formatter);
    }
}
