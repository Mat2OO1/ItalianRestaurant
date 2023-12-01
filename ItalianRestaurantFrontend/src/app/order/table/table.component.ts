import {Component} from '@angular/core';
import {Table} from "../../models/table";
import {DataStorageService} from "../../shared/data-storage.service";
import {CartService} from "../../shared/cart.service";
import {PaymentResponse} from "../../models/payment-response";
import {SnackbarService} from "../../shared/sncakbar.service";

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent {

  tables?: Table[];
  reservations?: Table[]
  processing = 0;

  constructor(private dataStorageService: DataStorageService,
              private cartService: CartService,
              private snackbarService: SnackbarService) {
    this.dataStorageService.getTables()
      .subscribe(
        res => {
          this.tables = res
        }
      )
    this.dataStorageService.getReservedTables()
      .subscribe(
        res => {
          console.log(res)
          this.reservations = res
          this.assignStatusToTable()
        }
      )
  }

  proceedOrder(table: number) {
    if (this.cartService.cart.meals.length === 0) {
      this.snackbarService.openSnackbarError('Cart is empty!');
      return;
    }
    this.cartService.addTable(table)
    this.processing = table;
    this.dataStorageService.makeAnOrder(this.cartService.cart)
      .subscribe(
        (res: PaymentResponse) => {
          this.cartService.clearCart();
          window.location.href = res.url;
        })
  }

  assignStatusToTable(){
    const reservedTablesIds = this.reservations!.map( table => table.id);
    reservedTablesIds.forEach(id => {
      let table = this.tables?.filter(table => table.id === id)[0]
      table!.reserved = true
    })
  }
}
