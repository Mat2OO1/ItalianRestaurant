import {Component, OnDestroy} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../auth/auth.service";
import {Router} from "@angular/router";
import {AppConstants} from "../../shared/constants";
import {CartService} from "../../shared/cart.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy {
  loginForm: FormGroup;
  error: string = ''
  googleURL = AppConstants.GOOGLE_AUTH_URL
  githubURL = AppConstants.GITHUB_AUTH_URL

  authSubscription!: Subscription;

  constructor(private authService: AuthService,
              private router: Router,
              private cartService: CartService) {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    })
  }

  onLoginSubmit() {
    let email = this.loginForm.value['email'];
    let password = this.loginForm.value['password'];
    this.authSubscription = this.authService.login(email, password)
      .subscribe(
        resData => {
          if (resData.role === "ADMIN") {
            this.router.navigate(['./admin-panel'])
          } else {
            this.router.navigate(['./menu'])

          }
        },
        errorMessage => {
          this.loginForm.reset()
          this.error = errorMessage;
        }
      );

    this.cartService.clearCart()
  }

  ngOnDestroy() {
    this.authSubscription.unsubscribe()
  }

}
