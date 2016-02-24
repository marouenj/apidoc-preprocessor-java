package apidoc.preprocessor.plugin.entrypoint.spring;

import org.testng.annotations.Test;

import java.io.IOException;

public class ScannerIT {

    @Test
    public void classes() throws IOException {
        Scanner.classes("apidoc.preprocessor.plugin.entrypoint.spring.sample");
    }
}
