import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable, of, switchMap} from "rxjs";
import {AuthService} from "./auth.service";
import {catchError, take} from "rxjs/operators";

@Injectable({providedIn: "root"})
export class AuthGuard {
  constructor(private authService: AuthService,
              private router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    router: RouterStateSnapshot
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
          if (!localStorage.getItem('token')) return of(this.router.createUrlTree(['login']));
          return this.authService.getUserDetails(localStorage.getItem('token')!).pipe(
            switchMap(res => {
              if (!!res) {
                this.authService.logInUser(res)
                return of(true)
              } else {
                return of(this.router.createUrlTree(['login']));
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
