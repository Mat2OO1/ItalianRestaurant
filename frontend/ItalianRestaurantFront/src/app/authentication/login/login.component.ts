import { Component } from '@angular/core';
import {FormControl, FormGroup, Validator, Validators} from "@angular/forms";
import {AuthService} from "../auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  error: string = ''

  constructor(private authService: AuthService,
              private router: Router) {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    })
  }

  onLoginSubmit() {
    let email = this.loginForm.value['email'];
    let password = this.loginForm.value['password'];
    this.authService.login(email, password)
      .subscribe(
        resData => {
          if(resData.role === "ADMIN"){
            this.router.navigate(['./admin-panel'])
          }
          else{
            this.router.navigate(['./menu'])

          }
        },
        errorMessage => {
          this.loginForm.reset()
          this.error = errorMessage;
        }
      )
  }


}
