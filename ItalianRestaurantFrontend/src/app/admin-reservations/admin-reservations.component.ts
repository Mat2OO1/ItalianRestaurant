import {Component} from '@angular/core';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import {DateAdapter} from '@angular/material/core';
import {DataStorageService} from '../shared/data-storage.service';
import {MatTableDataSource} from '@angular/material/table';
import {DatePipe} from "@angular/common";
import {Reservation} from "../models/reservation";

@Component({
  selector: 'app-admin-reservations',
  templateUrl: './admin-reservations.component.html',
  styleUrls: ['./admin-reservations.component.css'],
})
export class AdminReservationsComponent {
  selectedDate?: Date;
  showTables = false;
  dataSource = new MatTableDataSource();
  defaultDate: Date = new Date();

  constructor(
    private dateAdapter: DateAdapter<Date>,
    private dataStorageService: DataStorageService,
    private datePipe: DatePipe
  ) {
    this.dateAdapter.setLocale('pl-PL');
  }

  onChangeDate(event: MatDatepickerInputEvent<Date>) {
    if (event.value) {
      this.selectedDate = event.value;
      const formattedDate = this.datePipe.transform(this.selectedDate, 'yyyy-MM-dd')!
      this.dataStorageService.getReservationsForDay(formattedDate).subscribe(
        (response) => {
          this.dataSource.data = response;
          console.log(response);
          if (response.length > 0) this.showTables = true;
          else this.showTables = false
        });
    }
  }

  getUsername(reservation : Reservation) {
    return reservation.user?.username ? reservation.user.username : reservation.user?.firstName + " " + reservation.user?.lastName;
  }
}
