package com.recursoAvancados.principal;

import com.recursoAvancados.dao.JPAUtil;
import com.recursoAvancados.model.Veiculo;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ConsultaSimples {
    public static void main(String[] args) {

        EntityManager manager= JPAUtil.getEntityManager();

        //usado para consultar entidade e valores usando JPQL
        Query query= manager.createQuery(
                "select v from Veiculo v where anoFabricacao=2012");

        List veiculos = query.getResultList();

        for (Object obj: veiculos
             ) {

            Veiculo veiculo=(Veiculo) obj;

            System.out.println(veiculo.getModelo()+" "+veiculo.getFabricante()+": "+veiculo.getAnoFabricacao());
        }

        manager.close();
        JPAUtil.close();

    }
}
