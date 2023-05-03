import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {InfoComponent} from "./welcome/info/info.component";
import {LoginComponent} from "./authentication/login/login.component";
import {RegisterComponent} from "./authentication/register/register.component";
import {MenuComponent} from "./order/menu/menu.component";
import {CartComponent} from "./order/cart/cart.component";
import {SummaryComponent} from "./order/summary/summary.component";
import {BuyComponent} from "./order/buy/buy.component";
import {ConfirmationComponent} from "./order/confirmation/confirmation.component";
import {AuthGuard} from "./authentication/auth/auth.guard";
import {ForgotPasswordComponent} from "./authentication/forgot-password/forgot-password.component";

const routes: Routes = [
  {path: '', component: InfoComponent},
  {path: 'login', component: LoginComponent},
  {path: 'reset', component: ForgotPasswordComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'menu', component: MenuComponent, canActivate: [AuthGuard]},
  {path: 'cart', component: CartComponent, canActivate: [AuthGuard]},
  {path: 'summary', component: SummaryComponent, canActivate: [AuthGuard]},
  {path: 'buy', component: BuyComponent, canActivate: [AuthGuard]},
  {path: 'confirmation', component: ConfirmationComponent, canActivate: [AuthGuard]},
  {path: '**', redirectTo: ''}
]

@NgModule({
  imports: [
  CommonModule,
  RouterModule.forRoot(routes)
],
exports: [ RouterModule ]
})
export class AppRoutingModule { }

