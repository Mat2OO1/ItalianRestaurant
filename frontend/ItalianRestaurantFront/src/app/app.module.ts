import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NavComponent } from './welcome/nav/nav.component';
import { InfoComponent } from './welcome/info/info.component';
import { LoginComponent } from './authentication/login/login.component';
import {RouterLink, RouterOutlet} from "@angular/router";
import {AppRoutingModule} from "./app.routing.module";
import {MenuComponent} from "./order/menu/menu.component";
import {ReactiveFormsModule} from "@angular/forms";
import {RegisterComponent} from "./authentication/register/register.component";
import {CartComponent} from "./order/cart/cart.component";
import {CartService} from "./shared/cart.service";
import {SummaryComponent} from "./order/summary/summary.component";
import {BuyComponent} from "./order/buy/buy.component";
import {ConfirmationComponent} from "./order/confirmation/confirmation.component";
import {OrderService} from "./shared/order.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthService} from "./authentication/auth/auth.service";
import {AuthInterceptorService} from "./authentication/auth/auth-interceptor.service";
import {MealsService} from "./shared/meals.service";
import {DataStorageService} from "./shared/data-storage.service";
import {AuthGuard} from "./authentication/auth/auth.guard";
import {ForgotPasswordComponent} from "./authentication/forgot-password/forgot-password.component";

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    InfoComponent,
    LoginComponent,
    MenuComponent,
    RegisterComponent,
    CartComponent,
    SummaryComponent,
    BuyComponent,
    ConfirmationComponent,
    ForgotPasswordComponent
  ],
  imports: [
    BrowserModule,
    RouterLink,
    RouterOutlet,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    CartService,
    OrderService,
    AuthService,
    MealsService,
    DataStorageService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
