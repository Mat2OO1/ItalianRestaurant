import {Injectable} from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Meal} from "../models/meal";
import {Subject} from "rxjs";
import {Delivery} from "../models/delivery";
import {OrderRes} from "../models/order";
import {environment} from "../../environments/environment";
import {MealResponse, MealsWithPagination} from "../models/MealResponse";
import {Table} from "../models/table";
import {MealDto} from "../models/mealDto";
import {PaymentResponse} from "../models/payment-response";
import {Cart} from "../models/cart";
import {Reservation} from "../models/reservation";
import { Observable } from 'rxjs';
import {Category} from "../models/category";
@Injectable({
  providedIn: 'root'
})
export class DataStorageService {
  constructor(private http: HttpClient) {
  }

  getMeals(page: number, size: number) {
    return this.http
      .get<MealResponse>(
        `${environment.apiUrl}/meals?page=${page}&size=${size}`,
      )
  }

  getFilteredMeals(page: number, size: number, category: string) {
    return this.http
      .get<MealResponse>(
        `${environment.apiUrl}/meals/filter/${category}?page=${page}&size=${size}`,
      )
  }

  getMealsWithoutPagination() {
    return this.http
      .get<Meal[]>(`${environment.apiUrl}/meals/all`)
  }

  addMeal(meal: MealDto, file: File) {
    const formData = new FormData();
    formData.append('image', file);
    formData.append('meal', JSON.stringify(meal));
    return this.http
      .post<Meal>(`${environment.apiUrl}/meals/add`, formData)
  }

  editMeal(meal: MealDto, id: number, file: File) {
    const formData = new FormData();
    formData.append('image', file);
    formData.append('meal', JSON.stringify(meal));
    return this.http.put<Meal>(`${environment.apiUrl}/meals/edit/${id}`, formData)
  }

  deleteMeal(id: number) {
    return this.http
      .delete(`${environment.apiUrl}/meals/delete/${id}`)
  }

  addCategory(categoryName: string, categoryNamePl: string) {
    return this.http
      .post<Category>(`${environment.apiUrl}/meal-categories/add`, {name: categoryName, name_pl: categoryNamePl})
  }

  editCategory(categoryName: string, categoryNamePl: string, id: number) {
    return this.http
      .put<Category>(`${environment.apiUrl}/meal-categories/edit/${id}`, {name: categoryName, name_pl: categoryNamePl})
  }

  deleteCategory(id: number) {
    return this.http
      .delete(`${environment.apiUrl}/meal-categories/delete/${id}`);
  }

  getCategories() {
    return this.http
      .get<{ name: string, name_pl: string }[]>(`${environment.apiUrl}/meal-categories`)
  }

  makeAnOrder(cart: Cart, delivery?: Delivery): Observable<PaymentResponse> {
    const lang = localStorage.getItem('lang') || 'en';
    const headers = new HttpHeaders().set('Lang', lang);

    return this.http.post<PaymentResponse>(
      `${environment.apiUrl}/order`,
      {
        delivery: delivery,
        tableNr: cart.table,
        mealOrders: cart.meals,
        orderStatus: "IN_PREPARATION"
      },
      { headers: headers }
    );
  }
  getOrders() {
    return this.http
      .get<OrderRes[]>(`${environment.apiUrl}/order/all`)
  }

  getUserOrders() {
    return this.http
      .get<OrderRes[]>(`${environment.apiUrl}/order/user`)
  }

  updateOrder(orderStatus: string, deliveryDate: string, orderId: number) {
    return this.http
      .post(`${environment.apiUrl}/order/change-status`, {
        orderStatus: orderStatus,
        deliveryDate: deliveryDate,
        orderId: orderId
      })
      .subscribe()
  }

  deleteOrder(id: number) {
    return this.http
      .delete(`${environment.apiUrl}/order/${id}`)
  }

  getTables() {
    return this.http
      .get<Table[]>(`${environment.apiUrl}/tables`)
  }

  saveTable(table: Table) {
    return this.http
      .post<Table>(`${environment.apiUrl}/tables`, table)
  }

  deleteTable(id: number) {
    return this.http
      .delete(`${environment.apiUrl}/tables/${id}`)
  }

  getReservedTables(){
    return this.http
      .get<Table[]>(`${environment.apiUrl}/reservations/reserved/all`)
  }
  getReservationsForDay(date: string){
    return this.http
      .get<Reservation[]>(`${environment.apiUrl}/reservations/reserved/${date}`)
  }

  makeReservation(reservation: Reservation){
    return this.http
      .post(`${environment.apiUrl}/reservations/add`, reservation)
  }

  getReservationsForTable(tableId: number, date: string){
    return this.http
      .get<string[]>(`${environment.apiUrl}/reservations/reserved?tableId=${tableId}&date=${date}`)
  }

  getUserReservations(){
    return this.http
      .get<Reservation[]>(`${environment.apiUrl}/reservations/user`)
  }

  cancelReservation(reservation: Reservation){
    return this.http
      .delete(`${environment.apiUrl}/reservations/cancel?reservationId=${reservation.id}`)
  }

  getLastDeliveryInfo(){
    return this.http
      .get<Delivery>(`${environment.apiUrl}/order/last`)
  }

  updateTable(result: Table) {
    return this.http
      .put<Table>(`${environment.apiUrl}/tables/${result.id}`, {
        number: result.number,
        seats: result.seats,
      })
  }

  getTableByNumber(tableNr: number) {
    return this.http
      .get<Table>(`${environment.apiUrl}/tables/${tableNr}`)
  }
}
