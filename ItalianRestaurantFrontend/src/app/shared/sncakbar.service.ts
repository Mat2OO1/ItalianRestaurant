import {Injectable} from "@angular/core";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({providedIn: 'root'})
export class SnackbarService {

  constructor(private _snackBar: MatSnackBar) {
  }
  openSnackbarSuccess(message: string) {
    this._snackBar.open(message, 'OK', {
      duration: 2000,
      panelClass: ['success-snackbar'],
      horizontalPosition: "right",
      verticalPosition: "top"
    });
  }

  openSnackbarError(message: string) {
    this._snackBar.open(message, 'OK', {
      duration: 3000,
      panelClass: ['warn-snackbar'],
      horizontalPosition: "right",
      verticalPosition: "top"
    });
  }

  openSnackbarInfo(message: string) {
    this._snackBar.open(message, 'OK', {
      duration: 3000,
      panelClass: ['info-snackbar'],
      horizontalPosition: "right",
      verticalPosition: "top"
    });
  }
}
