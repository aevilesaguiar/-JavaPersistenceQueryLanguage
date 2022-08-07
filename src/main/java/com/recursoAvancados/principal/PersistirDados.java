package com.recursoAvancados.principal;

import com.recursoAvancados.dao.JPAUtil;
import com.recursoAvancados.model.Acessorio;
import com.recursoAvancados.model.Proprietario;
import com.recursoAvancados.model.TipoCombustivel;
import com.recursoAvancados.model.Veiculo;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.Date;

public class PersistirDados {
    public static void main(String[] args) {

        EntityManager manager= JPAUtil.getEntityManager();
        EntityTransaction tx= manager.getTransaction();
        tx.begin();

        // instancia acessórios
        Acessorio alarme = new Acessorio();
        alarme.setDescricao("Alarme");

        Acessorio arCondicionado = new Acessorio();
        arCondicionado.setDescricao("Ar condicionado");

        Acessorio bancosDeCouro = new Acessorio();
        bancosDeCouro.setDescricao("Bancos de couro");

        Acessorio direcaoHidraulica = new Acessorio();
        direcaoHidraulica.setDescricao("Direção hidráulica");

        // persiste acessórios
        manager.persist(alarme);
        manager.persist(arCondicionado);
        manager.persist(bancosDeCouro);
        manager.persist(direcaoHidraulica);

        // instancia veículos
        Veiculo veiculo1 = new Veiculo();
        veiculo1.setFabricante("VW");
        veiculo1.setModelo("Gol");
        veiculo1.setAnoFabricacao(2010);
        veiculo1.setAnoModelo(2010);
        veiculo1.setValor(new BigDecimal(17_200));
        veiculo1.setTipoCombustivel(TipoCombustivel.BICOMBUSTIVEL);
        veiculo1.setDataCadastro(new Date());
        veiculo1.getAcessorios().add(alarme);
        veiculo1.getAcessorios().add(arCondicionado);

        Veiculo veiculo2 = new Veiculo();
        veiculo2.setFabricante("Hyundai");
        veiculo2.setModelo("i30");
        veiculo2.setAnoFabricacao(2012);
        veiculo2.setAnoModelo(2012);
        veiculo2.setValor(new BigDecimal(53_500));
        veiculo2.setTipoCombustivel(TipoCombustivel.BICOMBUSTIVEL);
        veiculo2.setDataCadastro(new Date());
        veiculo2.getAcessorios().add(alarme);
        veiculo2.getAcessorios().add(arCondicionado);
        veiculo2.getAcessorios().add(bancosDeCouro);
        veiculo2.getAcessorios().add(direcaoHidraulica);
        veiculo2.getAcessorios().add(direcaoHidraulica);

        // persiste veículos
        manager.persist(veiculo1);
        manager.persist(veiculo2);

        Proprietario proprietario = new Proprietario();
        proprietario.setNome("Sebastião");
        proprietario.setTelefone("(34) 1234-5678");
        manager.persist(proprietario);

        tx.commit();
        manager.close();
        JPAUtil.close();

    }
}
