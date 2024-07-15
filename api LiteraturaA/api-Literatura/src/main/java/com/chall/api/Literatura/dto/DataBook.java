package com.chall.api.Literatura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataBook(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors")List<DataAuthor>authors,
        @JsonAlias("languages") List<String>idiomas,
        @JsonAlias("download_count") Integer numeroDeDescargas
        ) {
}
