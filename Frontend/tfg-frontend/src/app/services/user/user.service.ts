import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, retry, tap, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userIdSubject = new BehaviorSubject<number | null>(null);
  userId$ = this.userIdSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadUserId();
  }

  private loadUserId(): void {
    const token = localStorage.getItem('authToken'); 
    if (token) {
      this.getUserId().subscribe({
        next: (id) => this.userIdSubject.next(id),
        error: (error) => console.error('Failed to load user ID', error)
      });
    }
  }

  updateUserInfo(token: string, userData: any): Observable<any> {
    return this.http.put<any>('http://localhost:8080/api/v1/user/updateUser', userData, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(catchError(this.handleError));
  }

  getUserInfo(token: string): Observable<any> {
    return this.http.get<any>('http://localhost:8080/api/v1/user/user-info', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(catchError(this.handleError));
  }

  getUserId(): Observable<number> {
    return this.http.get<number>('http://localhost:8080/api/v1/user/user-id');
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('Se ha producido un error', error.error);
    } else {
      console.error('Backend retornó el código de estado', error.status, error.error);
    }
    return throwError(() => new Error('Algo falló. Por favor intente nuevamente.'));
  }

  getCurrentUserId(): number | null {
    return this.userIdSubject.getValue();
  }
}

/*export class UserService {
  private currentUserData = new BehaviorSubject<any>(null);
  currentUserData$ = this.currentUserData.asObservable();

  constructor(private http: HttpClient) { }

  updateUserInfo(token: string, userData: any): Observable<any> {
    return this.http.put<any>('http://localhost:8080/api/v1/user/updateUser', userData, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  getUserInfo(token: string): Observable<any> {
    return this.http.get<any>('http://localhost:8080/api/v1/user/user-info', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  getUserId(): Observable<number> {
    return this.http.get<number>('http://localhost:8080/api/v1/user/user-id');
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('Se ha producio un error ', error.error);
    }
    else {
      console.error('Backend retornó el código de estado ', error.status, error.error);
    }
    return throwError(() => new Error('Algo falló. Por favor intente nuevamente.'));
  }
}*/
