import {Meal} from "../models/meal";

export class CartService{
  cart: {meal: Meal,quantity: number}[] = []


  addToCart(meal: Meal){
    let item = this.cart.find(item => item.meal === meal)
    if(item){
      item.quantity +=1;
    }
    else{
      this.cart.push({meal:meal, quantity: 1});
    }

    console.log(this.cart)
  }
}
