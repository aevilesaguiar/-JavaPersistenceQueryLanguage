package com.recursoAvancados.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "acessorio")
public class Acessorio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;

    @Column(length = 60, nullable = false)
    private String descricao;

    @ManyToMany(mappedBy = "acessorios", cascade = CascadeType.PERSIST)
    private Set<Veiculo> veiculos=new HashSet<>();

    public Acessorio( ) {


    }

    public Acessorio(String descricao, Set<Veiculo> veiculos) {
        this.descricao = descricao;
        this.veiculos = veiculos;
    }

    public Acessorio(String descricao) {
        this.descricao = descricao;

    }


    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Acessorio acessorio = (Acessorio) o;
        return Objects.equals(codigo, acessorio.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
