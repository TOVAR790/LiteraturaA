package com.chall.api.Literatura.repository;

import com.chall.api.Literatura.model.Book;

import java.util.Arrays;
import java.util.List;

public interface BookRespository {
    List<Book> findAll();

    Arrays buscarTop10Libros();

    List<Book> findByIdioma(String idioma);
}
