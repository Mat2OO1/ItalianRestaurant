import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpParams,
  HttpHeaders
} from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, take, exhaustMap } from 'rxjs/operators';
import { throwError } from 'rxjs';

import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return this.authService.user.pipe(
      take(1),
      exhaustMap(user => {
        if (!user) {
          return next.handle(req);
        }
        const modifiedReq = req.clone({
          headers: new HttpHeaders().set('Authorization', 'Bearer ' + user.token)
        });

        return next.handle(modifiedReq).pipe(
          catchError(error => {
            // Handle the error here
            // For example, you can check the error status and navigate to the home route
              this.router.navigate(['/home']);
              // @ts-ignore
            this.authService.user.next(null);

            // Throw the error again to propagate it to the subscriber
            return throwError(error);
          })
        );
      })
    );
  }
}
