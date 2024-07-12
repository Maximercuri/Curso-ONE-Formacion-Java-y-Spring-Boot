package com.aluracursos.screenmatch.model;

import com.aluracursos.screenmatch.service.ConsultaChatGPT;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;
    private Integer totalDeTemporadas;
    private Double evaluacion;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String sinopsis;
    private String actores;
    private String imagenPromocional;
    @OneToMany(mappedBy = "serie",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<Capitulo> capitulos;

    public Serie(){
    }

    public Serie(DatosSerie datosSerie){

        this.titulo = datosSerie.titulo();
      //this.sinopsis = ConsultaChatGPT.obtenerTraduccion( datosSerie.sinopsis() ); para obtener el resultado en español a costa de pagar
        this.sinopsis = datosSerie.sinopsis();
        this.totalDeTemporadas = datosSerie.totalDeTemporadas();
        this.actores = datosSerie.actores();
        /*
        .split("regex")[limit]: divide un texto en n elementos en forma de lista, indicados con un carácter de quiebre
        y se toma los m primeros elementos indicados por el usuario.
        Esta función tiene 2 argumentos que recibe:
            . regex (es el caracter de quiebre usado para dividir los elementos)
            . limit (indica cuál de los elementos se toma de la lista resultante)
        */
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0.0);
        this.imagenPromocional = datosSerie.imagenPromocional();

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalDeTemporadas() {
        return totalDeTemporadas;
    }

    public void setTotalDeTemporadas(Integer totalDeTemporadas) {
        this.totalDeTemporadas = totalDeTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getImagenPromocional() {
        return imagenPromocional;
    }

    public void setImagenPromocional(String imagenPromocional) {
        this.imagenPromocional = imagenPromocional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Capitulo> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(List<Capitulo> capitulos) {
        capitulos.forEach(e -> e.setSerie(this));
        this.capitulos = capitulos;
    }

    @Override
    public String toString() {
        return "\n" + "Titulo: " + titulo +
               ", Temporadas: " + totalDeTemporadas +
               ", Evaluación: " + evaluacion +
               ", Genero: " + genero +
               ", Actores: " + actores + "\n" +
               "Sinopsis: " + sinopsis + "\n" +
               "Imagen Promocional: " + imagenPromocional + "\n";
    }
}
