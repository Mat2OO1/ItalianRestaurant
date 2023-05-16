import { Component } from '@angular/core';
import {DataStorageService} from "../shared/data-storage.service";
import {Order, OrderRes} from "../models/order";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {DatePipe, formatDate} from "@angular/common";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {
  orders: OrderRes[] = []
  isContentLoaded = false;
  forms: FormGroup[] = [];

  constructor(private dataStorageService: DataStorageService,
              private datePipe: DatePipe,
              private formBuilder: FormBuilder) {
    this.dataStorageService.getOrders()
      .subscribe(
        (res) => {
          this.orders = res;
          this.isContentLoaded = true;
          this.forms = this.orders.map(data =>
            this.formBuilder.group({
              deliveryDate: new FormControl(this.customFormatDate(data.delivery.deliveryDate!)),
              orderStatus: new FormControl(data.orderStatus),
            })
          );
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

  updateOrder(orderId: number, formId: number){
    const form = this.forms[formId];
    let orderStatus = form.value['orderStatus']
    let deliveryDate = new Date()
    deliveryDate.setHours(form.value['deliveryDate'].substring(0,2))
    deliveryDate.setMinutes(form.value['deliveryDate'].substring(3,5))
    let deliveryDateFormatted = formatDate(deliveryDate, 'YYYY-MM-ddTHH:mm:ss', 'en-GB')
    console.log(deliveryDateFormatted)
    this.dataStorageService.updateOrder(orderStatus,deliveryDateFormatted, orderId)
  }

  customFormatDate(date: Date){
    if(date != null){
      return formatDate(date, 'HH:mm', 'en-GB')
    }
    else{
      return '';
    }
  }



}
