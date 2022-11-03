#include <SoftwareSerial.h>
SoftwareSerial mySerial(0, 1); //RX, TX

String Data = ""; //initialise a string to hold my message

void setup(){
  Serial.begin(9600);
  mySerial.begin(9600);
}
  
  
  void loop(){
  while(mySerial.available()){
     char character = mySerial.read(); // Receive a single character from the software serial port
        Data.concat(character); // Add the received character to the receive buffer
        if (character == '#')//If end of message character recieved, move on.
        {
        
         Serial.print(Data); //Output the message
           Data =""; //clear the buffer/message
           Serial.println();
           delay(1000);
      
  }
  
}
}