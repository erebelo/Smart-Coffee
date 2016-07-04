Smart Coffee
==================

Smart Coffee, é um projeto de Internet das Coisas (IoT, do inglês Internet of Things), que informa qualquer mudança no status do preparo de um café, ou seja, se o café esta pronto, em preparo, ou se a cafeteira está disponível para uso.
A mudança de status, é feita através da ánalise e interpretação da variação da corrente elétrica implementada por um algoritmo, que quando identica tal mudança, envia seus dados para nuvem (Google Cloud Platform) através de uma Plataforma de IOT, e dispara um push notification para os dispositivos android que possuem o aplicativo (Smart Coffee) instalado.

## Componentes
- NodeMCU + ESP8266
- Sensor de Corrente ACS714


## Bibliotecas
- Importar as seguintes bibliotecas para dentro do diretório de instalação da IDE Arduino(\Arduino\libraries\)
	- \libraries\CiotAdapter
	- \libraries\SimpleTimer
	- \libraries\WiFiConnector-master
	- \libraries\WiFiManager-master
	
## IDE Arduino
- Configurar IDE Arduino para suportar ESP8266, seguindo o tutorial: [Instalando pelo Gerenciador de Placas][1]
- Abrir o código fonte hybrid.ino (\hybrid\hybrid.ino) na IDE Arduino
- Selecionar a placa (Ferramentas->Placa->NodeMCU 1.0 (ESP -12E Module))
- Selecionar frequência de CPU (Ferramentas->CPU Frequency->80MHz)
- Selecionar tamanho do flash (Ferramentas->Flash Size->4M (3M SPIFFS))
- Selecionar velocidade de upload (Ferramentas->Flash Size->115200)
- Selecionar a porta (Ferramentas->Porta->COMXX)
- Com o sensor conectado no NodeMCU, e este no computador, clicar em Upload

## Configurar WiFi pelo modo SmartConfig:
- Colocar um jumper, com uma ponta no pino D1 (GPIO 5) e a outra no GND
- Abrir o console monitor serial da IDE Arduino
- Pressionar o botao reset por 3 segundos
- Trocar o jumper do pino GND para o 3,3v (nesse momento, no console Monitor serial da IDE Arduino, aparecerá o log startSmartConfig(), indicando que o nodeMCU esta no modo Smart Config)
- Instalar o aplicativo [ESP8266 SmartConfig][2] (Nat Weerawan) disponível na Play Store (Android)
- Configurar a rede para o ESP8266 pelo app, inserindo a senha para o SSID conectado (conectar o dispositivo móvel em uma rede sem proxy de autenticação)

## Esquema Eletrônico
- O esquema pode ser visualizado abrindo do arquivo smart_coffee.fzz através do software [Fritzing][3]

[1]: https://github.com/esp8266/Arduino#installing-with-boards-manager
[2]: https://play.google.com/store/apps/details?id=com.cmmakerclub.iot.esptouch&hl=en
[3]: http://fritzing.org/home/