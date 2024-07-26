import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { RegisterRequest } from '../auth/registerRequest';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  currentUserRegisterOn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  currentUserData: BehaviorSubject<String> = new BehaviorSubject<String>("");

  constructor(private http: HttpClient) {
    this.currentUserRegisterOn = new BehaviorSubject<boolean>(sessionStorage.getItem("token") != null);
    this.currentUserData = new BehaviorSubject<String>(sessionStorage.getItem("token") || "");
  }

  register(credentials: RegisterRequest): Observable<any> {
    console.log('Realizando solicitud de registro:', credentials);
    return this.http.post<any>(environment.urlHost + "auth/register", credentials).pipe(
      tap((userData) => {
        console.log('Registro exitoso. Datos recibidos del servidor:', userData);
        sessionStorage.setItem("token", userData.token);
        this.currentUserData.next(userData.token);
        this.currentUserRegisterOn.next(true);
      }),
      catchError((error: HttpErrorResponse) => {
        let errorMessage: string;
        if (error.status === 502) {
          console.error('Error de nombre de usuario duplicado:', error.error);
          errorMessage = error.error.includes('Duplicate entry') ? 'El nombre de usuario ya está en uso. Por favor, elige otro.' : 'Error desconocido.';
        } else {
          console.error('Error desconocido:', error);
          errorMessage = 'Algo falló. Por favor, intente nuevamente.';
        }
        return of({ error: errorMessage });
      })
    );
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('Se ha producido un error ', error.error);
    } else {
      console.error('Backend retornó el código de estado ', error);
    }
    return throwError(() => new Error('Algo falló. Por favor intente nuevamente.'));
  }

  get userData(): Observable<String> {
    return this.currentUserData.asObservable();
  }

  get userRegisterOn(): Observable<boolean> {
    return this.currentUserRegisterOn.asObservable();
  }

  get userToken(): String {
    return this.currentUserData.value;
  }
}
