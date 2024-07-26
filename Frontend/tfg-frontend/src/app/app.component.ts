import { Component } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';
  mostrarBarraLateral: boolean = true;

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        if (event.url === '/iniciar-sesion' || event.url === '/datos-personales' || event.url === '/rutina' || event.url === '/objetivo' || event.url === '/logro' || event.url === '/sobre') {
          this.mostrarBarraLateral = true;
        } else {
          this.mostrarBarraLateral = false;
        }
      }
    });
  }

  mostrarSidebar(): boolean {
    return this.mostrarBarraLateral;
  }
}
