import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.css']
})
export class BuyComponent {
  isContentLoaded = false;
  buyForm: FormGroup;

  constructor(private router: Router) {
    this.buyForm = new FormGroup({
      address: new FormControl('',[Validators.required]),
      city: new FormControl('',[Validators.required]),
      pcode: new FormControl('', [Validators.required, Validators.email]),
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
    console.log(address)
    console.log(city)
    console.log(pcode)
    console.log(floor)
    console.log(info)
    console.log(deliveryOption)
    this.router.navigate(["/confirmation"])
  }

}
