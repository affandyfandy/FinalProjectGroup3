import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronDown } from '@ng-icons/heroicons/outline';
import { AuthService } from '../../../service/auth/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    NgIconComponent,
    RouterLink,
    RouterLinkActive,
    CommonModule
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
  providers: [
    provideIcons({ heroChevronDown})
  ]
})
export class HeaderComponent implements OnInit {
  email: string = '';
  fullName: string = '';
  
  constructor(private authService: AuthService, private router: Router) {  }

  isLoggedIn() {
    return this.authService.checkCredentials();
  }

  ngOnInit() {
    if (this.isLoggedIn()) {
      this.email = this.authService.getUserInformation().email;
      this.fullName = this.authService.getUserInformation().fullName;
    }
    console.log('Is logged in', this.isLoggedIn());
    console.log('User information', this.authService.getUserInformation());
  }

}