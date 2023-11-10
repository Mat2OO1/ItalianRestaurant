import {Component, Inject} from '@angular/core';
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {Table} from "../../models/table";
import {MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {ReserveTableComponent} from "../reserve-table.component";
import {Reservation} from "../../models/reservation";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import {DatePipe, NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-cancel-reservation-dialog',
  templateUrl: './cancel-reservation-dialog.component.html',
  styleUrls: ['./cancel-reservation-dialog.component.css'],
  imports: [
    MatDatepickerModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatOptionModule,
    MatSelectModule,
    NgForOf,
    NgIf,
    DatePipe
  ],
  standalone: true
})
export class CancelReservationDialogComponent {

  constructor(@Inject(DIALOG_DATA) public data: { reservation : Reservation },
              private dialogRef: MatDialogRef<ReserveTableComponent>) {
  }

  onCloseDialog(shouldCancel: boolean) {
    this.dialogRef.close(shouldCancel)
  }
}