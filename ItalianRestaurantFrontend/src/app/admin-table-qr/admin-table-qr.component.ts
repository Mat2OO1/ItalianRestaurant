import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {QRCodeComponent} from "angularx-qrcode";
import {DataStorageService} from "../shared/data-storage.service";
import {QrCodeService} from "../shared/qr-code.service";
import {Table} from "../models/table";
import {environment} from "../../environments/environment";
import {MatDialog} from "@angular/material/dialog";
import {TableEditDialogComponent} from "../table-edit-dialog/table-edit-dialog.component";
import {DialogMode} from "../models/modal-mode";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {MatSort, Sort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {TranslateService} from "@ngx-translate/core";
import {SnackbarService} from "../shared/sncakbar.service";

@Component({
  selector: 'app-admin-table-qr',
  templateUrl: './admin-table-qr.component.html',
  styleUrls: ['./admin-table-qr.component.css']
})
export class AdminTableQrComponent implements OnInit, AfterViewInit {

  tables?: Table[]
  protected readonly DialogMode = DialogMode;

  displayedColumns: string[] = ['number', 'seats', 'qrCode', 'download', 'edit', 'delete'];
  dataSource = new MatTableDataSource();


  @ViewChild(MatSort) sort: MatSort | null = null;
  processing = false;

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
  }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  constructor(
    private dataStorageService: DataStorageService,
    private qrCodeService: QrCodeService,
    private dialog: MatDialog,
    private _liveAnnouncer: LiveAnnouncer,
    private translate: TranslateService,
    private snackBarService: SnackbarService) {
  }

  ngOnInit(): void {
    this.getTables()
  }

  getTables() {
    this.dataStorageService.getTables().subscribe(
      (tables) => {
        this.tables = tables
        this.dataSource.data = tables
        this.processing = false;
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
      this.processing = true;
      if (mode === DialogMode.ADD && typeof result === 'object') {
        this.dataStorageService.saveTable(result).subscribe(() => {
          this.getTables();
        }, error => {
          this.translate.get('table_already_exists').subscribe((message: string) => {
            this.snackBarService.openSnackbarError(message);
          })
          this.processing = false;
        })
      } else if (mode === DialogMode.EDIT && typeof result === 'object') {
        this.dataStorageService.updateTable(result).subscribe(() => {
          this.getTables();
          this.processing = false;
        }, error => {
          this.translate.get('table_already_exists').subscribe((message: string) => {
            this.snackBarService.openSnackbarError(message);
          })
          this.processing = false;
        })
      } else if (mode === DialogMode.DELETE && typeof result === 'number') {
        this.dataStorageService.deleteTable(result).subscribe(() => {
          this.getTables();
        })
      }
    }
  }
}
