import {Component, Inject} from '@angular/core';
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {Table} from "../../models/table";
import {DateAdapter} from '@angular/material/core';
import {DataStorageService} from "../../shared/data-storage.service";
import {MatDatepickerInputEvent} from "@angular/material/datepicker";
import {DatePipe} from "@angular/common";
import {AppConstants} from "../../shared/constants"
import {MatDialogRef} from "@angular/material/dialog";
import {ReserveTableComponent} from "../reserve-table.component";
import {Reservation} from "../../models/reservation";
import {MatSelectChange} from "@angular/material/select";

@Component({
  selector: 'app-reserve-table-dialog',
  templateUrl: './reserve-table-dialog.component.html',
  styleUrls: ['./reserve-table-dialog.component.css']
})
export class ReserveTableDialogComponent {
  dateFetched = false;
  selectedDate?: string
  availableHours: string[] = []
  selectedTime?: string

  constructor(@Inject(DIALOG_DATA) public data: { table: Table },
              private dialogRef: MatDialogRef<ReserveTableComponent>,
              private dateAdapter: DateAdapter<Date>,
              private dataStorageService: DataStorageService,
              private datePipe: DatePipe) {
    this.dateAdapter.setLocale('pl-PL');
  }

  disablePastDates = (date: Date): boolean => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return date > today;
  };

  onChangeDate(event: MatDatepickerInputEvent<Date>) {
    if (event.value) {
      this.selectedDate = this.datePipe.transform(
        event.value,
        "yyyy-MM-dd"
      )!;
      this.dataStorageService
        .getReservationsForTable(
          this.data.table!.id,
          this.selectedDate
        )
        .subscribe(
          (res) => {
            const dates = res.map((date) => new Date(Date.parse(date)));
            this.availableHours = this.generateReservationHours(dates);
            this.dateFetched = true;
          }
        );
    }
  }

  onSelectTime(event: MatSelectChange) {
    this.selectedTime = event.value
  }

  generateReservationHours(hoursBooked: Date[]) {
    const openingTime = AppConstants.RESTAURANT_OPENING_TIME
    const closeTime = AppConstants.RESTAURANT_CLOSE_TIME
    const reservationDuration = AppConstants.RESERVATION_TIME
    const numbers = []
    for (let i = openingTime; i < closeTime; i++) {
      if (!hoursBooked.some(date => (date.getHours() === i))) {
        numbers.push(i + ":00");
      }
    }
    return numbers;
  }

  onSubmit() {
    if(this.selectedTime && this.selectedDate){
      const date = new Date(Date.parse(this.selectedDate + 'T'+this.selectedTime))
      this.dialogRef.close(new Reservation(this.data.table,this.datePipe.transform(date, "yyyy-MM-ddTHH:mm")!))
    }
    else{
      this.dialogRef.close()
    }
  }

  closeDialog() {
    this.dialogRef.close();
  }

}
