import { Component } from '@angular/core';
import {DataStorageService} from "../shared/data-storage.service";
import {Order, OrderRes} from "../models/order";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {
  orders: OrderRes[] = []
  isContentLoaded = false;
  constructor(private dataStorageService: DataStorageService) {
    this.dataStorageService.getOrders()
      .subscribe(
        (res) => {
          this.orders = res;
          console.log(this.orders)
          for(let order of this.orders){
            console.log(order.mealOrders)
          }
          this.isContentLoaded = true;
        }
      )
  }

  calculateSum(order: OrderRes){
    var sum = 0;
    for(let meals of order.mealOrders){
      sum += meals.price
    }

    return sum;
  }


}
