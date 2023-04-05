import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup
  constructor() {
    this.registerForm = new FormGroup({
      name: new FormControl('', [Validators.required]),
      surname: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    })
  }

  onRegisterSubmit(){
    let name = this.registerForm.value['name'];
    let username = this.registerForm.value['username'];
    let email = this.registerForm.value['email'];
    let password = this.registerForm.value['password'];
    console.log(email);
    console.log(password)
  }

}
