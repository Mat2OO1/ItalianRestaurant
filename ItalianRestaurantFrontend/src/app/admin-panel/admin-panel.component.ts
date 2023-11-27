import {Component, OnDestroy} from '@angular/core';
import {DataStorageService} from "../shared/data-storage.service";
import {OrderRes} from "../models/order";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {formatDate} from "@angular/common";
import {SnackbarService} from "../shared/sncakbar.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent implements OnDestroy{
  orders: OrderRes[] = []
  isContentLoaded = false;
  forms: FormGroup[] = [];
  interval;
  lang = ""

  constructor(private dataStorageService: DataStorageService,
              private formBuilder: FormBuilder,
              private snackbarService: SnackbarService,
              private translate: TranslateService) {
    this.dataStorageService.getOrders()
      .subscribe(
        (res) => {
          console.log(res)
          this.orders = res;
          this.isContentLoaded = true;
          this.forms = this.orders.map(data =>
            this.formBuilder.group({
              deliveryDate: new FormControl(this.customFormatDate(data.deliveryDate!)),
              orderStatus: new FormControl(data.orderStatus),
            })
          );
        }
      )
    this.interval = setInterval(this.fetchOrders, 10000);
  }

  calculateSum(order: OrderRes) {
    var sum = 0;
    for (let meals of order.mealOrders) {
      sum += meals.meal.price
    }
    return sum;
  }

  updateOrder(orderId: number, formId: number) {
    const form = this.forms[formId];
    let orderStatus = form.value['orderStatus']
    let deliveryDate = new Date()
    deliveryDate.setHours(form.value['deliveryDate'].substring(0, 2))
    deliveryDate.setMinutes(form.value['deliveryDate'].substring(3, 5))
    let deliveryDateFormatted = formatDate(deliveryDate, 'YYYY-MM-ddTHH:mm:ss', 'en-GB')
    this.dataStorageService.updateOrder(orderStatus, deliveryDateFormatted, orderId)
    this.translate.get('admin_panel_change_snack').subscribe((message) => {
      this.snackbarService.openSnackbarSuccess(message);
    });
  }
  getDeliveryOptionsTranslation(deliveryOptions: string): string {
    this.lang = localStorage.getItem('lang') || 'en';
    const translations: { [key: string]: { [key: string]: string } } = {
      pl: {
        'KNOCK': 'Zapukaj',
        'LEAVE': 'Zostaw pod drzwiami',
      },
      en: {
        'KNOCK': 'Knock',
        'LEAVE': 'Leave package',
      }
    };
    return translations[this.lang][deliveryOptions] || deliveryOptions;
  }
  customFormatDate(date: Date) {
    if (date != null) {
      return formatDate(date, 'HH:mm', 'en-GB')
    } else {
      return '';
    }
  }

  fetchOrders() {
    this.dataStorageService.getOrders()
      .subscribe(
        (res) => {
          console.log(res)
          this.orders = res;
        }
      );
  };
  getMealName(meal: any): string {
    this.lang = localStorage.getItem('lang') || 'en'
    return this.lang === 'pl' ? meal.name_pl || meal.name : meal.name || '';
  }
  ngOnDestroy(): void {
    clearInterval(this.interval)
  }

  deleteOrder(orderId: number) {
    this.dataStorageService.deleteOrder(orderId).subscribe(
      () => {
        const index = this.orders.findIndex(order => order.id === orderId);
        if (index !== -1) {
          this.orders.splice(index, 1);
        }
        this.translate.get('admin_panel_delete_snack').subscribe((message) => {
          this.snackbarService.openSnackbarError(message);
        });
      }
    );

  }
}
