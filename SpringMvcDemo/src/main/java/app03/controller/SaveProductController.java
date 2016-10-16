package app03.controller;

import app03.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/**
 * Created by asnju on 2016/9/16.
 */
public class SaveProductController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(SaveProductController.class);


    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("SaveProductController called");

        Product product = new Product();
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));

        float price = 0.0f;

        try {
            price = Float.parseFloat(request.getParameter("price"));
        } catch (NumberFormatException ex) {
            logger.error("价格转换出错，错误信息为: %s", ex.getMessage());
        }

        product.setPrice(price);


        return new ModelAndView("app03/product_details", "product", product);
    }
}
