import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { ToolbarModule } from 'primeng/toolbar';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { RippleModule } from 'primeng/ripple';
import { DropdownModule } from 'primeng/dropdown';
import { MessageService, ConfirmationService } from 'primeng/api';
import { TramiteService, CiudadanoService, TipoTramiteService } from '../../../core/services';
import { Tramite, Ciudadano, TipoTramite, EstadoTramite } from '../../../core/models';

@Component({
  selector: 'app-tramite-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    InputTextareaModule,
    ToastModule,
    ConfirmDialogModule,
    DialogModule,
    ToolbarModule,
    TagModule,
    TooltipModule,
    RippleModule,
    DropdownModule
  ],
  providers: [MessageService, ConfirmationService],
  template: `
    <p-toast></p-toast>
    <p-confirmDialog></p-confirmDialog>

    <div class="surface-card p-4 shadow-2 border-round">
      <!-- Toolbar -->
      <p-toolbar styleClass="mb-4 gap-2">
        <ng-template pTemplate="left">
          <div class="flex align-items-center gap-2">
            <i class="pi pi-file-edit text-primary text-2xl"></i>
            <div>
              <div class="text-xl font-bold text-color">Gestión de Trámites</div>
              <div class="text-color-secondary text-sm">Administre las solicitudes de trámites</div>
            </div>
          </div>
        </ng-template>
        <ng-template pTemplate="right">
          <p-button 
            label="Nuevo Trámite" 
            icon="pi pi-plus" 
            severity="success" 
            (onClick)="openNew()">
          </p-button>
        </ng-template>
      </p-toolbar>

      <!-- Tabla -->
      <p-table 
        #dt
        [value]="tramites"
        [rows]="10"
        [paginator]="true"
        [rowsPerPageOptions]="[5, 10, 25, 50]"
        [loading]="loading"
        [globalFilterFields]="['ciudadano.nombre', 'tipoTramite.nombre', 'estado']"
        [tableStyle]="{'min-width': '55rem'}"
        responsiveLayout="scroll"
        [scrollable]="true"
        dataKey="id"
        currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} trámites"
        [showCurrentPageReport]="true">

        <ng-template pTemplate="caption">
          <div class="flex flex-column md:flex-row md:justify-content-between md:align-items-center gap-3">
            <span class="p-input-icon-left">
              <i class="pi pi-search"></i>
              <input 
                pInputText 
                type="text"
                [(ngModel)]="searchTerm"
                (input)="dt.filterGlobal($any($event.target).value, 'contains')" 
                placeholder="Buscar trámite..." 
                style="width: 300px" />
            </span>
            <div class="flex gap-2">
              <p-dropdown
                [options]="estadoOptions"
                [(ngModel)]="selectedEstadoFilter"
                placeholder="Filtrar por estado"
                [showClear]="true"
                (onChange)="filterByEstado($event.value)"
                optionLabel="label"
                optionValue="value">
              </p-dropdown>
              <p-button 
                label="Limpiar" 
                icon="pi pi-filter-slash" 
                [outlined]="true"
                severity="secondary"
                (onClick)="clearFilters()">
              </p-button>
            </div>
          </div>
        </ng-template>

        <ng-template pTemplate="header">
          <tr>
            <th pSortableColumn="ciudadano.nombre" style="width:20%">
              Ciudadano <p-sortIcon field="ciudadano.nombre"></p-sortIcon>
            </th>
            <th pSortableColumn="tipoTramite.nombre" style="width:15%">
              Tipo <p-sortIcon field="tipoTramite.nombre"></p-sortIcon>
            </th>
            <th pSortableColumn="estado" style="width:12%">
              Estado <p-sortIcon field="estado"></p-sortIcon>
            </th>
            <th style="width:18%">Observación</th>
            <th pSortableColumn="fechaSolicitud" style="width:12%">
              Fecha <p-sortIcon field="fechaSolicitud"></p-sortIcon>
            </th>
            <th style="width:23%">Acciones</th>
          </tr>
        </ng-template>

        <ng-template pTemplate="body" let-tramite>
          <tr>
            <td>
              <div>
                <div class="font-semibold text-sm">{{ tramite.ciudadano.nombre }}</div>
                <div class="text-color-secondary text-xs">
                  {{ tramite.ciudadano.numeroDocumento }}
                </div>
              </div>
            </td>
            <td>
              <p-tag [value]="tramite.tipoTramite.nombre" severity="info" [style]="{'font-size': '0.75rem'}"></p-tag>
            </td>
            <td>
              <p-tag 
                [value]="tramite.estado" 
                [severity]="getEstadoSeverity(tramite.estado)"
                [style]="{'font-size': '0.75rem'}">
              </p-tag>
            </td>
            <td>
              <span *ngIf="tramite.observacion" class="text-sm">
                {{ tramite.observacion.length > 30 ? (tramite.observacion | slice:0:30) + '...' : tramite.observacion }}
              </span>
              <span *ngIf="!tramite.observacion" class="text-color-secondary text-xs font-italic">Sin obs.</span>
            </td>
            <td class="text-sm">{{ tramite.fechaSolicitud | date:'dd/MM/yyyy' }}</td>
            <td>
              <div class="flex gap-1 flex-wrap">
                <p-button 
                  icon="pi pi-check" 
                  [rounded]="true" 
                  severity="success"
                  pTooltip="Aprobar"
                  [disabled]="tramite.estado !== 'PENDIENTE'"
                  (onClick)="cambiarEstado(tramite, 'APROBADO')">
                </p-button>
                <p-button 
                  icon="pi pi-times" 
                  [rounded]="true" 
                  severity="warning"
                  pTooltip="Rechazar"
                  [disabled]="tramite.estado !== 'PENDIENTE'"
                  (onClick)="cambiarEstado(tramite, 'RECHAZADO')">
                </p-button>
                <p-button 
                  icon="pi pi-trash" 
                  [rounded]="true" 
                  [text]="true"
                  severity="danger"
                  pTooltip="Eliminar"
                  (onClick)="deleteTramite(tramite)">
                </p-button>
              </div>
            </td>
          </tr>
        </ng-template>

        <ng-template pTemplate="emptymessage">
          <tr>
            <td colspan="6" class="text-center p-5">
              <i class="pi pi-file text-color-secondary" style="font-size: 3rem"></i>
              <p class="text-color-secondary mt-3">No se encontraron trámites</p>
              <p-button 
                label="Crear Primer Trámite" 
                icon="pi pi-plus" 
                [outlined]="true"
                (onClick)="openNew()"
                class="mt-3">
              </p-button>
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>

    <!-- Dialog para crear trámite -->
    <p-dialog 
      [(visible)]="displayForm" 
      [style]="{width: '500px'}" 
      header="Nuevo Trámite"
      [modal]="true"
      styleClass="p-fluid">

      <ng-template pTemplate="content">
        <form [formGroup]="form" class="flex flex-column gap-3">
          <div class="field">
            <label for="ciudadano" class="font-semibold">Ciudadano *</label>
            <p-dropdown 
              id="ciudadano"
              [options]="ciudadanos" 
              formControlName="ciudadanoId"
              optionLabel="nombre"
              optionValue="id"
              placeholder="Seleccione ciudadano"
              [showClear]="true"
              [filter]="true"
              filterBy="nombre,numeroDocumento"
              styleClass="w-full">
              <ng-template let-ciudadano pTemplate="item">
                <div class="flex flex-column">
                  <span class="font-semibold">{{ ciudadano.nombre }}</span>
                  <span class="text-color-secondary text-sm">{{ ciudadano.tipoDocumento.nombre }}: {{ ciudadano.numeroDocumento }}</span>
                </div>
              </ng-template>
            </p-dropdown>
            <small class="text-red-500" *ngIf="form.get('ciudadanoId')?.invalid && form.get('ciudadanoId')?.touched">
              Seleccione un ciudadano
            </small>
          </div>

          <div class="field">
            <label for="tipoTramite" class="font-semibold">Tipo de Trámite *</label>
            <p-dropdown 
              id="tipoTramite"
              [options]="tiposTramite" 
              formControlName="tipoTramiteId"
              optionLabel="nombre"
              optionValue="id"
              placeholder="Seleccione tipo de trámite"
              [showClear]="true"
              styleClass="w-full">
            </p-dropdown>
            <small class="text-red-500" *ngIf="form.get('tipoTramiteId')?.invalid && form.get('tipoTramiteId')?.touched">
              Seleccione un tipo de trámite
            </small>
          </div>

          <div class="field">
            <label for="observacion" class="font-semibold">Observación</label>
            <textarea 
              id="observacion" 
              pInputTextarea 
              formControlName="observacion"
              rows="3"
              placeholder="Ingrese observaciones (opcional)">
            </textarea>
          </div>
        </form>
      </ng-template>

      <ng-template pTemplate="footer">
        <p-button 
          label="Cancelar" 
          icon="pi pi-times" 
          [text]="true"
          (onClick)="closeForm()">
        </p-button>
        <p-button 
          label="Crear Trámite" 
          icon="pi pi-check"
          (onClick)="save()"
          [disabled]="form.invalid"
          [loading]="saving">
        </p-button>
      </ng-template>
    </p-dialog>

    <!-- Dialog para cambiar estado -->
    <p-dialog 
      [(visible)]="displayEstadoDialog" 
      [style]="{width: '450px'}" 
      [header]="'Cambiar a ' + nuevoEstado"
      [modal]="true"
      styleClass="p-fluid">

      <ng-template pTemplate="content">
        <div class="flex flex-column gap-3">
          <div class="text-center mb-3">
            <i [class]="nuevoEstado === 'APROBADO' ? 'pi pi-check-circle text-green-500' : 'pi pi-times-circle text-orange-500'" 
               style="font-size: 3rem"></i>
            <p class="mt-3">
              ¿Desea {{ nuevoEstado === 'APROBADO' ? 'aprobar' : 'rechazar' }} este trámite?
            </p>
          </div>
          <div class="field">
            <label for="observacionEstado" class="font-semibold">Observación</label>
            <textarea 
              id="observacionEstado" 
              pInputTextarea 
              [(ngModel)]="observacionEstado"
              rows="3"
              placeholder="Ingrese observación (opcional)">
            </textarea>
          </div>
        </div>
      </ng-template>

      <ng-template pTemplate="footer">
        <p-button 
          label="Cancelar" 
          icon="pi pi-times" 
          [text]="true"
          (onClick)="closeEstadoDialog()">
        </p-button>
        <p-button 
          [label]="nuevoEstado === 'APROBADO' ? 'Aprobar' : 'Rechazar'" 
          [icon]="nuevoEstado === 'APROBADO' ? 'pi pi-check' : 'pi pi-times'"
          [severity]="nuevoEstado === 'APROBADO' ? 'success' : 'warning'"
          (onClick)="confirmarCambioEstado()"
          [loading]="savingEstado">
        </p-button>
      </ng-template>
    </p-dialog>
  `
})
export class TramiteListComponent implements OnInit {
  @ViewChild('dt') table!: Table;

  tramites: Tramite[] = [];
  ciudadanos: Ciudadano[] = [];
  tiposTramite: TipoTramite[] = [];
  
  displayForm = false;
  displayEstadoDialog = false;
  loading = false;
  saving = false;
  savingEstado = false;
  
  form!: FormGroup;
  searchTerm = '';
  selectedEstadoFilter: string | null = null;
  
  selectedTramite: Tramite | null = null;
  nuevoEstado: EstadoTramite = 'PENDIENTE';
  observacionEstado = '';

  estadoOptions = [
    { label: 'Pendiente', value: 'PENDIENTE' },
    { label: 'Aprobado', value: 'APROBADO' },
    { label: 'Rechazado', value: 'RECHAZADO' }
  ];

  constructor(
    private tramiteService: TramiteService,
    private ciudadanoService: CiudadanoService,
    private tipoTramiteService: TipoTramiteService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      ciudadanoId: ['', [Validators.required]],
      tipoTramiteId: ['', [Validators.required]],
      observacion: ['']
    });
  }

  ngOnInit() {
    this.loadData();
    this.loadCiudadanos();
    this.loadTiposTramite();
  }

  loadData() {
    this.loading = true;
    this.tramiteService.getAll().subscribe({
      next: (data) => {
        this.tramites = data;
        this.loading = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar trámites' });
        this.loading = false;
      }
    });
  }

  loadCiudadanos() {
    this.ciudadanoService.getAll().subscribe({
      next: (data) => this.ciudadanos = data,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar ciudadanos' })
    });
  }

  loadTiposTramite() {
    this.tipoTramiteService.getAll().subscribe({
      next: (data) => this.tiposTramite = data,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar tipos de trámite' })
    });
  }

  getEstadoSeverity(estado: EstadoTramite): 'success' | 'warning' | 'danger' | 'info' {
    switch (estado) {
      case 'APROBADO': return 'success';
      case 'RECHAZADO': return 'warning';
      default: return 'info';
    }
  }

  openNew() {
    this.form.reset();
    this.displayForm = true;
  }

  cambiarEstado(tramite: Tramite, estado: EstadoTramite) {
    this.selectedTramite = tramite;
    this.nuevoEstado = estado;
    this.observacionEstado = '';
    this.displayEstadoDialog = true;
  }

  confirmarCambioEstado() {
    if (!this.selectedTramite) return;
    
    this.savingEstado = true;
    this.tramiteService.updateEstado(this.selectedTramite.id, {
      estado: this.nuevoEstado,
      observacion: this.observacionEstado || undefined
    }).subscribe({
      next: (result) => {
        const index = this.tramites.findIndex(t => t.id === this.selectedTramite!.id);
        if (index !== -1) this.tramites[index] = result;
        this.messageService.add({ 
          severity: 'success', 
          summary: 'Éxito', 
          detail: `Trámite ${this.nuevoEstado.toLowerCase()}` 
        });
        this.closeEstadoDialog();
        this.savingEstado = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cambiar estado' });
        this.savingEstado = false;
      }
    });
  }

  deleteTramite(tramite: Tramite) {
    this.confirmationService.confirm({
      message: `¿Eliminar este trámite?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, Eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.tramiteService.delete(tramite.id).subscribe({
          next: () => {
            this.tramites = this.tramites.filter(t => t.id !== tramite.id);
            this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Trámite eliminado correctamente' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al eliminar' });
          }
        });
      }
    });
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.tramiteService.create(this.form.value).subscribe({
      next: (result) => {
        this.tramites.push(result);
        this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Trámite creado' });
        this.closeForm();
        this.saving = false;
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message || 'Error al crear trámite', life: 6000 });
        this.saving = false;
      }
    });
  }

  closeForm() {
    this.displayForm = false;
    this.form.reset();
  }

  closeEstadoDialog() {
    this.displayEstadoDialog = false;
    this.selectedTramite = null;
    this.observacionEstado = '';
  }

  filterByEstado(estado: string | null) {
    if (estado) {
      this.table.filter(estado, 'estado', 'equals');
    } else {
      this.table.filter('', 'estado', 'contains');
    }
  }

  clearFilters() {
    this.searchTerm = '';
    this.selectedEstadoFilter = null;
    this.table.clear();
  }
}
