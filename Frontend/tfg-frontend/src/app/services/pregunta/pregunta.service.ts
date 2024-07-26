import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Pregunta } from "../../pregunta/pregunta";

@Injectable({
  providedIn: 'root',
})
export class PreguntaService {
  private urlEndPoint: string = 'http://localhost:8080/api/v1/preguntas';
  constructor(private http: HttpClient) { }

  getPreguntas(): Observable<Pregunta[]> {
    return this.http.get<Pregunta[]>('http://localhost:8080/api/v1/preguntas/listar');
  }

  getPreguntaById(id: number): Observable<Pregunta> {
    const url = `${this.urlEndPoint}/${id}`;
    return this.http.get<Pregunta>(url);
  }

  getPreguntaTexto(id: number): Observable<Pregunta> {
    return this.http.get<Pregunta>(`http://localhost:8080/api/v1/preguntas/pregunta/${id}`);
  }
}
