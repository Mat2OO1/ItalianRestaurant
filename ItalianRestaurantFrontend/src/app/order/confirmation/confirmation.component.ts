import {Component, OnDestroy, OnInit} from '@angular/core';
import {Meal} from "../../models/meal";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription, timer} from "rxjs";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnInit, OnDestroy {
  stat = ""
  order: { meal: Meal, quantity: number }[] = []
  orderNumber: number | null = null;
  orderDetails: { date: Date | null, status: string, table: number | null } = {
    date: null,
    status: '',
    table: null
  }

  isContentLoaded: boolean = false;
  lang="";

  timeSubscription: Subscription | null = null;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private http: HttpClient) {
  }

  adjustStatus(status: string) {
    const circle2 = document.getElementById("circle2")
    const circle3 = document.getElementById("circle3")
    const progressBar = document.getElementById("indicator")
    if (!circle2 || !circle3 || !progressBar) {
      return;
    }
    if (status.toLowerCase() == 'in delivery') {
      circle2.classList.add("active")
      progressBar.style.width = "50%";
    } else if (status.toLowerCase() == 'delivered') {
      circle2.classList.add("active")
      circle3.classList.add("active")
      progressBar.style.width = "100%";
    }
  }

  ngOnInit(): void {
    this.route.paramMap
      .subscribe(params => {
        this.orderNumber = Number(params.get('orderId'))
        this.getOrderDetails()
      })
  }

  calculateOrder() {
    let sum: number = 0
    this.order.forEach(item => sum += item.meal.price * item.quantity)
    return sum.toFixed(2);
  }

  getOrderDetails() {
    this.timeSubscription = timer(0, 5000).subscribe(() => {
        return this.http
          .get(`${environment.apiUrl}/order/user`)
          .subscribe(
            (res: any) => {
              res = res.filter((order: any) => order.id === this.orderNumber)[0]
              if (res === undefined) {
                this.router.navigate([''])
                return
              }
              const status = res.orderStatus.toLowerCase().replace(/_/g, ' ').replace(/\b\w/g, (c: string) => c.toUpperCase());
              this.order = res.mealOrders;
              this.orderDetails = {date: res.deliveryDate, status: status, table: res.table?.number}
              this.isContentLoaded = true;
              this.adjustStatus(status)
            })
      }
    );
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

  getMealName(item:any): string {
    this.lang = localStorage.getItem('lang') || 'en'
    return this.lang === 'pl' ? item.meal.name_pl || item.meal.name : item.meal.name || '';
  }
  ngOnDestroy() {
    this.timeSubscription?.unsubscribe();
  }
}
