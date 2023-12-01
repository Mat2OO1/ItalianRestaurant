import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable, of, switchMap} from "rxjs";
import {AuthService} from "./auth.service";
import {catchError, take} from "rxjs/operators";
import {Role} from "./user.model";
import {SnackbarService} from "../../shared/sncakbar.service";

@Injectable({providedIn: "root"})
export class AdminGuard {
  constructor(private authService: AuthService,
              private router: Router,
              private snackbarService: SnackbarService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | boolean
    | UrlTree
    | Promise<boolean | UrlTree>
    | Observable<boolean | UrlTree> {
    return this.authService.user.pipe(
      take(1),
      switchMap(user => {
        if (!!user) {
          return of(true);
        } else {
          if (!localStorage.getItem('token')) {
            this.snackbarService.openSnackbarError('You are not authorized to access this page');
            return of(this.router.createUrlTree(['/login'],
              { queryParams: { returnUrl: state.url }}));
          }
          return this.authService.getUserDetails(localStorage.getItem('token')!).pipe(
            switchMap(res => {
              if (!!res && res.role === Role.ADMIN) {
                this.authService.logInUser(res)
                return of(true)
              } else {
                this.snackbarService.openSnackbarError('You are not authorized to access this page');
                return of(this.router.createUrlTree(['/login'],
                  { queryParams: { returnUrl: state.url }}));
              }
            })
            , catchError(err => {
              this.authService.logout();
              return of(false);
            }));
        }
      })
    );
  }
}

