import { Rutina } from "../rutina/rutina";

export interface Ejercicio {
    id: number;
    nombre: string;
    datos: string;
    realizado: boolean;
    clienteId: number;
    rutina: Rutina;
  }