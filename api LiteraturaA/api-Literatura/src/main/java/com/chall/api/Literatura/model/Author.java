package com.chall.api.Literatura.model;

import com.chall.api.Literatura.dto.DataAuthor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimieno;

    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Book> libros = new ArrayList<>();

    public  Author(DataAuthor author){
        this.nombre = author.nombre();
        this.fechaDeNacimiento = author.fechaDeNacimiento();
        this.fechaDeFallecimieno = author.fechaDeFallecimiento();
    }
    public void  setBook(List<Book> libros) { this.libros = libros;}
}
