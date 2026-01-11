import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Tramite, TramiteCreate, TramiteEstadoUpdate, ApiResponse } from '../models';

/**
 * Servicio para gestionar los trámites
 * Centraliza todas las operaciones CRUD con la API
 */
@Injectable({
  providedIn: 'root'
})
export class TramiteService {

  private readonly apiUrl = `${environment.apiUrl}/tramites`;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todos los trámites activos
   * @returns Observable con lista de trámites
   */
  getAll(): Observable<Tramite[]> {
    return this.http.get<Tramite[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  /**
   * Obtiene un trámite por su ID
   * @param id UUID del trámite
   * @returns Observable con el trámite
   */
  getById(id: string): Observable<Tramite> {
    return this.http.get<Tramite>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Obtiene todos los trámites de un ciudadano
   * @param ciudadanoId UUID del ciudadano
   * @returns Observable con lista de trámites del ciudadano
   */
  getByCiudadano(ciudadanoId: string): Observable<Tramite[]> {
    return this.http.get<Tramite[]>(`${this.apiUrl}/ciudadano/${ciudadanoId}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Crea un nuevo trámite
   * @param tramite Datos del trámite a crear
   * @returns Observable con el trámite creado
   */
  create(tramite: TramiteCreate): Observable<Tramite> {
    return this.http.post<ApiResponse<Tramite>>(this.apiUrl, tramite)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Actualiza el estado de un trámite
   * @param id UUID del trámite
   * @param estadoUpdate Nuevo estado y observación
   * @returns Observable con el trámite actualizado
   */
  updateEstado(id: string, estadoUpdate: TramiteEstadoUpdate): Observable<Tramite> {
    return this.http.patch<ApiResponse<Tramite>>(`${this.apiUrl}/${id}/estado`, estadoUpdate)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Aprueba un trámite
   * @param id UUID del trámite
   * @param observacion Observación opcional sobre la aprobación
   * @returns Observable con el trámite aprobado
   */
  aprobar(id: string, observacion?: string): Observable<Tramite> {
    let params = new HttpParams();
    if (observacion) {
      params = params.set('observacion', observacion);
    }
    return this.http.patch<ApiResponse<Tramite>>(`${this.apiUrl}/${id}/aprobar`, null, { params })
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Rechaza un trámite
   * @param id UUID del trámite
   * @param observacion Observación opcional sobre el rechazo
   * @returns Observable con el trámite rechazado
   */
  rechazar(id: string, observacion?: string): Observable<Tramite> {
    let params = new HttpParams();
    if (observacion) {
      params = params.set('observacion', observacion);
    }
    return this.http.patch<ApiResponse<Tramite>>(`${this.apiUrl}/${id}/rechazar`, null, { params })
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  /**
   * Elimina (soft delete) un trámite
   * @param id UUID del trámite
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
   * Cuenta los trámites pendientes activos
   * @returns Observable con el número de trámites pendientes
   */
  countPendientes(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count/pendientes`)
      .pipe(catchError(this.handleError));
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
        errorMessage = 'Trámite no encontrado';
      } else if (error.status === 400) {
        errorMessage = error.error?.message || 'Datos inválidos o acción no permitida';
      }
    }

    console.error('Error en TramiteService:', error);
    return throwError(() => ({ message: errorMessage, status: error.status, error: error.error }));
  }
}
