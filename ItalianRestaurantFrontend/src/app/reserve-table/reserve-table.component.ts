import {Component} from '@angular/core';
import {DataStorageService} from "../shared/data-storage.service";
import {Table} from "../models/table";
import {MatDialog} from "@angular/material/dialog";
import {Reservation} from "../models/reservation";
import {ReserveTableDialogComponent} from "./reserve-table-dialog/reserve-table-dialog.component";
import {DatePipe} from "@angular/common";
import {ToastService} from "../shared/toast.service";

@Component({
  selector: 'app-reserve-table',
  templateUrl: './reserve-table.component.html',
  styleUrls: ['./reserve-table.component.css']
})
export class ReserveTableComponent {
  tables ?: Table[]

  constructor(private dataStorageService: DataStorageService,
              private dialog: MatDialog,
              private toastService: ToastService) {
    this.dataStorageService.getTables()
      .subscribe(
        (res) => {
          this.tables = res
        }
      )
  }


  openTableDialog(table?: Table) {
    const dialogRef = this.dialog.open(ReserveTableDialogComponent, {
      data: {table: table},
    });
    dialogRef.afterClosed().subscribe(result => {
      this.handleDialogResult(result);
    });
  }

  private handleDialogResult(result: Reservation) {
    if (result) {
      this.dataStorageService.makeReservation(result).subscribe(
        (res) => {
          console.log(res)
          this.toastService.showSuccessToast("Reservation", "Table reserved successfully")
        }
      )
    }
  }

}
