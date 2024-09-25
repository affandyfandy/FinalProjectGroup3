import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../../../../model/user.model';
import { UserService } from '../../../../services/user.service';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';
import { Router } from '@angular/router';
import { ToastService } from '../../../../services/toast.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  providers: [
    provideIcons({ heroChevronLeft })
  ]
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  editForm: FormGroup;
  isEditMode: boolean = false;
  isViewMode: boolean = false;
  
  constructor(
    private userService: UserService, 
    private fb: FormBuilder, 
    private router: Router,
    private toastService: ToastService) { 
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
        this.toastService.showToast('Profile updated successful!', 'success');
      },
      error: (error) => {
        console.error('update user error', error);
        this.toastService.showToast('Error updating user data', 'error');
      }
    });
  }
  onCancel() {
    this.router.navigate(['/']);
  }
}
