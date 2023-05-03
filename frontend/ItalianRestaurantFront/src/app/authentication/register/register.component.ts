import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {tap} from "rxjs";
import {AuthService} from "../auth/auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent{
  registerForm: FormGroup
  constructor(private http: HttpClient,
              private authService: AuthService) {
    this.registerForm = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    })
  }

  onRegisterSubmit(){
    let firstName = this.registerForm.value['firstName'];
    let lastName = this.registerForm.value['lastName'];
    let email = this.registerForm.value['email'];
    let password = this.registerForm.value['password'];
    this.authService.signup(firstName,lastName,email,password)
  }

}
