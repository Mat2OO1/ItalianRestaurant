import {Component, OnInit} from '@angular/core';
import {Meal} from "../../models/meal";
import {CartService} from "../../shared/cart.service";

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent implements OnInit{
  cart: {meal: Meal,quantity: number, price: number}[] = [];
  sum: number = 0;
  constructor(private cartService: CartService) {
    this.cart = this.cartService.cart
    this.cartService.cartSubject
      .subscribe(
        (cartChanged => this.cart = cartChanged)
      )
    this.calculateSum()
  }

  ngOnInit(): void {
    this.cartService.getCurrentCart()
    this.calculateSum()
  }

  increaseQuantity(item: {meal: Meal,quantity: number}){
    this.cartService.addToCart(item.meal)
    this.calculateSum()

  }

  decreaseQuantity(item: {meal: Meal,quantity: number}){
    this.cartService.removeOneFromCart(item.meal)
    this.calculateSum()

  }

  calculateSum(){
    this.sum = this.cartService.calculateSum()
  }
  removeItem(item: {meal: Meal,quantity: number}) {
    this.cartService.removeFromCart(item.meal)
    this.calculateSum()
  }

}
