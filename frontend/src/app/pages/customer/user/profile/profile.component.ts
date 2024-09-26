import { Component, OnInit } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { User } from '../../../../model/user.model';
import { UserService } from '../../../../services/user.service';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';
import { Router } from '@angular/router';
import { ToastService } from '../../../../services/toast.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { APIConstants } from '../../../../config/app.constants';
import { AuthService } from '../../../../services/auth/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent,
    NgOptimizedImage
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
  photo: SafeUrl | null = "https://ui-avatars.com/api/?name=";

  selectedFile: File | null = null;
  
  constructor(
    private userService: UserService,
    private fb: FormBuilder, 
    private authService: AuthService,
    private toastService: ToastService,
    private router: Router) { 
    this.editForm = this.fb.group({
      fullName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^\\+62[0-9]{3,14}$')]],
      address: ['', [Validators.required]],
      dateOfBirth: ['', [Validators.required, this.minAgeValidator(18)]],
    });
  }

  ngOnInit() {
    this.loadUserData();
    console.log(this.authService.getUserInformation());
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
        if (this.user.photo) {
          this.fetchUserPhoto(this.user.photo);
        } else {
          this.photo = "https://ui-avatars.com/api/?name=" + this.user.fullName;
        }
      }
      
    });
  }

  get f() {
    return this.editForm.controls;
  }

  minAgeValidator(minAge: number): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const today = new Date();
      const birthDate = new Date(control.value);
      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      const dayDiff = today.getDate() - birthDate.getDate();

      if (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)) {
        age--;
      }

      return age >= minAge ? null : { minAge: { requiredAge: minAge, actualAge: age } };
    };
  }

  onSubmit() {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      this.toastService.showToast('Please fill all required fields', 'error');
      return;
    }

    if (this.userService.getUserById(this.f['email'].value)) {
      this.toastService.showToast('Email already exists', 'error');
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

    if (this.selectedFile) {
      this.userService.uploadUserPhoto(this.user!.email, this.selectedFile).subscribe({
        next: (response) => {
          console.log('upload photo success', response);
        },
        error: (error) => {
          console.error('upload photo error', error);
        }
      })
    }

    this.userService.updateUser(updatedUser as User).subscribe({
      next: (response) => {
        console.log('update user success', response);
        location.reload();
        this.toastService.showToast('Profile updated successful!', 'success');
      },
      error: (error) => {
        console.error('update user error', error);
        this.toastService.showToast('Error updating user data' + error, 'error');
      }
    });
  }
  onCancel() {
    this.router.navigate(['/']);
  }

  onFileChanged(event: Event) {
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;

    
    if (fileList && fileList.length > 0) {
      this.selectedFile = fileList[0];
      this.updateImageDisplay();
    }
  }

  updateImageDisplay() {
    if (this.selectedFile) {
      const reader = new FileReader();

      reader.onload = (e: any) => {
          console.log("After onload");
          const element = document.getElementById('profile-image') as HTMLImageElement;
          if (element) {
              element.src = e.target.result as string;
          }
      };

      reader.readAsDataURL(this.selectedFile); 
    } else {
        console.log("No file selected");
    }
  }

  fetchUserPhoto(photo: string) {
    this.userService.getUserPhoto(photo).subscribe({
      next: (response) => {
        const objectURL = URL.createObjectURL(response);
        this.photo = objectURL;
      },
      error: (error) => {
        console.error('fetch photo error', error);
      }
    });
  }
  
}
