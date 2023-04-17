import { Component } from '@angular/core';
import {Meal} from "../models/meal";
import {CartService} from "../shared/cart.service";

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent {
  cart: {meal: Meal,quantity: number}[];
  constructor(private cartService: CartService) {
    this.cart = this.cartService.cart
  }

  increaseQuantity(item: {meal: Meal,quantity: number}){
    item.quantity++
    console.log(this.cartService.cart)
  }

  decreaseQuantity(item: {meal: Meal,quantity: number}){
    if(item.quantity > 0)
      item.quantity--
    console.log(this.cartService.cart)
  }

  calculateSum(){
    return this.cartService.calculateSum();
  }
}
