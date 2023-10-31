import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Meal} from "../../models/meal";
import {CartService} from "../../shared/cart.service";
import {Subscription} from "rxjs";
import {Cart} from "../../models/cart";
import {Router} from "@angular/router";
import {DataStorageService} from "../../shared/data-storage.service";

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent implements OnInit, OnDestroy {
  cart: Cart;
  sum: number = 0;

  cartSubscription!: Subscription | null
  selectedDeliveryOption?: string;
  constructor(private cartService: CartService,
              private router: Router,
              private dataStorageService: DataStorageService) {
    this.cart = this.cartService.cart
    this.cartSubscription = this.cartService.cartSubject
      .subscribe(
        (cartChanged => this.cart = cartChanged)
      );
    this.calculateSum()
  }

  ngOnInit(): void {
    this.cartService.getCurrentCart()
    this.calculateSum()
  }

  increaseQuantity(item: { meal: Meal, quantity: number }) {
    this.cartService.addToCart(item.meal)
    this.calculateSum()

  }

  decreaseQuantity(item: { meal: Meal, quantity: number }) {
    this.cartService.removeOneFromCart(item.meal)
    this.calculateSum()

  }

  calculateSum() {
    this.sum = this.cartService.calculateSum()
  }

  removeItem(item: { meal: Meal, quantity: number }) {
    this.cartService.removeFromCart(item.meal)
    this.calculateSum()
  }

  ngOnDestroy() {
    this.cartSubscription?.unsubscribe()
  }

  removeTable() {
    this.cartService.removeTable()
  }

  proceed() {
    if (this.cart.table) {
      this.dataStorageService.makeAnOrder(this.cart)
        .subscribe(
          (res) => {
            this.cartService.clearCart()
            window.location.href = res.url;
          }
        )
    }
    else if(this.selectedDeliveryOption === "TAKEAWAY") {
      this.router.navigate(['buy'])
    }
    else if(this.selectedDeliveryOption === "ONSITE") {
      this.router.navigate(['table'])
    }
  }
}
