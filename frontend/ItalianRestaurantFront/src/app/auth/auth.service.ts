import {Injectable} from "@angular/core";
import {BehaviorSubject, catchError, first, tap, throwError} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {User} from "./user.model";

export interface AuthResponseData{
  token: string;
}
@Injectable()
export class AuthService {
  user = new BehaviorSubject<User>(null);
  private logoutTimer: any;
  constructor(private http: HttpClient, private router: Router){}


  signup(firstName: string, lastName: string, email: string, password: string){
    console.log(firstName)
    return this.http
      .post<AuthResponseData>(
        "http://localhost:8080/auth/register",
        {
          firstname: firstName,
          lastname: lastName,
          email: email,
          password: password}
      )
      .subscribe(resData => {
          const user = new User(resData.token)
          this.user.next(user)
          this.autoLogout(1000000);
          localStorage.setItem('userData', resData.token);
          this.router.navigate(['/menu']);
        })

  }

  login(email: string, password: string){
    return this.http
      .post<AuthResponseData>(
        "http://localhost:8080/auth/authenticate",
        {
          email: email,
          password: password,
        }
      )
      .subscribe(resData => {
        const user = new User(resData.token)
        this.user.next(user)
        this.autoLogout(1000000);
        localStorage.setItem('userData', resData.token);
        console.log(resData.token)
        this.router.navigate(['/menu']);
      })
  }

  autoLogout(expiration: number) {
    this.logoutTimer = setTimeout( () => {
      this.logout()
    }, expiration)
  }

  logout() {
    this.user.next(null);
    this.router.navigate(['/login']);
    localStorage.removeItem('userData');
    // if (this.tokenExpirationTimer) {
    //   clearTimeout(this.tokenExpirationTimer);
    // }
    // this.tokenExpirationTimer = null;
  }

}
