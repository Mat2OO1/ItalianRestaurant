import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../auth/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AppConstants} from "../../shared/constants";
import {CartService} from "../../shared/cart.service";
import {Subscription} from "rxjs";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy, OnInit {
  loginForm: FormGroup;
  hide = true;
  error: string = ''
  googleURL = AppConstants.GOOGLE_AUTH_URL
  facebookURL = AppConstants.FACEBOOK_AUTH_URL
  githubURL = AppConstants.GITHUB_AUTH_URL
  authSubscription ?: Subscription;

  constructor(private authService: AuthService,
              private router: Router,
              private cartService: CartService,
              private cookieService: CookieService,
              private activatedRoute: ActivatedRoute) {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    })
  }

  ngOnInit() {
    let token = this.cookieService.get('token');
    if (token) {
      this.authService.saveToken(token);
      this.cookieService.delete('token', '/oauth2/redirect');
      this.authService.user.subscribe(
        data => {
          this.router.navigate(['./menu'])
        }
      );
    }
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['error']) {
        this.error = `Error local`
      }
    });
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
          this.error = errorMessage
        }
      );

    this.cartService.clearCart()
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe()
    }
  }


}
