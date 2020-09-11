package zh.bookreader.api.util;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@ResponseBody
@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping(produces = "application/json;charset=UTF-8")
public @interface ApiController {
}
