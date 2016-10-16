package app05.service;

import app05.domain.Book;
import app05.domain.Category;

import java.util.List;

/**
 * Created by asnju on 2016/9/16.
 */
public interface BookService {

    List<Category> getAllCategories();

    Category getCategory(int id);

    List<Book> getAllBooks();

    boolean save(Book book);

    boolean update(Book book);

    Book get(long id);

    long getNextId();
}
