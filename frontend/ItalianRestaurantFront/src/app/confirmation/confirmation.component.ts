import {Component, OnDestroy, ViewChild} from '@angular/core';
import {Meal} from "../models/meal";
import {CartService} from "../shared/cart.service";
import {OrderService} from "../shared/order.service";

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent {
  order: {meal: Meal,quantity: number}[]
  orderNumber: number | undefined
  sum: number;
  orderDetails: {date: Date, status: string} = {
    // @ts-ignore
    date: null,
    status: ''
  }
  constructor(private cartService: CartService,
              private orderService: OrderService) {
    this.orderService.getOrderDetails()
    this.orderService.orderDetails
      .subscribe(
        (res: {date: Date, status: string}) => {
          this.adjustStatus()
          const status = res.status.toLowerCase().replace(/_/g, ' ').replace(/\b\w/g, (c) => c.toUpperCase());
          this.orderDetails = {date: res.date , status: status}
        }
      )
    this.order = this.cartService.cart;
    this.sum = this.cartService.calculateSum()
    this.orderNumber = this.orderService.orderId;
  }

  adjustStatus(){
    const circle2 = document.getElementById("circle2")!
    const circle3 = document.getElementById("circle3")!
    const progressBar = document.getElementById("indicator")!
    if(this.orderDetails.status.toLowerCase() == 'in delivery'){
      circle2.classList.add("active")
      progressBar.style.width = "50%";
    }
    else if(this.orderDetails.status.toLowerCase() == 'delivered'){
      circle3.classList.add("active")
      progressBar.style.width = "100%";
    }
  }

}
