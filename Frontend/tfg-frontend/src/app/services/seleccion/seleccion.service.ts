import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, map, switchMap, throwError } from "rxjs";
import { Seleccion } from "../../pregunta/seleccion";
import { Rutina } from "../../rutina/rutina";
import { Ejercicio } from "../../pregunta/ejercicio";
import { Objetivo } from "../../objetivo/objetivo";

@Injectable({
  providedIn: 'root',
})
export class SeleccionService {
  private urlEndPoint: string = 'http://localhost:8080/api/seleccion/guardar-seleccion';
  constructor(private http: HttpClient) { }

  guardarSeleccion(seleccion: Seleccion): Observable<string> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>('http://localhost:8080/api/seleccion/guardar-seleccion', seleccion, { headers }).pipe(
      map(response => response.message), 
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 0) {
          return throwError(function () {
            return new Error('Error interno al procesar la solicitud');
          });
        } else {
          return throwError(function () {
            return new Error(error.message || 'Error interno al procesar la solicitud');
          });
        }
      })
    );
  }

  obtenerSelecciones(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/api/seleccion/listar-selecciones/${userId}`);
  }

  obtenerRutinaPorClienteId(clienteId: number): Observable<Rutina[]> {
    return this.http.get<Rutina[]>(`http://localhost:8080/rutina/${clienteId}`);
  }

  obtenerEjerciciosPorRutina(rutinaId: number): Observable<Ejercicio[]> {
    return this.http.get<Ejercicio[]>(`http://localhost:8080/ejercicio/${rutinaId}`);
  }

  obtenerEjerciciosPorCliente(userId: number): Observable<Ejercicio[]> {
    return this.http.get<Ejercicio[]>(`http://localhost:8080/ejercicios/${userId}`);
  }

  generarPrompt(promptRequest: any): Observable<any> {
    return this.http.post<any>(`http://localhost:8080/generate-prompt`, promptRequest);
  }

  obtenerIdDeRutinaPorUsuario(userId: number): Observable<number> {
    return this.http.get<number>(`http://localhost:8080/rutina-id/${userId}`);
  }

  getSiguienteRutinaId(clienteId: number, rutinaActualId: number): Observable<number> {
    return this.http.get<number>(`http://localhost:8080/rutina-id/${clienteId}/${rutinaActualId}`);
  }

  obtenerUltimaRutinaPorUsuario(userId: number): Observable<number> {
    return this.http.get<number>(`http://localhost:8080/ultrutina-id/${userId}`);
  }

  getEjercicioId(): Observable<number> {
    return this.http.get<number>('http://localhost:8080/api/v1/ejercicio/ejercicio-id');
  }

  actualizarEstadoEjercicio(ejercicioId: number, nuevoEstado: boolean): Observable<any> {
    return this.http.patch<any>(`http://localhost:8080/ejercicio/${ejercicioId}`, nuevoEstado);
  }

  checkRutinaCompletada(rutinaId: number): Observable<boolean> {
    return this.http.get<boolean>(`http://localhost:8080/rutina/${rutinaId}/completada`);
  }

  getOpcionesByPreguntaId(preguntaId: number): Observable<string[]> {
    return this.http.get<string[]>(`http://localhost:8080/api/preguntas/${preguntaId}/opciones`);
  }

  getOpcionSeleccionada(userId: number, preguntaId: number): Observable<string> {
    return this.http.get(`http://localhost:8080/api/seleccion/opcion-seleccionada/${userId}/${preguntaId}`, { responseType: 'text' });
  }

  updateSeleccion(cambioSeleccionRequest: any): Observable<any> {
    return this.http.put<any>(`http://localhost:8080/api/seleccion/updateSeleccion`, cambioSeleccionRequest);
  }

  obtenerSeleccionId(clienteId: number, preguntaId: number): Observable<number> {
    return this.http.get<number>(`http://localhost:8080/api/seleccion/listar-seleccion/${clienteId}/${preguntaId}`);
  }

  generarPrompt2(promptRequest: any): Observable<any> {
    return this.http.post<any>(`http://localhost:8080/generate-prompt2`, promptRequest);
  }

  generarPrompt3(promptRequest: any): Observable<any> {
    return this.http.post<any>(`http://localhost:8080/generate-prompt3`, promptRequest);
  }

  obtenerDescripcionDeAnalisisPorClienteId(clienteId: number): Observable<string[]> {
    return this.http.get<string[]>(`http://localhost:8080/analisis/${clienteId}`);
  }

  actualizarObjetivo(clienteId: number): Observable<any> {
    const url = `http://localhost:8080/actualizar-objetivo/${clienteId}`;
    return this.http.post<any>(url, {});
  }

  obtenerObjetivo(clienteId: number): Observable<Objetivo> {
    return this.http.get<Objetivo>(`http://localhost:8080/objetivo/${clienteId}`);
  }

  recuperarUltimaRutinaVisualizada(clienteId: number): Observable<Rutina> { //bien
    const url = `http://localhost:8080/ultima-visualizada/${clienteId}`;
    return this.http.get<Rutina>(url);
  }

  actualizarUltimaVisualizacion(rutinaId: number): Observable<void> { //bien
    const url = `http://localhost:8080/actualizar-ultima-visualizacion/${rutinaId}`;
    return this.http.put<void>(url, {});
  }

  todosEjerciciosCompletos(clienteId: number): Observable<boolean> { //bien
    return this.http.get<boolean>(`http://localhost:8080/todosCompletados/${clienteId}`);
  }

  obtenerRutinaRealizadaPorClienteId(clienteId: number): Observable<Date[]> {
    return this.http.get<Date[]>(`http://localhost:8080/rutina-fecha/${clienteId}`);
  }

  obtenerEjerciciosPorFecha(fecha: string): Observable<Ejercicio[]> {
    return this.http.get<Ejercicio[]>(`http://localhost:8080/buscar-por-fecha/${fecha}`);
  }

  obtenerNumeroRutinasNoRealizadas(clienteId: number): Observable<number> {
    return this.http.get<number>(`http://localhost:8080/contar-no-realizadas/${clienteId}`);
  }

  borrarAnalisis(clienteId: number): Observable<any> {
    return this.http.delete<any>(`http://localhost:8080/analisis/${clienteId}`);
  }
}
