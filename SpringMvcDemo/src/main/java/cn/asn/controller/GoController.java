package cn.asn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by asnju on 2016/9/16.
 */
@Controller
public class GoController implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(GoController.class);

    @RequestMapping(value = "/", method = {RequestMethod.HEAD})
    public String head() {
        return "go";
    }


    @RequestMapping(value = {"/index", "/"}, method = {RequestMethod.GET})
    public String index(Model model) {
        logger.info("=========processed by index method=============");
        model.addAttribute("msg", "GO GO GO");
        return "go";
    }

    private Environment environment = null;

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
