import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { PreguntaComponent } from './pregunta/pregunta.component';
import { PersonalDetailsComponent } from './perfil/personal-details.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { RutinaComponent } from './rutina/rutina.component';
import { ObjetivoComponent } from './objetivo/objetivo.component';
import { LogrosComponent } from './logros/logros.component';

const routes: Routes = [
  {path:'',redirectTo:'/inicio', pathMatch:'full'},
  {path:'inicio',component:LoginComponent},
  {path:'iniciar-sesion',component:SidebarComponent},
  {path:'datos-personales',component:PersonalDetailsComponent},
  {path:'registro', component:RegisterComponent},
  {path:'pregunta', component:PreguntaComponent},
  {path:'rutina', component:RutinaComponent},
  {path:'objetivo', component:ObjetivoComponent},
  {path:'logro', component:LogrosComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
