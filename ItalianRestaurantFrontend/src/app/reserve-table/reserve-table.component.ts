import {Component, ViewEncapsulation} from '@angular/core';
import {DataStorageService} from "../shared/data-storage.service";
import {Table} from "../models/table";
import {MatDialog} from "@angular/material/dialog";
import {Reservation} from "../models/reservation";
import {ReserveTableDialogComponent} from "./reserve-table-dialog/reserve-table-dialog.component";
import {ToastService} from "../shared/toast.service";
import {CancelReservationDialogComponent} from "./cancel-reservation-dialog/cancel-reservation-dialog.component";

@Component({
  selector: 'app-reserve-table',
  templateUrl: './reserve-table.component.html',
  styleUrls: ['./reserve-table.component.css'],
})
export class ReserveTableComponent {
  tables ?: Table[]
  userReservations: Reservation[] = []

  constructor(private dataStorageService: DataStorageService,
              private dialog: MatDialog,
              private toastService: ToastService) {
    this.dataStorageService.getTables()
      .subscribe(
        (res) => {
          this.tables = res
        }
      )
    this.getReservations();
  }


  openTableDialog(table?: Table) {
    const dialogRef = this.dialog.open(ReserveTableDialogComponent, {
      data: {table: table},
    });
    dialogRef.afterClosed().subscribe(result => {
      this.handleDialogResult(result);
    });
  }

  openCancelationDialog(reservation: Reservation) {
    const dialogRef = this.dialog.open(CancelReservationDialogComponent, {
      data: {reservation: reservation},
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result){
        this.dataStorageService.cancelReservation(reservation)
          .subscribe(
            (res) => {
              this.getReservations();
            }
          )
      }
    });
  }

  private handleDialogResult(result: Reservation) {
    if (result) {
      this.dataStorageService.makeReservation(result).subscribe(
        (res) => {
          this.toastService.showSuccessToast("Reservation", "Table reserved successfully")
          this.getReservations();
        },
        (err) => {
          this.toastService.showErrorToast("Reservation", err.error)
        }
      )
    }
  }

  getReservations(){
    this.dataStorageService.getUserReservations()
      .subscribe(
        (res) => {
          this.userReservations = res;
        }
      )
  }

}
