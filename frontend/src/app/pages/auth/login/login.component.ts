import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';
import { AuthService } from '../../../services/auth/auth.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    NgIconComponent,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  providers: [
    provideIcons({ heroChevronLeft })
  ]
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  remember: boolean = false;

  forgotForm: FormGroup;

  authSubscription: Subscription | null = null;
  isForgotPassword: boolean = false;

  constructor(
    private authService: AuthService, 
    private router: Router,
    private fb: FormBuilder
  ) {
    this.forgotForm = this.fb.group({
      email_forgot: ['', [Validators.required, Validators.email]],
      password_forgot: ['', [Validators.required, Validators.minLength(8)]],
      password_confirmation_forgot: ['', [Validators.required]]
    }, { validators: this.passwordsMatch });
  }

  toggleForgotPassword() {
      this.isForgotPassword = !this.isForgotPassword;
  }

  onLogin(): void {
    console.log('Logging in with email', this.email);
    this.authSubscription = this.authService.login(this.email, this.password, this.remember).subscribe(
      (success) => {
        console.log('Logged in successfully', success);
        if (this.authService.isAdmin()){
          console.log("masuk ke sini");
          this.router.navigate(['/admin'])
          console.log("masuk ke sini2");
        }
        else {
          this.router.navigate(['/']);
        }
        // this.router.navigate(['/']);
      },
      (error) => {
        console.error('Error logging in', error);
      }
    );
  }

  onForgotPassword(): void {
    if (this.forgotForm.valid){
      this.authService.forgotPassword(this.forgotForm.value.email_forgot, this.forgotForm.value.password_forgot).subscribe(
        (success) => {
          console.log('Forgot password email sent', success);
        },
        (error) => {
          console.error('Error sending forgot password email', error);
        }
      );
    } else {
      this.forgotForm.markAllAsTouched();
    }
  }

  passwordsMatch(form: FormGroup) {
    const password_forgot = form.get('password_forgot');
    const password_confirmation_forgot = form.get('password_confirmation_forgot');
    return password_forgot && password_confirmation_forgot && password_forgot.value === password_confirmation_forgot.value ? null : { passwordMismatch: true };
  }

  get f() {
    return this.forgotForm.controls;
  }
}
