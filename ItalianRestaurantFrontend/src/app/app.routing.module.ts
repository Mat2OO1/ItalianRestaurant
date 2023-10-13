import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {InfoComponent} from "./welcome/info/info.component";
import {LoginComponent} from "./authentication/login/login.component";
import {RegisterComponent} from "./authentication/register/register.component";
import {MenuComponent} from "./order/menu/menu.component";
import {SummaryComponent} from "./order/summary/summary.component";
import {BuyComponent} from "./order/buy/buy.component";
import {ConfirmationComponent} from "./order/confirmation/confirmation.component";
import {AuthGuard} from "./authentication/auth/auth.guard";
import {EmailFormComponent} from "./authentication/password-reset/email-form/email-form.component";
import {AdminPanelComponent} from "./admin-panel/admin-panel.component";
import {PasswordFormComponent} from "./authentication/password-reset/password-form/password-form.component";
import {AdminGuard} from "./authentication/auth/admin.guard";
import {OrderHistoryComponent} from "./order-history/order-history.component";
import {AdminTableQrComponent} from "./admin-table-qr/admin-table-qr.component";

const routes: Routes = [
  {path: '', component: InfoComponent},
  {path: 'login', component: LoginComponent},
  {path: 'reset', component: EmailFormComponent},
  {path: 'password', component: PasswordFormComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'menu', component: MenuComponent},
  {path: 'summary', component: SummaryComponent, canActivate: [AuthGuard]},
  {path: 'buy', component: BuyComponent, canActivate: [AuthGuard]},
  {path: 'confirmation', component: ConfirmationComponent, canActivate: [AuthGuard]},
  {path: 'order-history', component: OrderHistoryComponent, canActivate: [AuthGuard]},
  {path: 'admin-panel', component: AdminPanelComponent, canActivate: [AdminGuard]},
  {path: 'admin-tables-panel', component: AdminTableQrComponent},
  {path: '**', redirectTo: ''}
]

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

