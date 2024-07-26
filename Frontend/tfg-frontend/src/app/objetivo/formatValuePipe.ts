import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formatValue'
})
export class FormatValuePipe implements PipeTransform {
  transform(value: any): string {
    // Verificar si value tiene la propiedad Datos y si es una cadena JSON
    if (value && value.datos && this.isJsonString(value.datos)) {
      const datos = JSON.parse(value.datos);
      //delete datos['Technical explanation'];
      return this.formatJsonData(datos);
    } else {
      return '';
    }
  }

  private isJsonString(str: string): boolean {
    try {
      JSON.parse(str);
      return true;
    } catch (e) {
      return false;
    }
  }

  private formatJsonData(jsonData: any): string {
    let formatted = '';
    const name = jsonData.Name; // Guardamos el nombre del ejercicio
    delete jsonData.Name; // Eliminamos la clave "Name" del objeto
    formatted += `<h5><strong>${name.toUpperCase()}</strong></h5>`;  // Título con el valor en mayúsculas
    formatted += '<hr>'; // Separador entre ejercicios
    formatted += '<div>'; // Div para el contenido de los datos

    // Verificar si existen las claves "Sets" y "Repetitions" y formatearlas
    if (jsonData.Sets && jsonData.Repetitions) {
      formatted += `<div><strong>Sets/Repetitions:</strong> ${jsonData.Sets} x ${jsonData.Repetitions}</div>`;
      delete jsonData.Sets; // Eliminar "Sets" del objeto para que no se muestre de nuevo
      delete jsonData.Repetitions; // Eliminar "Repetitions" del objeto para que no se muestre de nuevo
    }
    

    // Formatear el resto de los datos
    for (const key in jsonData) {
      if (jsonData.hasOwnProperty(key)) {
        formatted += `<div><strong>${this.formatKey(key)}:</strong> ${jsonData[key]}</div>`;
        formatted += '<br>'; // Separador entre ejercicios
      }
    }
    formatted += '</div>';
    return formatted;
  }

  private formatKey(key: string): string {
    return key.replace(/_/g, ' ').replace(/\b\w/g, char => char.toUpperCase());
  }
}
