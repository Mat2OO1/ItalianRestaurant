import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Meal} from "../models/meal";
import {map, Subject} from "rxjs";
import {Delivery} from "../models/delivery";
import {OrderRes} from "../models/order";
import {environment} from "../../environments/environment";
import {MealResponse, MealsWithPagination} from "../models/MealResponse";
import {MealDto} from "../models/mealDto";
import {CategoryDto} from "../models/categoryDto";
import {Category} from "../models/category";

@Injectable()
export class DataStorageService {
  page = 0
  size = 3
  meals = new Subject<MealsWithPagination>();

  constructor(private http: HttpClient) {
  }

  getMeals() {
    return this.http
      .get<MealResponse>(`${environment.apiUrl}/meals?page=${this.page}&size=${this.size}`,)
      .subscribe(
        (res) => {
          var menu = res.content.map(meal => new Meal(meal.id, meal.name, meal.imgPath, meal.description, meal.price, meal.mealCategory))
          this.meals.next({meals: menu, numOfPages: res.totalPages, currPage: res.number})
        }
      )
  }

  getMealsWithoutPagination() {
    return this.http
      .get<{ content: Meal[] }>(`${environment.apiUrl}/meals`)
  }

  addMeal(meal: MealDto) {
    return this.http
      .post(`${environment.apiUrl}/meals/add`, {
        name: meal.name,
        imgPath: meal.img,
        category: meal.mealCategory,
        description: meal.description,
        price: meal.price
      })
  }

  editMeal(meal: MealDto, id: number){
    return this.http.put(`${environment.apiUrl}/meals/edit/${id}`, {
      name: meal.name,
      imgPath: meal.img,
      category: meal.mealCategory,
      description: meal.description,
      price: meal.price
    })
  }
  deleteMeal(id: number){
    return this.http
      .delete(`${environment.apiUrl}/meals/delete/${id}`)
  }

  addCategory(categoryName: string, img: File) {
    return this.http
      .post(`${environment.apiUrl}/meal-categories/add`, {
        name: categoryName,
        imgPath: img
      })
  }

  editCategory(categoryName: string, img: File, id: number) {
    return this.http
      .put(`${environment.apiUrl}/meal-categories/edit/${id}`, {
        name: categoryName,
        imgPath: img
      })
  }

  deleteCategory(id: number) {
    return this.http
      .delete(`${environment.apiUrl}/meal-categories/delete/${id}`);
  }

  getCategories() {
    return this.http
      .get<Category[]>(`${environment.apiUrl}/meal-categories`)
  }

  makeAnOrder(delivery: Delivery, order: { meal: Meal, quantity: number, price: number }[]) {
    return this.http
      .post(`${environment.apiUrl}/order`,
        {
          delivery: delivery,
          mealOrders: order,
          orderStatus: "IN_PREPARATION"
        })
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
      .subscribe(
        (res) => {
          console.log(res)
        }
      )

  }

  nextPage() {
    this.page++
    this.getMeals();
  }

  previousPage() {
    this.page--
    this.getMeals();
  }

}
