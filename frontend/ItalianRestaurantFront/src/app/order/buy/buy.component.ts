import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {DataStorageService} from "../../shared/data-storage.service";
import {CartService} from "../../shared/cart.service";
import {Delivery} from "../../models/delivery";
import {OrderService} from "../../shared/order.service";

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
              private orderService: OrderService,
              private cartService: CartService) {
    this.buyForm = new FormGroup({
      address: new FormControl('',[Validators.required]),
      city: new FormControl('',[Validators.required]),
      pcode: new FormControl('', [Validators.required]),
      floor: new FormControl(''),
      info: new FormControl(''),
      deliveryOption: new FormControl(''),


    })
    this.isContentLoaded = true;
  }

  onBuySubmit(){
    let address = this.buyForm.value['address']
    let city = this.buyForm.value['city'];
    let pcode = this.buyForm.value['pcode'];
    let floor = this.buyForm.value['floor'];
    let info = this.buyForm.value['info'];
    let deliveryOption = this.buyForm.value['deliveryOption'];
    this.dataStorageService.makeAnOrder(
      new Delivery(address,city,pcode,floor,info,deliveryOption,), this.cartService.cart
    )
      .subscribe(
      (res: any) => {
        this.orderService.orderId = res.id
        console.log(this.orderService.orderId)
        localStorage.setItem('orderId', JSON.stringify(res.id));
        this.router.navigate(["/confirmation"])
      }
    )
    this.cartService.cart = [];
  }

}
