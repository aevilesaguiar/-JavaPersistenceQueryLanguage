# Java Persistence QueryLanguage


## Introdução à JPQL

Se você quer consultar um objeto e já sabe o identificador dele, pode usar os
métodos find ou getReference de EntityManager, como já vimos anteriormente.

Agora, caso o identificador seja desconhecido ou você quer consultar uma
coleção de objetos, você precisará de uma query.

A JPQL (Java Persistence Query Language) é a linguagem de consulta padrão da
JPA, que permite escrever consultas portáveis, que funcionam independente do
banco de dados.

Esta linguagem de query usa uma sintaxe parecida com a SQL, para selecionar
objetos e valores de entidades e os relacionamentos entre elas.

## tipos de Cascade

Existem seis tipos de cascades (CascadeType) na especificação do JPA. São eles:

ALL = Realiza todas as operações em cascata
DETACH = Realiza a operação detach em cascata
MERGE = Realiza a operação merge em cascata
PERSIST = Realiza a operação persist em cascata
REFRESH = Realiza a operação refresh em cascata
REMOVE = Realiza a operação remove em cascata

Realizar operações em cascata só faz sentido em relacionamentos Pai - Filho (a transição do estado 
da entidade Pai sendo realizada em cascata na entidade Filho). Por mais que seja permitido mapear
o cascade no sentido inverso (Filho - Pai) não é muito útil e não pode ser considerado uma boa 
prática.

REFRESH não salva, mas atualiza a entidade com as informações do banco.

Quando o MERGE é executado, ele também persiste os filhos caso eles ainda não tenham sido persistidos.

É boa prática que as operações em cascata sejam feitas de pai para filho, e não o contrário, apesar de ser válido.

Cascade DETACH faz com que a operação detach, quando executada no pai, também seja executada no filho. Dizemos
que uma entidade é detached quando ela não está sendo gerenciada pelo EntityManager. Isso acontece quando exectamos 
o método detach(entidade) do EntityManager, quando a transação é comitada ou dá rollback.

Cascade LOCK não faz parte do JPA mas sim do Hibernate. Basicamente quando você adquirir um lock na 
entidade pai, essa operação será realizada também nos filhos.


O que significa um rollback?
é uma palavra de informatica usada para deffinir encerramento da transação DESCARTANDO (desfazendo) 
todas as alterações (updates,deletes,inserts) realizadas durante a transação.


## Cascade Hibernate: Conhecendo diferentes tipos

O cascade não é um recurso específico do Hibernate, este já vem de longa data e é um conceito sólido em se tratando de 
banco de dados.

A função do cascade é cascatear operações de persistência. Com isso você já deve perceber que este é 
utilizada apenas quando existe algum tipo de relacionamento entre 2 ou mais classes.

- Cascade save-update: Vamos iniciar pelo cascade save-update. E como o nome sugere, quando é feito um save 
ou um update na classe “Pai” as classes “Filhas” também são salvas ou atualizadas.
- Cascade Delete: O cascade delete faz com que os filhos sejam deletados se o pai for deletado, é a mesma lógica de Nota
fiscaL e ItemNotafiscal, ou seja, se deletarmos a Nota fiscal os itens devem ser deletados.
- Cascade delete-orphan:Existe ainda o delete-orphan, quando você salva ou atualiza a classe pai, 
os registros filhos que estavam marcados como removidos são de fato deletados.


## Consultas simples e iteração no resultado

O método EntityManager.createQuery é usado para consultar entidades e valores
usando JPQL.

As consultas criadas através do método createQuery são chamadas de consultas
dinâmicas, pois elas são definidas diretamente no código da aplicação.

Query query = manager.createQuery(
        "select v from Veiculo v where anoFabricacao = 2012");
List veiculos = query.getResultList();

for (Object obj : veiculos) {
    Veiculo veiculo = (Veiculo) obj;

    System.out.println(veiculo.getModelo() + " " + veiculo.getFabricante()
    + ": " + veiculo.getAnoFabricacao());

}

O método createQuery retorna um objeto do tipo Query, que pode ser consultado
através de getResultList. Este método retorna um List não tipado, por isso
fizemos um cast na iteração dos objetos.


A consulta JPQL que escrevemos seleciona todos os objetos veículos que
possuem o ano de fabricação igual a 2012.

select v from Veiculo v where anoFabricacao = 2012


Essa consulta é o mesmo que:

from Veiculo where anoFabricacao = 2012

Veja que não somos obrigados a incluir a instrução select neste caso, e nem
informar um alias para a entidade Veiculo.


## Usando parâmetros nomeados


Se você precisar fazer uma consulta filtrando por valores informados pelo
usuário, não é recomendado que faça concatenações na string da consulta,
principalmente para evitar SQL Injection. O ideal é que use parâmetros da Query.

Query query = manager.createQuery(
        "from Veiculo where anoFabricacao >= :ano and valor <= :preco");
query.setParameter("ano", 2009);
query.setParameter("preco", new BigDecimal(60_000));
List veiculos = query.getResultList();
for (Object obj : veiculos) {
        Veiculo veiculo = (Veiculo) obj;
        System.out.println(veiculo.getModelo() + " " + veiculo.getFabricante()
        + ": " + veiculo.getAnoFabricacao());

}

Nomeamos os parâmetros com um prefixo :, e atribuímos os valores para cada
parâmetro usando o método setParameter de Query.

Veja a consulta SQL gerada:

Hibernate:
        select
        veiculo0_.codigo as codigo1_2_,
        veiculo0_.ano_fabricacao as ano_fabr2_2_,
        veiculo0_.ano_modelo as ano_mode3_2_,
        veiculo0_.data_cadastro as data_cad4_2_,
        veiculo0_.fabricante as fabrican5_2_,
        veiculo0_.modelo as modelo6_2_,
        veiculo0_.cod_proprietario as cod_prop9_2_,
        veiculo0_.tipo_combustivel as tipo_com7_2_,
        veiculo0_.valor as valor8_2_
from
        veiculo veiculo0_
where
        veiculo0_.ano_fabricacao>=?
        and veiculo0_.valor<=?


## Consultas tipadas

Quando fazemos uma consulta, podemos obter o resultado de um tipo específico,
sem precisar fazer casting dos objetos ou até mesmo conversões não checadas
para coleções genéricas.

TypedQuery<Veiculo> query = manager.createQuery("from Veiculo",
        Veiculo.class);
List<Veiculo> veiculos = query.getResultList();
for (Veiculo veiculo : veiculos) {
    System.out.println(veiculo.getModelo() + " " + veiculo.getFabricante()
    + ": " + veiculo.getAnoFabricacao());

}

Usamos a interface TypedQuery, que é um subtipo de Query. A diferença é que o
método getResultList já retorna uma lista do tipo que especificamos na criação
da query, no segundo parâmetro.


## Paginação

Uma consulta que retorna muitos objetos pode ser um problema para a maioria
das aplicações. Se existir a necessidade de exibir um conjunto de dados grande,
é interessante implementar uma paginação de dados e deixar o usuário navegar
entre as páginas.
As interfaces Query e TypedQuery suportam paginação através dos métodos
setFirstResult e setMaxResults, que define a posição do primeiro registro
(começando de 0) e o número máximo de registros que podem ser retornados,
respectivamente.



## Relembrando Relacionamentos

Na JPA os relacionamentos existentes são: OneToOne, ManyToOne, OneToMany, ManyToMany.

- Relacionamento @OneToOne

![img.png](img.png)

No relacionamento OneToOne, um item pode pertencer a apenas um outro item, é uma ligação um para um. Isso significa que cada linha de uma entidade se refere a apenas uma linha de outra entidade.
Vamos considerar o exemplo acima. Blog e Categoria de forma unidirecional reversa, a relação é uma relação OneToOne.
Isso significa que cada blog pertence a apenas uma categoria.

A anotação para mapear uma única entidade para uma única outra entidade é @OneToOne.

Abaixo como fica na classe Blog

![img_1.png](img_1.png)

Abaixo como fica na classe Categoria

![img_2.png](img_2.png)

Antes de se configurar o modelo citado no exemplo, temos de lembrar que um relacionamento tem um lado proprietário, de preferência o lado que manterá a chave estrangeira no banco de dados.


- Relacionamento @ManyToOne

![img_3.png](img_3.png)

Relação ManyToOne entre entidades: onde uma entidade é referenciada com outra entidade que contém valores únicos.
Em bancos de dados relacionais, esses relacionamentos são aplicáveis usando chave estrangeira/chave primária entre as tabelas.


Vamos considerar um exemplo de relação entre entidades de Blog e Seção.
De maneira unidirecional, ou seja, de blogs para seções, a relação muitos para um é aplicável.
Isso significa que cada registro de blog contém um código de seção, que deve ser uma chave primária na tabela seções.

A anotação para mapear esse tipo de relacionamento de muitas entidades de seção para uma única outra entidade blog é @ManyToOne.

![img_4.png](img_4.png)


- Relacionamento @OneToMany

![img_5.png](img_5.png)

Neste relacionamento, cada linha de uma entidade é referenciada a muitos registros filho em outra entidade.
O importante é que os registros de filhos não podem ter vários pais. Em uma relação OneToMany entre a Tabela A e a Tabela B, cada linha da Tabela A está ligada a 0, 1 ou muitas linhas da Tabela B.
Se o Blog e a Seção estiverem de maneira unidirecional reversa, a relação será uma relação OneToMany.
No nosso exemplo, seria um blog e suas seções. Um blog pode fazer parte de várias seções.

A anotação para mapear esse tipo de relacionamento de uma única entidade blog para muitas entidades de seção para é @OneToMany.

Abaixo como fica na classe Seção:

![img_6.png](img_6.png)


- Relacionamento @ManyToMany

![img_7.png](img_7.png)


O relacionamento muitos para muitos é onde uma ou mais linhas de uma entidade são associadas a mais de uma linha em outra entidade.
Vamos considerar um exemplo de relação entre as entidades Blog e Período. Na maneira bidirecional, tanto o blog quanto o período têm relação muitos para um. Isso significa que cada registro de Blog é referido por conjunto de Período (códigos de período), que devem ser chaves primárias na tabela Blog e armazenadas na tabela blog_periodo e vice-versa. Aqui, a tabela blog_periodo contém ambos os campos de chave estrangeira.

Os relacionamentos ManyToMany exigem um pouco mais de trabalho do que os outros relacionamentos.

Efetivamente, em um banco de dados, um relacionamento ManyToMany envolve uma tabela intermediária que faz referência a ambas as outras tabelas.

Portanto, para nosso exemplo, o relacionamento ManyToMany será aquele entre as instâncias Blog e Período, pois um blog ter vários períodos e um período pode ter participação de vários blogs.

A anotação para mapear esse tipo de relacionamento de muitas entidades blog para muitas entidades de período para é @ManyToMany.

![img_8.png](img_8.png)




Referencias

- https://www.tidicas.com.br/?p=2058
- https://www.devmedia.com.br/cascade-hibernate-conhecendo-diferentes-tipos/28892
- https://support.microsoft.com/pt-br/office/acesso-sql-conceitos-b%C3%A1sicos-vocabul%C3%A1rio-e-sintaxe-444d0303-cde1-424e-9a74-e8dc3e460671