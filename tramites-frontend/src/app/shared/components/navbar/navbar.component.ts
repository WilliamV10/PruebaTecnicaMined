import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { MenuItem } from 'primeng/api';

/**
 * Componente de navegación principal de la aplicación
 * Proporciona la barra de menú con enlaces a las diferentes secciones
 */
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MenubarModule
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  /** Fecha actual para mostrar en la navbar */
  today = new Date();

  /** Items del menú de navegación */
  menuItems: MenuItem[] = [
    {
      label: 'Inicio',
      icon: 'pi pi-home',
      routerLink: '/'
    },
    {
      label: 'Ciudadanos',
      icon: 'pi pi-users',
      routerLink: '/ciudadanos'
    },
    {
      label: 'Trámites',
      icon: 'pi pi-file-edit',
      routerLink: '/tramites'
    }
  ];
}
