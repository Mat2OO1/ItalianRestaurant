import {Injectable} from "@angular/core";
import {BehaviorSubject, catchError, first, tap, throwError} from "rxjs";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {User} from "./user.model";

export interface AuthResponseData{
  token: string;
  expiration: string;
  role: string;
}
@Injectable()
export class AuthService {
  // @ts-ignore
  user = new BehaviorSubject<User>(null);
  private logoutTimer: any;
  constructor(private http: HttpClient, private router: Router){}

  signup(firstName: string, lastName: string, email: string, password: string){
    return this.http
      .post<AuthResponseData>(
        "http://localhost:8080/auth/register",
        {
          firstname: firstName,
          lastname: lastName,
          email: email,
          password: password}
      )
      .pipe(
        catchError(this.handleError),
        tap(
        resData => {
        this.handleAuthentication(resData.token, new Date(resData.expiration).getTime(),resData.role)
        })
      )
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
      .pipe(
        catchError(this.handleError),
        tap(
          resData => {
            this.handleAuthentication(resData.token, new Date(resData.expiration).getTime(), resData.role)
          })
      )
  }

  autoLogin() {
    // @ts-ignore
    const userData: {_token: string, _tokenExpirationDate: string, role: string} = JSON.parse(localStorage.getItem('userData'));
    if(!userData){
      return;
    }
    const loadedUser = new User(
      userData._token,
      new Date(userData._tokenExpirationDate),
      userData.role
    );
    if (loadedUser.token) {
      this.user.next(loadedUser);
      const expirationDuration =
        new Date(userData._tokenExpirationDate).getTime() -
        new Date().getTime();
      //this.autoLogout(100000000);
    }
  }

  logout() {
    // @ts-ignore
    this.user.next(null);
    this.router.navigate(['/']);
    localStorage.removeItem('userData');
    if (this.logoutTimer) {
      clearTimeout(this.logoutTimer);
    }
    this.logoutTimer = null;
  }
  // autoLogout(expirationDuration: number) {
  //   this.logoutTimer = setTimeout( () => {
  //     this.logout()
  //   }, expirationDuration)
  // }


  handleAuthentication(token: string, expiresIn: number, role: string){
    const expirationDate: Date = new Date(new Date().getTime() + expiresIn * 1000);
    const user = new User(token, expirationDate,role);
    this.user.next(user);
    //this.autoLogout(1000000000);
    localStorage.setItem('userData', JSON.stringify(user));
  }

  resetPassword(email: string){
    this.http.get(`http://localhost:8080/password/request?email=${email}`)
      .subscribe(
        (res) => {
          console.log(res);
          this.router.navigate([''])
        }
      )

  }

  private handleError(errorRes: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if(errorRes.error.error === 'Not Found') {
      errorMessage = `Bad credentials`;
      }
    else if (errorRes.error === 'User already exists') {
      errorMessage = 'This user exists already';
    }
    return throwError(errorMessage);
  }

}
