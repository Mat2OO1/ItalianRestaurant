import {Table} from "./table";

export class Reservation{
  tableId: number;
  reservationDateStart: Date;


  constructor(tableId: number, reservationDateStart: Date) {
    this.tableId = tableId;
    this.reservationDateStart = reservationDateStart;
  }
}
