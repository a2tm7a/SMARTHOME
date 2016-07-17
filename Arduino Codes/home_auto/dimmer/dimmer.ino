/*
 * dimmer_328p.cpp
 *
 * Created: 1/12/2016 1:21:24 AM
 * Author : shaurya
 */ 


#include <avr/io.h>
#include<avr/interrupt.h>
#define FOSC 16000000 // Clock Speed
#define BAUD 9600
#define MYUBRR FOSC/16/BAUD-1
void USART_Init( unsigned int ubrr);
unsigned char USART_Receive( void );
void USART_Transmit( unsigned char data );


int main(void)
{
    /* Replace with your application code */
  DDRD=0x02;//make rx as output
  DDRB|=(1<<4);//arduino pin12 out
  
  USART_Init(MYUBRR);
  sei();
  EICRA=(1<<ISC11) ;  //int1 falling edge
  EICRA|=(1<<ISC01)|(1<<ISC00); //int0 rising edge
  EIMSK =(1<<INT1)|(1<<INT0);
  
  int compare;
    while (1) 
    {
    
    char ab=USART_Receive();
    UCSR0A|=(1<<RXC0);//reset the flag
    switch(ab)
    {
      case'0'://off
      PORTB&=(~(1<<4));
      USART_Transmit('0');
      break;
      
      case'1':
      PORTB&=(~(1<<4));
      compare=8/(1024/FOSC)/1000;
      OCR0A=(unsigned char)compare;
      USART_Transmit('1');
      break;
      
      case'2':
      PORTB&=(~(1<<4));
      compare=6/(1024/FOSC)/1000;
      OCR0A=(unsigned char)compare;
      USART_Transmit('2');
      break;
      
      case'3':
      PORTB&=(~(1<<4));
      compare=4/(1024/FOSC)/1000;
      OCR0A=(unsigned char)compare;
      USART_Transmit('3');
      PORTB&=(~(1<<4));
      break;
      
      case'4':
      PORTB&=(~(1<<4));
      compare=2/(1024/FOSC)/1000;
      OCR0A=(unsigned char)compare;
      USART_Transmit('4');
      break;
      
      case'5'://highest speed
      USART_Transmit('5');
      PORTB|=(1<<4);
      break;
    }
    
    }
}

ISR(INT0_vect)
{
  PORTB&=(~(1<<4));
  TCNT0=0;
 
  
}
ISR(INT1_vect)
{
  PORTB&=(~(1<<4));
  TCNT0=0;
  
}
ISR(TIMER0_COMPA_vect)
{
  PORTB|=(1<<4);
}

    



void USART_Init( unsigned int ubrr)
{
  /*Set baud rate */
  UBRR0H = (unsigned char)(ubrr>>8);
  UBRR0L = (unsigned char)ubrr;
  /*Enable receiver and transmitter */
  UCSR0B = (1<<RXEN0)|(1<<TXEN0)|(1<<RXCIE0);
  /* Set frame format: 8data, 2stop bit */
  UCSR0C = (1<<USBS0)|(3<<UCSZ00);
}
unsigned char USART_Receive( void )
{
  /* Wait for data to be received */
  while ( !(UCSR0A & (1<<RXC0)) )
  ;
  /* Get and return received data from buffer */
  return UDR0;
}
void USART_Transmit( unsigned char data )
{
  /* Wait for empty transmit buffer */
  while ( !( UCSR0A & (1<<UDRE0)) )
  ;
  /* Put data into buffer, sends the data */
  UDR0 = data;
}

