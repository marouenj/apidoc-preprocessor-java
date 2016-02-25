package apidoc.preprocessor.scanner;

import apidoc.preprocessor.model.Endpoint;
import org.springframework.web.bind.annotation.RequestMethod;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.TreeSet;

public class SpringScannerIT {

    private static Method classes;
    private static Method controllers;
    private static Method prefix;
    private static Method methods;
    private static Method endpoints;
    private static Method entryPoint;

    @BeforeClass
    public void init() throws NoSuchMethodException {
        classes = Scanner.class.getDeclaredMethod("classes", String[].class);
        classes.setAccessible(true);

        controllers = SpringScanner.class.getDeclaredMethod("controllers");
        controllers.setAccessible(true);

        prefix = SpringScanner.class.getDeclaredMethod("prefix", Class.class);
        prefix.setAccessible(true);

        methods = Scanner.class.getDeclaredMethod("methods", Class.class);
        methods.setAccessible(true);

        endpoints = SpringScanner.class.getDeclaredMethod("endpoints", Set.class);
        endpoints.setAccessible(true);

        entryPoint = Scanner.class.getDeclaredMethod("endpoints", String[].class);
        entryPoint.setAccessible(true);
    }

    @DataProvider(name = "controllers")
    public Object[][] controllers() {
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
                                "apidoc.preprocessor.scanner.sample_a.sample2",
                        },
                        new TreeSet<String>() {{
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.Controller4");
                            add("class apidoc.preprocessor.scanner.sample_a.sample2.sample3.Controller5");
                        }}
                },
                {
                        new String[]{
                                "apidoc.preprocessor.scanner.sample_b",
                        },
                        new TreeSet<String>() {{
                            add("class apidoc.preprocessor.scanner.sample_b.Controller6");
                        }}
                },
        };
    }

    @Test(dataProvider = "controllers")
    public void controllers(String[] basePackages, Set<String> expected) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SpringScanner scanner = new SpringScanner();

        classes.invoke(scanner, (Object) basePackages);
        controllers.invoke(scanner);
        Set<Class<?>> classes = scanner.classes;

        Assert.assertNotNull(classes);
        Assert.assertEquals(classes.size(), expected.size());

        for (Class<?> clazz : classes) {
            Assert.assertTrue(expected.contains(clazz.toString()));
        }
    }

    @DataProvider(name = "prefix")
    public Object[][] prefix() {
        return new Object[][]{
                {
                        new String[]{"apidoc.preprocessor.scanner.sample_b"},
                        new Object[]{
                                new String[]{},
                        }
                },
                {
                        new String[]{"apidoc.preprocessor.scanner.sample_a.sample2"},
                        new Object[]{
                                new String[]{"controller4/path1"},
                                new String[]{"controller5/path1", "controller5/path2"},
                        }
                },
        };
    }

    @Test(dataProvider = "prefix")
    public void prefix(String[] basePackages, Object[] expected) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SpringScanner scanner = new SpringScanner();

        classes.invoke(scanner, (Object) basePackages);
        controllers.invoke(scanner);
        Set<Class<?>> classes = scanner.classes;
        int i = -1;
        for (Class<?> clazz : classes) {
            String[] actual = (String[]) prefix.invoke(scanner, clazz);
            Assert.assertNotNull(actual);
            String[] expect = (String[]) expected[++i];
            Assert.assertEquals(actual.length, expect.length);
            for (int j = 0; j < actual.length; j++) {
                Assert.assertEquals(actual[j], expect[j]);
            }
        }
    }

    @DataProvider(name = "endpoints")
    public Object[][] endpoints() {
        return new Object[][]{
                {
                        new String[]{"apidoc.preprocessor.scanner.sample_a.sample2"},
                        new Object[]{
                                new TreeSet<String>() {{
                                    add("method1");
                                }},
                                new TreeSet<String>() {{
                                    add("method1");
                                    add("method2");
                                }},
                        }
                },
        };
    }

    @Test(dataProvider = "endpoints")
    public void endpoints(String[] basePackages, Object[] expected) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SpringScanner scanner = new SpringScanner();

        classes.invoke(scanner, (Object) basePackages);
        controllers.invoke(scanner);
        Set<Class<?>> classes = scanner.classes;
        int i = -1;
        for (Class<?> clazz : classes) {
            Set<Method> actual = (Set<Method>) methods.invoke(scanner, clazz);
            actual = (Set<Method>) endpoints.invoke(scanner, actual);
            Assert.assertNotNull(actual);
            Set<String> expect = (Set<String>) expected[++i];
            Assert.assertEquals(actual.size(), expect.size());
            for (Method method : actual) {
                Assert.assertTrue(expect.contains(method.getName()));
            }
        }
    }

    @DataProvider(name = "entrypoint")
    public Object[][] entrypoint() {
        return new Object[][]{
                {
                        new String[]{"apidoc.preprocessor.scanner.sample_a.sample2"},
                        new TreeSet<Endpoint>() {{
                            add(new Endpoint(
                                    new RequestMethod[]{RequestMethod.GET},
                                    new String[]{"controller4/path1"},
                                    new String[]{"method1"})
                            );
                            add(new Endpoint(
                                    new RequestMethod[]{RequestMethod.GET},
                                    new String[]{"controller5/path1", "controller5/path2"},
                                    new String[]{"method1"})
                            );
                            add(new Endpoint(
                                    new RequestMethod[]{RequestMethod.GET, RequestMethod.POST},
                                    new String[]{"controller5/path1", "controller5/path2"},
                                    new String[]{"method2"})
                            );
                        }}
                },
        };
    }

    @Test(dataProvider = "entrypoint")
    public void entrypoint(String[] basePackages, TreeSet<Endpoint> expected) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SpringScanner scanner = new SpringScanner();

        TreeSet<Endpoint> actual = (TreeSet<Endpoint>) entryPoint.invoke(scanner, (Object) basePackages);
        Assert.assertEquals(actual.size(), expected.size());
        Assert.assertEquals(actual, expected);
    }
}
