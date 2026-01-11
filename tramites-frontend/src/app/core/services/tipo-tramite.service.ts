import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { TipoTramite, TipoTramiteCreate, ApiResponse } from '../models';

/**
 * Servicio para gestionar los tipos de trámite
 * Centraliza todas las operaciones CRUD con la API
 */
@Injectable({
  providedIn: 'root'
})
export class TipoTramiteService {

  private readonly apiUrl = `${environment.apiUrl}/tipos-tramite`;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todos los tipos de trámite activos
   * @returns Observable con lista de tipos de trámite
   */
  getAll(): Observable<TipoTramite[]> {
    return this.http.get<TipoTramite[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  /**
   * Obtiene un tipo de trámite por su ID
   * @param id UUID del tipo de trámite
   * @returns Observable con el tipo de trámite
   */
  getById(id: string): Observable<TipoTramite> {
    return this.http.get<TipoTramite>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Crea un nuevo tipo de trámite
   * @param tipoTramite Datos del tipo de trámite a crear
   * @returns Observable con el tipo de trámite creado
   */
  create(tipoTramite: TipoTramiteCreate): Observable<TipoTramite> {
    return this.http.post<ApiResponse<TipoTramite>>(this.apiUrl, tipoTramite)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Actualiza un tipo de trámite existente
   * @param id UUID del tipo de trámite
   * @param tipoTramite Datos actualizados
   * @returns Observable con el tipo de trámite actualizado
   */
  update(id: string, tipoTramite: TipoTramiteCreate): Observable<TipoTramite> {
    return this.http.put<ApiResponse<TipoTramite>>(`${this.apiUrl}/${id}`, tipoTramite)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Elimina (soft delete) un tipo de trámite
   * @param id UUID del tipo de trámite
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
   * Activa un tipo de trámite
   * @param id UUID del tipo de trámite
   * @returns Observable con el tipo de trámite activado
   */
  activate(id: string): Observable<TipoTramite> {
    return this.http.patch<ApiResponse<TipoTramite>>(`${this.apiUrl}/${id}/activate`, null)
      .pipe(
        map(response => response.data),
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
        errorMessage = 'Recurso no encontrado';
      } else if (error.status === 400) {
        errorMessage = error.error?.message || 'Datos inválidos';
      }
    }

    console.error('Error en TipoTramiteService:', error);
    return throwError(() => ({ message: errorMessage, status: error.status, error: error.error }));
  }
}
