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
import { TipoTramiteService } from '../../../core/services';
import { TipoTramite } from '../../../core/models';

@Component({
  selector: 'app-tipo-tramite-list',
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
            <i class="pi pi-list text-primary text-2xl"></i>
            <div>
              <div class="text-xl font-bold text-color">Tipos de Trámite</div>
              <div class="text-color-secondary text-sm">Gestione los tipos de trámites del sistema</div>
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
        [value]="tiposTramite"
        [rows]="10"
        [paginator]="true"
        [rowsPerPageOptions]="[5, 10, 25]"
        [loading]="loading"
        [globalFilterFields]="['nombre']"
        [tableStyle]="{'min-width': '30rem'}"
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
            <th pSortableColumn="nombre" style="width:60%">
              Nombre <p-sortIcon field="nombre"></p-sortIcon>
            </th>
            <th style="width:40%">Acciones</th>
          </tr>
        </ng-template>

        <ng-template pTemplate="body" let-tipo>
          <tr>
            <td>
              <span class="font-semibold">{{ tipo.nombre }}</span>
            </td>
            <td>
              <div class="flex gap-1">
                <p-button 
                  icon="pi pi-pencil" 
                  [rounded]="true" 
                  [text]="true"
                  severity="success"
                  pTooltip="Editar"
                  (onClick)="editTipo(tipo)">
                </p-button>
                <p-button 
                  icon="pi pi-trash" 
                  [rounded]="true" 
                  [text]="true"
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
            <td colspan="2" class="text-center p-5">
              <i class="pi pi-inbox text-color-secondary" style="font-size: 3rem"></i>
              <p class="text-color-secondary mt-3">No se encontraron tipos de trámite</p>
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>

    <!-- Dialog para crear/editar -->
    <p-dialog 
      [(visible)]="displayForm" 
      [style]="{width: '450px'}" 
      [header]="isEditing ? 'Editar Tipo de Trámite' : 'Nuevo Tipo de Trámite'"
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
              placeholder="Ingrese el nombre"
              [class.ng-invalid]="form.get('nombre')?.invalid && form.get('nombre')?.touched"
              [class.ng-dirty]="form.get('nombre')?.touched" />
            <small class="text-red-500" *ngIf="form.get('nombre')?.invalid && form.get('nombre')?.touched">
              El nombre es requerido (mínimo 3 caracteres)
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
export class TipoTramiteListComponent implements OnInit {
  @ViewChild('dt') table!: Table;

  tiposTramite: TipoTramite[] = [];
  selectedTipo: TipoTramite | null = null;
  displayForm = false;
  loading = false;
  isEditing = false;
  form!: FormGroup;
  searchTerm = '';

  constructor(
    private tipoTramiteService: TipoTramiteService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]]
    });
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this.tipoTramiteService.getAll().subscribe({
      next: (data) => {
        this.tiposTramite = data;
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

  editTipo(tipo: TipoTramite) {
    this.selectedTipo = tipo;
    this.isEditing = true;
    this.form.patchValue({ nombre: tipo.nombre });
    this.displayForm = true;
  }

  deleteTipo(tipo: TipoTramite) {
    this.confirmationService.confirm({
      message: `¿Eliminar el tipo de trámite "${tipo.nombre}"?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, Eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.tipoTramiteService.delete(tipo.id).subscribe({
          next: () => {
            this.tiposTramite = this.tiposTramite.filter(t => t.id !== tipo.id);
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
      this.tipoTramiteService.update(this.selectedTipo.id, data).subscribe({
        next: (result) => {
          const index = this.tiposTramite.findIndex(t => t.id === this.selectedTipo!.id);
          if (index !== -1) this.tiposTramite[index] = result;
          this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Tipo actualizado' });
          this.closeForm();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al actualizar' });
        }
      });
    } else {
      this.tipoTramiteService.create(data).subscribe({
        next: (result) => {
          this.tiposTramite.push(result);
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
