import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Router} from '@angular/router';
import {catchError, exhaustMap, take} from 'rxjs/operators';
import {EMPTY, Observable, throwError} from 'rxjs';

import {AuthService} from './auth.service';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return this.authService.user.pipe(
      take(1),
      exhaustMap(user => {
          if (!user) {
            return next.handle(req);
          }
          const allHeaders = req.headers
            .append('Authorization', 'Bearer ' + user.token);
          const authReq = req.clone({headers: allHeaders});
          return next.handle(authReq).pipe(
            catchError(this.handleAuthError)
          );
        }
      ))
  }

  private handleAuthError(err: HttpErrorResponse): Observable<any> {
    if (err.status === 401 || err.status === 403) {
      this.router.navigate(['/login']);
      this.authService.user.next(null);
      localStorage.removeItem("userData")
      return EMPTY;
    }
    return throwError(err);
  }
}
