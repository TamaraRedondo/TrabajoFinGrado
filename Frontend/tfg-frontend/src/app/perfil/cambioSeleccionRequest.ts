export class CambioSeleccionRequest {
    constructor(
      public seleccionId: number,
      public preguntaTexto: string,
      public nuevaOpcionTexto: string
    ) {}
}