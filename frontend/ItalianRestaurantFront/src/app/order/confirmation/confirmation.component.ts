import {Component, HostListener, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Meal} from "../../models/meal";
import {CartService} from "../../shared/cart.service";
import {OrderService} from "../../shared/order.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnInit {
  order: {meal: Meal,quantity: number}[] = []
  orderNumber: number | undefined
  sum: number | undefined;
  orderDetails: {date: Date, status: string} = {
    // @ts-ignore
    date: null,
    status: ''
  }
  isContentLoaded: boolean | undefined
  constructor(private cartService: CartService,
              private http: HttpClient,
              private orderService: OrderService) {}

  adjustStatus(status: string){
    const circle2 = document.getElementById("circle2")!
    const circle3 = document.getElementById("circle3")!
    const progressBar = document.getElementById("indicator")!
    if(status.toLowerCase() == 'in delivery'){
      circle2.classList.add("active")
      progressBar.style.width = "50%";
    }
    else if(status.toLowerCase() == 'delivered'){
      circle2.classList.add("active")
      circle3.classList.add("active")
      progressBar.style.width = "100%";
    }
  }

  ngOnInit(): void {
    this.orderService.orderId = Number(localStorage.getItem("orderId"));
    this.orderService.getOrderDetails()
    this.orderService.orderDetails
      .subscribe(
        (res: {date: Date, status: string}) => {
          const status = res.status.toLowerCase().replace(/_/g, ' ').replace(/\b\w/g, (c) => c.toUpperCase());
          this.adjustStatus(status)
          this.orderDetails = {date: res.date , status: status}
        }
      )
    if(this.cartService.cart.length > 0) {
      this.order = this.cartService.cart;
    }
    else {
      this.orderService.order
        .subscribe(
          (order) => {
            this.order = order
            this.isContentLoaded = true;
          }
        )
    }
    this.sum = this.cartService.calculateSum()
    this.orderNumber = this.orderService.orderId;
    this.isContentLoaded = this.order.length > 0;
  }

  calculateOrder() {
    let sum: number =0
    this.order.forEach(item => sum += item.meal.price * item.quantity)
    return sum;
  }

}
