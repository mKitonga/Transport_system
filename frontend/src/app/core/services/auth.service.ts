import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

    private api = environment.apiUrl;

    constructor(private http: HttpClient) {}

    register(data: any): Observable<any> {
        return this.http.post(`${this.api}/auth/register`, data);
    }

    login(data: any): Observable<any> {
        return this.http.post(`${this.api}/auth/login`, data);
    }
}
