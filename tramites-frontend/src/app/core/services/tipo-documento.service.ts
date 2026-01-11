import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { TipoDocumento, TipoDocumentoCreate, ApiResponse } from '../models';

/**
 * Servicio para gestionar los tipos de documento
 * Centraliza todas las operaciones CRUD con la API
 */
@Injectable({
  providedIn: 'root'
})
export class TipoDocumentoService {

  private readonly apiUrl = `${environment.apiUrl}/tipos-documento`;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todos los tipos de documento activos
   * @returns Observable con lista de tipos de documento
   */
  getAll(): Observable<TipoDocumento[]> {
    return this.http.get<TipoDocumento[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  /**
   * Obtiene un tipo de documento por su ID
   * @param id UUID del tipo de documento
   * @returns Observable con el tipo de documento
   */
  getById(id: string): Observable<TipoDocumento> {
    return this.http.get<TipoDocumento>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Obtiene un tipo de documento por su nombre
   * @param nombre Nombre del tipo de documento (case-insensitive)
   * @returns Observable con el tipo de documento
   */
  getByNombre(nombre: string): Observable<TipoDocumento> {
    return this.http.get<TipoDocumento>(`${this.apiUrl}/nombre/${nombre}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Crea un nuevo tipo de documento
   * @param tipoDocumento Datos del tipo de documento a crear
   * @returns Observable con el tipo de documento creado
   */
  create(tipoDocumento: TipoDocumentoCreate): Observable<TipoDocumento> {
    return this.http.post<ApiResponse<TipoDocumento>>(this.apiUrl, tipoDocumento)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Actualiza un tipo de documento existente
   * @param id UUID del tipo de documento
   * @param tipoDocumento Datos actualizados
   * @returns Observable con el tipo de documento actualizado
   */
  update(id: string, tipoDocumento: TipoDocumentoCreate): Observable<TipoDocumento> {
    return this.http.put<ApiResponse<TipoDocumento>>(`${this.apiUrl}/${id}`, tipoDocumento)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Elimina (soft delete) un tipo de documento
   * @param id UUID del tipo de documento
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
   * Activa un tipo de documento
   * @param id UUID del tipo de documento
   * @returns Observable con el tipo de documento activado
   */
  activate(id: string): Observable<TipoDocumento> {
    return this.http.patch<ApiResponse<TipoDocumento>>(`${this.apiUrl}/${id}/activate`, null)
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
      // Error del lado del cliente
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del lado del servidor
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

    console.error('Error en TipoDocumentoService:', error);
    return throwError(() => ({ message: errorMessage, status: error.status, error: error.error }));
  }
}
