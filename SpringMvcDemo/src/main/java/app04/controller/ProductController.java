package app04.controller;

import app03.domain.Product;
import app04.form.ProductForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by asnju on 2016/9/16.
 */
@Controller
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    @RequestMapping(value = "/product/input", method = RequestMethod.GET)
    public String input() {
        logger.info("input product called");
        return "app04/product_form";
    }

    @RequestMapping(value = "/product/save", method = RequestMethod.POST)
    public String save(ProductForm form, Model model) {

        logger.info("save product called");

        Product product = new Product();
        product.setName(form.getName());
        product.setDescription(form.getDescription());

        float price = 0.0f;

        try {
            price = Float.parseFloat(form.getPrice());
        } catch (NumberFormatException ex) {
            logger.error("价格转换出错，错误信息为: %s", ex.getMessage());
        }

        product.setPrice(price);

        model.addAttribute("product", product);

        return "app04/product_details";
    }

}
