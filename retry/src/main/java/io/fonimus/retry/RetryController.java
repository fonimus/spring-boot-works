package io.fonimus.retry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryController {

    private ServiceByAnno serviceByAnno;

    private ServiceByConstr serviceByConstr;

    public RetryController(ServiceByAnno serviceByAnno, ServiceByConstr serviceByConstr) {
        this.serviceByAnno = serviceByAnno;
        this.serviceByConstr = serviceByConstr;
    }

    @GetMapping(path = "/anno")
    public String serviceByAnno() throws Exception {
        return serviceByAnno.springReTryTest();
    }

    @GetMapping(path = "/constr")
    public String serviceByConstr() throws Exception {
        return serviceByConstr.springReTryTest();
    }

}
