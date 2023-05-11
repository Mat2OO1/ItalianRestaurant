import { Component } from '@angular/core';
import {DataStorageService} from "../shared/data-storage.service";
import {Order, OrderRes} from "../models/order";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {
  orders: OrderRes[] = []
  isContentLoaded = false;
  orderDetailsForm: FormGroup;

  constructor(private dataStorageService: DataStorageService,
              private datePipe: DatePipe) {
    this.dataStorageService.getOrders()
      .subscribe(
        (res) => {
          this.orders = res;
          for(let order of this.orders){
          }
          this.isContentLoaded = true;
        }
      )
    this.orderDetailsForm = new FormGroup({
      deliveryDate: new FormControl(''),
      orderStatus: new FormControl(''),
    })
  }

  calculateSum(order: OrderRes){
    var sum = 0;
    for(let meals of order.mealOrders){
      sum += meals.price
    }

    return sum;
  }

  updateOrder(orderId: number){
    let orderStatus = this.orderDetailsForm.value['orderStatus']
    let deliveryDate = new Date()
    deliveryDate.setHours(this.orderDetailsForm.value['deliveryDate'].substring(0,2))
    deliveryDate.setMinutes(this.orderDetailsForm.value['deliveryDate'].substring(3,5))
    deliveryDate.setHours(deliveryDate.getHours() + 2)
    let deliveryDateFormatted = deliveryDate.toLocaleString('en-US', { timeZone: 'Europe/Paris' });
    console.log(deliveryDateFormatted)
    this.dataStorageService.updateOrder(orderStatus,deliveryDate.toISOString(), orderId)
  }


}
