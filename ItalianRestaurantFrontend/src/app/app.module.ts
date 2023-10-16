import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {NavComponent} from './welcome/nav/nav.component';
import {InfoComponent} from './welcome/info/info.component';
import {LoginComponent} from './authentication/login/login.component';
import {RouterLink, RouterOutlet} from "@angular/router";
import {AppRoutingModule} from "./app.routing.module";
import {MenuComponent} from "./order/menu/menu.component";
import {ReactiveFormsModule} from "@angular/forms";
import {RegisterComponent} from "./authentication/register/register.component";
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
import {EmailFormComponent} from "./authentication/password-reset/email-form/email-form.component";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {LoadingSpinnerComponent} from "./shared/loading-spinner/loading-spinner.component";
import {AdminPanelComponent} from "./admin-panel/admin-panel.component";
import {PasswordFormComponent} from "./authentication/password-reset/password-form/password-form.component";
import {DatePipe} from "@angular/common";
import {ToastComponent} from './toast-notifications/toast/toast.component';
import {ToasterComponent} from './toast-notifications/toaster/toaster.component';
import {OrderHistoryComponent} from "./order-history/order-history.component";
import { AdminTableQrComponent } from './admin-table-qr/admin-table-qr.component';
import {QRCodeModule} from "angularx-qrcode";
import { TableEditDialogComponent } from './table-edit-dialog/table-edit-dialog.component';
import {AdminMenuComponent} from "./admin-menu/admin-menu.component";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {MealEditDialogComponent} from "./meal-edit-dialog/meal-edit-dialog.component";
import {MatButtonModule} from "@angular/material/button";
import {CategoryEditDialogComponent} from "./category-edit-dialog/category-edit-dialog.component";

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    InfoComponent,
    LoginComponent,
    MenuComponent,
    RegisterComponent,
    SummaryComponent,
    BuyComponent,
    ConfirmationComponent,
    EmailFormComponent,
    LoadingSpinnerComponent,
    AdminPanelComponent,
    PasswordFormComponent,
    ToastComponent,
    ToasterComponent,
    OrderHistoryComponent,
    AdminTableQrComponent,
    TableEditDialogComponent,
    OrderHistoryComponent,
    AdminMenuComponent,
    MealEditDialogComponent,
    CategoryEditDialogComponent
  ],
    imports: [
        BrowserModule,
        RouterLink,
        RouterOutlet,
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
        BrowserAnimationsModule,
        MatProgressSpinnerModule,
        QRCodeModule,
        MatDialogModule,
      MatTooltipModule,
      MatButtonModule,
    ],

  providers: [
    CartService,
    OrderService,
    AuthService,
    MealsService,
    DataStorageService,
    DatePipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    },
    MatDialog
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
