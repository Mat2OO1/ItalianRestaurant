import {Meal} from "../models/meal";
import {Subject} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable()
export class CartService {
  cartSubject = new Subject<{ meal: Meal, quantity: number, price: number }[]>()
  cart: { meal: Meal, quantity: number, price: number }[] = []

  getCurrentCart() {
    const cartData: { meal: Meal, quantity: number, price: number }[] = JSON.parse(localStorage.getItem('cartData')!);
    if (cartData) {
      this.cart = cartData
      this.cartSubject.next(this.cart)
    }
  }

  addToCart(meal: Meal) {
    let item = this.cart.find(item => item.meal.name === meal.name)
    if (item) {
      item.quantity += 1;
      item.price = item.meal.price * item.quantity
    } else {
      this.cart.push({meal: meal, quantity: 1, price: meal.price})
    }
    this.cartSubject.next(this.cart);
    localStorage.setItem("cartData", JSON.stringify(this.cart))
  }

  removeOneFromCart(meal: Meal) {
    let item = this.cart.find(item => item.meal === meal)
    if (item != null) {
      if (item.quantity > 1)
        item.quantity--
      else {
        this.cart = this.cart.filter(elem => elem !== item)
      }
    }
    this.cartSubject.next(this.cart)
    localStorage.setItem("cartData", JSON.stringify(this.cart))
  }

  removeFromCart(meal: Meal) {
    this.cart = this.cart.filter(elem => elem.meal !== meal)
    this.cartSubject.next(this.cart)
    localStorage.setItem("cartData", JSON.stringify(this.cart))
  }

  calculateSum() {
    let sum: number = 0
    this.cart.forEach(item => sum += item.meal.price * item.quantity)
    return sum;
  }

  clearCart() {
    this.cart = []
    this.cartSubject.next([])
    localStorage.removeItem("cartData")
  }

}
