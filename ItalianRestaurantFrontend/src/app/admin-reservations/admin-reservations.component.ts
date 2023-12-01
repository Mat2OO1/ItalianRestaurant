import {Component} from '@angular/core';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import {DateAdapter} from '@angular/material/core';
import {DataStorageService} from '../shared/data-storage.service';
import {Reservation} from '../models/reservation';
import {MatTableDataSource} from '@angular/material/table';
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-admin-reservations',
  templateUrl: './admin-reservations.component.html',
  styleUrls: ['./admin-reservations.component.css'],
})
export class AdminReservationsComponent {
  selectedDate?: Date;
  showTables = false;
  dataSource = new MatTableDataSource();

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
          this.showTables = true
        });
    }
  }
}
