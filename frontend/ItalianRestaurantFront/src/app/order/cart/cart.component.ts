import { Component } from '@angular/core';
import {CartService} from "../../shared/cart.service";
import {Meal} from "../../models/meal";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {
  cart: {meal: Meal,quantity: number}[];
  constructor(private cartService: CartService) {
    this.cart = this.cartService.cart
  }

  calculateSum(){
    return this.cartService.calculateSum()
  }




}
