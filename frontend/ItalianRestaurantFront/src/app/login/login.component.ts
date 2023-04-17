import { Component } from '@angular/core';
import {FormControl, FormGroup, Validator, Validators} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor() {
    this.loginForm = new FormGroup({
      name: new FormControl('',[Validators.required]),
      surname: new FormControl('',[Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),

    })
  }

  onLoginSubmit(){
    let name = this.loginForm.value['name']
    let surname = this.loginForm.value['surname'];
    let email = this.loginForm.value['email'];
    let password = this.loginForm.value['password'];
    console.log(name);
    console.log(surname);
    console.log(email);
    console.log(password)
  }


}
