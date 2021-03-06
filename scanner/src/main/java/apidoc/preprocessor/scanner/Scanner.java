package apidoc.preprocessor.scanner;

import apidoc.preprocessor.model.Endpoint;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class Scanner {

    protected Set<Class<?>> classes = new TreeSet<>((a, b) -> a.toString().compareTo(b.toString()));
    protected final Set<Endpoint> ENDPOINTS = new TreeSet<>(); // TODO add comparator

    private final static String DOT_CLASS = ".class";

    private final static ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

    private final static Pattern PACKAGE_PATTERN = Pattern.compile("^([^.]+\\.)*[^.]+$");
    private final static Pattern RESOURCE_PATTERN = Pattern.compile("^file:(.*)!(.*)$");

    public Set<Endpoint> endpoints(String[] basePackages) {
        classes(basePackages);
        controllers();
        for (Class<?> clazz : classes) {
            String[] prefix = prefix(clazz);
            controller(clazz, prefix);
        }

        return ENDPOINTS;
    }

    private void classes(String[] basePackages) {
        for (String basePackage : basePackages) {
            Collection<Class<?>> classesPerPackage = classesPerPackage(basePackage);
            if (classesPerPackage == null) {
                throw new NullPointerException();
            }

            classes.addAll(classesPerPackage);
        }
    }

    private static Collection<Class<?>> classesPerPackage(String basePackage) {
        if (!isPackage(basePackage)) {
            throw new RuntimeException();
        }

        String basePackagePath = basePackage.replace(".", "/");

        URL resource = CLASS_LOADER.getResource(basePackagePath);
        if (resource == null) {
            throw new NullPointerException();
        }

        Enumeration<JarEntry> jarEntries = jarEntries(resource.getFile());

        return Collections.list(jarEntries).stream()
                .filter(jarEntry -> jarEntry.getName().startsWith(basePackagePath))
                .filter(jarEntry -> jarEntry.getName().endsWith(DOT_CLASS))
                .filter(jarEntry -> !jarEntry.isDirectory())
                .map(jarEntry -> {
                    try {
                        int to = jarEntry.getName().length() - DOT_CLASS.length();
                        String clazz = jarEntry.getName()
                                .substring(0, to)
                                .replace("/", ".");
                        return CLASS_LOADER.loadClass(clazz); // using Class.forName(clazz) instead is wrong! It won't load dependencies...
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException();
                    }
                })
                .collect(Collectors.toList());
    }

    private static Enumeration<JarEntry> jarEntries(String resourcePath) {
        String jarPath = jarPath(resourcePath);
        if (jarPath == null) {
            throw new NullPointerException();
        }

        JarFile jarFile;
        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        return jarFile.entries();
    }

    private static boolean isPackage(String pack) {
        return PACKAGE_PATTERN.matcher(pack).find();
    }

    private static String jarPath(String path) {
        Matcher matcher = RESOURCE_PATTERN.matcher(path);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    protected abstract void controllers();

    protected abstract String[] prefix(Class<?> controllerClass);

    private void controller(Class<?> controllerClass, String[] prefix) {
        Set<Method> methods = methods(controllerClass);
        methods = endpoints(methods);
        for (Method method : methods) {
            endpoint(method, prefix);
        }
    }

    private Set<Method> methods(Class<?> controllerClass) {
        Set<Method> methods = new TreeSet<>((a, b) -> {
            return a.getName().compareTo(b.getName());
        });

        Collections.addAll(methods, controllerClass.getMethods());
        Collections.addAll(methods, controllerClass.getDeclaredMethods());

        return methods;
    }

    protected abstract Set<Method> endpoints(Set<Method> methods);

    protected abstract void endpoint(Method method, String[] prefix);
}
