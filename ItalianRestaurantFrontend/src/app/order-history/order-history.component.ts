import {Component, ViewChild} from '@angular/core';
import {DataStorageService} from "../shared/data-storage.service";
import {OrderRes} from "../models/order";
import {Router} from "@angular/router";
import {formatDate} from "@angular/common";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css']
})
export class OrderHistoryComponent {
  isContentLoaded = false;
  orders: OrderRes[] = [];
  currentOrders: OrderRes[] = []
  archivalOrders: OrderRes[] = []
  lang = ""
  @ViewChild("currentOrdersPag") currentOrdersPaginator!: MatPaginator;
  @ViewChild("archivalOrdersPag") archivalOrdersPaginator!: MatPaginator;
  constructor(private dataStorageService: DataStorageService,
              private router: Router) {
    this.dataStorageService.getUserOrders()
      .subscribe(
        (res) => {
          this.orders = res;
          this.orders.sort((a, b) => {
            return b.id - a.id
          })
          this.currentOrders = this.orders.filter(order => order.orderStatus !== 'DELIVERED').slice(0, 5)
          this.archivalOrders = this.orders.filter(order => order.orderStatus === 'DELIVERED').slice(0, 5)
          this.isContentLoaded = true;
        }
      )
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


  onChangePageCurrentOrders(event: PageEvent) {
    const startIndex = this.currentOrdersPaginator.pageIndex * this.currentOrdersPaginator.pageSize;
    const endIndex = startIndex + this.currentOrdersPaginator.pageSize;
    this.currentOrders = this.orders.filter(order => order.orderStatus !== 'DELIVERED')
      .slice(startIndex, endIndex);
    window.scrollTo({top: 0});
  }

  onChangePageArchivalOrders(event: PageEvent) {
    const startIndex = this.archivalOrdersPaginator.pageIndex * this.archivalOrdersPaginator.pageSize;
    const endIndex = startIndex + this.archivalOrdersPaginator.pageSize;
    this.archivalOrders = this.orders.filter(order => order.orderStatus === 'DELIVERED').slice(startIndex, endIndex);
    window.scrollTo({top: 0});
  }

  get getCurrentOrdersLength() {
    return this.orders.filter(order => order.orderStatus !== 'DELIVERED').length;
  }

  get getArchivalOrdersLength() {
    return this.orders.filter(order => order.orderStatus === 'DELIVERED').length;
  }
}
