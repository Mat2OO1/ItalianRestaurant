import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {InfoComponent} from "./info/info.component";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {MenuComponent} from "./menu/menu.component";
import {CartComponent} from "./cart/cart.component";
import {SummaryComponent} from "./summary/summary.component";
import {BuyComponent} from "./buy/buy.component";
import {ConfirmationComponent} from "./confirmation/confirmation.component";

const routes: Routes = [
  {path: '', component: InfoComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'menu', component: MenuComponent},
  {path: 'cart', component: CartComponent},
  {path: 'summary', component: SummaryComponent},
  {path: 'buy', component: BuyComponent},
  {path: 'confirmation', component: ConfirmationComponent},
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

