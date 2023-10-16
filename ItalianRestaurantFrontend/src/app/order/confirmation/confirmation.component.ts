import {Component, OnDestroy, OnInit} from '@angular/core';
import {Meal} from "../../models/meal";
import {ActivatedRoute, Router} from "@angular/router";
import {interval, Subscription} from "rxjs";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnInit, OnDestroy {
  order: { meal: Meal, quantity: number }[] = []
  orderNumber: number | null = null;
  sum: number | undefined;
  orderDetails: { date: Date | null, status: string } = {
    date: null,
    status: ''
  }
  isContentLoaded: boolean = false;

  timeSubscription: Subscription | null = null;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private http: HttpClient) {
  }

  adjustStatus(status: string) {
    const circle2 = document.getElementById("circle2")!
    const circle3 = document.getElementById("circle3")!
    const progressBar = document.getElementById("indicator")!
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
    return sum;
  }

  getOrderDetails() {
    this.timeSubscription = interval(5000).subscribe(() => {
        return this.http
          .get(`${environment.apiUrl}/order/user`)
          .subscribe(
            (res: any) => {
              res = res.filter((order: any) => order.id === this.orderNumber)[0]
              if(res === undefined) {
                this.router.navigate([''])
                return
              }
              this.order = res.mealOrders;
              this.orderDetails = {date: res.delivery.deliveryDate, status: res.orderStatus}
              const status = res.orderStatus.toLowerCase().replace(/_/g, ' ').replace(/\b\w/g, (c: string) => c.toUpperCase());
              this.adjustStatus(status)
              this.isContentLoaded = true;
            })
      }
    );
  }

  ngOnDestroy() {
    this.timeSubscription?.unsubscribe();
  }
}
