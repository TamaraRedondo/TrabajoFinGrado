import { Opcion } from "./opcion";

export class Pregunta {
    id!: number;
    texto!: string;
    esDesarrollo!: boolean;
    opciones!: Opcion[];
}
  
