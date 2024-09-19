import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth/auth.service';
import { Router } from '@angular/router';
import { User } from '../../../model/user.model';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router, private userService: UserService) {
    this.registerForm = this.fb.group({
      fullName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      password_confirmation: ['', [Validators.required]],
      phone: ['', [Validators.required, Validators.pattern('^\\+62[0-9]{3,14}$')]],
      address: ['', [Validators.required]],
      dateOfBirth: ['', [Validators.required]],
      agreeTerms: [false, [Validators.requiredTrue]]
    }, { validators: this.passwordsMatch });
  }

  passwordsMatch(form: FormGroup) {
    const password = form.get('password');
    const password_confirmation = form.get('password_confirmation');
    return password && password_confirmation && password.value === password_confirmation.value ? null : { passwordMismatch: true };
  }

  onSubmit(): void {
    console.log(this.userService.getUserById('admin@example.com'));
    if (this.registerForm.valid) {
      const newUser: User = {
        email: this.registerForm.value.email,
        fullName: this.registerForm.value.fullName,
        password: this.registerForm.value.password,
        role: 'CUSTOMER',
        status: 'ACTIVE',
        phone: this.registerForm.value.phone,
        address: this.registerForm.value.address,
        dateOfBirth: this.registerForm.value.dateOfBirth,
        photo: 'https://ui-avatars.com/api/?name=' + this.registerForm.value.fullName
      };
      this.authService.register(newUser);
    } else {
      this.registerForm.markAllAsTouched();
    }
  }

  get f() {
    return this.registerForm.controls;
  }
}
