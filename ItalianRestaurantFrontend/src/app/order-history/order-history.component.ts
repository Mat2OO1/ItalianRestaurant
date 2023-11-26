import {Component} from '@angular/core';
import {DataStorageService} from "../shared/data-storage.service";
import {OrderRes} from "../models/order";
import {Router} from "@angular/router";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css']
})
export class OrderHistoryComponent {
  isContentLoaded = false;
  orders: OrderRes[] = []
  lang = ""
  constructor(private dataStorageService: DataStorageService,
              private router: Router) {
    this.dataStorageService.getUserOrders()
      .subscribe(
        (res) => {
          this.orders = res
          this.orders.sort((a, b) => {
            return b.id - a.id
          })
          this.isContentLoaded = true;
        }
      )
  }

  getCurrentOrders() {
    return this.orders.filter(order => order.orderStatus !== 'DELIVERED')
  }

  getArchivalOrders() {
    return this.orders.filter(order => order.orderStatus === 'DELIVERED')
  }

  getOrderImg(order: OrderRes) {
    const mealWithMaxQuantity = order.mealOrders.reduce((maxMeal, mealOrder) =>
      mealOrder.quantity > maxMeal.quantity ? mealOrder : maxMeal
    );

    return mealWithMaxQuantity.meal.image
  }

  formatDeliveryStatus(status: string) {
    return status.toLowerCase().replace(/_/g, ' ').replace(/\b\w/g, (c) => c.toUpperCase());

  }

  customFormatDate(date: Date | undefined) {
    if (date != null) {
      return formatDate(date, 'HH:mm', 'en-GB')
    } else {
      return '';
    }
  }


  goToDetails(orderId: number) {
    this.router.navigate(['/confirmation', orderId])
  }

  getStatusTranslation(status: string): string {
    this.lang = localStorage.getItem('lang') || 'en';
    const translations: { [key: string]: { [key: string]: string } } = {
      pl: {
        'In Preparation': 'W przygotowaniu',
        'In Delivery': 'W dostawie',
        'Delivered': 'Dostarczono',
        'processing': 'Przetwarzanie'
      },
      en: {
        'In Preparation': 'In Preparation',
        'In Delivery': 'In Delivery',
        'Delivered': 'Delivered',
        'processing': 'Processing'
      }
    };
    return translations[this.lang][status] || status;
  }

}
