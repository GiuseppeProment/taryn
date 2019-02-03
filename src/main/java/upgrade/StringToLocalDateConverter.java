package upgrade;

import java.time.LocalDate;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {
	public LocalDate convert(String source) {return LocalDate.parse(source);}
}