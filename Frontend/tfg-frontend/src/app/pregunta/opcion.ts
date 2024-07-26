import { Pregunta } from "./pregunta";

export class Opcion {
    id!: number;
    texto!: string;
    esDesarrollo!:Boolean;
    pregunta!: Pregunta;
}

