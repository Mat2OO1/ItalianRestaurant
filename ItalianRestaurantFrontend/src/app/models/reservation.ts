import {Table} from "./table";
import {Tab} from "bootstrap";
import {Profile} from "./profile";

export class Reservation{
  id?: number;
  table: Table;
  reservationDateStart: string;
  user?: Profile;


  constructor(table: Table, reservationDateStart: string, user?: Profile, id?: number) {
    this.id = id;
    this.table = table;
    this.reservationDateStart = reservationDateStart;
    this.user = user;
  }
}
