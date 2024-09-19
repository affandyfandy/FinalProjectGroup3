import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronDown } from '@ng-icons/heroicons/outline';
import { AuthService } from '../../../services/auth/auth.service';
import { UserService } from '../../../services/user.service';
import { User } from '../../../model/user.model';

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
  
  constructor(private authService: AuthService, private router: Router, private userService: UserService) {  }

  isLoggedIn() {
    return this.authService.checkCredentials();
  }

  ngOnInit() {
    if (this.isLoggedIn()) {
      let userPayload = this.authService.getUserInformation();
      this.userService.getUserById(userPayload[1].value).subscribe((user: User) => {
        this.email = user.email;
        this.fullName = user.fullName;
      });
    }

    console.log('isLoggedIn', this.isLoggedIn());
  }

  logout() {
    this.authService.logout();
  }

}