import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputMaskModule } from 'primeng/inputmask';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { ToolbarModule } from 'primeng/toolbar';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { RippleModule } from 'primeng/ripple';
import { DropdownModule } from 'primeng/dropdown';
import { MessageService, ConfirmationService } from 'primeng/api';
import { CiudadanoService, TipoDocumentoService } from '../../../core/services';
import { Ciudadano, TipoDocumento, DocumentoConfig, getDocumentoConfig } from '../../../core/models';

@Component({
  selector: 'app-ciudadano-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    InputMaskModule,
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
            <i class="pi pi-users text-primary text-2xl"></i>
            <div>
              <div class="text-xl font-bold text-color">Gestión de Ciudadanos</div>
              <div class="text-color-secondary text-sm">Administre la información de ciudadanos</div>
            </div>
          </div>
        </ng-template>
        <ng-template pTemplate="right">
          <p-button 
            label="Nuevo Ciudadano" 
            icon="pi pi-plus" 
            severity="success" 
            (onClick)="openNew()">
          </p-button>
        </ng-template>
      </p-toolbar>

      <!-- Tabla -->
      <p-table 
        #dt
        [value]="ciudadanos"
        [rows]="10"
        [paginator]="true"
        [rowsPerPageOptions]="[5, 10, 25, 50]"
        [loading]="loading"
        [globalFilterFields]="['nombre', 'numeroDocumento', 'correo', 'tipoDocumento.nombre']"
        [tableStyle]="{'min-width': '50rem'}"
        responsiveLayout="scroll"
        dataKey="id"
        currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} ciudadanos"
        [showCurrentPageReport]="true"
        [scrollable]="true">

        <ng-template pTemplate="caption">
          <div class="flex flex-column md:flex-row md:justify-content-between md:align-items-center gap-3">
            <span class="p-input-icon-left">
              <i class="pi pi-search"></i>
              <input 
                pInputText 
                type="text"
                [(ngModel)]="searchTerm"
                (input)="dt.filterGlobal($any($event.target).value, 'contains')" 
                placeholder="Buscar ciudadano..." 
                style="width: 250px" />
            </span>
            <p-button 
              label="Limpiar" 
              icon="pi pi-filter-slash" 
              [outlined]="true"
              severity="secondary"
              (onClick)="clearFilters()">
            </p-button>
          </div>
        </ng-template>

        <ng-template pTemplate="header">
          <tr>
            <th pSortableColumn="nombre" style="width:25%">
              Nombre <p-sortIcon field="nombre"></p-sortIcon>
            </th>
            <th pSortableColumn="tipoDocumento.nombre" style="width:15%">
              Tipo <p-sortIcon field="tipoDocumento.nombre"></p-sortIcon>
            </th>
            <th pSortableColumn="numeroDocumento" style="width:18%">
              Número Doc. <p-sortIcon field="numeroDocumento"></p-sortIcon>
            </th>
            <th pSortableColumn="correo" style="width:22%">
              Correo <p-sortIcon field="correo"></p-sortIcon>
            </th>
            <th style="width:20%">Acciones</th>
          </tr>
        </ng-template>

        <ng-template pTemplate="body" let-ciudadano>
          <tr>
            <td>
              <span class="font-semibold">{{ ciudadano.nombre }}</span>
            </td>
            <td>
              <p-tag [value]="ciudadano.tipoDocumento.nombre" severity="info" [style]="{'font-size': '0.75rem'}"></p-tag>
            </td>
            <td>
              <span class="font-mono text-sm">{{ ciudadano.numeroDocumento }}</span>
            </td>
            <td>
              <span class="text-sm">{{ ciudadano.correo }}</span>
            </td>
            <td>
              <div class="flex gap-1">
                <p-button 
                  icon="pi pi-pencil" 
                  [rounded]="true" 
                  [text]="true"
                  severity="success"
                  pTooltip="Editar"
                  (onClick)="editCiudadano(ciudadano)">
                </p-button>
                <p-button 
                  icon="pi pi-trash" 
                  [rounded]="true" 
                  [text]="true"
                  severity="danger"
                  pTooltip="Eliminar"
                  (onClick)="deleteCiudadano(ciudadano)">
                </p-button>
              </div>
            </td>
          </tr>
        </ng-template>

        <ng-template pTemplate="emptymessage">
          <tr>
            <td colspan="5" class="text-center p-5">
              <i class="pi pi-users text-color-secondary" style="font-size: 3rem"></i>
              <p class="text-color-secondary mt-3">No se encontraron ciudadanos</p>
              <p-button 
                label="Registrar Primer Ciudadano" 
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

    <!-- Dialog para crear/editar -->
    <p-dialog 
      [(visible)]="displayForm" 
      [style]="{width: '500px'}" 
      [header]="isEditing ? 'Editar Ciudadano' : 'Nuevo Ciudadano'"
      [modal]="true"
      styleClass="p-fluid">

      <ng-template pTemplate="content">
        <form [formGroup]="form" class="flex flex-column gap-3">
          <div class="field">
            <label for="nombre" class="font-semibold">Nombre Completo *</label>
            <input 
              id="nombre" 
              type="text" 
              pInputText 
              formControlName="nombre"
              placeholder="Ingrese nombre completo (solo letras)"
              [class.ng-invalid]="form.get('nombre')?.invalid && form.get('nombre')?.touched"
              [class.ng-dirty]="form.get('nombre')?.touched" />
            <small class="text-red-500" *ngIf="form.get('nombre')?.hasError('required') && form.get('nombre')?.touched">
              El nombre es requerido
            </small>
            <small class="text-red-500" *ngIf="form.get('nombre')?.hasError('pattern') && form.get('nombre')?.touched">
              El nombre solo debe contener letras
            </small>
          </div>

          <div class="field">
            <label for="tipoDocumento" class="font-semibold">Tipo de Documento *</label>
            <p-dropdown 
              id="tipoDocumento"
              [options]="tiposDocumento" 
              formControlName="tipoDocumentoId"
              optionLabel="nombre"
              optionValue="id"
              placeholder="Seleccione tipo de documento"
              [showClear]="true"
              styleClass="w-full"
              (onChange)="onTipoDocumentoChange($event)">
            </p-dropdown>
            <small class="text-red-500" *ngIf="form.get('tipoDocumentoId')?.invalid && form.get('tipoDocumentoId')?.touched">
              Seleccione un tipo de documento
            </small>
          </div>

          <div class="field">
            <label for="numeroDocumento" class="font-semibold">Número de Documento *</label>
            <p-inputMask 
              *ngIf="currentDocConfig; else noMask"
              id="numeroDocumento" 
              formControlName="numeroDocumento"
              [mask]="currentDocConfig.mascara"
              [placeholder]="getPlaceholder()"
              styleClass="w-full"
              [unmask]="false">
            </p-inputMask>
            <ng-template #noMask>
              <input 
                id="numeroDocumento" 
                type="text" 
                pInputText 
                formControlName="numeroDocumento"
                placeholder="Ingrese número de documento"
                [class.ng-invalid]="form.get('numeroDocumento')?.invalid && form.get('numeroDocumento')?.touched"
                [class.ng-dirty]="form.get('numeroDocumento')?.touched" />
            </ng-template>
            <small class="text-color-secondary" *ngIf="currentDocConfig">
              Formato: {{ getFormatoEjemplo() }}
            </small>
            <small class="text-red-500" *ngIf="form.get('numeroDocumento')?.invalid && form.get('numeroDocumento')?.touched">
              El número de documento es requerido
            </small>
          </div>

          <div class="field">
            <label for="correo" class="font-semibold">Correo Electrónico *</label>
            <input 
              id="correo" 
              type="email" 
              pInputText 
              formControlName="correo"
              placeholder="ejemplo@correo.com"
              [class.ng-invalid]="form.get('correo')?.invalid && form.get('correo')?.touched"
              [class.ng-dirty]="form.get('correo')?.touched" />
            <small class="text-red-500" *ngIf="form.get('correo')?.hasError('required') && form.get('correo')?.touched">
              El correo es requerido
            </small>
            <small class="text-red-500" *ngIf="form.get('correo')?.hasError('email') && form.get('correo')?.touched">
              Ingrese un correo válido
            </small>
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
          [label]="isEditing ? 'Actualizar' : 'Guardar'" 
          icon="pi pi-check"
          (onClick)="save()"
          [disabled]="form.invalid"
          [loading]="saving">
        </p-button>
      </ng-template>
    </p-dialog>
  `
})
export class CiudadanoListComponent implements OnInit {
  @ViewChild('dt') table!: Table;

  ciudadanos: Ciudadano[] = [];
  tiposDocumento: TipoDocumento[] = [];
  selectedCiudadano: Ciudadano | null = null;
  selectedTipoDocumento: TipoDocumento | null = null;
  currentDocConfig: DocumentoConfig | null = null;
  displayForm = false;
  loading = false;
  saving = false;
  isEditing = false;
  form!: FormGroup;
  searchTerm = '';

  constructor(
    private ciudadanoService: CiudadanoService,
    private tipoDocumentoService: TipoDocumentoService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(150), Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/)]],
      tipoDocumentoId: ['', [Validators.required]],
      numeroDocumento: ['', [Validators.required, Validators.maxLength(30)]],
      correo: ['', [Validators.required, Validators.email, Validators.maxLength(150)]]
    });
  }

  ngOnInit() {
    this.loadData();
    this.loadTiposDocumento();
  }

  loadData() {
    this.loading = true;
    this.ciudadanoService.getAll().subscribe({
      next: (data) => {
        this.ciudadanos = data;
        this.loading = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar ciudadanos' });
        this.loading = false;
      }
    });
  }

  loadTiposDocumento() {
    this.tipoDocumentoService.getAll().subscribe({
      next: (data) => {
        this.tiposDocumento = data;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar tipos de documento' });
      }
    });
  }

  openNew() {
    this.selectedCiudadano = null;
    this.selectedTipoDocumento = null;
    this.currentDocConfig = null;
    this.isEditing = false;
    this.form.reset();
    this.displayForm = true;
  }

  editCiudadano(ciudadano: Ciudadano) {
    this.selectedCiudadano = ciudadano;
    this.selectedTipoDocumento = ciudadano.tipoDocumento;
    this.currentDocConfig = getDocumentoConfig(ciudadano.tipoDocumento.nombre);
    this.isEditing = true;
    this.form.patchValue({
      nombre: ciudadano.nombre,
      tipoDocumentoId: ciudadano.tipoDocumento.id,
      numeroDocumento: ciudadano.numeroDocumento,
      correo: ciudadano.correo || ''
    });
    this.displayForm = true;
  }

  onTipoDocumentoChange(event: any) {
    const tipoDocId = event.value;
    this.selectedTipoDocumento = this.tiposDocumento.find(t => t.id === tipoDocId) || null;
    this.currentDocConfig = this.selectedTipoDocumento ? getDocumentoConfig(this.selectedTipoDocumento.nombre) : null;
    // Limpiar número de documento al cambiar tipo
    this.form.patchValue({ numeroDocumento: '' });
  }

  getPlaceholder(): string {
    return this.currentDocConfig?.placeholder || 'Ingrese número de documento';
  }

  getFormatoEjemplo(): string {
    return this.currentDocConfig ? `Ej: ${this.currentDocConfig.ejemplo}` : '';
  }

  deleteCiudadano(ciudadano: Ciudadano) {
    this.confirmationService.confirm({
      message: `¿Eliminar al ciudadano "${ciudadano.nombre}"?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, Eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.ciudadanoService.delete(ciudadano.id).subscribe({
          next: () => {
            this.ciudadanos = this.ciudadanos.filter(c => c.id !== ciudadano.id);
            this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Ciudadano eliminado correctamente' });
          },
          error: (err) => {
            const mensaje = err?.error?.message || err?.message || 'Error al eliminar';
            this.messageService.add({ severity: 'error', summary: 'Error', detail: mensaje, life: 5000 });
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
    const formData = this.form.value;
    
    // Limpiar el número de documento - solo guardar dígitos (sin guiones ni caracteres especiales)
    const data = {
      ...formData,
      numeroDocumento: formData.numeroDocumento.replace(/[^0-9a-zA-Z]/g, '')
    };

    if (this.isEditing && this.selectedCiudadano) {
      this.ciudadanoService.update(this.selectedCiudadano.id, data).subscribe({
        next: (result) => {
          const index = this.ciudadanos.findIndex(c => c.id === this.selectedCiudadano!.id);
          if (index !== -1) this.ciudadanos[index] = result;
          this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Ciudadano actualizado' });
          this.closeForm();
          this.saving = false;
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message || 'Error al actualizar' });
          this.saving = false;
        }
      });
    } else {
      this.ciudadanoService.create(data).subscribe({
        next: (result) => {
          this.ciudadanos.push(result);
          this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Ciudadano registrado' });
          this.closeForm();
          this.saving = false;
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: err.message || 'Error al crear' });
          this.saving = false;
        }
      });
    }
  }

  closeForm() {
    this.displayForm = false;
    this.selectedTipoDocumento = null;
    this.currentDocConfig = null;
    this.form.reset();
  }

  clearFilters() {
    this.searchTerm = '';
    this.table.clear();
  }
}
