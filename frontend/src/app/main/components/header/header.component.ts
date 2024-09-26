import { CommonModule, NgOptimizedImage } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronDown } from '@ng-icons/heroicons/outline';
import { AuthService } from '../../../services/auth/auth.service';
import { UserService } from '../../../services/user.service';
import { User } from '../../../model/user.model';
import { APIConstants } from '../../../config/app.constants';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    NgIconComponent,
    RouterLink,
    RouterLinkActive,
    CommonModule,
    NgOptimizedImage
  ],
  templateUrl: './header.component.html',
  providers: [
    provideIcons({ heroChevronDown})
  ]
})
export class HeaderComponent implements OnInit {
  email: string = '';
  fullName: string = '';
  photo: SafeUrl | null = null;
  
  constructor(
    private authService: AuthService, 
    private router: Router, 
    private userService: UserService,
    private sanitizer: DomSanitizer
  ) {  }

  isLoggedIn() {
    return this.authService.checkCredentials();
    // return true;
  }

  isAdmin() {
    return this.authService.isAdmin();
    // return true;
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
          this.userService.getUserPhoto(user.photo).subscribe(
            (blob) => {
              const objectURL = URL.createObjectURL(blob);
              this.photo = this.sanitizer.bypassSecurityTrustUrl(objectURL);
            },
            (error) => {
              console.error('Error loading user photo', error);
            }
          );
        }
      });
    }

    console.log('isLoggedIn', this.isLoggedIn());
  }

  logout() {
    console.log('logout');
    this.authService.logout();
    console.log('isLoggedIn', this.isLoggedIn());
  }

}