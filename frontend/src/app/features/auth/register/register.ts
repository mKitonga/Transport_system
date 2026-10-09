import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  registerRequest = {
    // =========================
    // COMMON FIELDS
    // =========================
    fullName: '',
    phoneNumber: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: '',

    // =========================
    // SACCO ADMIN
    // =========================
    saccoName: '',
    routeName: '',

    // =========================
    // SACCO DRIVER
    // =========================
    numberPlate: '',
    matatuName: '',

    // =========================
    // SCHOOL ADMIN / DRIVER
    // =========================
    schoolName: '',
    schoolLocation: '',

    // =========================
    // PARENT
    // =========================
    studentName: '',
    grade: '',
    admissionNumber: ''
  };


  register(): void {

    // Remove accidental spaces from important fields
    this.registerRequest.fullName =
      this.registerRequest.fullName.trim();

    this.registerRequest.phoneNumber =
      this.registerRequest.phoneNumber.trim();

    this.registerRequest.email =
      this.registerRequest.email.trim();

    this.registerRequest.numberPlate =
      this.registerRequest.numberPlate
        .trim()
        .toUpperCase();


    // =========================
    // CHECK PASSWORDS
    // =========================

    if (
      this.registerRequest.password !==
      this.registerRequest.confirmPassword
    ) {

      alert('Passwords do not match.');

      return;
    }


    // =========================
    // CHECK ROLE
    // =========================

    if (!this.registerRequest.role) {

      alert('Please select a role.');

      return;
    }


    // =========================
    // SEND REQUEST TO BACKEND
    // =========================

    console.log(
      'Registration request:',
      this.registerRequest
    );

    this.authService
      .register(this.registerRequest)
      .subscribe({

        next: (response) => {

          console.log(
            'Registration successful:',
            response
          );

          alert(
            'Registration successful! You can now login.'
          );

          // Redirect to login page
          this.router.navigate(['/login']);

        },

        error: (error) => {

          console.error(
            'Registration failed:',
            error
          );

          console.error(
            'Backend response:',
            error.error
          );


          // Try to display the backend validation message
          if (error.error?.message) {

            alert(error.error.message);

          } else if (error.error?.errors) {

            alert(
              'Please correct the validation errors.'
            );

          } else {

            alert(
              'Registration failed. Please check your information and try again.'
            );
          }

        }

      });
  }
}