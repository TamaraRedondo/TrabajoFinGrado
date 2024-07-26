import { Component, ElementRef, HostListener, OnInit, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../services/auth/login.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit{
  userLoginOn:boolean=false;
  info: any = {};
  isLoggedIn = false;

  constructor(private el: ElementRef, 
    private renderer: Renderer2, 
    private router: Router, 
    private loginService:LoginService) {}

  ngOnInit() {
    this.loginService.currentUserLoginOn.subscribe(
      {
        next:(userLoginOn) => {
          this.userLoginOn=userLoginOn;
        }
      }
    )
  }

  getImgUrl(imgFileName: string): string {
    return `assets/${imgFileName}`;
  }

  logout(){
    this.loginService.logout();
    this.router.navigate(['/inicio'])
  }
}

