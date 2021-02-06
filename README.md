# TheMovies

Desafio técnico para ClickBus, cuja proposta é desenvolver um aplicativo que realiza a listagem de filmes mais populares atualmente.

😀 Thiago Ribeiro Filadelfo - thiago.filadelfo@gmail.com

📅 Entregue em 06/02/2021

## Atividades
- [x] Criação do repositório
- [-] [Wireframe de baixa fidelidade](https://drive.google.com/file/d/1E_WkYyO8KlyiEsldDDypeT09ji8Wr3eT/view?usp=sharing) 
    - [x] Login
    - [x] Filmes
    - [x] Compartilhamento
    - [x] Favoritados
- [x] Configurar firebase
- [-] Lista de Filmes
    - [x] Listagem dos filmes (poster do filme, o título, a média e etc)
    - [x] Botão para marcar como favorito nas células
    - [x] Barra de pesquisa para buscar um filme pelo nome
    - [x] Interface de lista vazia, erro ou sem internet
- [-] Detalhes do Filme
    - [x] Botão de favorito
    - [x] Poster do filme em tamanho maior
    - [x] Descrição (se houver)
    - [x] Orçamento e bilheteria
- [-] Favoritos
    - [x] Listagem dos filmes marcados como favoritos pelo usuário
    - [x] Interface de lista vazia
    
## Minhas considerações
Para a execução deste desafio técnico escolhi utilizar algumas abordagens mais contemporâneas, com a arquitetura MVVM, binding e Coroutines. E para o meu espanto eu codifiquei bem menos do que estava imaginando, gostei muito do resultado.

Também me desafiei para tentar colocar o máximo possivel de animação e pensar bastante em usabilidade, mais especificamente na inclusão de acessibilidade (estou bastante engajado nesta causa) e internacionalização do aplicativo.

O aplicativo tem suporte a DarkMode :)
 
## Como executar?
- Pré-requisito:
    - Git/Github instalador
    - Ter Android Studio instalado: 4.1.2 ou maior
    - Utilizar Gradle na versão: 6.5 ou maior
    - BuildTools na versão: 30.0.2 ou maior
    - Aplicativo compatível com o Sistema operacional Android API 21 ou maior
    
- Instalação do projeto:
    ```
    $ git clone https://github.com/trfiladelfo/desafio_clickbus.git
    ```
    - Abrir o Android Studio na pasta onde foi realizado o clone
    - Clique em Run -> App
 
## Bibliotecas usadas
- [Android X - Jetpack](https://developer.android.com/jetpack)
    - Room: Manipulador de SQLite
    - Navigation: Manipulador de navegar entre fragments e activities
- [Firebase](https://firebase.google.com/?hl=pt-br): Biblioteca ampla de recursos, o principal dele é o crash analytics e autheticator
- [Retrofit2](https://square.github.io/retrofit/): Biblioteca para realizar requisições Rest
- [Glide](https://bumptech.github.io/glide/): Biblioteca para manipular imagens
- Bibliotecas diversas
    - [GButton](https://github.com/TutorialsAndroid/GButton): Componente "Button" com estilo da empresa Google
    - [CircularProgressView](https://github.com/shubhamnandanwar/CircularProgressView): Componente gráfico para exibir o progresso
    - [Shimmer](http://facebook.github.io/shimmer-android/): Componente gráfico para realizar o placeholder de uma lista
- [Timber](https://github.com/JakeWharton/timber): Biblioteca (env. debug) para realizar logs locais

## Obrigado!
Agradeço muito pela oportunidade e ficarei muito grato pelo retorno de vocês acerca da minha execução independentemente do resultado final, isso nos engrandece como pessoa e mais ainda como um melhoramento profissional.