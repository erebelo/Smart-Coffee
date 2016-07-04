App Smart Coffee
==================

O aplicativo SmartCoffee utiliza as API's Google Cloud Platform e Google Cloud Messaging para envio e recebimento de push notifications em tempo real, exibindo o status no preparo do café.

## Linguagens
- Java
- HTML
- CSS

## APIs
- [Google Cloud Endpoints][1]
- [Google Cloud Messaging][2]
- [Fabric][3]

## IDE
- [Android Studio][4]

## Google Cloud Endpoints
- Arquivos de Configuração:
    - web.xml: configurações do webapp
    - pom.xml: dependências maven
    - appengine-web.xml: configurações app engine

## Fabric
- Plugin usado para controlar o fluxo de acesso e erros no aplicativo, bem como, sua distribuição em modo Beta
	- Add Fabric (Beta) para IDE Android Studio	(File -> Settings -> Plugins -> Browse repositories -> Fabric)
		- Abrir pelo ícone criado no canto superior direito
		- Instalar o Crashlytics
		- Instalar Answers
		- Selecionar a organização e o app
		- Selecionar o símbolo distribuitions e fazer upload do app.apk e distribuir para os convidados por email
		- IMPORTANTE: ao criar uma nova organização pela [interface web][3], deve-se confirmar por email sua criação para exibir na IDE Android Studio
- Mudar a versão do app toda vez que for distribuir uma nova versão(app -> build.grade):
	- versionCode 2
    - versionName "1.0.3"

## Execução
- Localhost (Run app ou backend)
	- http://localhost:8080/
    - http://localhost:8080/_ah/api/explorer
    - http://localhost:8080/_ah/admin

- Servidor (Deploy)
	- Build -> Deploy Module to App Engine...

[1]: https://cloud.google.com/appengine/docs/java/endpoints/
[2]: https://developers.google.com/cloud-messaging/android/start
[3]: https://fabric.io/
[4]: https://developer.android.com/studio/index.html
