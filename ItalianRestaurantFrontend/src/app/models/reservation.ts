import {Table} from "./table";
import {Tab} from "bootstrap";

export class Reservation{
  id?: number;
  table: Table;
  reservationDateStart: Date;


  constructor(table: Table, reservationDateStart: Date, id?: number) {
    this.id = id;
    this.table = table;
    this.reservationDateStart = reservationDateStart;
  }
}
