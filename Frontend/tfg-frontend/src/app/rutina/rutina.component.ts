import { Component, OnInit } from '@angular/core';
import { SeleccionService } from '../services/seleccion/seleccion.service';
import { UserService } from '../services/user/user.service';
import { Rutina } from './rutina';
import { PromptResponse } from '../pregunta/promptResponse';
import { Ejercicio } from '../pregunta/ejercicio';
import { ComunicacionService } from '../services/comunicacion/comunicacion.service';

@Component({
  selector: 'app-rutina',
  templateUrl: './rutina.component.html',
  styleUrls: ['./rutina.component.css']
})
export class RutinaComponent implements OnInit {
  titulo = 'DAILY ROUTINE';
  userId: number = 0;
  rutina: Rutina | null = null;
  rutinaId: number = 0;
  ejercicios: Ejercicio[] = [];
  isLoading = true;
  route: any;
  parsedDescripcion: string | undefined;

  parsedDescripciones: { [key: string]: string }[] = [];
  currentCardIndex: number | null = null;

  realizado: boolean = false;
  rutinaCompletada = false;

  siguienteRutinaId: number | null = null;
  mensaje: string | null = null;


  ultimaRutinaId: number = 0;
  finalRutina: boolean = false;
  ultimaRutinaVisualizada: Rutina = {} as Rutina;
  allExercisesCompleted: boolean = false;

  constructor(private seleccionService: SeleccionService,
    private userService: UserService,
    private comunicacionService: ComunicacionService) { }

  ngOnInit(): void {
    this.userService.getUserId().subscribe({
      next: (userId) => {
        console.log('ID del usuario:', userId);
        this.userId = userId;
        //this.obtenerPrimeraRutina(userId);
        this.rutinaPorUsuario(userId);
      },
      error: (error) => {
        console.error('Error al obtener el ID del usuario:', error);
      }
    });
  }

  rutinaPorUsuario(userId: number): void {
    this.seleccionService.recuperarUltimaRutinaVisualizada(userId).subscribe({
      next: (rutina: Rutina) => {
        console.log('Se encontró una última rutina vista:', rutina);
        const rutinaId = rutina.id;
        this.comunicacionService.enviarRutinaId(rutinaId);
        this.rutinaId = rutinaId;
        this.actualizarUltimaVisualizacion(rutinaId);
        this.obtenerEjerciciosPorRutina(rutinaId);
      },
      error: (err) => {
        if (err.status === 204) { // No Content
          this.isLoading = false;
          this.obtenerPrimeraRutina(userId);
        }
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  obtenerPrimeraRutina(userId: number): void {
    this.seleccionService.obtenerIdDeRutinaPorUsuario(userId).subscribe({
      next: (primeraRutinaId: number) => {
        if (primeraRutinaId !== null && primeraRutinaId !== undefined) {
          console.log('ID de la primera rutina asociada al usuario:', primeraRutinaId);
          this.rutinaId = primeraRutinaId;
          this.actualizarUltimaVisualizacion(primeraRutinaId);
          this.obtenerEjerciciosPorRutina(primeraRutinaId);
        } else {
          console.error('No se encontró una primera rutina asociada al usuario.');
          this.enviarPromptAlBackend();
        }
      },
      error: (err) => {
        console.error('Error al obtener la primera rutina del usuario:', err);
        this.enviarPromptAlBackend();
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  /*rutinaPorUsuario(userId: number): void {
    this.seleccionService.recuperarUltimaRutinaVisualizada(userId).subscribe({
      next: (rutina: Rutina) => {
        if (rutina) {
          console.log('Se encontró una última rutina vista:', rutina);
          const rutinaId = rutina.id;
          this.comunicacionService.enviarRutinaId(rutinaId);
          this.rutinaId = rutinaId;
          this.actualizarUltimaVisualizacion(rutinaId);
          this.obtenerEjerciciosPorRutina(rutinaId);
        } else {
          this.seleccionService.obtenerIdDeRutinaPorUsuario(userId).subscribe({
            next: (primeraRutinaId: number) => {
              if (primeraRutinaId !== null && primeraRutinaId !== undefined) {
                console.log('ID de la primera rutina asociada al usuario:', primeraRutinaId);
                this.rutinaId = primeraRutinaId;
                this.actualizarUltimaVisualizacion(primeraRutinaId);
                this.obtenerEjerciciosPorRutina(primeraRutinaId);
              } else {
                console.error('No se encontró una primera rutina asociada al usuario.');
                this.enviarPromptAlBackend();
              }
            },
            error: (err) => {
              console.error('Error al obtener la primera rutina del usuario:', err);
              this.enviarPromptAlBackend();
            },
            complete: () => {
              this.isLoading = false;
            }
          });
        }
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }*/

  actualizarUltimaVisualizacion(rutinaId: number): void {
    this.seleccionService.actualizarUltimaVisualizacion(rutinaId).subscribe(
      () => {
        console.log('Última visualización actualizada correctamente.');
      },
      (error: any) => {
        console.error('Error al actualizar la última visualización:', error);
      }
    );
  }

  obtenerSiguienteRutina(): void {
    this.seleccionService.getSiguienteRutinaId(this.userId, this.rutinaId).subscribe({
      next: (data: number) => {
        if (data !== null && data !== undefined) {
          this.rutinaId = data;
          this.mensaje = `Siguiente rutina cargada con ID: ${data}`;
          this.actualizarUltimaVisualizacion(data);
          this.obtenerEjerciciosPorRutina(data);
        }
      },
      error: (error) => {
        if (error.status === 404) {
          console.log('No se encontró la siguiente rutina, verificando si todas están completas.');
          this.verificarTodasRutinasCompletadas(this.userId);
        } else {
          console.error('Error al obtener el siguiente ID de rutina:', error);
        }
      }
    });
  }

  rutinaSiguienteMes() {
    this.enviarPromptAlBackend();
    this.rutinaCompletada = false;
    this.finalRutina = false;
    this.isLoading = true;
  }

  enviarPromptAlBackend(): void {
    console.log('Enviando el prompt al backend...');
    const promptRequest = {
      enunciado: 'Send me a .json file with an exercise routine, taking into account the answers to these questions',
      //enunciado: 'You are a fitness assistant. Based on the user preferences, you need to create a personalized exercise routine. Send me a .json file with an exercise routine, taking into account the answers to these questions',
      clienteId: this.userId
    };
    this.seleccionService.generarPrompt(promptRequest).subscribe({
      next: (promptResponse: PromptResponse) => {
        console.log('Prompt generado correctamente por el backend:', promptResponse);
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

  obtenerEjerciciosPorRutina(rutinaId: number): void {
    console.log('Obteniendo ejercicios para la rutina con ID:', rutinaId);

    this.seleccionService.obtenerEjerciciosPorRutina(rutinaId).subscribe({
      next: (ejercicios) => {
        this.ejercicios = ejercicios;
        this.isLoading = false;

        this.parsedDescripciones = this.ejercicios.map(ejercicio => this.parseDescription(ejercicio.datos));

        console.log('Ejercicios obtenidos:', this.ejercicios);
        console.log('Descripciones parseadas:', this.parsedDescripciones);
      },
      error: (error) => {
        console.error('Error al obtener los ejercicios', error);
        this.isLoading = false;
      }
    });
  }

  parseDescription(datos: string | undefined | null): any {
    if (!datos) {
      console.error('Descripción JSON no proporcionada o indefinida:', datos);
      return {};
    }
    try {
      return JSON.parse(datos);
    } catch (error) {
      console.error('Error al analizar la descripción JSON:', error);
      return {};
    }
  }

  getImageSrc(first: boolean, last: boolean, index: number): string {
    if (first) {
      return '/assets/ejercicio.png';
    } else if (last) {
      return '/assets/capacitacion.png';
    } else if (index >= 1 && index <= 5) {
      return '/assets/alternativa.jpg';
    } else {
      return '/assets/Inicio_sesion.jpg';
    }
  }

  toggleExerciseCompleted(ejercicio: any): void {
    const newState = !ejercicio.realizado;
    console.log('Ejercicio:', ejercicio);
    this.seleccionService.actualizarEstadoEjercicio(ejercicio.id, newState).subscribe({
      next: () => {
        console.log(`Estado del ejercicio ${ejercicio.id} actualizado a ${newState}`);
        ejercicio.realizado = newState;
        this.checkRutinaCompletada();
      },
      error: (error) => {
        console.error('Error al actualizar el estado del ejercicio', error);
      }
    });
  }

  checkRutinaCompletada() {
    this.seleccionService.checkRutinaCompletada(this.rutinaId).subscribe({
      next: (completada: boolean) => {
        this.rutinaCompletada = completada;
        console.log(completada);
        console.log(this.rutinaId);
        if (completada) {
          console.log('La rutina está completada.');
        } else {
          console.log('La rutina no está completada.');
        }
      },
      error: (error) => {
        console.error('Error al verificar la rutina completada:', error);
      }
    });
  }

  verificarTodasRutinasCompletadas(userId: number): void {
    this.seleccionService.todosEjerciciosCompletos(userId).subscribe({
      next: (completed: boolean) => {
        this.allExercisesCompleted = completed;
        console.log(`Todos los ejercicios completados: ${completed}`);
        if (completed) {
          this.finalRutina = true;
        }
      },
      error: (error) => {
        console.error('Error al verificar los ejercicios completados:', error);
      }
    });
  }

  getEjercicioName(index: number): string {
    if (this.ejercicios[index] && this.parsedDescripciones[index]) {
      return this.parsedDescripciones[index]['Name'];
    }
    return '';
  }

  hasDifficultyLevel(keyValuePair: any, level: number): boolean {
    return keyValuePair.key === 'Difficulty level' && parseInt(keyValuePair.value) === level;
  }

  getBackgroundColor(value: any): string {
    if (typeof value === 'number') {
      switch (value) {
        case 1:
          return '#00986C';
        case 2:
          return '#00AED6';
        case 3:
          return '#4682B4';
        case 4:
          return '#A770B2';
        case 5:
          return '#7B2CBF';
        case 6:
          return '#5A189A';
        case 7:
          return '#005C6F';
        default:
          return '#A9A9A9';
      }
    } else if (typeof value === 'string') {
      switch (value) {
        case '1':
          return '#00986C';
        case '2':
          return '#00AED6';
        case '3':
          return '#4682B4';
        case '4':
          return '#A770B2';
        case '5':
          return '#7B2CBF';
        case '6':
          return '#5A189A';
        case '7':
          return '#005C6F';
        default:
          return '#A9A9A9';
      }
    } else {
      return '';
    }
  }
}

