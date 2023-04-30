import {Component, OnDestroy} from '@angular/core';
import {Meal} from "../models/meal";
import {CartService} from "../shared/cart.service";
import {Subscription} from "rxjs";
import {OrderService} from "../shared/order.service";
import {DataStorageService} from "../shared/data-storage.service";

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent {
  order: {meal: Meal,quantity: number}[]
  orderNumber = "1414dasrqwda"
  sum: number;
  orderDetails: {date: string, status: string} = {
    date: '',
    status: ''
  }
  constructor(private cartService: CartService,
              private orderService: OrderService) {
    this.orderService.getOrderDetails()
    this.orderService.orderDetails
      .subscribe(
        (res: {date: string, status: string}) => {
          const status = res.status.toLowerCase().replace(/_/g, ' ').replace(/\b\w/g, (c) => c.toUpperCase());
          this.orderDetails = {date: res.date , status: status}
          console.log(this.orderDetails)
        }
      )
    this.order = this.cartService.cart;
    this.sum = this.cartService.calculateSum()
  }

}
