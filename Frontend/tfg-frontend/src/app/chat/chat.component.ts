import { Component, Input, OnInit } from '@angular/core';
import { Message } from './message';
import { ChatService } from '../services/chat/chat.service';
import { ComunicacionService } from '../services/comunicacion/comunicacion.service';
import { SeleccionService } from '../services/seleccion/seleccion.service';
import { Ejercicio } from '../pregunta/ejercicio';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  messages: Message[] = [];
  newMessage = '';
  chatVisible = false;
  response: Message | null = null;
  initialMessage = ''; 
  contextmessage = "You are a qualified personal trainer capable of answering questions about this exercise routine and related sports aspects. ";
  
  initialMessageReceived = false;
  rutinaId: number | undefined;

  constructor(public chatService: ChatService, public comunicacionService: ComunicacionService, public seleccionService: SeleccionService) {}

  ngOnInit(): void {
    this.comunicacionService.rutinaId$.subscribe({
      next: (id) => {
        console.log('ID de rutina recibido:', id);
        if (id && id !== 0) { 
          this.rutinaId = id;
          this.seleccionService.obtenerEjerciciosPorRutina(id).subscribe({
            next: (ejercicios) => {
              console.log('Exercise routine can ask me about:', ejercicios);
              this.initialMessage = this.createInitialMessage(ejercicios)
              this.sendInitialMessage();
            },
            error: (error) => {
              console.error('Error al obtener ejercicios:', error);
            }
          });
        }
      },
      error: (error) => {
        console.error('Error al recibir el ID de rutina:', error);
      }
    });
  }  

  toggleChat(): void {
    this.chatVisible = !this.chatVisible;
  }

  getMessages() {
    return this.messages;
  }

  addMessage(message: Message) {
    this.messages.push(message);
    console.log(message);
  }

createInitialMessage(ejercicios: Ejercicio[]): string {
  let message = "";
  if (ejercicios.length > 0) {
    message += "This is the list of exercises you can be asked about:\n";
    ejercicios.forEach((ejercicio, index) => {
      message += `${index + 1}. ${ejercicio.nombre}: ${ejercicio.datos}\n`;
    });
  } else {
    message += "There are currently no exercises available.\n";
  }

  return message;
}

sendInitialMessage(): void {
  const systemMessage: Message = { role: 'assistant', content: this.initialMessage};
    this.addMessage(systemMessage);
  this.chatService.sendInitialMessage(this.contextmessage,this.initialMessage).subscribe({
    next: (response) => {
      console.log('Initial response from AI:', response);
      if (response && response.content && response.role) {
        this.response = { content: response.content, role: response.role };
        this.addMessage({ content: response.content, role: 'assistant' });
        this.initialMessageReceived = true;
      } else {
        console.error('Unexpected server response structure:', response);
      }
    },
    error: (error) => {
      console.error('Error in initial AI response:', error);
    }
  });
}

  sendMessage(): void {
    if (this.newMessage.trim() === '') {
      return;
    }

    console.log('Mensaje enviado por el usuario:', this.newMessage);

    const userMessage: Message = { role: 'user', content: this.newMessage };
    this.addMessage(userMessage);

    this.chatService.sendMessage(this.messages).subscribe({
      next: (response) => {
        console.log('Respuesta de la IA:', response);
        if (response && response.content && response.role) {
          this.response = { content: response.content, role: response.role };
          this.addMessage({ content: response.content, role: 'assistant' });
        } else {
          console.error('La respuesta del servidor no tiene la estructura esperada:', response);
        }
      },
      error: (error) => {
        console.error('Error en la respuesta de la IA:', error);
      }
    });
    
    this.newMessage = '';
  }
}