import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    NgIconComponent,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  providers: [
    provideIcons({ heroChevronLeft })
  ]
})
export class LoginComponent {
    isForgotPassword: boolean = false;

    toggleForgotPassword() {
        this.isForgotPassword = !this.isForgotPassword;
    }
}
