import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';
import { AuthService } from '../../../services/auth/auth.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    NgIconComponent,
    FormsModule
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
    authSubscription: Subscription | null = null;
    isForgotPassword: boolean = false;

    constructor(private authService: AuthService, private router: Router) {}

    toggleForgotPassword() {
        this.isForgotPassword = !this.isForgotPassword;
    }

    onLogin(): void {
      console.log('Logging in with email', this.email);
      this.authSubscription = this.authService.login(this.email, this.password).subscribe(
        (success) => {
          console.log('Logged in successfully', success);
          this.router.navigate(['/']);
        },
        (error) => {
          console.error('Error logging in', error);
        }
      );
    }
}
