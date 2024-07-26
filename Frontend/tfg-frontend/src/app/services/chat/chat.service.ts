import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Message } from '../../chat/message';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = 'http://localhost:8080/api/chat';
  
  constructor(private http: HttpClient) {}

    sendInitialMessage(contextMessage: string, initialMessage: string): Observable<Message> {
      const payload = {
        model: 'llama3',
        messages: [
          {
            role: 'system',
            content: contextMessage
          },
          {
            role: 'assistant',
            content: initialMessage
          },
          {
            role: 'user',
            content: 'Hello AI'
          }
        ]
      };
      console.log('Mensaje inicial completo:', payload);
      return this.http.post<Message>(`${this.apiUrl}`, payload);
    }

  sendMessage(messages: Message[]): Observable<Message> {
    const payload = {
      model: 'llama3',
      messages: messages
    };
    return this.http.post<Message>(this.apiUrl, payload);
  }
}