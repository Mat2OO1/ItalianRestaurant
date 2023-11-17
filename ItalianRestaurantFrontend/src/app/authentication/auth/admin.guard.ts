import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable, of, switchMap} from "rxjs";
import {AuthService} from "./auth.service";
import {take} from "rxjs/operators";
import {Role} from "./user.model";

@Injectable({providedIn: "root"})
export class AdminGuard {
  constructor(private authService: AuthService, private router: Router) {
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
          return this.authService.getUserDetails(localStorage.getItem('token')!).pipe(
            switchMap(newUser => {
              if (newUser.role === Role.ADMIN) {
                return of(true)
              } else {
                return of(this.router.createUrlTree(['login']));
              }
            })
          );
        }
      })
    );
  }
}

