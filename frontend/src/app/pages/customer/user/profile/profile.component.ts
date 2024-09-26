import { Component, OnInit } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../../../../model/user.model';
import { UserService } from '../../../../services/user.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { APIConstants } from '../../../../config/app.constants';
import { AuthService } from '../../../../services/auth/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgOptimizedImage
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  editForm: FormGroup;
  photo: SafeUrl | null = null;

  selectedFile: File | null = null;
  
  constructor(private userService: UserService, private fb: FormBuilder, private authService: AuthService) { 
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
        }
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
      },
      error: (error) => {
        console.error('update user error', error);
      }
    });
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
