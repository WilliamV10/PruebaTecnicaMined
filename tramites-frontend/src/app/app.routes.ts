import { Routes } from '@angular/router';
import { LayoutComponent } from './shared/components/layout/layout.component';

/**
 * Configuración de rutas de la aplicación
 * Implementa lazy loading para optimizar la carga inicial
 * Usa layout con sidebar como contenedor principal
 */
export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        loadComponent: () => import('./features/home/home.component')
          .then(m => m.HomeComponent),
        title: 'Inicio - Sistema de Trámites'
      },
      {
        path: 'ciudadanos',
        loadComponent: () => import('./features/ciudadanos/ciudadano-list/ciudadano-list.component')
          .then(m => m.CiudadanoListComponent),
        title: 'Ciudadanos - Sistema de Trámites'
      },
      {
        path: 'tramites',
        loadComponent: () => import('./features/tramites/tramite-list/tramite-list.component')
          .then(m => m.TramiteListComponent),
        title: 'Trámites - Sistema de Trámites'
      },
      {
        path: 'tipos-tramite',
        loadComponent: () => import('./features/catalogos/tipo-tramite-list/tipo-tramite-list.component')
          .then(m => m.TipoTramiteListComponent),
        title: 'Tipos de Trámite - Sistema de Trámites'
      },
      {
        path: 'tipos-documento',
        loadComponent: () => import('./features/catalogos/tipo-documento-list/tipo-documento-list.component')
          .then(m => m.TipoDocumentoListComponent),
        title: 'Tipos de Documento - Sistema de Trámites'
      }
    ]
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full'
  }
];
