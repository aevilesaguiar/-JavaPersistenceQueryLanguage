package com.recursoAvancados.principal;

import javax.persistence.Persistence;

public class CriarTabela {
    public static void main(String[] args) {
        Persistence.createEntityManagerFactory("recursos_avancados");
    }
}
