<h1>Bem-vindo a API de votação em pauta</h1>

<h3>1. Para facilitar a configuração do banco, fiz um arquivo docker-compose. Abra o terminal na raíz do projeto e 
insira:</h3>
```
cd docker
```
<h3>2. Execute o comando(o docker tentará conectar na porta 5432 que precisará estar liberada):</h3>
```
docker-compose up
```
<h3>3. Aguarde o contêiner do docker subir e rode a aplicação. Quando a aplicação terminar de subir, clique abaixo
para acessar o swagger :</h3>
[Swagger](http://localhost:8099/api/voting-challenge/swagger-ui/)
<h3>4. Em ASSOCIATES podemos criar um associado através do método de POST, se o CPF informado for válido. Precisamos de
pelo menos um para realizar a votação das pautas.</h3>
<h3>5. Em SCHEDULES podemos criar uma pauta e votar nela antes que o tempo escolhido se encerre. Cada associado só
poderá votar uma vez por pauta. A pauta poderá ser encerrada antes do tempo pelo endpoint de PUT. O sistema de
mensageria exibirá o resultado da pauta encerrada automaticamente pelos logs da API.</h3>