import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { Observable } from "rxjs";
import { AuthService } from "../../services/auth/auth.service";

export const AuthInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
    const authService = inject(AuthService);
    const authToken = authService.getToken();

    const authReq = authToken ? req.clone({
        headers: req.headers.set('Authorization', `Bearer ${authToken}`)
    }) : req;
    
    return next(authReq);
}