import {Component, OnDestroy} from '@angular/core';
import {Meal} from "../models/meal";
import {CartService} from "../shared/cart.service";
import {Subscription} from "rxjs";
import {OrderService} from "../shared/order.service";

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnDestroy{
  order: {meal: Meal,quantity: number}[]
  orderNumber = "1414dasrqwda"
  subscription: Subscription
  orderStatus: string = '';
  constructor(private cartService: CartService,
              private orderService: OrderService) {
    this.order = this.cartService.cart;
    this.subscription = this.orderService.orderStatus.subscribe(
      (data) => {
        this.orderStatus = data;
        console.log(data)
      }
    )
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
