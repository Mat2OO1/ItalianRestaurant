export class Table {
  id: number;
  number: number;
  seats: number;
  reserved: boolean;

  constructor(id: number, number: number, seats: number, reserved: boolean) {
    this.id = id;
    this.number = number;
    this.seats = seats;
    this.reserved = reserved;
  }
}
