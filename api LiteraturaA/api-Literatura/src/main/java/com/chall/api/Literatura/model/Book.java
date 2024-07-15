package com.chall.api.Literatura.model;

import com.chall.api.Literatura.dto.DataAuthor;
import com.chall.api.Literatura.dto.DataBook;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Optional;

@Entity
@Table(name = "libros")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Author author;
    private String idioma;
    @Column(name = "numero_de_descargas")
    private Integer numeroDeDescargas;

    public Book(DataBook libro) {
        this.titulo = libro.titulo();
        Optional<DataAuthor> author = libro.authors().stream()
                .findFirst();
        if (author.isPresent()) {
            this.author = new Author(author.get());
        }else {
            System.out.println("No se encontró el autor");
        }
        this.idioma = libro.idiomas().get(0);
        this.numeroDeDescargas = libro.numeroDeDescargas();
    }
    public void  setAuthor(Author author) { this.author = author; }
}
