package com.recursoAvancados.principal;

import com.recursoAvancados.dao.JPAUtil;
import com.recursoAvancados.model.Veiculo;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

public class ParametrosNomeados {
    public static void main(String[] args) {

        EntityManager manager= JPAUtil.getEntityManager();

        //usado para consultar entidade e valores usando JPQL
        Query query= manager.createQuery(
                "from Veiculo where anoFabricacao >= :ano and valor<= :preco"
        );
        query.setParameter("ano",2009);
        query.setParameter("preco", new BigDecimal(60_400));
        List veiculos= query.getResultList();

        for (Object obj: veiculos
             ) {
            Veiculo veiculo=(Veiculo) obj;
            System.out.println(veiculo.getModelo()+" "+veiculo.getFabricante()+": "+veiculo.getAnoFabricacao());

        }


        manager.close();
        JPAUtil.close();

    }
}
