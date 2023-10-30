import {Meal} from "../models/meal";
import {Subject} from "rxjs";
import {Injectable} from "@angular/core";
import {Cart} from "../models/cart";

@Injectable()
export class CartService {
  cartSubject = new Subject<Cart>()
  cart: Cart = new Cart()

  getCurrentCart() {
    const cartData: Cart = JSON.parse(localStorage.getItem('cartData')!);
    if (cartData) {
      this.cart = cartData
      this.cartSubject.next(this.cart)
    }
  }

  addToCart(meal: Meal) {
    let item = this.cart.meals.find(item => item.meal.name === meal.name)
    if (item) {
      item.quantity += 1;
      item.price = item.meal.price * item.quantity
    } else {
      this.cart.meals.push({meal: meal, quantity: 1, price: meal.price})
    }
    this.cartSubject.next(this.cart);
    localStorage.setItem("cartData", JSON.stringify(this.cart))
  }

  removeOneFromCart(meal: Meal) {
    let item = this.cart.meals.find(item => item.meal === meal)
    if (item != null) {
      if (item.quantity > 1)
        item.quantity--
      else {
        this.cart.meals = this.cart.meals.filter(elem => elem !== item)
      }
    }
    this.cartSubject.next(this.cart)
    localStorage.setItem("cartData", JSON.stringify(this.cart))
  }

  removeFromCart(meal: Meal) {
    this.cart.meals = this.cart.meals.filter(elem => elem.meal !== meal)
    this.cartSubject.next(this.cart)
    localStorage.setItem("cartData", JSON.stringify(this.cart))
  }

  calculateSum() {
    let sum: number = 0
    this.cart.meals.forEach(item => sum += item.meal.price * item.quantity)
    return sum;
  }

  clearCart() {
    this.cart = new Cart()
    this.cartSubject.next(this.cart)
    localStorage.removeItem("cartData")
  }

  addTable(tableNr: number) {
    this.getCurrentCart();
    this.cart.table = tableNr;
    this.cartSubject.next(this.cart)
    localStorage.setItem("cartData", JSON.stringify(this.cart))
  }

  removeTable() {
    this.cart.table = undefined;
    this.cartSubject.next(this.cart)
    localStorage.setItem("cartData", JSON.stringify(this.cart))
  }
}
