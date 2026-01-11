import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { DividerModule } from 'primeng/divider';
import { CiudadanoService, TramiteService, TipoTramiteService, TipoDocumentoService } from '../../core/services';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    CardModule,
    ButtonModule,
    RippleModule,
    DividerModule
  ],
  template: `
    <div class="grid">
      <!-- Header de bienvenida -->
      <div class="col-12">
        <div class="surface-card p-4 shadow-2 border-round">
          <div class="flex flex-column md:flex-row md:align-items-center md:justify-content-between">
            <div>
              <div class="text-3xl font-bold text-color mb-2">
                Bienvenido al Sistema de Trámites
              </div>
              <div class="text-color-secondary">
                Ministerio de Educación - Gobierno Digital
              </div>
            </div>
            <div class="mt-3 md:mt-0">
              <p-button 
                label="Nuevo Trámite" 
                icon="pi pi-plus" 
                routerLink="/tramites"
                styleClass="mr-2">
              </p-button>
              <p-button 
                label="Ver Ciudadanos" 
                icon="pi pi-users" 
                routerLink="/ciudadanos"
                [outlined]="true">
              </p-button>
            </div>
          </div>
        </div>
      </div>

      <!-- Estadísticas -->
      <div class="col-12 md:col-6 lg:col-3">
        <div class="surface-card shadow-2 p-4 border-round">
          <div class="flex justify-content-between mb-3">
            <div>
              <span class="block text-color-secondary font-medium mb-3">Ciudadanos</span>
              <div class="text-color text-3xl font-bold">{{ stats.ciudadanos }}</div>
            </div>
            <div class="flex align-items-center justify-content-center bg-blue-100 border-round" style="width: 2.5rem; height: 2.5rem">
              <i class="pi pi-users text-blue-500 text-xl"></i>
            </div>
          </div>
          <span class="text-green-500 font-medium">Registrados </span>
          <span class="text-color-secondary">en el sistema</span>
        </div>
      </div>

      <div class="col-12 md:col-6 lg:col-3">
        <div class="surface-card shadow-2 p-4 border-round">
          <div class="flex justify-content-between mb-3">
            <div>
              <span class="block text-color-secondary font-medium mb-3">Trámites Pendientes</span>
              <div class="text-color text-3xl font-bold">{{ stats.tramites }}</div>
            </div>
            <div class="flex align-items-center justify-content-center bg-orange-100 border-round" style="width: 2.5rem; height: 2.5rem">
              <i class="pi pi-file text-orange-500 text-xl"></i>
            </div>
          </div>
          <span class="text-orange-500 font-medium">En proceso </span>
          <span class="text-color-secondary">esperando resolución</span>
        </div>
      </div>

      <div class="col-12 md:col-6 lg:col-3">
        <div class="surface-card shadow-2 p-4 border-round">
          <div class="flex justify-content-between mb-3">
            <div>
              <span class="block text-color-secondary font-medium mb-3">Tipos Trámite</span>
              <div class="text-color text-3xl font-bold">{{ stats.tiposTramite }}</div>
            </div>
            <div class="flex align-items-center justify-content-center bg-cyan-100 border-round" style="width: 2.5rem; height: 2.5rem">
              <i class="pi pi-list text-cyan-500 text-xl"></i>
            </div>
          </div>
          <span class="text-cyan-500 font-medium">Configurados </span>
          <span class="text-color-secondary">disponibles</span>
        </div>
      </div>

      <div class="col-12 md:col-6 lg:col-3">
        <div class="surface-card shadow-2 p-4 border-round">
          <div class="flex justify-content-between mb-3">
            <div>
              <span class="block text-color-secondary font-medium mb-3">Tipos Documento</span>
              <div class="text-color text-3xl font-bold">{{ stats.tiposDocumento }}</div>
            </div>
            <div class="flex align-items-center justify-content-center bg-purple-100 border-round" style="width: 2.5rem; height: 2.5rem">
              <i class="pi pi-id-card text-purple-500 text-xl"></i>
            </div>
          </div>
          <span class="text-purple-500 font-medium">Tipos </span>
          <span class="text-color-secondary">aceptados</span>
        </div>
      </div>

      <!-- Cards de navegación -->
      <div class="col-12">
        <p-divider align="left">
          <span class="text-color-secondary font-semibold">Módulos del Sistema</span>
        </p-divider>
      </div>

      <div class="col-12 md:col-6 xl:col-3">
        <p-card styleClass="h-full shadow-2 hover:shadow-4 transition-all transition-duration-300">
          <ng-template pTemplate="header">
            <div class="flex align-items-center justify-content-center bg-blue-500 p-4">
              <i class="pi pi-users text-white text-5xl"></i>
            </div>
          </ng-template>
          <ng-template pTemplate="title">
            Gestión de Ciudadanos
          </ng-template>
          <ng-template pTemplate="subtitle">
            Administre la información de ciudadanos
          </ng-template>
          <ng-template pTemplate="content">
            <p class="text-color-secondary line-height-3">
              Registre nuevos ciudadanos, actualice su información y gestione sus datos de contacto.
            </p>
          </ng-template>
          <ng-template pTemplate="footer">
            <p-button 
              label="Ir a Ciudadanos" 
              icon="pi pi-arrow-right" 
              iconPos="right"
              routerLink="/ciudadanos"
              styleClass="w-full">
            </p-button>
          </ng-template>
        </p-card>
      </div>

      <div class="col-12 md:col-6 xl:col-3">
        <p-card styleClass="h-full shadow-2 hover:shadow-4 transition-all transition-duration-300">
          <ng-template pTemplate="header">
            <div class="flex align-items-center justify-content-center bg-orange-500 p-4">
              <i class="pi pi-file-edit text-white text-5xl"></i>
            </div>
          </ng-template>
          <ng-template pTemplate="title">
            Gestión de Trámites
          </ng-template>
          <ng-template pTemplate="subtitle">
            Administre trámites y solicitudes
          </ng-template>
          <ng-template pTemplate="content">
            <p class="text-color-secondary line-height-3">
              Cree nuevos trámites, actualice estados y dé seguimiento a las solicitudes ciudadanas.
            </p>
          </ng-template>
          <ng-template pTemplate="footer">
            <p-button 
              label="Ir a Trámites" 
              icon="pi pi-arrow-right" 
              iconPos="right"
              routerLink="/tramites"
              severity="warning"
              styleClass="w-full">
            </p-button>
          </ng-template>
        </p-card>
      </div>

      <div class="col-12 md:col-6 xl:col-3">
        <p-card styleClass="h-full shadow-2 hover:shadow-4 transition-all transition-duration-300">
          <ng-template pTemplate="header">
            <div class="flex align-items-center justify-content-center bg-cyan-500 p-4">
              <i class="pi pi-list text-white text-5xl"></i>
            </div>
          </ng-template>
          <ng-template pTemplate="title">
            Tipos de Trámite
          </ng-template>
          <ng-template pTemplate="subtitle">
            Catálogo de tipos de trámite
          </ng-template>
          <ng-template pTemplate="content">
            <p class="text-color-secondary line-height-3">
              Configure los diferentes tipos de trámites disponibles en el sistema gubernamental.
            </p>
          </ng-template>
          <ng-template pTemplate="footer">
            <p-button 
              label="Ver Catálogo" 
              icon="pi pi-arrow-right" 
              iconPos="right"
              routerLink="/tipos-tramite"
              severity="info"
              styleClass="w-full">
            </p-button>
          </ng-template>
        </p-card>
      </div>

      <div class="col-12 md:col-6 xl:col-3">
        <p-card styleClass="h-full shadow-2 hover:shadow-4 transition-all transition-duration-300">
          <ng-template pTemplate="header">
            <div class="flex align-items-center justify-content-center bg-purple-500 p-4">
              <i class="pi pi-id-card text-white text-5xl"></i>
            </div>
          </ng-template>
          <ng-template pTemplate="title">
            Tipos de Documento
          </ng-template>
          <ng-template pTemplate="subtitle">
            Catálogo de documentos de identidad
          </ng-template>
          <ng-template pTemplate="content">
            <p class="text-color-secondary line-height-3">
              Administre los tipos de documentos de identificación aceptados por el sistema.
            </p>
          </ng-template>
          <ng-template pTemplate="footer">
            <p-button 
              label="Ver Catálogo" 
              icon="pi pi-arrow-right" 
              iconPos="right"
              routerLink="/tipos-documento"
              severity="help"
              styleClass="w-full">
            </p-button>
          </ng-template>
        </p-card>
      </div>
    </div>
  `
})
export class HomeComponent implements OnInit {
  stats = {
    ciudadanos: 0,
    tramites: 0,
    tiposTramite: 0,
    tiposDocumento: 0
  };

  constructor(
    private ciudadanoService: CiudadanoService,
    private tramiteService: TramiteService,
    private tipoTramiteService: TipoTramiteService,
    private tipoDocumentoService: TipoDocumentoService
  ) {}

  ngOnInit() {
    this.loadStats();
  }

  loadStats() {
    forkJoin({
      ciudadanos: this.ciudadanoService.getAll(),
      tramitesPendientes: this.tramiteService.countPendientes(),
      tiposTramite: this.tipoTramiteService.getAll(),
      tiposDocumento: this.tipoDocumentoService.getAll()
    }).subscribe({
      next: (data) => {
        this.stats = {
          ciudadanos: data.ciudadanos.length,
          tramites: data.tramitesPendientes,
          tiposTramite: data.tiposTramite.length,
          tiposDocumento: data.tiposDocumento.length
        };
      },
      error: (err) => {
        console.error('Error loading stats:', err);
      }
    });
  }
}
