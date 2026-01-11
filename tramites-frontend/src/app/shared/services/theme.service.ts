import { Injectable, signal, effect } from '@angular/core';

/**
 * Servicio para gestionar el tema de la aplicación (claro/oscuro)
 * Persiste la preferencia del usuario en localStorage
 */
@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  /** Signal que indica si el modo oscuro está activo */
  readonly darkMode = signal<boolean>(this.getInitialTheme());

  constructor() {
    // Efecto para aplicar el tema cuando cambia
    effect(() => {
      this.applyTheme(this.darkMode());
    });
  }

  /**
   * Obtiene el tema inicial basado en localStorage o preferencia del sistema
   */
  private getInitialTheme(): boolean {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
      return savedTheme === 'dark';
    }
    // Usar preferencia del sistema si no hay tema guardado
    return window.matchMedia('(prefers-color-scheme: dark)').matches;
  }

  /**
   * Aplica el tema al documento
   */
  private applyTheme(isDark: boolean): void {
    const html = document.documentElement;
    const body = document.body;
    
    if (isDark) {
      html.classList.add('dark-theme');
      body.classList.add('dark-theme');
      localStorage.setItem('theme', 'dark');
    } else {
      html.classList.remove('dark-theme');
      body.classList.remove('dark-theme');
      localStorage.setItem('theme', 'light');
    }
  }

  /**
   * Alterna entre modo claro y oscuro
   */
  toggleTheme(): void {
    this.darkMode.set(!this.darkMode());
  }

  /**
   * Establece el tema explícitamente
   */
  setTheme(isDark: boolean): void {
    this.darkMode.set(isDark);
  }
}
