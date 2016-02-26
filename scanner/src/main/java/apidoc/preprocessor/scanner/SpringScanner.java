package apidoc.preprocessor.scanner;

import apidoc.preprocessor.model.SpringEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

public class SpringScanner extends Scanner {

    @Override
    protected void controllers() {
        classes = classes.stream()
                .filter(clazz -> {
                    Controller controller = clazz.getAnnotation(Controller.class);
                    RestController restController = clazz.getAnnotation(RestController.class);
                    return controller != null || restController != null;
                })
                .collect(Collectors.toSet());
    }

    @Override
    protected String[] prefix(Class<?> controllerClass) {
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return new String[]{};
        }
        return requestMapping.value();
    }

    @Override
    protected Set<Method> endpoints(Set<Method> methods) {
        return methods.stream()
                .filter(method -> {
                    RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                    return requestMapping != null;
                })
                .collect(Collectors.toSet());
    }

    @Override
    protected void endpoint(Method method, String[] prefix) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

        String[] suffix = requestMapping.value();
        if (suffix.length == 0) {
            suffix = requestMapping.path();
        }

        ENDPOINTS.add(new SpringEndpoint(requestMapping.method(), prefix, suffix));
    }
}
