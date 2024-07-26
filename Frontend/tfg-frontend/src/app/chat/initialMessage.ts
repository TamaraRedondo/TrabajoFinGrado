import { Ejercicio } from "../pregunta/ejercicio";

export interface InitialMessage {
    content: string;
    ejercicios: Ejercicio[]; 
}