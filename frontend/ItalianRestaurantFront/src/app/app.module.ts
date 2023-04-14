import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NavComponent } from './nav/nav.component';
import { InfoComponent } from './info/info.component';
import { LoginComponent } from './login/login.component';
import {RouterLink, RouterOutlet} from "@angular/router";
import {AppRoutingModule} from "./app.routing.module";
import {MenuComponent} from "./menu/menu.component";
import {ReactiveFormsModule} from "@angular/forms";
import {RegisterComponent} from "./register/register.component";
import {CartComponent} from "./cart/cart.component";
import {CartService} from "./shared/cart.service";

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    InfoComponent,
    LoginComponent,
    MenuComponent,
    RegisterComponent,
    CartComponent
  ],
  imports: [
    BrowserModule,
    RouterLink,
    RouterOutlet,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [CartService],
  bootstrap: [AppComponent]
})
export class AppModule { }
