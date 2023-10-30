import {Component, OnInit} from '@angular/core';
import {QRCodeComponent} from "angularx-qrcode";
import {DataStorageService} from "../shared/data-storage.service";
import {QrCodeService} from "../shared/qr-code.service";
import {Table} from "../models/table";
import {environment} from "../../environments/environment";
import {MatDialog} from "@angular/material/dialog";
import {TableEditDialogComponent} from "../table-edit-dialog/table-edit-dialog.component";
import {DialogMode} from "../models/modal-mode";

@Component({
  selector: 'app-admin-table-qr',
  templateUrl: './admin-table-qr.component.html',
  styleUrls: ['./admin-table-qr.component.css']
})
export class AdminTableQrComponent implements OnInit {

  tables?: Table[]
  protected readonly DialogMode = DialogMode;

  constructor(
    private dataStorageService: DataStorageService,
    private qrCodeService: QrCodeService,
    private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.getTables()
  }

  getTables() {
    this.dataStorageService.getTables().subscribe(
      (tables) => {
        this.tables = tables
      })
  }

  downloadQrCode(qrcode: QRCodeComponent) {
    this.qrCodeService.downloadQrCode(qrcode)
  }

  generateQrUrl(number: number) {
    return `${environment.frontUrl}/menu?table=${number}`;
  }

  openTableDialog(mode: DialogMode, table?: Table) {
    const dialogRef = this.dialog.open(TableEditDialogComponent, {
      data: {mode: mode, table: table},
    });
    dialogRef.afterClosed().subscribe(result => {
      this.handleDialogResult(mode, result);
    });
  }

  private handleDialogResult(mode: DialogMode, result: Table | number) {
    if (result) {
      if (mode === DialogMode.ADD && typeof result === 'object') {
        this.dataStorageService.saveTable(result).subscribe()
      } else if (mode === DialogMode.EDIT && typeof result === 'object') {
        this.dataStorageService.saveTable(result).subscribe()
      }
      else if (mode === DialogMode.DELETE && typeof result === 'number') {
        this.dataStorageService.deleteTable(result).subscribe()
      }
      this.getTables();
    }
  }
}
