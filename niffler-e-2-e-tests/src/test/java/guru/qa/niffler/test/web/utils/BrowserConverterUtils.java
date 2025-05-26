package guru.qa.niffler.test.web.utils;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.model.Browser;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import static guru.qa.niffler.model.Browser.*;

public class BrowserConverterUtils implements ArgumentConverter {
    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof Browser)){
            throw new ArgumentConversionException("Source must be instance of enum Browser");
        }
        return ((Browser) source).createDriver();
    }
}