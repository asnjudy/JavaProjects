package app03.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by asnju on 2016/9/16.
 */
public class InputProductController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(InputProductController.class);

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("InputProductController called");

        return new ModelAndView("app03/product_form");
    }
}
