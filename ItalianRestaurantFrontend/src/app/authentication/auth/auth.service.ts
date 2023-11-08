import {Injectable} from "@angular/core";
import {BehaviorSubject, catchError, tap, throwError} from "rxjs";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";
import {Role, User} from "./user.model";
import {environment} from "../../../environments/environment";
import jwtDecode from "jwt-decode";
import {ToastService} from "../../shared/toast.service";

export interface AuthResponseData {
  token: string;
  expiration: string;
  role: Role;
}

@Injectable()
export class AuthService {
  user = new BehaviorSubject<User | null>(null);
  private logoutTimer: any;

  constructor(private http: HttpClient, private router: Router, private toastService: ToastService) {
  }

  signup(firstName: string, lastName: string, email: string, password: string) {
    return this.http
      .post<AuthResponseData>(
        `${environment.apiUrl}/auth/register`,
        {
          firstname: firstName,
          lastname: lastName,
          email: email,
          password: password
        }
      )
      .pipe(
        catchError(this.handleError),
        tap(
          resData => {
            this.handleAuthentication(resData.token, new Date(resData.expiration).getTime(), resData.role)
            this.toastService.showSuccessToast("Login", "User registered successfully")
          })
      )
  }

  login(email: string, password: string) {
    return this.http
      .post<AuthResponseData>(
        `${environment.apiUrl}/auth/authenticate`,
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
    const token: string = localStorage.getItem('token')!;
    if (!token) return;
    this.getUserDetails(token)
      .subscribe((res) => {
          const loadedUser = new User(res.token, new Date(res.expiration), res.role);
          if (loadedUser.token) {
            this.user.next(loadedUser);
            const expirationDuration =
              new Date(res.expiration).getTime() -
              new Date().getTime();
            //this.autoLogout(100000000);
          }
        }
      )
  }

  getUserDetails(token: string) {
    return this.http.get<AuthResponseData>(`${environment.apiUrl}/auth/user`,
      {headers: new HttpHeaders({'Authorization': token})})
  }

  logout() {
    this.user.next(null);
    this.router.navigate(['/']);
    localStorage.removeItem('token');
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


  handleAuthentication(token: string, expirationDate: number, role: Role) {
    const user = new User(token, new Date(expirationDate), role);
    this.user.next(user);
    //this.autoLogout(1000000000);
    localStorage.setItem('token', token);
  }

  requestResetPassword(email: string) {
    this.http.get(`${environment.apiUrl}/password/request?email=${email}`)
      .subscribe(
        (res) => {
          this.toastService.showSuccessToast("Password reset", "Check your email to reset your password")
          this.router.navigate([''])
        }
      )
  }

  public saveToken(token: string): void {
    const decodedToken: any = jwtDecode(token);
    const expirationDate = new Date(decodedToken.exp * 1000); // Multiply by 1000 to convert from seconds to milliseconds
    const user = new User(token, expirationDate, Role.USER);
    this.user.next(user);
    localStorage.setItem('token', token);
  }

  resetPassword(password: string, token: string) {
    this.http
      .post(`${environment.apiUrl}/password/reset`,
        {
          token: token,
          password: password,
        }, {responseType: 'text'})
      .subscribe(
        () => {
          this.router.navigate([''])
          this.toastService.showSuccessToast("Password reset", "Password has been reset successfully")
        }
      );
  }

  private handleError(errorRes: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (errorRes.error.error === 'Not Found') {
      errorMessage = `Bad credentials`;
    } else if (errorRes.error === 'User already exists') {
      errorMessage = 'This user exists already';
    }
    return throwError(errorMessage);
  }
}
