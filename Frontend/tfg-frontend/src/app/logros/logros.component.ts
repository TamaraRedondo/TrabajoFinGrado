import { Component, OnInit } from '@angular/core';
import { Achievement } from './achievement';
import { Objetivo } from '../objetivo/objetivo';
import { SeleccionService } from '../services/seleccion/seleccion.service';
import { Rutina } from '../rutina/rutina';

@Component({
  selector: 'app-logros',
  templateUrl: './logros.component.html',
  styleUrl: './logros.component.css'
})
export class LogrosComponent implements OnInit{
  selectedTrophyId: number = 1; 
  selectedAchievement: Achievement | null = null; 
  objetivo: Objetivo = {
    id: 0,
    ejerciciosRealizados: 0,
    rutinasRealizadas: 0,
    horasTotales: 0,
    puntosObtenidos: 0
  };

  rutinas: Rutina[] = [];

  morningCount: number = 0;
  afternoonCount: number = 0;
  sameTimeOfDay: boolean = false;

  constructor(private seleccionService: SeleccionService) {}

  ngOnInit(): void {
    const clienteId = 5602; 
    this.seleccionService.obtenerObjetivo(clienteId).subscribe((objetivo: Objetivo) => {
      this.objetivo = objetivo;
      
    });

    this.seleccionService.obtenerRutinaPorClienteId(clienteId).subscribe(
      (rutinas: Rutina[]) => {
        console.log('Rutinas fetched:', rutinas);
    
        this.morningCount = this.countMorningRoutines(rutinas);
        this.afternoonCount = this.countAfternoonRoutines(rutinas);
        this.sameTimeOfDay = this.allSameTimeOfDay(rutinas);

        console.log(`Número de rutinas realizadas por la mañana: ${this.morningCount}`);
        console.log(`Número de rutinas realizadas por la tarde: ${this.afternoonCount}`);
        console.log(`¿Todas las rutinas fueron realizadas en el mismo momento del día?: ${this.sameTimeOfDay ? 'Sí' : 'No'}`);
        this.actualizarLogros();
      },
      (error) => {
        console.error('Error fetching rutinas', error);
      }
    );
  }

  achievements: Achievement[] = [
    {
      id: 1,
      title: 'Complete 5 routines',
      description: 'Complete 5 exercise routines to unlock this achievement. Track your progress and stay committed to your fitness goals.',
      benefits: 'Improved stamina and energy levels, better overall health and mood.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 2,
      title: 'Complete 10 routines',
      description: 'Complete 10 exercise routines to unlock this achievement. Build consistency in your workout routine and see noticeable improvements in your fitness levels.',
      benefits: 'Increased strength and endurance, enhanced mental clarity and focus.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 3,
      title: 'Complete 20 routines',
      description: 'Complete 20 exercise routines to unlock this achievement. Challenge yourself to maintain a regular exercise regimen and achieve significant fitness milestones.',
      benefits: 'Enhanced cardiovascular health, improved muscle tone and flexibility.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 4,
      title: 'Complete 50 routines',
      description: 'Complete 50 exercise routines to unlock this achievement. Reach a major milestone in your fitness journey and celebrate your dedication and hard work.',
      benefits: 'Achieve sustainable weight management, reduce stress and boost self-confidence.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 5,
      title: 'Complete 100 routines',
      description: 'Complete 100 exercise routines to unlock this achievement.  Establish a weekly workout routine to maintain consistency and progress.',
      benefits: 'Establish healthy habits, improve discipline and motivation.',
      unlocked: false,
      isTrophy: true
    },
    {
      id: 6,
      title: '24 hours of activity',
      description: 'You have been active for the equivalent of one full day (24 hours). Build momentum in your fitness journey and establish a foundation for long-term success.',
      benefits: 'Form lasting fitness habits, increase accountability and dedication.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 7,
      title: 'One week of activity',
      description: 'You have been active for the equivalent of one full week (168 hours). Experience the cumulative benefits of regular exercise and improve overall fitness levels.',
      benefits: 'Enhanced endurance and resilience, better sleep quality and mood.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 8,
      title: 'One month of activity',
      description: 'You have been active for the equivalent of one full month (720 hours). Transform your fitness routine into a sustainable lifestyle and achieve significant health benefits.',
      benefits: 'Increased muscle strength and flexibility, reduced risk of chronic diseases.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 9,
      title: 'Five months of activity',
      description: 'You have been active for the equivalent of five months (3,600 hours). Establish a strong fitness foundation and enjoy continued improvements in your physical and mental well-being.',
      benefits: 'Enhanced cardiovascular health, boosted immune system.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 10,
      title: 'Ten months of activity',
      description: 'You have been active for the equivalent of ten months (7,200 hours). Celebrate a major milestone in your fitness journey and enjoy the long-term benefits of regular exercise.',
      benefits: 'Improved overall fitness, increased longevity and quality of life.',
      unlocked: false,
      isTrophy: true
    },
    {
      id: 11,
      title: 'Achieve 50 points',
      description: 'Achieve a total of 50 points to unlock this achievement. Earn points by completing routines and track your progress towards your fitness goals.',
      benefits: 'Increased motivation and accountability, measurable progress in fitness goals.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 12,
      title: 'Achieve 100 points',
      description: 'Achieve a total of 100 points to unlock this achievement. Continue to accumulate points and challenge yourself to achieve higher fitness milestones.',
      benefits: 'Enhanced physical and mental well-being, improved self-esteem.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 13,
      title: 'Achieve 200 points',
      description: 'Achieve a total of 200 points to unlock this achievement. Maintain consistency in your workouts and unlock rewards as you progress towards your fitness goals.',
      benefits: 'Stronger muscles and bones, increased energy levels.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 14,
      title: 'Achieve 500 points',
      description: 'Achieve a total of 500 points to unlock this achievement. Stay committed to your fitness journey and celebrate significant achievements along the way.',
      benefits: 'Improved cardiovascular health, reduced stress levels.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 15,
      title: 'Achieve 1000 points',
      description: 'Achieve a total of 1000 points to unlock this achievement. Reach a major milestone in your fitness journey and enjoy the benefits of a healthier lifestyle.',
      benefits: 'Increased strength and endurance, enhanced overall well-being.',
      unlocked: false,
      isTrophy: true
    },
    {
      id: 16,
      title: 'Complete a routine in the morning',
      description: 'Complete an exercise routine in the morning to unlock this achievement. Start your day with physical activity to boost energy levels and improve focus.',
      benefits: 'Enhanced productivity throughout the day, improved mood and mental clarity.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 17,
      title: 'Complete a routine in the afternoon',
      description: 'Complete an exercise routine in the afternoon to unlock this achievement. Stay active and maintain momentum in your fitness routine.',
      benefits: 'Increased energy levels, stress relief.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 18,
      title: 'Complete 20 routines in the morning',
      description: 'Complete 20 exercise routines in the morning to unlock this achievement. Build consistency in your morning workout routine and experience the benefits of regular exercise.',
      benefits: 'Improved metabolism, better physical and mental health.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 19,
      title: 'Complete 20 routines in the afternoon',
      description: 'Complete 20 exercise routines in the afternoon to unlock this achievement. Incorporate exercise into your afternoon routine for better fitness and overall well-being.',
      benefits: 'Enhanced cardiovascular fitness, improved muscle tone.',
      unlocked: false,
      isTrophy: false
    },
    {
      id: 20,
      title: 'Complete all routines in the morning or afternoon',
      description: 'Complete all exercise routines exclusively in either the morning or afternoon to unlock this achievement. Establish a consistent workout schedule that fits your daily routine.',
      benefits: 'Enhanced workout efficiency, improved time management.',
      unlocked: false,
      isTrophy: true
    }
  ];

  selectAchievement(id: number): void {
    this.selectedTrophyId = id;
    this.selectedAchievement = this.achievements.find(achievement => achievement.id === id) || null;
  }

  getDescription(id: number): string {
    const achievement = this.achievements.find(achievement => achievement.id === id);
    
    if (achievement) {
      let description = `${achievement.description}<br><br>`;
      description += `<strong>Benefits:</strong> ${achievement.benefits}<br><br>`;
      description += `<strong>Unlocked:</strong> ${achievement.unlocked ? 'Yes' : 'No'}`;
      return description;
    } else {
      return 'Achievement not found';
    }
  }

  getTitle(id: number): string {
    const achievement = this.achievements.find(a => a.id === id);
    return achievement ? achievement.title.toUpperCase() : 'Achievement not found';
  }

  isUnlocked(id: number): boolean {
    const achievement = this.achievements.find(achievement => achievement.id === id);
    return achievement ? achievement.unlocked : false;
  }

  actualizarLogros(): void {
    if (!this.objetivo) return;
    
    this.achievements.forEach(logro => {
      if (logro.id === 1 && this.objetivo.rutinasRealizadas >= 5) {
        logro.unlocked = true;
      } else if (logro.id === 2 && this.objetivo.rutinasRealizadas >= 10) {
        logro.unlocked = true;
      } else if (logro.id === 3 && this.objetivo.rutinasRealizadas >= 20) {
        logro.unlocked = true;
      } else if (logro.id === 4 && this.objetivo.rutinasRealizadas >= 50) {
        logro.unlocked = true;
      } else if (logro.id === 5 && this.objetivo.rutinasRealizadas >= 100) {
        logro.unlocked = true;
      } else if (logro.id === 6 && this.objetivo.horasTotales >= 24) {
        logro.unlocked = true;
      } else if (logro.id === 7 && this.objetivo.horasTotales >= 168) {
        logro.unlocked = true;
      } else if (logro.id === 8 && this.objetivo.horasTotales >= 720) {
        logro.unlocked = true;
      } else if (logro.id === 9 && this.objetivo.horasTotales >= 3600) {
        logro.unlocked = true;
      } else if (logro.id === 10 && this.objetivo.horasTotales >= 7200) {
        logro.unlocked = true;
      } else if (logro.id === 11 && this.objetivo.puntosObtenidos >= 50) {
        logro.unlocked = true;
      } else if (logro.id === 12 && this.objetivo.puntosObtenidos >= 100) {
        logro.unlocked = true;
      } else if (logro.id === 13 && this.objetivo.puntosObtenidos >= 200) {
        logro.unlocked = true;
      } else if (logro.id === 14 && this.objetivo.puntosObtenidos >= 500) {
        logro.unlocked = true;
      } else if (logro.id === 15 && this.objetivo.puntosObtenidos >= 1000) {
        logro.unlocked = true;
      } else if (logro.id === 16 && this.morningCount >= 1) {
        logro.unlocked = true;
      } else if (logro.id === 17 && this.afternoonCount >= 1) {
        logro.unlocked = true;
      } else if (logro.id === 18 && this.morningCount >= 20) {
        logro.unlocked = true;
      } else if (logro.id === 19 && this.afternoonCount >= 20) {
        logro.unlocked = true;
      } else if (logro.id === 20 && this.sameTimeOfDay) {
        logro.unlocked = true;
      }      
    });
  }

  private countMorningRoutines(rutinas: Rutina[]): number {
    return rutinas.filter(rutina => this.isMorning(new Date(rutina.rutinaRealizada))).length;
  }
  
  private countAfternoonRoutines(rutinas: Rutina[]): number {
    return rutinas.filter(rutina => this.isAfternoon(new Date(rutina.rutinaRealizada))).length;
  }
  
  private allSameTimeOfDay(rutinas: Rutina[]): boolean {
    const hasMorning = rutinas.some(rutina => this.isMorning(new Date(rutina.rutinaRealizada)));
    const hasAfternoon = rutinas.some(rutina => this.isAfternoon(new Date(rutina.rutinaRealizada)));
  
    return hasMorning !== hasAfternoon;
  }
  
  private isMorning(date: Date): boolean {
    const hour = date.getHours();
    return hour >= 6 && hour < 14; 
  }
  
  private isAfternoon(date: Date): boolean {
    const hour = date.getHours();
    return hour >= 14 && hour < 23; 
  }
  
}

