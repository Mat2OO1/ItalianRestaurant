import {filter, interval, Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Meal} from "../models/meal";

@Injectable()
export class OrderService {
  orderDetails: Subject<{date: Date, status: string}> = new Subject()
  orderId: number | undefined;
  order: Subject<{meal: Meal,quantity: number, price: number}[]> = new Subject();
  constructor(private http: HttpClient) {}

  getOrderDetails(){
    this.orderId = Number(localStorage.getItem("orderId"));
    interval(5000).subscribe(() => {
        return this.http
          .get("http://localhost:8080/order/user")
          .subscribe(
            (res: any) => {
              res = res.filter((order: any) => order.id === this.orderId)[0]
              this.order.next(res.mealOrders);
              console.log(res)
              this.orderDetails.next({date: res.delivery.deliveryDate, status: res.orderStatus})
            }
          )
      }
    )
  }




}
