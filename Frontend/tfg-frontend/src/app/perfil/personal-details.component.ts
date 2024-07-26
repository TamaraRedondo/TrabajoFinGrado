import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PreguntaService } from '../services/pregunta/pregunta.service';
import { Seleccion } from '../pregunta/seleccion';
import { SeleccionService } from '../services/seleccion/seleccion.service';
import { User } from '../services/auth/user';
import { UserService } from '../services/user/user.service';
import { CambioSeleccionRequest } from './cambioSeleccionRequest';
import { ComunicacionService } from '../services/comunicacion/comunicacion.service';

@Component({
  selector: 'app-personal-details',
  templateUrl: './personal-details.component.html',
  styleUrls: ['./personal-details.component.css']
})
export class PersonalDetailsComponent implements OnInit {
  userInfo: any;
  token!: string;
  isEditing: boolean = false;
  updatedUserInfo: any = {};

  errorMessage: String = "";
  userLoginOn: boolean = false;
  editMode: boolean = false;
  titulo = 'Mi Perfil';
  user?: User;
  selecciones: Seleccion[] = [];

  textosPersonalizados: string[] = [
    "Height (cm)",
    "Weight (kg)",
    "Age Range",
    "Gender",
    "Body Type",
    "Physical Activity Level",
    "Current Injury",
    "Injury Location",
    "Regular Exercise Location",
    "Days Available to Train per Week",
    "Available Session Time",
    "Goal",
    "Additional Goal",
    "Walking Time per Day",
    "Sleeping Time per Day",
    "Daily Water Intake"
  ];

  analisisPersonalizados: string[] = [
    "Body Type",
    "Goals",
    "Walking",
    "Sleeping",
    "Water Consumption",
    "BMI Calculation"
  ];

  userId: number = 0;
  preguntaIds: number[] = [4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16];
  opciones: { [preguntaId: number]: string[] } = {};
  opcionSeleccionada: { [preguntaId: number]: string } = {};

  modoEditar: boolean[] = [];

  isLoading = true;
  analisisId: number = 0;
  descripcionDelAnalisis: string = '';
  descripciones: string[] = [];
  parsedDescripciones: { [key: string]: string }[] = [];

  actualizacionCompletada: EventEmitter<void> = new EventEmitter<void>();
  loadingChanged: EventEmitter<boolean> = new EventEmitter<boolean>();

  cardImages: string[] = [
    'assets/Cuerpo-humano.png',
    'assets/Objetivo.png',
    'assets/Ubicacion.png',
    'assets/Dormir.png',
    'assets/Agua.png',
    'assets/Imc.png'
  ];

  editedFields: Partial<User> = {};

  showFullContent: boolean[] = [];
  currentCardIndex: number | null = null;

  constructor(private userService: UserService,
    private router: Router, private activatedRoute: ActivatedRoute,
    private seleccionService: SeleccionService,
    private preguntaService: PreguntaService,
    private comunicacionService: ComunicacionService) { }

  ngOnInit(): void {
    this.userService.getUserInfo(this.token).subscribe({
      next: (data) => {
        this.userInfo = data;
      },
      error: (error) => {
        console.error('Error al obtener datos del usuario:', error);
      }
    });
    this.obtenerUserIdYSeleccion();
    this.obtenerUserId();

    this.actualizacionCompletada.subscribe(() => {
      this.onActualizacionCompletada();
    });

    this.loadingChanged.subscribe((isLoading: boolean) => {
      this.isLoading = isLoading;
    });
  }

  activarModoEditar(preguntaId: number) {
    this.modoEditar[preguntaId] = true;
  }
  emptyCards(descriptionsCount: number): number[] {
    const emptyCount = 6 - descriptionsCount;
    if (emptyCount > 0) {
      return Array(emptyCount).fill(0).map((x, i) => i + descriptionsCount);
    } else {
      return [];
    }
  }

  obtenerUserId(): void {
    this.userService.getUserId().subscribe({
      next: userId => {
        this.userId = userId;
        this.cargarOpcionesYSeleccion();
        this.obtenerDetalles(userId);
      },
      error: error => {
        console.error('Error al obtener el userId:', error);
      }
    });
  }

  cargarOpcionesYSeleccion(): void {
    this.preguntaIds.forEach(preguntaId => {
      this.cargarOpciones(preguntaId);
      this.cargarOpcionSeleccionada(preguntaId);
    });
  }

  cargarOpciones(preguntaId: number): void {
    this.seleccionService.getOpcionesByPreguntaId(preguntaId).subscribe({
      next: opciones => this.opciones[preguntaId] = opciones,
      error: error => console.error(`Error al cargar las opciones para preguntaId ${preguntaId}:`, error)
    });
  }

  cargarOpcionSeleccionada(preguntaId: number): void {
    this.seleccionService.getOpcionSeleccionada(this.userId, preguntaId).subscribe({
      next: opcion => {
        console.log(`Opción seleccionada para preguntaId ${preguntaId}:`, opcion);
        this.opcionSeleccionada[preguntaId] = opcion;
      },
      error: (error: HttpErrorResponse) => {
        if (error.error instanceof ErrorEvent) {
          // Client-side error
          console.error(`Error al cargar la opción seleccionada para preguntaId ${preguntaId}:`, error.error.message);
        } else {
          // Server-side error
          console.error(`Error al cargar la opción seleccionada para preguntaId ${preguntaId}:`, error);
          console.error('Response body:', error.error);
        }
      }
    });
  }

  enableEditing() {
    this.isEditing = true;
  }

  updateUserInfo() {
    this.userService.updateUserInfo(this.userInfo, this.token).subscribe(updatedUser => {
      console.log('User info updated:', updatedUser);
    });
  }

  onSubmit() {
    // Actualizar solo los campos editados
    const updatedUser: User = { ...this.userInfo, ...this.editedFields };

    this.userService.updateUserInfo(this.token, updatedUser).subscribe(updatedUser => {
      console.log('User updated:', updatedUser);
      this.isEditing = false;
    });
  }

  addEditedField(fieldName: string, value: any) {
    this.editedFields[fieldName as keyof Partial<User>] = value;
  }

  cancelEdit() {
    this.isEditing = false;
  }

  obtenerUserIdYSeleccion(): void {
    this.userService.getUserId().subscribe({
      next: userId => {
        this.userId = userId;
        this.obtenerSeleccionesUsuario();
      },
      error: error => {
        console.error('Error al obtener el clienteId:', error);
      }
    });
  }

  obtenerSeleccionesUsuario(): void {
    if (this.userId !== undefined) {
      this.seleccionService.obtenerSelecciones(this.userId)
        .subscribe({
          next: selecciones => {
            this.selecciones = selecciones;
            console.log('Selecciones:', this.selecciones);
          },
          error: error => {
            console.error('Error al obtener las selecciones:', error);
          }
        });
    } else {
      console.error('userId no definido');
    }
  }

  guardarSeleccion(preguntaId: number) {
    let seleccionId: number;

    this.seleccionService.obtenerSeleccionId(this.userId, preguntaId).subscribe(id => {
      seleccionId = id;

      this.preguntaService.getPreguntaTexto(preguntaId).subscribe(pregunta => {
        const cambioSeleccionRequest = new CambioSeleccionRequest(seleccionId, pregunta.texto, this.opcionSeleccionada[preguntaId]);
        console.log('Datos de selección a enviar:', cambioSeleccionRequest);
        this.seleccionService.updateSeleccion(cambioSeleccionRequest).subscribe(resultado => {
          console.log('Datos de selección a enviar:', resultado);
          console.log('Emitiendo evento de actualización completada');
          this.actualizacionCompletada.emit();
        });
        this.modoEditar[preguntaId - 1] = false;
      });
    });
  }

  cancelarSeleccion(preguntaId: number) {
    this.modoEditar[preguntaId - 1] = false;
  }

  obtenerDetalles(userId: number): void {
    console.log('Obteniendo descripción del análisis del backend...');
    this.seleccionService.obtenerDescripcionDeAnalisisPorClienteId(userId).subscribe({
      next: (descripciones: string[]) => {
        console.log('Descripciones del análisis:', descripciones);
        this.descripciones = descripciones;
        this.parsedDescripciones = descripciones.map(descripcion => this.parseDescription(descripcion));
        if (descripciones.length === 0) {
          console.warn('No se encontraron descripciones de análisis, enviando prompt al backend...');
          this.comunicacionService.enviarPromptAlBackend(userId);
        }
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al obtener descripciones del análisis:', error);
        this.isLoading = false;
        if (error.status === 403) {
          this.errorMessage = 'Acceso prohibido. Verifica tus credenciales.';
        } else {
          this.errorMessage = 'Ocurrió un error inesperado.';
        }
      }
    });
  }

  parseDescription(descripcion: string | undefined | null): any {
    if (!descripcion) {
      console.error('Descripción JSON no proporcionada o indefinida:', descripcion);
      return {};
    }
    try {
      return JSON.parse(descripcion);
    } catch (error) {
      console.error('Error al analizar la descripción JSON:', error);
      return {};
    }

  }

  toggleContent(index: number) {
    this.showFullContent[index] = !this.showFullContent[index];
    this.currentCardIndex = this.showFullContent[index] ? index : null;
  }

  getBackgroundColor(index: number) {
    return index === 5 ? '#D8B4E2' : '#F5F5F5';
  }

  isLastCard(index: number): boolean {
    return index === this.analisisPersonalizados.length - 1;
  }

  enviarPromptAlBackend_actualizar(): void {

    console.log('Enviando el prompt al backend...');
    const promptRequest = {
      enunciado: 'Send me a .json file with a detailed analysis, taking into account the answers to these questions',
      clienteId: this.userId
    };
    this.seleccionService.generarPrompt3(promptRequest).subscribe({
      next: (promptResponse: any) => {
        console.log('Prompt generado correctamente por el backend:', promptResponse);
        this.obtenerDetalles2(this.userId)
      },
      error: (error) => {
        console.error('Error al generar el prompt en el backend:', error);
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  obtenerDetalles2(userId: number): void {
    console.log('Obteniendo descripción del análisis del backend...');
    this.seleccionService.obtenerDescripcionDeAnalisisPorClienteId(userId).subscribe({
      next: (descripciones: string[]) => {
        console.log('Descripciones del análisis:', descripciones);
        this.descripciones = descripciones;
        this.parsedDescripciones = descripciones.map(descripcion => this.parseDescription(descripcion));
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al obtener descripciones del análisis:', error);
        this.isLoading = false;
        if (error.status === 403) {
          this.errorMessage = 'Acceso prohibido. Verifica tus credenciales.';
        } else {
          this.errorMessage = 'Ocurrió un error inesperado.';
        }
      }
    });
  }

  onActualizacionCompletada() {
    console.log('Se recibió el evento de actualización completada');
    this.cargarDatos();
  }

  cargarDatos() {
    console.log('Cargando datos...');
    this.obtenerSeleccionesUsuario();
  }

  confirmarActualizacion() {
    const confirmacion = confirm("Are you sure? You will lose the previous analysis.");
    if (confirmacion) {
      this.enviarPromptAlBackend_actualizar();
      const isLoading = !this.isLoading;
      this.onLoading(isLoading);
    } else {
      this.onLoading(false);
      console.log("La actualización fue cancelada.");
    }
  }

  confirmarActualizacionRutinas() {
    const confirmacion = confirm("Are you sure? Your routines for the next month will change.");
    if (confirmacion) {
      this.comunicacionService.generarPrompt(this.userId);
    } else {
      console.log("La actualización fue cancelada.");
    }
  }

  onLoading(isLoading: boolean): void {
    this.loadingChanged.emit(isLoading);
  }
}
