package apidoc.preprocessor.scanner;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScannerTest {

    private static Method IS_PACKAGE;
    private static Method JAR_PATH;

    @BeforeClass
    public void init() throws NoSuchMethodException {
        IS_PACKAGE = Scanner.class.getDeclaredMethod("isPackage", String.class);
        IS_PACKAGE.setAccessible(true);

        JAR_PATH = Scanner.class.getDeclaredMethod("jarPath", String.class);
        JAR_PATH.setAccessible(true);
    }

    @DataProvider(name = "isPackage")
    public Object[][] isPackage() {
        return new Object[][]{
                {"marouenj", true},
                {"marouenj.apidoc", true},
                {"marouenj.apidoc.plugin", true},

                {".", false},
                {"marouenj.", false},
                {"marouenj.apidoc.", false},
        };
    }

    @Test(dataProvider = "isPackage")
    public void isPackage(String expression, Boolean expected) throws InvocationTargetException, IllegalAccessException {
        Assert.assertEquals(IS_PACKAGE.invoke(null, expression), expected);
    }

    @DataProvider(name = "jarPath")
    public Object[][] jarPath() {
        return new Object[][]{
                {"file:a!b", "a"},
        };
    }

    @Test(dataProvider = "jarPath")
    public void jarPath(String resourcePath, String expected) throws InvocationTargetException, IllegalAccessException {
        String pathToJar = (String) JAR_PATH.invoke(null, resourcePath);
        Assert.assertEquals(pathToJar, expected);
    }
}
