package apidoc.preprocessor.scanner.sample_a.sample2.sample3;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"controller5/path1", "controller5/path2"})
public class Controller5 {

    @RequestMapping(method = RequestMethod.GET, value = "method1")
    public void method1() {
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "method2")
    public void method2() {
    }
}
