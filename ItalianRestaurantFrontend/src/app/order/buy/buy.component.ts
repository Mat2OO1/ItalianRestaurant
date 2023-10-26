import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {DataStorageService} from "../../shared/data-storage.service";
import {CartService} from "../../shared/cart.service";
import {Delivery} from "../../models/delivery";
import {PaymentResponse} from "../../models/payment-response";

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.css']
})
export class BuyComponent {
  isContentLoaded = false;
  buyForm: FormGroup;

  constructor(private router: Router,
              private dataStorageService: DataStorageService,
              private cartService: CartService) {
    this.buyForm = new FormGroup({
      address: new FormControl('', [Validators.required]),
      city: new FormControl('', [Validators.required]),
      pcode: new FormControl('', [Validators.required]),
      floor: new FormControl(''),
      info: new FormControl(''),
      deliveryOption: new FormControl(''),


    })
    this.isContentLoaded = true;
  }

  onBuySubmit() {
    let address = this.buyForm.value['address']
    let city = this.buyForm.value['city'];
    let pcode = this.buyForm.value['pcode'];
    let floor = this.buyForm.value['floor'] === '' ? null : this.buyForm.value['floor'];
    let info = this.buyForm.value['info'] === '' ? null : this.buyForm.value['info'];
    let deliveryOption = this.buyForm.value['deliveryOption'] === '' ? null : this.buyForm.value['deliveryOption'];
    this.dataStorageService.makeAnOrder(
      new Delivery(address, city, pcode, floor, info, deliveryOption), this.cartService.cart
    )
      .subscribe(
        (res: PaymentResponse) => {
          console.log("lala")
          window.location.href = res.url;
        }
      )
    this.cartService.clearCart()
  }

}
