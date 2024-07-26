import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { Pregunta } from './pregunta';
import { PreguntaService } from '../services/pregunta/pregunta.service';
import { Opcion } from './opcion';
import { UserService } from '../services/user/user.service';
import { Router } from '@angular/router';
import { SeleccionService } from '../services/seleccion/seleccion.service';
import { Seleccion } from './seleccion';
import { Rutina } from '../rutina/rutina';
import { ComunicacionService } from '../services/comunicacion/comunicacion.service';

@Component({
  selector: 'app-pregunta',
  templateUrl: './pregunta.component.html',
  styleUrl: './pregunta.component.css'
})
export class PreguntaComponent implements OnInit {
  curiosityQuestions: string[] = [
    "Did you know that a healthy height for an adult varies depending on factors like genetics and ethnicity?",
    "Did you know that muscle mass is denser than fat tissue, so it takes up less space?",
    "Did you know that aging is associated with changes in metabolism, hormone levels, and body composition?",
    "Did you know that nutritional needs can vary between genders due to differences in body composition, hormone levels, and metabolism?",
    "Did you know you can identify your body type by simply grasping your wrist between your thumb and index finger?",
    "Did you know that regular physical activity can improve cardiovascular health, strengthen muscles and bones, and boost mood?",
    "Did you know that even minor injuries can impact your ability to exercise and may require modifications to your fitness routine?",
    "Did you know that the location of an injury can affect rehabilitation strategies and recovery time?",
    "Did you know that exercising outdoors can provide additional benefits such as exposure to fresh air, sunlight, and nature?",
    "Did you know that consistency is key when it comes to training?",
    "Did you know that the duration of your workout can impact the type of adaptations your body undergoes?",
    "Did you know that setting specific, measurable, achievable, relevant, and time-bound (SMART) goals can increase motivation and adherence to exercise?",
    "Did you know that having multiple fitness goals can provide a well-rounded approach to health and performance?",
    "Did you know that walking is a simple and effective form of exercise that offers numerous health benefits?",
    "Did you know that sleep is crucial for recovery, hormone regulation, and cognitive function?",
    "Did you know that staying hydrated is crucial for maintaining bodily functions, regulating body temperature, and aiding nutrient transport?"
  ];
  curiosityTexts: string[] = [
    "In general, a healthy BMI range for adults is between 18.5 and 24.9.",
    "Building muscle through strength training can increase metabolism and improve body composition.",
    "Staying active and maintaining a balanced diet can help support healthy aging.",
    "It's essential to consider these factors when planning your diet.",
    "If your fingers overlap easily, you might be an ectomorph. If they barely touch, you could be a mesomorph. And if they don't touch at all, you may lean towards being an endomorph.",
    "Aim for at least 150 minutes of moderate-intensity exercise per week.",
    "It's essential to listen to your body and seek appropriate medical advice.",
    "Working with a healthcare professional can help develop a personalized treatment plan.",
    "It can also help reduce stress and improve mood.",
    "Aim for at least 3-5 days of exercise per week to see improvements in strength, endurance, and overall fitness.",
    "Longer workouts may prioritize endurance, while shorter, high-intensity sessions can improve anaerobic fitness.",
    "Whether it's running a marathon or improving flexibility, clear goals can guide your fitness journey.",
    "Whether it's weight loss, strength gains, or flexibility improvements, addressing various aspects of fitness can lead to overall wellness.",
    "Aim for at least 30 minutes of brisk walking most days of the week to improve cardiovascular health and maintain weight.",
    "Aim for 7-9 hours of quality sleep per night to support overall health and well-being.",
    "Aim to drink at least 8 glasses (64 ounces) of water per day, or more if you're physically active."
  ];

  preguntas: Pregunta[] = [];
  opciones: Opcion[] = [];
  preguntaActualIndex: number = 0;
  ultimoClienteId!: number;
  preguntaAnteriorSeleccionada: boolean = false;
  showTooltip = false;
  cargando: boolean = true; 
  
  userId: number = 0;
  preguntaTexto: string | null = null;
  opcionSeleccionada: string | null = null;
  respuestaTexto: string | null = null;
  respuestaDesarrollo: string | null = null;
  promptRequest: any;
  rutina: Rutina | null = null;

  preguntasContestadas: number = 0;
  @Output() generarRutina: EventEmitter<void> = new EventEmitter<void>();

  constructor(private preguntaService: PreguntaService,
    private userService: UserService,
    private seleccionService: SeleccionService,
    private router: Router,
    private comunicacionService: ComunicacionService) { }

  ngOnInit(): void {
    this.getPreguntas();

    this.userService.getUserId().subscribe(id => {
      this.userId = id;
      console.log('ID del usuario:', this.userId);
    });
  }

  getPreguntas(): void {
    this.preguntaService.getPreguntas()
      .subscribe(preguntas => this.preguntas = preguntas);
  }

  seleccionarRespuesta(opcion: string): void {
    this.preguntaTexto = this.preguntas[this.preguntaActualIndex].texto;
    this.opcionSeleccionada = opcion;
  }

  mostrarPreguntaAnterior(): boolean {
    return this.preguntaActualIndex > 0;
  }

  anteriorPregunta(): void {
    if (this.mostrarPreguntaAnterior()) {
      this.preguntaActualIndex--;
      this.preguntaAnteriorSeleccionada = true;
    }
  }

  siguientePregunta(): void {
    if (this.preguntaActualIndex < this.preguntas.length - 1) {
      this.preguntaActualIndex++;
      this.opcionSeleccionada = '';
    } else {
      this.router.navigate(['/rutina']);
    }
  }

  mostrarSiguientePregunta(): boolean {
    return !this.ultimaPregunta() && this.opcionSeleccionada !== null || this.preguntaActualIndex < 2;
  }

  ultimaPregunta(): boolean {
    return this.preguntaActualIndex === this.preguntas.length - 1;
  }

  guardarSeleccion() {
    const preguntaActual = this.preguntas[this.preguntaActualIndex]?.texto;

    this.preguntasContestadas++;
    console.log(this.preguntasContestadas);

    if (this.preguntasContestadas === 11) {
      console.log('Alcanzada la pregunta 11, emitiendo evento...');
      this.comunicacionService.ejecutarMetodoEnRutina(this.userId);
    }

    if (this.preguntasContestadas === 16) {
      console.log('Alcanzada la pregunta 16, emitiendo evento...');
      this.comunicacionService.ejecutarMetodoEnDetails(this.userId);
    }

    if (!preguntaActual || !this.opcionSeleccionada) {
      console.error('La pregunta actual u opción seleccionada no son válidas.');
      return; 
    }
    console.log(this.userId);
    console.log(preguntaActual);
    console.log(this.opcionSeleccionada);

    if (this.userId && this.preguntas[this.preguntaActualIndex].texto && this.opcionSeleccionada || this.respuestaDesarrollo) {
      const seleccion: Seleccion = {
        clienteId: this.userId,
        preguntaTexto: this.preguntas[this.preguntaActualIndex].texto,
        opcionSeleccionada: this.opcionSeleccionada,
        respuestaDesarrollo: this.respuestaDesarrollo ?? '' 
      };

      this.seleccionService.guardarSeleccion(seleccion).subscribe({
        next: (data) => {
          console.log('Selección guardada:', data);
        },
        error: (error) => {
          console.error('Error al guardar la selección:', error);
        }
      });
      this.siguientePregunta();
    } else {
      console.error('Datos incompletos');
      console.log(this.userId);
      console.log(this.preguntas[this.preguntaActualIndex].texto);
      console.log(this.opcionSeleccionada);
    }
  }
  limpiarInput() {
    this.opcionSeleccionada = '';
  }
}