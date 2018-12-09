package io.fonimus.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
public class SwaggerController {

    @GetMapping(path = "/api/test/{path}")
    @ApiOperation(value = "Get Ex", nickname = "getEx")
    private SwaggerBean bean(@PathVariable(value = "path") String path) {
        return new SwaggerBean();
    }

    @PutMapping(path = "/api/test/{path}")
    @ApiOperation(value = "Put Ex", nickname = "putEx")
    private SwaggerBean bean(@PathVariable(value = "path") String path, @RequestBody SwaggerBean bean) {
        return new SwaggerBean();
    }
}
