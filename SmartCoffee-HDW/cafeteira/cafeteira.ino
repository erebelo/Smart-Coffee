#include <SimpleTimer.h>
#include <CiotAdapter.h>

/*
Measuring Current Using ACS712
*/
const int analogInPin = A0, numElements = 1;
int sensorValue, contPreparing=0, contWaiting=0, onMake=0, marginNumber=7;
String oldStatusCoffee="0", statusCoffee="0"; /* statusCoffee: 1 - esperando, 2 - preparando, 3 - pronto */
float milliAmps;

CiotAdapter adapter;
SimpleTimer timer;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  /*adapter.initialize();*/
  Serial.print("\nMAC Address: ");
  Serial.println(adapter.getMacAddress());
  Serial.println("\n*** ESPERANDO... ***");
}

void loop() {

  while (adapter.processConfigButton() == LOW) {     
    adapter.processConfigButton(); // inicia processo de configuração do wi-fi, caso necessário
    timer.run();
  }

  sensorValue = analogRead(analogInPin);
  
  /*Serial.print("raw sensorValue = ");
  Serial.println(sensorValue);*/
  
  // Convert to milli amps
  // The offset of 512 has to be determined, perhaps during startup.
  milliAmps = ((float) (sensorValue  - 512) * 5.0 / 1023.0 ) * 1000.0 / 66.0;
  
  Serial.print("milliAmps = ");
  Serial.println(milliAmps);
    
  delay(1000);

  // Arduino Board
  /*if (milliAmps <= 0 && milliAmps > -1) {*/
  // NodeMCU Board
  if (milliAmps >= 17 && milliAmps <= 18) {
    contPreparing++;
    contWaiting = 0;
  } else {
    contPreparing = 0;
    contWaiting++;
  }
  
  /*if (!onMake) {
    Serial.println("\n*** ESPERANDO... ***");
    statusCoffee = "1";
  }*/
  
  if (contWaiting == marginNumber && !onMake) {
    onMake = 1;
    Serial.println("\n*** PREPARANDO O CAFE ***");
    statusCoffee = "2";
  }
  
  if (contPreparing >= marginNumber && onMake) {
    Serial.println("\n*** CAFE PRONTO ***");
    onMake = 0;
    contWaiting = 0;
    statusCoffee = "3";
  }

  // Verificando mudança no status para enviar à plataforma
  if (oldStatusCoffee != statusCoffee) {
    oldStatusCoffee = statusCoffee;
    
    /*if (statusCoffee == "1") {
      Serial.println("\n*** STATUS 1: ESPERANDO... ***");
      Serial.println("*** DELAY 1 MINUTO ");
      delay(60000);
    }*/
    
    /* Enviando dados à plataforma */
    Serial.println("\n*** NOVO STATUS: " + statusCoffee);
    Serial.println("ENVIANDO DADOS PARA A PLATAFORMA IOT... ***");
    String array[numElements][2] =
    {
      { "statuscoffee", statusCoffee }
    };
    Serial.println(adapter.sendStream(array, numElements));

    if (statusCoffee == "3") {
      delay(30000);
      Serial.println("\n*** ESPERANDO... ***");
    }
  }
}
