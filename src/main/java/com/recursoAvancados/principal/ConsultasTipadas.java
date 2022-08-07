package com.recursoAvancados.principal;

import com.recursoAvancados.dao.JPAUtil;
import com.recursoAvancados.model.Veiculo;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ConsultasTipadas {

    public static void main(String[] args) {

        EntityManager manager = JPAUtil.getEntityManager();

        TypedQuery<Veiculo> query = manager.createQuery(
                "from Veiculo", Veiculo.class
        );
        List<Veiculo> veiculos=query.getResultList();

        for (Veiculo veiculo : veiculos
        ){
            System.out.println(veiculo.getModelo()+" "+veiculo.getFabricante()+": "+veiculo.getAnoFabricacao());
        }

    }

    }
