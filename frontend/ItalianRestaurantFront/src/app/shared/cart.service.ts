import {Meal} from "../models/meal";

export class CartService{
  cart: {meal: Meal,quantity: number}[] = [{meal: new Meal('Carbonara','http://kuchnia-domowa.pl/images/content/176/spaghetti-carbonara.jpg','grana padano, pasta, basil', 6.99), quantity: 3},
    {meal: new Meal('Aglio Oglio','https://italia-by-natalia.pl/wp-content/uploads/2020/11/aglio-olio-e-peperoncino.jpg','olive oil, garlic, pasta', 10.49), quantity: 3}]

  addToCart(meal: Meal){
    let item = this.cart.find(item => item.meal === meal)
    if(item != null){
      item.quantity +=1;
    }
    else{
      this.cart.push({meal:meal, quantity: 1});
    }
    console.log(this.cart)
  }
  calculateSum(){
    let sum =0
    this.cart.forEach(item => sum += item.meal.price * item.quantity)
    return sum;
  }
}
