import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DataStorageService} from "../../shared/data-storage.service";
import {CartService} from "../../shared/cart.service";
import {Delivery} from "../../models/delivery";
import {PaymentResponse} from "../../models/payment-response";
import {SnackbarService} from "../../shared/sncakbar.service";

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.css']
})
export class BuyComponent {
  isContentLoaded = false;
  buyForm: FormGroup;
  lastDelivery ?: Delivery
  showHint = true;
  processing = false;

  constructor(private dataStorageService: DataStorageService,
              private cartService: CartService,
              private snackbarService: SnackbarService) {
    this.dataStorageService.getLastDeliveryInfo()
      .subscribe(
        (res) => {
          this.lastDelivery = res;
        }
      )
    this.buyForm = new FormGroup({
      address: new FormControl('', [Validators.required]),
      city: new FormControl('', [Validators.required]),
      pcode: new FormControl('', [Validators.pattern('^\\d{2}-\\d{3}$'), Validators.required]),
      floor: new FormControl('', [Validators.pattern('^[0-9]*$')]),
      info: new FormControl(''),
      deliveryOption: new FormControl('')
    })
    this.isContentLoaded = true;
  }

  onBuySubmit() {
    if (this.cartService.cart.meals.length === 0) {
      this.snackbarService.openSnackbarError('Cart is empty!');
      return;
    }
    if (this.buyForm.invalid) return;
    this.processing = true;
    let address = this.buyForm.value['address']
    let city = this.buyForm.value['city'];
    let pcode = this.buyForm.value['pcode'];
    let floor = this.buyForm.value['floor'] === '' ? null : this.buyForm.value['floor'];
    let info = this.buyForm.value['info'] === '' ? null : this.buyForm.value['info'];
    let deliveryOption = this.buyForm.value['deliveryOption'] === '' ? null : this.buyForm.value['deliveryOption'];
    this.dataStorageService.makeAnOrder(
      this.cartService.cart, new Delivery(address, city, pcode, floor, info, deliveryOption)
    )
      .subscribe(
        (res: PaymentResponse) => {
          this.cartService.clearCart();
          window.location.href = res.url;
        }
      )
  }


  onPopulateInput() {
    this.buyForm.get('address')?.setValue(this.lastDelivery?.address);
    this.buyForm.get('city')?.setValue(this.lastDelivery?.city);
    this.buyForm.get('pcode')?.setValue(this.lastDelivery?.postalCode);
    this.buyForm.get('floor')?.setValue(this.lastDelivery?.floor);
    this.buyForm.get('info')?.setValue(this.lastDelivery?.info);
    this.buyForm.get('deliveryOption')?.setValue(this.lastDelivery?.deliveryOptions);
    this.showHint = false;
  }

}
