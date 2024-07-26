export class PromptRequest {
    enunciado: string;
    clienteId: number;
  
    constructor(enunciado: string, clienteId: number) {
      this.enunciado = enunciado;
      this.clienteId = clienteId;
    }
}