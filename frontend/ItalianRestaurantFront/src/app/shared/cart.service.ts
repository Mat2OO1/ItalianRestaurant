import {Meal} from "../models/meal";

export class CartService{
  cart: {meal: Meal,quantity: number, price: number}[] = []
  addToCart(meal: Meal){
    let item = this.cart.find(item => item.meal === meal)
    if(item != null){
      item.quantity +=1;
      item.price = item.meal.price * item.quantity
    }
    else{
      this.cart.push({meal:meal, quantity: 1, price: meal.price});
    }
    console.log(this.cart)
  }
  calculateSum(){
    let sum: number =0
    this.cart.forEach(item => sum += item.meal.price * item.quantity)
    return sum;
  }
}
