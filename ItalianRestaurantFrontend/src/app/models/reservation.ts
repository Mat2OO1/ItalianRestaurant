import {Table} from "./table";
import {Tab} from "bootstrap";

export class Reservation{
  id?: number;
  table: Table;
  reservationDateStart: string;


  constructor(table: Table, reservationDateStart: string, id?: number) {
    this.id = id;
    this.table = table;
    this.reservationDateStart = reservationDateStart;
  }
}
