package com.recursoAvancados.principal;

import com.recursoAvancados.dao.JPAUtil;
import com.recursoAvancados.model.Categoria;
import com.recursoAvancados.model.Produto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class PersistirDados {
    public static void main(String[] args) {

        EntityManager manager= JPAUtil.getEntityManager();
        EntityTransaction tx= manager.getTransaction();
        tx.begin();

        Categoria categoria=new Categoria();
        categoria.setNome("Bebidas");

        Categoria categoria1=new Categoria();
        categoria.setNome("Eletrodomestico");

        Produto produto1= new Produto();
        produto1.setNome("Refrigerante");
        produto1.setCategoria(categoria);

        Produto produto2=new Produto("Água",categoria);

        Produto produto3=new Produto("Suco",categoria);


        Produto produto4=new Produto("Geladeira",categoria1);

        manager.persist(categoria1);
        manager.persist(categoria);

        manager.persist(produto1);
        manager.persist(produto2);
        manager.persist(produto3);
        manager.persist(produto4);


        tx.commit();
        manager.close();
        JPAUtil.close();

    }
}
