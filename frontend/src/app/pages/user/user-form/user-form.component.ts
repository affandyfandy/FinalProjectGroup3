import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { User } from '../../../model/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.scss'
})
export class UserFormComponent {
  email = '';
  fullName = '';
  password = '';
  phone = '';
  dateOfBirth = '';
  status = 'ACTIVE';
  role = 'CUSTOMER';
  address = '';

  constructor(private userService: UserService, private router: Router) {}

  onSubmit(form: NgForm) {
    if (form.valid) {
      const newUser: User = {
        email: this.email,
        fullName: this.fullName,
        password: this.password,
        phone: this.phone,
        dateOfBirth: this.dateOfBirth,
        status: this.status,
        role: this.role,
        address: this.address,
      };
      let user = this.userService.createUser(newUser).subscribe(
        response => {
          console.log('User created', response);
          this.router.navigate(['/users']);
        }
      );
    }
  }

  cancel() {
    console.log('Cancel clicked');
    // Handle cancel action
  }
}
