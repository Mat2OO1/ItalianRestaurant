import {Component} from '@angular/core';
import {DataStorageService} from "../shared/data-storage.service";
import {Table} from "../models/table";
import {MatDialog} from "@angular/material/dialog";
import {Reservation} from "../models/reservation";
import {ReserveTableDialogComponent} from "./reserve-table-dialog/reserve-table-dialog.component";
import {CancelReservationDialogComponent} from "./cancel-reservation-dialog/cancel-reservation-dialog.component";
import {SnackbarService} from "../shared/sncakbar.service";
import {TranslateService} from "@ngx-translate/core";

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
              private snackbarService: SnackbarService,
              private translate: TranslateService) {
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
      if (result) {
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
          this.translate.get('table_reserved_successfully').subscribe((message) => {
            this.snackbarService.openSnackbarSuccess(message);
          });
          this.getReservations();
        },
        (err) => {
          this.translate.get('table_reserved_successfully').subscribe((message) => {
            this.snackbarService.openSnackbarError(message);
          });
        }
      )
    }
  }

  getReservations() {
    this.dataStorageService.getUserReservations()
      .subscribe(
        (res) => {
          this.userReservations = res;
        }
      )
  }

}
