import { Component, OnInit } from '@angular/core';
import { User } from '../../../model/user.model';
import { UserService } from '../../../services/user.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  editForm: FormGroup;
  
  constructor(private userService: UserService, private fb: FormBuilder) { 
    this.editForm = this.fb.group({
      fullName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^\\+62[0-9]{3,14}$')]],
      address: ['', [Validators.required]],
      dateOfBirth: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    this.loadUserData();
  }

  loadUserData() {
    this.userService.getLoggedUser().subscribe((user: User | null) => {
      this.user = user;
      if (this.user) {
        this.editForm.patchValue({
          fullName: this.user.fullName,
          email: this.user.email,
          phone: this.user.phone,
          address: this.user.address,
          dateOfBirth: this.user.dateOfBirth
        });
      }
    });
  }

  get f() {
    return this.editForm.controls;
  }

  onSubmit() {
    if (this.editForm.invalid) {
      return;
    }

    const updatedUser: Partial<User> = {
      ...this.user,
      fullName: this.f['fullName'].value,
      email: this.f['email'].value,
      phone: this.f['phone'].value,
      address: this.f['address'].value,
      dateOfBirth: this.f['dateOfBirth'].value
    };

    this.userService.updateUser(updatedUser as User).subscribe({
      next: (response) => {
        console.log('update user success', response);
        location.reload();
      },
      error: (error) => {
        console.error('update user error', error);
      }
    });
  }
}
