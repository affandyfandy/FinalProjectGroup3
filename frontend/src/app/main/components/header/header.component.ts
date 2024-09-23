import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
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
  providers: [
    provideIcons({ heroChevronDown})
  ]
})
export class HeaderComponent implements OnInit {
  email: string = '';
  fullName: string = '';
  photo: string = 'https://ui-avatars.com/api/?name=User';
  
  constructor(private authService: AuthService, private router: Router, private userService: UserService) {  }

  isLoggedIn() {
    return this.authService.checkCredentials();
  }

  isAdmin() {
    return this.authService.isAdmin();
  }

  ngOnInit() {
    this.loadUserData();
  }

  loadUserData() {
    if (this.isLoggedIn()) {
      let userPayload = this.authService.getUserInformation();
      this.userService.getUserById(userPayload[1].value).subscribe((user: User) => {
        this.email = user.email;
        this.fullName = user.fullName;
        if (user.photo) {
          this.photo = user.photo;
        }
      });
    }

    console.log('isLoggedIn', this.isLoggedIn());
  }

  logout() {
    this.authService.logout();
  }

}