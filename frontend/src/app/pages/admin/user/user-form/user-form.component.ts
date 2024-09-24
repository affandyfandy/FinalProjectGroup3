import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../../services/user.service';
import { User } from '../../../../model/user.model';

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
export class UserFormComponent implements OnInit { 

  user: any = {};
  isEditMode: boolean = false;
  isViewMode: boolean = false;
  
  email = '';
  fullName = '';
  password = '';
  phone = '';
  dateOfBirth = '';
  status = 'ACTIVE';
  role = 'CUSTOMER';
  address = '';

  constructor(
    private userService: UserService, 
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const selectedEmail = this.route.snapshot.paramMap.get('id');
    if (selectedEmail) {
      this.email = selectedEmail;
      this.loadUser(this.email);
      this.isEditMode = this.route.snapshot.url.map(segment => segment.path).includes('edit');
      this.isViewMode = !this.isEditMode;
    }
  }

  loadUser(email: string) {
    this.userService.getUserById(email).subscribe(
      response => {
        this.user = response;
        this.email = this.user.email;
        this.fullName = this.user.fullName;
        this.password = this.user.password;
        this.phone = this.user.phone;
        this.dateOfBirth = this.user.dateOfBirth;
        this.status = this.user.status;
        this.role = this.user.role;
        this.address = this.user.address;
      }
    );
  }

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

  onCancel() {
    this.router.navigate(['/admin/users']);
  }
}
