import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { ToolbarModule } from 'primeng/toolbar';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { RippleModule } from 'primeng/ripple';
import { MessageService, ConfirmationService } from 'primeng/api';
import { TipoDocumentoService } from '../../../core/services';
import { TipoDocumento, DOCUMENTO_CONFIGS } from '../../../core/models';

@Component({
  selector: 'app-tipo-documento-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    ToastModule,
    ConfirmDialogModule,
    DialogModule,
    ToolbarModule,
    TagModule,
    TooltipModule,
    RippleModule
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
            <i class="pi pi-id-card text-primary text-2xl"></i>
            <div>
              <div class="text-xl font-bold text-color">Tipos de Documento</div>
              <div class="text-color-secondary text-sm">Gestione los tipos de documentos de identificación</div>
            </div>
          </div>
        </ng-template>
        <ng-template pTemplate="right">
          <p-button 
            label="Nuevo" 
            icon="pi pi-plus" 
            severity="success" 
            (onClick)="openNew()">
          </p-button>
        </ng-template>
      </p-toolbar>

      <!-- Tabla -->
      <p-table 
        #dt
        [value]="tiposDocumento"
        [rows]="10"
        [paginator]="true"
        [rowsPerPageOptions]="[5, 10, 25]"
        [loading]="loading"
        [globalFilterFields]="['nombre']"
        [tableStyle]="{'min-width': '35rem'}"
        responsiveLayout="scroll"
        [scrollable]="true"
        dataKey="id"
        currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} registros"
        [showCurrentPageReport]="true">

        <ng-template pTemplate="caption">
          <div class="flex justify-content-between align-items-center">
            <span class="p-input-icon-left">
              <i class="pi pi-search"></i>
              <input 
                pInputText 
                type="text"
                [(ngModel)]="searchTerm"
                (input)="dt.filterGlobal($any($event.target).value, 'contains')" 
                placeholder="Buscar..." />
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
            <th pSortableColumn="nombre" style="width:35%">
              Nombre <p-sortIcon field="nombre"></p-sortIcon>
            </th>
            <th style="width:30%">Máscara</th>
            <th style="width:35%">Acciones</th>
          </tr>
        </ng-template>

        <ng-template pTemplate="body" let-tipo>
          <tr>
            <td>
              <p-tag [value]="tipo.nombre" severity="info" [style]="{'font-size': '0.8rem'}"></p-tag>
            </td>
            <td>
              <span *ngIf="getConfigForTipo(tipo.nombre) as config" class="font-mono text-xs surface-100 p-1 border-round">{{ config.mascara }}</span>
              <span *ngIf="!getConfigForTipo(tipo.nombre)" class="text-color-secondary font-italic text-sm">Sin máscara</span>
            </td>
            <td>
              <div class="flex gap-1">
                <p-button 
                  icon="pi pi-pencil" 
                  [rounded]="true" 
                  [outlined]="true"
                  severity="success"
                  pTooltip="Editar"
                  (onClick)="editTipo(tipo)">
                </p-button>
                <p-button 
                  icon="pi pi-trash" 
                  [rounded]="true" 
                  [outlined]="true"
                  severity="danger"
                  pTooltip="Eliminar"
                  (onClick)="deleteTipo(tipo)">
                </p-button>
              </div>
            </td>
          </tr>
        </ng-template>

        <ng-template pTemplate="emptymessage">
          <tr>
            <td colspan="4" class="text-center p-5">
              <i class="pi pi-inbox text-color-secondary" style="font-size: 3rem"></i>
              <p class="text-color-secondary mt-3">No se encontraron tipos de documento</p>
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>

    <!-- Dialog para crear/editar -->
    <p-dialog 
      [(visible)]="displayForm" 
      [style]="{width: '450px'}" 
      [header]="isEditing ? 'Editar Tipo de Documento' : 'Nuevo Tipo de Documento'"
      [modal]="true"
      styleClass="p-fluid">

      <ng-template pTemplate="content">
        <form [formGroup]="form">
          <div class="field">
            <label for="nombre" class="font-semibold">Nombre *</label>
            <input 
              id="nombre" 
              type="text" 
              pInputText 
              formControlName="nombre"
              placeholder="Ej: DUI, Pasaporte, NIT"
              [class.ng-invalid]="form.get('nombre')?.invalid && form.get('nombre')?.touched"
              [class.ng-dirty]="form.get('nombre')?.touched" />
            <small class="text-red-500" *ngIf="form.get('nombre')?.invalid && form.get('nombre')?.touched">
              El nombre es requerido (mínimo 2 caracteres)
            </small>
            <small class="text-color-secondary mt-2 block" *ngIf="!isEditing">
              <i class="pi pi-info-circle mr-1"></i>
              Los nombres DUI, Pasaporte y NIT tienen máscaras predefinidas
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
          [disabled]="form.invalid">
        </p-button>
      </ng-template>
    </p-dialog>
  `
})
export class TipoDocumentoListComponent implements OnInit {
  @ViewChild('dt') table!: Table;

  tiposDocumento: TipoDocumento[] = [];
  selectedTipo: TipoDocumento | null = null;
  displayForm = false;
  loading = false;
  isEditing = false;
  form!: FormGroup;
  searchTerm = '';

  constructor(
    private tipoDocumentoService: TipoDocumentoService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]]
    });
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this.tipoDocumentoService.getAll().subscribe({
      next: (data) => {
        this.tiposDocumento = data;
        this.loading = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar datos' });
        this.loading = false;
      }
    });
  }

  openNew() {
    this.selectedTipo = null;
    this.isEditing = false;
    this.form.reset();
    this.displayForm = true;
  }

  editTipo(tipo: TipoDocumento) {
    this.selectedTipo = tipo;
    this.isEditing = true;
    this.form.patchValue({ nombre: tipo.nombre });
    this.displayForm = true;
  }

  getConfigForTipo(nombre: string) {
    return DOCUMENTO_CONFIGS[nombre] || null;
  }

  deleteTipo(tipo: TipoDocumento) {
    this.confirmationService.confirm({
      message: `¿Eliminar el tipo de documento "${tipo.nombre}"?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, Eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.tipoDocumentoService.delete(tipo.id).subscribe({
          next: () => {
            this.tiposDocumento = this.tiposDocumento.filter(t => t.id !== tipo.id);
            this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Tipo eliminado correctamente' });
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

    const data = this.form.value;

    if (this.isEditing && this.selectedTipo) {
      this.tipoDocumentoService.update(this.selectedTipo.id, data).subscribe({
        next: (result) => {
          const index = this.tiposDocumento.findIndex(t => t.id === this.selectedTipo!.id);
          if (index !== -1) this.tiposDocumento[index] = result;
          this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Tipo actualizado' });
          this.closeForm();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al actualizar' });
        }
      });
    } else {
      this.tipoDocumentoService.create(data).subscribe({
        next: (result) => {
          this.tiposDocumento.push(result);
          this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Tipo creado' });
          this.closeForm();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al crear' });
        }
      });
    }
  }

  closeForm() {
    this.displayForm = false;
    this.form.reset();
  }

  clearFilters() {
    this.searchTerm = '';
    this.table.clear();
  }
}
