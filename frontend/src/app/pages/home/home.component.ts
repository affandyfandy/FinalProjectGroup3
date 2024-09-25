import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
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

  constructor(private fb: FormBuilder) {
    this.searchRoomForm = this.fb.group({
      checkIn: ['', Validators.required],
      checkOut: ['', Validators.required],
      guest: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });
  }

  onSubmit(): void {
    if (this.searchRoomForm.valid) {
      console.log(this.searchRoomForm.value);
    }
  }
}
