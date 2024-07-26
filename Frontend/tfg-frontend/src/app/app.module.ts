import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { PersonalDetailsComponent } from './perfil/personal-details.component';
import { JwtInterceptorService } from './services/auth/jwt-interceptor.service';
import { ErrorInterceptorService } from './services/auth/error-interceptor.service';
import { RegisterComponent } from './register/register.component';
import { PreguntaComponent } from './pregunta/pregunta.component';
import { AuthInterceptor } from './services/auth/authInterceptor';
import { SidebarComponent } from './sidebar/sidebar.component';
import { RutinaComponent } from './rutina/rutina.component';
import { ObjetivoComponent } from './objetivo/objetivo.component';
import { FormatValuePipe } from './objetivo/formatValuePipe';
import { ComunicacionService } from './services/comunicacion/comunicacion.service';
import { ChatComponent } from './chat/chat.component';
import { LogrosComponent } from './logros/logros.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    PreguntaComponent,
    PersonalDetailsComponent,
    SidebarComponent,
    RutinaComponent,
    ObjetivoComponent,
    FormatValuePipe,
    ChatComponent,
    LogrosComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [
    {provide:HTTP_INTERCEPTORS,useClass:JwtInterceptorService,multi:true},
    {provide:HTTP_INTERCEPTORS,useClass:ErrorInterceptorService,multi:true},
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    ComunicacionService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
