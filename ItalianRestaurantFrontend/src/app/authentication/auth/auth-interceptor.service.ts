import {Injectable} from '@angular/core';
import {HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Router} from '@angular/router';
import {catchError, exhaustMap, take} from 'rxjs/operators';
import {throwError} from 'rxjs';

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
        const modifiedReq = req.clone({
          headers: new HttpHeaders()
            .set('Authorization', 'Bearer ' + user.token)
            .set('Content-Type', 'application/json;charset=UTF-8')
        });
        return next.handle(modifiedReq).pipe(
          catchError(error => {
            this.router.navigate(['/home']);
            // @ts-ignore
            this.authService.user.next(null);
            return throwError(error);
          })
        );
      })
    );
  }
}
