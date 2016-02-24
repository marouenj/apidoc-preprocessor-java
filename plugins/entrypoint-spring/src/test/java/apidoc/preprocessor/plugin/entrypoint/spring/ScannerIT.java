package apidoc.preprocessor.plugin.entrypoint.spring;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ScannerIT {

    @DataProvider(name = "classes")
    public Object[][] classes() {
        return new Object[][]{
                {
                        new String[]{"apidoc.preprocessor.plugin.entrypoint.spring.sample"},
                        new TreeSet<String>() {{
                            add("class apidoc.preprocessor.plugin.entrypoint.spring.sample.ControllerA");
                            add("class apidoc.preprocessor.plugin.entrypoint.spring.sample.ControllerB");
                            add("class apidoc.preprocessor.plugin.entrypoint.spring.sample.ControllerC");
                        }}
                },
        };
    }

    @Test(dataProvider = "classes")
    public void classes(String[] basePackages, Set<String> expected) throws IOException {
        Set<Class<?>> classes = Scanner.classes(basePackages);

        Assert.assertNotNull(classes);
        Assert.assertEquals(classes.size(), expected.size());

        Iterator<String> itr = expected.iterator();
        for (Class<?> clazz : classes) {
            Assert.assertEquals(clazz.toString(), itr.next());
        }
    }
}
