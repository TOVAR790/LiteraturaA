package com.chall.api.Literatura.principal;


import com.chall.api.Literatura.Conversion.Conversion;
import com.chall.api.Literatura.dto.Data;
import com.chall.api.Literatura.dto.DataBook;
import com.chall.api.Literatura.model.Author;
import com.chall.api.Literatura.model.Book;
import com.chall.api.Literatura.repository.AuthorRespository;
import com.chall.api.Literatura.repository.BookRespository;
import com.chall.api.Literatura.repository.service.ConsumeApi;
import org.springframework.boot.ansi.AnsiColor;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL = "https://gutendex.com/books/";
    private ConsumeApi consumeApi = new ConsumeApi();
    private Conversion conversion = new Conversion();
    private Integer opcion = 10;
    private Scanner scanner = new Scanner(System.in);
    private BookRespository libroRespositorio;
    private AuthorRespository authorRespository;
    private Object autorRepositorio;
    private BookRespository libroRepositorio;
    private Object Ansi;
    private Object autores;

    public Principal(BookRespository libroRespositorio, AuthorRespository authorRespository){
        this.libroRespositorio = libroRespositorio;
        this.authorRespository = authorRespository;
    }

    private void leerLibro(Book libro) {
        String magenta = ansi().fg(AnsiColor.MAGENTA).toString();
        String defaultColor = ansi().fg(AnsiColor.DEFAULT).toString();

        System.out.println(magenta + "----- 📖 -----" + defaultColor);
        System.out.println(magenta + "Titulo:" + defaultColor + " " + libro.getTitulo());
        System.out.println(magenta + "Autor:" + defaultColor + " " + libro.getAuthor().getNombre());
        System.out.println(magenta + "Idioma:" + defaultColor + " " + libro.getIdioma());
        System.out.println(magenta + "Numero de descargas:" + defaultColor + " " + libro.getNumeroDeDescargas());
        System.out.println(magenta + "--------------------" + defaultColor);
    }

    private Object ansi() {
        return null;
    }

    private void buscarLibro() {
        System.out.println("Ingresa el nombre del libro:");
        scanner.nextLine();
        String nombreLibro = scanner.nextLine();
        String json = consumoApi.obtenerLibros(URL + "?search=" + nombreLibro.replace(" ", "+"));
        List<DataBook> libros = conversion.obtenerDatos(json, Data.class).resultados();
        Optional<DataBook> libroOptional = libros.stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst();
        if (libroOptional.isPresent()) {
            var libro = new Book(libroOptional.get());
            libroRepositorio.save(libro);
            leerLibro(libro);
        } else {
            System.out.println(ansi().fg(Ansi.Color.RED).a("El libro no se encuentra").reset());
        }
    }
        private void listarLibros(){
        List<Book> libros = libroRespositorio.findAll();
        libros.stream()
                .forEach((this::leerLibro));
    }
    private void leerAuthor(Author author, String defaultColor) {
        String magenta =  ansi().fg(Ansi.Color.MAGENTA).toString();
        String defaltcolor = ansi().fg(Ansi.Color.DEFAULT).toString();

        System.out.println(magenta + "Autor:" + defaultColor + " " + author.getNombre());
        System.out.println(magenta + "Fecha de fallecimiento:" + defaultColor + " " + autor.getFechaDeNaciminto());
        System.out.println(magenta + "Fecha de fallecimiento:" + defaultColor + " " + autor.getFechaDeFallecimiento());

        var libros = autor.getLibros().stream()
                .map(Book::getTitulo)
                .collect(Collectors.toList());
        System.out.println(magenta + "Libros:" + defaultColor + " " + libros + "\n");
    }

    private void listarAutores(Scanner autorRepositorio) {
        List<Author> autores = autorRepositorio.findAll();
        autores.stream()
                .forEach(this::leerAutor);
    }

    private void leerAutor(Author author) {
    }

    private void listarAutoresPorano() {
        System.out.println("Ingresa el año en que vivió el autor(es):");
        Integer año = scanner.nextInt();
        List<Author> autores = autorRepositorio.findByFechaDeFallecimientoGreaterThan(año);
        autores.stream()
                .forEach(this::leerAutor);
    }

    private void listarLibrosPorIdioma() {
        String cyan = ansi().fg(Ansi.Color.CYAN).toString();
        String defaultColor = ansi().fg(Ansi.Color.DEFAULT).toString();

        System.out.println("Ingresa el idioma para buscar los libros:");
        System.out.println(cyan + "es" + defaultColor + " - español");
        System.out.println(cyan + "en" + defaultColor + " - ingles");
        System.out.println(cyan + "fr" + defaultColor + " - frances");
        System.out.println(cyan + "pt" + defaultColor + " - portugues" + defaultColor);

        String idioma = scanner.next();
        List<Book> libros = libroRepositorio.findByIdioma(idioma);
        libros.stream()
                .forEach(this::leerLibro);
    }

    private void generarEstadisticasDelNumeroDeDescargas() {
        String magenta = ansi().fg(Ansi.Color.MAGENTA).toString();  // Color magenta
        String defaultColor = ansi().fg(Ansi.Color.DEFAULT).toString();  // Color por defecto

        var libros = libroRepositorio.findAll();
        DoubleSummaryStatistics doubleSummaryStatistics = new DoubleSummaryStatistics();
        for (Book libro : libros) doubleSummaryStatistics.accept(libro.getNumeroDeDescargas());

        System.out.println(magenta + "Conteo del numero de descargas:" + defaultColor + " " + doubleSummaryStatistics.getCount());
        System.out.println(magenta + "Numero de descargas minimo:" + defaultColor + " " + doubleSummaryStatistics.getMin());
        System.out.println(magenta + "Numero de descargas maximo:" + defaultColor + " " + doubleSummaryStatistics.getMax());
        System.out.println(magenta + "Suma del numero de descargas:" + defaultColor + " " + doubleSummaryStatistics.getSum());
        System.out.println(magenta + "Promedio del numero de descargas:" + defaultColor + " " + doubleSummaryStatistics.getAverage() + "\n");
    }

    private void listarTop10Libros() {
        libroRepositorio.buscarTop10Libros().stream()
                .forEach(this::leerLibro);
    }

    private void buscarAutorPorNombre() {
        System.out.println("Ingresa el nombre del autor:");
        scanner.nextLine();
        var nombre = scanner.nextLine().trim();
        List<Author> autores = autorRepositorio.findByNombre(nombre);
        if (!autores.isEmpty()) {
            System.out.println("Autores encontrados:");
            System.out.println("--------------------");
            autores.forEach(this::leerAutor);
        } else {
            System.out.println(ansi().fg(Ansi.Color.RED).a("No se encontraron autores con ese nombre.").reset());
        }
    }

    public void mostrarMenu() {
        while (opcion != 9) {
            System.out.println("""
                    ---Bienvenido a Literatura de CJ Nieto--- \n
                    Elije una opcion a través del número en el menú:
                    1- Buscar libro por titulo
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    6- Generar estadisticas del número de descargas
                    7- Listar el top 10 de libros más descargados
                    8- Buscar autor por nombre
                    9- Salir
                    """);
            try {
                opcion = scanner.nextInt();
                switch (opcion) {
                    case 1:
                        buscarLibro();
                        break;
                    case 2:
                        listarLibros();
                        break;
                    case 3:
                        listarAutores((Scanner) autorRepositorio);
                        break;
                    case 4:
                        listarAutoresPorano();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        generarEstadisticasDelNumeroDeDescargas();
                        break;
                    case 7:
                        listarTop10Libros();
                        break;
                    case 8:
                        buscarAutorPorNombre();
                        break;
                    case 9:
                        System.out.println(ansi().fg(Ansi.Color.YELLOW).a("¡Hasta luego!").reset());
                        System.exit(0); // Salir del bucle
                    default:
                        System.out.println(ansi().fg(Ansi.Color.RED).a("Opción no válida, por favor intenta nuevamente.").reset());
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a("Entrada inválida. Por favor, ingresa un número.").reset());
                scanner.next(); // Limpiar el buffer del scanner
            }
        }
    }
}






