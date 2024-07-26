import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user/user.service';
import { SeleccionService } from '../services/seleccion/seleccion.service';
import { Objetivo } from './objetivo';
import { Rutina } from '../rutina/rutina';
declare var $: any;
import { ComunicacionService } from '../services/comunicacion/comunicacion.service';
import { Ejercicio } from '../pregunta/ejercicio';
import moment from 'moment';


@Component({
  selector: 'app-objetivo',
  templateUrl: './objetivo.component.html',
  styleUrl: './objetivo.component.css'
})
export class ObjetivoComponent implements OnInit {
  userId: number = 0;
  objetivo: Objetivo | undefined;
  rutinas: Rutina[] = [];
  rutinaDescripcion: any;
  imagePaths: string[] = [];

  rutinasRealizadas: Date[] = [];
  week: string[] = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday"
  ];

  monthSelect: any[] = [];
  dateSelect: any;
  dateValue: any;
  ejerciciosDeRutina: Ejercicio[] = [];

  diasAColor: Date[] = [];

  cardImages: string[] = [
    'assets/1.jpeg', 
    'assets/1.jpg', 
    'assets/2.jpg', 
    'assets/3.jpg',
    'assets/4.jpg',
    'assets/5.jpg',
    'assets/6.jpg'
  ];

  ejercicios: Ejercicio[] = [];
  mensaje: string = '';

  constructor(private userService: UserService,
    private seleccionService: SeleccionService,
    private comunicacionService: ComunicacionService) { }

  highlightedDays: Date[] = [];
  numeroRutinasNoRealizadas: number = 0;
  ngOnInit(): void {
    this.userService.getUserId().subscribe(id => {
      this.userId = id;
      console.log('ID del usuario:', this.userId);
      this.actualizarObjetivo(this.userId);
      this.obtenerRutinasRealizadas();

      this.seleccionService.obtenerNumeroRutinasNoRealizadas(this.userId)
        .subscribe(numero => {
          this.numeroRutinasNoRealizadas = numero;
          this.calcularDiasAColor(numero);
        });
    });

    this.getDaysFromDate(6, 2024)
    this.comunicacionService.highlightedDaysChanged.subscribe(
      (highlightedDays: Date[]) => {
        this.highlightedDays = highlightedDays
        this.applyRutinasRealizadasToCalendar(highlightedDays);
      }
    );
  }

  calcularDiasAColor(numeroRutinas: number): void {
    const ultimaFechaRealizada = this.rutinasRealizadas.length > 0 ? new Date(this.rutinasRealizadas[this.rutinasRealizadas.length - 1]) : null;
    console.log('Última fecha realizada:', ultimaFechaRealizada);
    const totalDiasMes = this.monthSelect.length;

    if (ultimaFechaRealizada) {
      while (this.diasAColor.length < numeroRutinas) {
        const diaAleatorio = Math.floor(Math.random() * totalDiasMes) + 1;
        const dayObject = new Date(this.dateSelect.year(), this.dateSelect.month(), diaAleatorio);

        if (!this.diasAColor.some(date => date.getDate() === dayObject.getDate())) {
          if (dayObject.getTime() >= ultimaFechaRealizada.getTime()) {
            this.diasAColor.push(dayObject);
          }
        }
      }
      console.log('Días a colorear calculados:', this.diasAColor);

      this.monthSelect.forEach(day => {
        const dayObject = new Date(this.dateSelect.year(), this.dateSelect.month(), day.value);
        day.isHighlighted = this.diasAColor.some(date => date.getDate() === dayObject.getDate());
      });
    }
  }

  actualizarObjetivo(clienteId: number): void {
    this.seleccionService.actualizarObjetivo(clienteId).subscribe(
      () => {
        console.log('Objetivo actualizado correctamente');
        this.obtenerObjetivo();
      },
      error => {
        console.error('Error al actualizar el objetivo:', error);
      }
    );
  }

  obtenerObjetivo(): void {
    this.seleccionService.obtenerObjetivo(this.userId).subscribe(data => {
      this.objetivo = data;
      console.log(this.objetivo);
    });
  }

  obtenerRutinasRealizadas(): void {
    this.seleccionService.obtenerRutinaRealizadaPorClienteId(this.userId)
      .subscribe((rutinas: Date[]) => {
        this.rutinasRealizadas = rutinas;
        console.log('Fechas de rutinas realizadas:', this.rutinasRealizadas);
        this.comunicacionService.emitHighlightedDays(this.rutinasRealizadas);
      });
  }

  applyRutinasRealizadasToCalendar(highlightedDays: Date[]) {
    console.log('Fechas resaltadas recibidas:', highlightedDays);

    this.monthSelect.forEach(day => {
      const dayObject = moment(`${this.dateSelect.year()}-${this.dateSelect.month() + 1}-${day.value}`);
      day.isRutinaRealizada = highlightedDays.some(date => moment(date).isSame(dayObject, 'day'));
    });
    console.log('Estado actual del mes:', this.monthSelect);
  }

  parseRutinaDescripcion(descripcion: string | undefined | null): { key: string, value: string }[] {
    if (!descripcion) {
      console.error('Descripción JSON no proporcionada o indefinida:', descripcion);
      return []; 
    }
    try {
      const parsedDescripcion = JSON.parse(descripcion);
      return Object.keys(parsedDescripcion).map(key => {
        const value = parsedDescripcion[key];
        return { key: key, value: JSON.stringify(value) };
      });
    } catch (error) {
      console.error('Error al analizar la descripción JSON de la rutina:', error);
      return [];
    }
  }

  getDaysFromDate(month: number, year: number) {
    const startDate = moment.utc(`${year}/${month}/01`)
    const endDate = startDate.clone().endOf('month')
    this.dateSelect = startDate;

    const diffDays = endDate.diff(startDate, 'days', true)
    const numberDays = Math.round(diffDays);

    const arrayDays = Object.keys([...Array(numberDays)]).map((a: any) => {
      a = parseInt(a) + 1;
      const dayObject = moment(`${year}-${month}-${a}`);
      return {
        name: dayObject.format("dddd"),
        value: a,
        indexWeek: dayObject.isoWeekday()
      };
    });

    this.monthSelect = arrayDays;
  }

  changeMonth(flag: number) {
    if (flag < 0) {
      const prevDate = this.dateSelect.clone().subtract(1, "month");
      this.getDaysFromDate(prevDate.format("MM"), prevDate.format("YYYY"));
    } else {
      const nextDate = this.dateSelect.clone().add(1, "month");
      this.getDaysFromDate(nextDate.format("MM"), nextDate.format("YYYY"));
    }

    this.applyRutinasRealizadasToCalendar(this.highlightedDays);
  }

  clickDay(day: { value: any; }) {
    const monthYear = this.dateSelect.format('YYYY-MM');
    const parse = `${monthYear}-${day.value}`;
    const objectDate = moment(parse);
    this.dateValue = objectDate;

    const rutinaSeleccionada = this.rutinasRealizadas.find(rutina =>
      moment(rutina).isSame(objectDate, 'day')
    );

    if (rutinaSeleccionada) {
      this.onClickRutinaRealizada(rutinaSeleccionada);
      this.mensaje = '';
    } else {
      this.ejerciciosDeRutina = [];
      this.mensaje = 'You dont have any routines for that day.';
    }
  }

  onClickRutinaRealizada(fecha: Date): void {
    const fechaFormateada = moment(fecha).format('YYYY-MM-DDTHH:mm:ss.SSS');
    this.seleccionService.obtenerEjerciciosPorFecha(fechaFormateada).subscribe({
      next: (ejercicios) => {
        this.ejerciciosDeRutina = ejercicios;
        console.log('Ejercicios de la rutina:', ejercicios);
      }
    });
  }
}
