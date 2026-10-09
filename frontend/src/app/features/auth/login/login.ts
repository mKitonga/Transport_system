import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../../core/services/auth.service';

@Component({

  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'

})
export class Login {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  loginRequest = {
      email: '',
      password: ''
  };

  login(): void {

      this.authService.login(this.loginRequest)

      .subscribe({

          next: (response: any) => {

              localStorage.setItem(
                  'token',
                  response.token
              );

              localStorage.setItem(
                  'role',
                  response.role
              );

              this.router.navigate( ['/dashboard'] );
          },

          error: (error: any) => {
            console.error('Login failed:', error);

              alert(
                error?.error?.message || 'Login failed. Please check your email and password and try again.'
              );
          }

      });

  }

}