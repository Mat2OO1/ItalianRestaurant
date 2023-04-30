import {Meal} from "../models/meal";
import {interval, Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Injectable, OnInit} from "@angular/core";

@Injectable()
export class OrderService {
  orderDetails: Subject<{date: string, status: string}> = new Subject()

  constructor(private http: HttpClient) {
  }

  getOrderDetails(){
    interval(30000).subscribe(() => {
        return this.http
          .get("http://localhost:8080/order/user")
          .subscribe(
            (res: any) => {
              this.orderDetails.next({date: res[0].orderDate, status: res[0].orderStatus})
            }
          )
      }
    )
  }
}
