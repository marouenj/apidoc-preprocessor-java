package apidoc.preprocessor.scanner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

public class SpringScanner extends Scanner {

    @Override
    protected void keepControllersOnly() {
        classes = classes.stream()
                .filter(clazz -> {
                    Controller controller = clazz.getAnnotation(Controller.class);
                    RestController restController = clazz.getAnnotation(RestController.class);
                    return controller != null || restController != null;
                })
                .collect(Collectors.toSet());
    }
}
