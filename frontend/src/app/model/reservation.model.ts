import { Payment } from "./payment.model";
import { Room } from "./room.model";

export interface Reservation {
  id: string;
  reservationDate: string;
  checkInDate: string;
  checkOutDate: string;
  status: string;
  userId: string;
  roomId: string;
  room: Room;
  amount: Payment;
  createdBy?: string;
  createdTime?: string;
  updatedBy?: string;
  updatedTime?: string;
}
