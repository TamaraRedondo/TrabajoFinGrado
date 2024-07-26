import { Message } from "./message";

export interface chatResponse {
  done: boolean;
  message: Message;
}


  /*Para asegurarte de que el historial de la conversación se mantenga en cada solicitud al API, debes enviar todos los mensajes anteriores junto con el nuevo mensaje cada vez que el usuario envíe uno nuevo. Esto puede lograrse modificando el método sendMessage en tu componente ChatComponent. Aquí te muestro cómo puedes hacerlo:

Modifica el servicio de chat: Asegúrate de que el método sendMessage en tu ChatService acepte y envíe todos los mensajes anteriores al API.

Actualizar el componente ChatComponent: Modifica el método sendMessage para enviar todos los mensajes almacenados, no solo el nuevo mensaje.*/