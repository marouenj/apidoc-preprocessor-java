package apidoc.preprocessor.scanner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    protected void controller(Class<?> controllerClass, String[] prefix) {
    }
}
