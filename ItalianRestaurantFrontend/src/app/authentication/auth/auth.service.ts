import {Injectable} from "@angular/core";
import {BehaviorSubject, catchError, EMPTY, map, switchMap, tap, throwError} from "rxjs";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";
import {Role, User} from "./user.model";
import {environment} from "../../../environments/environment";
import jwtDecode from "jwt-decode";
import {SnackbarService} from "../../shared/sncakbar.service";
import {take} from "rxjs/operators";
import { TranslateService } from '@ngx-translate/core';

export interface AuthResponseData {
  token: string;
  expiration: string;
  role: Role;
}

@Injectable()
export class AuthService {
  user = new BehaviorSubject<User | null>(null);
  private logoutTimer: any;
  private errorMessage="";

  constructor(private http: HttpClient, private router: Router, private snackbarService: SnackbarService, private translate: TranslateService) {
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
            this.translate.get('user_registered_successfully').subscribe((message) => {
              this.snackbarService.openSnackbarSuccess(message);
            });
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

  logInUser(authData: AuthResponseData) {
    const loadedUser = new User(authData.token, new Date(authData.expiration), authData.role);
    if (loadedUser.token) {
      this.user.next(loadedUser);
      const expirationDuration =
        new Date(authData.expiration).getTime() -
        new Date().getTime();
      this.autoLogout(expirationDuration);
    }
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

  autoLogout(expirationDuration: number) {
    this.logoutTimer = setTimeout(() => {
      this.logout()
    }, expirationDuration);
  }


  handleAuthentication(token: string, expirationDate: number, role: Role) {
    const loadedUser = new User(token, new Date(expirationDate), role);
    if (loadedUser.token) {
      this.user.next(loadedUser);
      const expirationDuration =
        new Date(expirationDate).getTime() -
        new Date().getTime();
      this.autoLogout(expirationDuration);
      localStorage.setItem('token', token);

    }
  }

  requestResetPassword(email: string, lang: string) {
    lang = localStorage.getItem('lang')||'en';
    const url = `${environment.apiUrl}/password/request?email=${email}&lang=${lang}`;

    return this.http.get(url)
  }

  public saveToken(token: string): void {
    const decodedToken: any = jwtDecode(token);
    const expirationDate = new Date(decodedToken.exp * 1000); // Multiply by 1000 to convert from seconds to milliseconds
    const user = new User(token, expirationDate, Role.USER);
    this.user.next(user);
    localStorage.setItem('token', token);
  }

  resetPassword(password: string, token: string) {
    return this.http
      .post(`${environment.apiUrl}/password/reset`,
        {
          token: token,
          password: password,
        }, {responseType: 'text'})

  }

  private handleError(errorRes: HttpErrorResponse) {
    this.translate.get('unknown_error').subscribe((message) => {
      this.errorMessage = message;
    });
    if (errorRes.error.error === 'Not Found') {
      this.errorMessage = `Bad credentials`;
    } else if (errorRes.error === 'User already exists') {
      this.errorMessage = 'This user exists already';
    }
    return throwError(this.errorMessage);
  }

  autoLogin() {
    this.user.pipe(
      take(1),
      switchMap(user => {
        const token: string | null = localStorage.getItem('token');
        if (user || !token) return EMPTY
        return this.getUserDetails(token).pipe(
          catchError(() => {
            this.logout();
            return EMPTY;
          }),
          map(res => {
            const loadedUser = new User(res.token, new Date(res.expiration), res.role);
            if (loadedUser.token) {
              this.user.next(loadedUser);
              this.autoLogout(new Date(res.expiration).getTime() - new Date().getTime());
            }
          })
        );
      })
    ).subscribe();
  }

}
