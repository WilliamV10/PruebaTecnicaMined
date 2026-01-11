import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Ciudadano, CiudadanoCreate, CiudadanoUpdate, ApiResponse } from '../models';

/**
 * Servicio para gestionar los ciudadanos
 * Centraliza todas las operaciones CRUD con la API
 */
@Injectable({
  providedIn: 'root'
})
export class CiudadanoService {

  private readonly apiUrl = `${environment.apiUrl}/ciudadanos`;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todos los ciudadanos activos
   * @returns Observable con lista de ciudadanos
   */
  getAll(): Observable<Ciudadano[]> {
    return this.http.get<Ciudadano[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  /**
   * Obtiene un ciudadano por su ID
   * @param id UUID del ciudadano
   * @returns Observable con el ciudadano
   */
  getById(id: string): Observable<Ciudadano> {
    return this.http.get<Ciudadano>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Registra un nuevo ciudadano
   * @param ciudadano Datos del ciudadano a registrar
   * @returns Observable con el ciudadano creado
   */
  create(ciudadano: CiudadanoCreate): Observable<Ciudadano> {
    return this.http.post<ApiResponse<Ciudadano>>(this.apiUrl, ciudadano)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Actualiza un ciudadano existente
   * @param id UUID del ciudadano
   * @param ciudadano Datos actualizados
   * @returns Observable con el ciudadano actualizado
   */
  update(id: string, ciudadano: CiudadanoUpdate): Observable<Ciudadano> {
    return this.http.put<ApiResponse<Ciudadano>>(`${this.apiUrl}/${id}`, ciudadano)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Elimina (soft delete) un ciudadano
   * @param id UUID del ciudadano
   * @returns Observable void
   */
  delete(id: string): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`)
      .pipe(
        map(() => void 0),
        catchError(this.handleError)
      );
  }

  /**
   * Maneja los errores de las peticiones HTTP
   * @param error Error HTTP recibido
   * @returns Observable con el error procesado
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ha ocurrido un error inesperado';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      if (error.error?.message) {
        errorMessage = error.error.message;
      } else if (error.status === 0) {
        errorMessage = 'No se pudo conectar con el servidor. Verifique su conexión.';
      } else if (error.status === 404) {
        errorMessage = 'Ciudadano no encontrado';
      } else if (error.status === 400) {
        errorMessage = error.error?.message || 'Datos inválidos';
      }
    }

    console.error('Error en CiudadanoService:', error);
    return throwError(() => ({ message: errorMessage, status: error.status, error: error.error }));
  }
}
