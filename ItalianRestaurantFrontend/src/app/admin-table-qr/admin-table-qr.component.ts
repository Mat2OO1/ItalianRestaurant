import {Component, OnInit} from '@angular/core';
import {QRCodeComponent} from "angularx-qrcode";
import {DataStorageService} from "../shared/data-storage.service";
import {QrCodeService} from "../shared/qr-code.service";
import {Table} from "../models/table";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-admin-table-qr',
  templateUrl: './admin-table-qr.component.html',
  styleUrls: ['./admin-table-qr.component.css']
})
export class AdminTableQrComponent implements OnInit {

  tables?: Table[]

  constructor(
    private dataStorageService: DataStorageService,
    private qrCodeService: QrCodeService) {
  }

  ngOnInit(): void {
    this.dataStorageService.getTables().subscribe(
      (tables) => {
        this.tables = tables
      })
  }

  downloadQrCode(qrcode: QRCodeComponent) {
    this.qrCodeService.downloadQrCode(qrcode)
  }

  generateQrCode(number: number) {
    return `${environment.frontUrl}/reservation/${number}`;
  }
}
