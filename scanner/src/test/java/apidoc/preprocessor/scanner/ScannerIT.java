package apidoc.preprocessor.scanner;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ScannerIT {

    private static Method classes;

    @BeforeClass
    public void init() throws NoSuchMethodException {
        classes = Scanner.class.getDeclaredMethod("classes", String[].class);
        classes.setAccessible(true);
    }

    @DataProvider(name = "classes")
    public Object[][] classes() {
        return new Object[][]{
                {
                        new String[]{"apidoc.preprocessor.scanner.sample_a"},
                        new TreeSet<String>() {{
                            add("class apidoc.preprocessor.scanner.sample_a.Controller1");
                            add("class apidoc.preprocessor.scanner.sample_a.Controller2");
                            add("class apidoc.preprocessor.scanner.sample_a.Controller3");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.Controller4");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.sample3.Controller5");
                        }}
                },
                {
                        new String[]{
                                "apidoc.preprocessor.scanner.sample_a",
                                "apidoc.preprocessor.scanner.sample_a.sample2",
                        },
                        new TreeSet<String>() {{
                            add("class apidoc.preprocessor.scanner.sample_a.Controller1");
                            add("class apidoc.preprocessor.scanner.sample_a.Controller2");
                            add("class apidoc.preprocessor.scanner.sample_a.Controller3");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.Controller4");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.sample3.Controller5");
                        }}
                },
                {
                        new String[]{
                                "apidoc.preprocessor.scanner.sample_a",
                                "apidoc.preprocessor.scanner.sample_a.sample2",
                                "apidoc.preprocessor.scanner.sample_a.sample2.sample3",
                        },
                        new TreeSet<String>() {{
                            add("class apidoc.preprocessor.scanner.sample_a.Controller1");
                            add("class apidoc.preprocessor.scanner.sample_a.Controller2");
                            add("class apidoc.preprocessor.scanner.sample_a.Controller3");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.Controller4");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.sample3.Controller5");
                        }}
                },
                {
                        new String[]{
                                "apidoc.preprocessor.scanner.sample_a",
                                "apidoc.preprocessor.scanner.sample_a.sample2.sample3",
                        },
                        new TreeSet<String>() {{
                            add("class apidoc.preprocessor.scanner.sample_a.Controller1");
                            add("class apidoc.preprocessor.scanner.sample_a.Controller2");
                            add("class apidoc.preprocessor.scanner.sample_a.Controller3");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.Controller4");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.sample3.Controller5");
                        }}
                },
                {
                        new String[]{
                                "apidoc.preprocessor.scanner.sample_a.sample2",
                                "apidoc.preprocessor.scanner.sample_a.sample2.sample3",
                        },
                        new TreeSet<String>() {{
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.Controller4");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.sample3.Controller5");
                        }}
                },
                {
                        new String[]{
                                "apidoc.preprocessor.scanner.sample_b",
                                "apidoc.preprocessor.scanner.sample_a.sample2",
                        },
                        new TreeSet<String>() {{
                            add("class apidoc.preprocessor.scanner.sample_b.Controller6");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.Controller4");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.sample3.Controller5");
                        }}
                },
        };
    }

    @Test(dataProvider = "classes")
    public void classes(String[] basePackages, Set<String> expected) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SpringScanner scanner = new SpringScanner();

        classes.invoke(scanner, (Object) basePackages);
        Set<Class<?>> classes = scanner.classes;

        Assert.assertNotNull(classes);
        Assert.assertEquals(classes.size(), expected.size());

        Iterator<String> itr = expected.iterator();
        for (Class<?> clazz : classes) {
            Assert.assertEquals(clazz.toString(), itr.next());
        }
    }
}
