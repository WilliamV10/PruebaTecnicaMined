import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser, DOCUMENT } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { SidebarModule } from 'primeng/sidebar';
import { ButtonModule } from 'primeng/button';
import { PanelMenuModule } from 'primeng/panelmenu';
import { AvatarModule } from 'primeng/avatar';
import { RippleModule } from 'primeng/ripple';
import { TooltipModule } from 'primeng/tooltip';
import { MenuModule } from 'primeng/menu';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    SidebarModule,
    ButtonModule,
    PanelMenuModule,
    AvatarModule,
    RippleModule,
    TooltipModule,
    MenuModule
  ],
  template: `
    <!-- Sidebar móvil -->
    <p-sidebar 
      [(visible)]="sidebarMobileVisible" 
      [style]="{width: '280px'}"
      styleClass="p-sidebar-sm">
      <ng-template pTemplate="header">
        <div class="flex align-items-center gap-2">
          <i class="pi pi-building text-primary text-2xl"></i>
          <span class="font-bold text-xl text-color">MINED</span>
        </div>
      </ng-template>
      <ng-template pTemplate="content">
        <p-menu [model]="mobileMenuItems" styleClass="w-full border-none bg-transparent"></p-menu>
      </ng-template>
    </p-sidebar>

    <!-- Layout principal -->
    <div class="min-h-screen flex flex-column surface-ground">
      
      <!-- Header -->
      <header class="surface-card shadow-2 px-3 py-2 flex align-items-center justify-content-between sticky top-0 z-5">
        <div class="flex align-items-center gap-2">
          <!-- Botón hamburguesa móvil -->
          <p-button 
            icon="pi pi-bars" 
            [rounded]="true" 
            [text]="true"
            severity="secondary"
            (onClick)="sidebarMobileVisible = true"
            styleClass="lg:hidden"
            pTooltip="Menú">
          </p-button>
          
          <!-- Botón colapsar sidebar desktop -->
          <p-button 
            [icon]="sidebarCollapsed ? 'pi pi-angle-right' : 'pi pi-angle-left'" 
            [rounded]="true" 
            [text]="true"
            severity="secondary"
            (onClick)="toggleSidebar()"
            styleClass="hidden lg:flex"
            [pTooltip]="sidebarCollapsed ? 'Expandir menú' : 'Colapsar menú'">
          </p-button>
          
          <!-- Logo -->
          <div class="flex align-items-center gap-2 cursor-pointer" routerLink="/">
            <i class="pi pi-building text-primary text-2xl"></i>
            <div class="hidden sm:block">
              <div class="font-bold text-lg text-color">MINED</div>
              <div class="text-xs text-color-secondary">Gestión de Trámites</div>
            </div>
          </div>
        </div>

        <div class="flex align-items-center gap-2">
          <span class="text-color-secondary text-sm hidden lg:block">
            Sistema de Gestión de Trámites
          </span>
          
          <!-- Toggle tema -->
          <p-button 
            [icon]="isDarkTheme ? 'pi pi-sun' : 'pi pi-moon'" 
            [rounded]="true" 
            [text]="true"
            severity="secondary"
            (onClick)="toggleTheme()"
            [pTooltip]="isDarkTheme ? 'Modo claro' : 'Modo oscuro'">
          </p-button>
          
          <p-avatar 
            icon="pi pi-user" 
            shape="circle"
            styleClass="bg-primary">
          </p-avatar>
        </div>
      </header>

      <!-- Contenido con sidebar -->
      <div class="flex flex-1">
        <!-- Sidebar desktop colapsable -->
        <aside 
          class="hidden lg:flex flex-column surface-card border-right-1 surface-border transition-all transition-duration-300"
          [style.width]="sidebarCollapsed ? '60px' : '250px'">
          
          <div class="flex-1 overflow-y-auto overflow-x-hidden">
            <!-- Menú colapsado (solo iconos) -->
            <div *ngIf="sidebarCollapsed" class="p-2">
              <div *ngFor="let item of collapsedMenuItems" class="mb-1">
                <p-button 
                  [icon]="item.icon" 
                  [rounded]="true" 
                  [text]="true"
                  severity="secondary"
                  styleClass="w-full"
                  [routerLink]="item.routerLink"
                  [pTooltip]="item.label"
                  tooltipPosition="right">
                </p-button>
              </div>
            </div>
            
            <!-- Menú expandido -->
            <div *ngIf="!sidebarCollapsed" class="p-3">
              <p-panelMenu [model]="menuItems" styleClass="w-full" [multiple]="true"></p-panelMenu>
            </div>
          </div>
          
          <!-- Footer del sidebar -->
          <div class="p-2 border-top-1 surface-border" *ngIf="!sidebarCollapsed">
            <div class="text-xs text-color-secondary text-center">
              © {{ currentYear }} MINED
            </div>
          </div>
        </aside>

        <!-- Área de contenido -->
        <main class="flex-1 p-3 overflow-auto">
          <router-outlet></router-outlet>
        </main>
      </div>
    </div>
  `
})
export class LayoutComponent implements OnInit {
  sidebarMobileVisible = false;
  sidebarCollapsed = false;
  isDarkTheme = true;
  menuItems: MenuItem[] = [];
  mobileMenuItems: MenuItem[] = [];
  collapsedMenuItems: { label: string; icon: string; routerLink: string[] }[] = [];
  currentYear = new Date().getFullYear();

  constructor(
    private router: Router,
    @Inject(DOCUMENT) private document: Document,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    // Cargar preferencia de tema
    if (isPlatformBrowser(this.platformId)) {
      const savedTheme = localStorage.getItem('theme');
      this.isDarkTheme = savedTheme !== 'light';
      this.applyTheme();
      
      // Cargar estado del sidebar
      const savedSidebar = localStorage.getItem('sidebarCollapsed');
      this.sidebarCollapsed = savedSidebar === 'true';
    }

    this.collapsedMenuItems = [
      { label: 'Inicio', icon: 'pi pi-home', routerLink: ['/'] },
      { label: 'Ciudadanos', icon: 'pi pi-users', routerLink: ['/ciudadanos'] },
      { label: 'Trámites', icon: 'pi pi-file', routerLink: ['/tramites'] },
      { label: 'Tipos de Trámite', icon: 'pi pi-list', routerLink: ['/tipos-tramite'] },
      { label: 'Tipos de Documento', icon: 'pi pi-id-card', routerLink: ['/tipos-documento'] }
    ];

    this.menuItems = [
      {
        label: 'Principal',
        icon: 'pi pi-fw pi-home',
        expanded: true,
        items: [
          { label: 'Inicio', icon: 'pi pi-fw pi-home', routerLink: ['/'] }
        ]
      },
      {
        label: 'Gestión',
        icon: 'pi pi-fw pi-briefcase',
        expanded: true,
        items: [
          { label: 'Ciudadanos', icon: 'pi pi-fw pi-users', routerLink: ['/ciudadanos'] },
          { label: 'Trámites', icon: 'pi pi-fw pi-file', routerLink: ['/tramites'] }
        ]
      },
      {
        label: 'Catálogos',
        icon: 'pi pi-fw pi-cog',
        expanded: true,
        items: [
          { label: 'Tipos de Trámite', icon: 'pi pi-fw pi-list', routerLink: ['/tipos-tramite'] },
          { label: 'Tipos de Documento', icon: 'pi pi-fw pi-id-card', routerLink: ['/tipos-documento'] }
        ]
      }
    ];

    this.mobileMenuItems = [
      { label: 'Inicio', icon: 'pi pi-home', routerLink: ['/'], command: () => this.closeMobileSidebar() },
      { separator: true },
      { label: 'Ciudadanos', icon: 'pi pi-users', routerLink: ['/ciudadanos'], command: () => this.closeMobileSidebar() },
      { label: 'Trámites', icon: 'pi pi-file', routerLink: ['/tramites'], command: () => this.closeMobileSidebar() },
      { separator: true },
      { label: 'Tipos de Trámite', icon: 'pi pi-list', routerLink: ['/tipos-tramite'], command: () => this.closeMobileSidebar() },
      { label: 'Tipos de Documento', icon: 'pi pi-id-card', routerLink: ['/tipos-documento'], command: () => this.closeMobileSidebar() }
    ];
  }

  toggleSidebar() {
    this.sidebarCollapsed = !this.sidebarCollapsed;
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('sidebarCollapsed', String(this.sidebarCollapsed));
    }
  }

  toggleTheme() {
    this.isDarkTheme = !this.isDarkTheme;
    // Tema dinámico deshabilitado temporalmente - usar solo tema oscuro
    // Para habilitar cambio de tema, configurar los assets correctamente en producción
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('theme', this.isDarkTheme ? 'dark' : 'light');
    }
  }

  private applyTheme() {
    // El tema está cargado estáticamente en angular.json
    // Para cambio dinámico en producción, descomentar y configurar assets
    /*
    const themeLink = this.document.getElementById('app-theme') as HTMLLinkElement;
    if (themeLink) {
      themeLink.href = this.isDarkTheme 
        ? 'themes/lara-dark-blue/theme.css'
        : 'themes/lara-light-blue/theme.css';
    }
    */
  }

  closeMobileSidebar() {
    this.sidebarMobileVisible = false;
  }
}
