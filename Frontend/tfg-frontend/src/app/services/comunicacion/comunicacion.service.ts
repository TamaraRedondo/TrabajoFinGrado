import { EventEmitter, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { PromptResponse } from '../../pregunta/promptResponse';
import { SeleccionService } from '../seleccion/seleccion.service';
import { UserService } from '../user/user.service';

@Injectable({
  providedIn: 'root'
})
export class ComunicacionService {
  isLoading = true;
  userId: number = 0;

  highlightedDaysChanged = new EventEmitter<Date[]>();
  emitHighlightedDays(highlightedDays: Date[]): void {
    console.log('Emitiendo fechas resaltadas:', highlightedDays);
    this.highlightedDaysChanged.emit(highlightedDays);
  }

  constructor(private seleccionService: SeleccionService,
    private userService: UserService) { }

  ejecutarMetodoEnRutina(userId: number) {
    console.log('Método enviarPrompt ejecutado desde ComunicacionService');
    this.generarPrompt(userId);
  }

  generarPrompt(userId: number): void {
    console.log('Enviando el prompt al backend...');
    const promptRequest = {
      enunciado: 'Send me a .json file with an exercise routine, taking into account the answers to these questions',
      clienteId: userId
    };
    this.seleccionService.generarPrompt(promptRequest).subscribe({
      next: (promptResponse: PromptResponse) => {
        console.log('Prompt generado correctamente por el backend:', promptResponse);
      },
      error: (error) => {
        console.error('Error al generar el prompt en el backend:', error);
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  ejecutarMetodoEnDetails(userId: number) {
    console.log('Método enviarPrompt ejecutado desde ComunicacionService');
    this.enviarPromptAlBackend(userId);
  }

  enviarPromptAlBackend(userId: number): void {
    console.log('Enviando el prompt al backend...');
    const promptRequest = {
      enunciado: 'Send me a .json file with a detailed analysis, taking into account the answers to these questions',
      clienteId: userId
    };
    this.seleccionService.generarPrompt2(promptRequest).subscribe({
      next: (promptResponse: any) => {
        console.log('Prompt generado correctamente por el backend:', promptResponse);
      },
      error: (error) => {
        console.error('Error al generar el prompt en el backend:', error);
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  private rutinaIdSource = new BehaviorSubject<number>(0);
  rutinaId$ = this.rutinaIdSource.asObservable();

  enviarRutinaId(rutinaId: number) {
    this.rutinaIdSource.next(rutinaId);
  }
}
