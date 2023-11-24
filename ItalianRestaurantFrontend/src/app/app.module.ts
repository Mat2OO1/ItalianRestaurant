import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {NavComponent} from './welcome/nav/nav.component';
import {InfoComponent} from './welcome/info/info.component';
import {LoginComponent} from './authentication/login/login.component';
import {RouterLink, RouterOutlet} from "@angular/router";
import {AppRoutingModule} from "./app.routing.module";
import {MenuComponent} from "./order/menu/menu.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RegisterComponent} from "./authentication/register/register.component";
import {CartService} from "./shared/cart.service";
import {SummaryComponent} from "./order/summary/summary.component";
import {BuyComponent} from "./order/buy/buy.component";
import {ConfirmationComponent} from "./order/confirmation/confirmation.component";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {AuthService} from "./authentication/auth/auth.service";
import {AuthInterceptorService} from "./authentication/auth/auth-interceptor.service";
import {DataStorageService} from "./shared/data-storage.service";
import {EmailFormComponent} from "./authentication/password-reset/email-form/email-form.component";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {LoadingSpinnerComponent} from "./shared/loading-spinner/loading-spinner.component";
import {AdminPanelComponent} from "./admin-panel/admin-panel.component";
import {PasswordFormComponent} from "./authentication/password-reset/password-form/password-form.component";
import {DatePipe} from "@angular/common";
import {OrderHistoryComponent} from "./order-history/order-history.component";
import {AdminTableQrComponent} from './admin-table-qr/admin-table-qr.component';
import {QRCodeModule} from "angularx-qrcode";
import {TableEditDialogComponent} from './table-edit-dialog/table-edit-dialog.component';
import {AdminMenuComponent} from "./admin-menu/admin-menu.component";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {MealEditDialogComponent} from "./meal-edit-dialog/meal-edit-dialog.component";
import {MatButtonModule} from "@angular/material/button";
import {CategoryEditDialogComponent} from "./category-edit-dialog/category-edit-dialog.component";
import {TableComponent} from "./order/table/table.component";
import {MatCardModule} from "@angular/material/card";
import {MatListModule} from "@angular/material/list";
import {TranslateLoader, TranslateModule, TranslateService} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {ReserveTableComponent} from "./reserve-table/reserve-table.component";
import {ReserveTableDialogComponent} from "./reserve-table/reserve-table-dialog/reserve-table-dialog.component";
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import {MatTabsModule} from "@angular/material/tabs";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatPaginatorIntl, MatPaginatorModule} from "@angular/material/paginator";
import {MatRadioModule} from "@angular/material/radio";
import {MatIconModule} from "@angular/material/icon";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTableModule} from "@angular/material/table";
import {MatSortModule} from "@angular/material/sort";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatSidenavModule} from "@angular/material/sidenav";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MatMenuModule} from "@angular/material/menu";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {PaginatorI18n} from "./shared/PaginatorI18n";
import {MatBadgeModule} from "@angular/material/badge";
import {MatChipsModule} from "@angular/material/chips";

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

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
    OrderHistoryComponent,
    AdminTableQrComponent,
    TableEditDialogComponent,
    OrderHistoryComponent,
    AdminMenuComponent,
    MealEditDialogComponent,
    CategoryEditDialogComponent,
    TableComponent,
    ReserveTableComponent,
    ReserveTableDialogComponent
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
    FormsModule,
    MatCardModule,
    MatListModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    MatTabsModule,
    MatCheckboxModule,
    MatPaginatorModule,
    MatRadioModule,
    MatIconModule,
    MatTableModule,
    MatSortModule,
    MatToolbarModule,
    MatSidenavModule,
    FlexLayoutModule,
    MatMenuModule,
    MatSnackBarModule,
    MatBadgeModule,
    MatExpansionModule,
  ],

  providers: [
    CartService,
    AuthService,
    DataStorageService,
    DatePipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    },
    MatDialog,
    HttpClient,
    MatSnackBarModule,
    {
      provide: MatPaginatorIntl, deps: [TranslateService],
      useFactory: (translateService: TranslateService) => new PaginatorI18n(translateService).getPaginatorIntl()
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
