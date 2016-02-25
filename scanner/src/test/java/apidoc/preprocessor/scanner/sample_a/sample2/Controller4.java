package apidoc.preprocessor.scanner.sample_a.sample2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "controller4/path1")
public class Controller4 {

    @RequestMapping(method = RequestMethod.GET, path = "method1")
    public void method1() {
    }

    public void method2() {
    }
}
