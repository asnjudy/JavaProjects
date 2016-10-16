package app05.controller;

import app05.domain.Book;
import app05.domain.Category;
import app05.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by asnju on 2016/9/16.
 */

@RequestMapping(value = "/book")
@Controller
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;


    @RequestMapping(value = "/input", method = RequestMethod.GET)
    public String input(Model model) {
        List<Category> categories = bookService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("book", new Book());
        return "app05/book_add_form";
    }

    @RequestMapping(value = "/edit/{id}")
    public String edit(Model model, @PathVariable long id) {
        List<Category> categories = bookService.getAllCategories();
        model.addAttribute("categories", categories);
        Book book = bookService.get(id);
        model.addAttribute("book", book);
        return "app05/book_edit_form";
    }


    @RequestMapping(value = "/save")
    public String save(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/book/list";
    }

    @RequestMapping(value = "/update")
    public String update(@ModelAttribute Book book) {
        bookService.update(book);
        return "redirect:/book/list";
    }

    @RequestMapping(value = "/list")
    public String list(Model model) {
        logger.info("book list");
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "app05/book_list";
    }
}
