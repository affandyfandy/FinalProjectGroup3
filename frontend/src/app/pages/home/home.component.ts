import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { bootstrapFacebook, bootstrapInstagram, bootstrapLinkedin, bootstrapTwitterX, bootstrapYoutube } from '@ng-icons/bootstrap-icons';
import { NgIconComponent, provideIcons, provideNgIconsConfig } from '@ng-icons/core';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NgIconComponent,
    ReactiveFormsModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  providers: [
    provideNgIconsConfig({
      size: '2em',
    }),
    provideIcons({ 
      bootstrapTwitterX,
      bootstrapInstagram,
      bootstrapFacebook,
      bootstrapYoutube,
      bootstrapLinkedin
    })
  ]
})
export class HomeComponent {
  searchRoomForm: FormGroup;
  queryParams: any = {};

  constructor(private fb: FormBuilder, private router: Router) {
    this.searchRoomForm = this.fb.group({
      checkIn: ['', Validators.required],
      checkOut: ['', Validators.required],
      guest: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });
  }

  onSubmit(): void {
    if (this.searchRoomForm.valid) {
      this.queryParams = {
        checkIn: this.searchRoomForm.value.checkIn,
        checkOut: this.searchRoomForm.value.checkOut,
        guest: this.searchRoomForm.value.guest
      };
      this.router.navigate(['/rooms'], {
        queryParams: this.queryParams
      });
    }
  }
}
