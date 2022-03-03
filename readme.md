<h1>Bem-vindo a API de votação em pauta</h1>

<h3>0. Realizei o deploy de uma branch separada no heroku, disponível através do link:</h3>
[https://voting-challenge.herokuapp.com/api/voting-challenge/swagger-ui](https://voting-challenge.herokuapp.com/api/voting-challenge/swagger-ui)
<h3>1. Para facilitar a configuração do banco, fiz um arquivo docker-compose. Abra o terminal na raíz do projeto e
insira:</h3>
```
cd docker-vc
```
<h3>2. Execute o comando(o docker tentará conectar na porta 4321 que precisa estar liberada):</h3>
```
docker-compose up
```
<h3>3. Aguarde o contêiner do docker subir e rode a aplicação pela classe 'Application'. Quando a aplicação terminar de subir, clique abaixo
para acessar o swagger(o perfil 'local' deve estar ativo):</h3>
[http://localhost:8099/api/voting-challenge/swagger-ui/](http://localhost:8099/api/voting-challenge/swagger-ui/)
<h3>4. Em ASSOCIATES podemos criar um associado através do método de POST, se o CPF informado for válido. Precisamos de
pelo menos um para realizar a votação das pautas. A integração com a API de validação dos CPFs é feita dentro de uma
anotação personalizada.</h3>
<h3>5. Em SCHEDULES podemos criar uma pauta e votar nela antes que o tempo escolhido se encerre. Cada associado só
poderá votar uma vez por pauta. A pauta poderá ser encerrada antes do tempo pelo endpoint de PUT. O sistema de
mensageria exibirá o resultado da pauta encerrada automaticamente pelos logs da API.</h3>
<h3>6. A API se encontra com 100% de coverage de testes unitários nas classes de serviço.</h3>
<h3>7. A API tem liquibase implementada para facilitar o versionamento.</h3>