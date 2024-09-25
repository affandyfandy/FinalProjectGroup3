import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { bootstrapFacebook, bootstrapInstagram, bootstrapLinkedin, bootstrapTwitterX, bootstrapYoutube } from '@ng-icons/bootstrap-icons';
import { NgIconComponent, provideIcons, provideNgIconsConfig } from '@ng-icons/core';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
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

  galleryItems = [
    { src: '/assets/images/socgal1.jpg', alt: 'Two men walking in a hallway', className: 'col-span-1 row-span-1' },
    { src: '/assets/images/socgal2.jpg', alt: 'Number 27 on an elevator', className: 'col-span-1 row-span-1' },
    { src: '/assets/images/socgal3.jpg', alt: 'Promotional image for a luxury event', className: 'col-span-1 row-span-2' },
    { src: '/assets/images/socgal4.jpg', alt: 'Event poster', className: 'col-span-1 row-span-1' },
    { src: '/assets/images/socgal5.jpg', alt: 'Various food dishes', className: 'col-span-1 row-span-2' },
    { src: '/assets/images/socgal6.jpg', alt: 'Person working with pottery', className: 'col-span-1 row-span-1' },
    { src: '/assets/images/socgal7.jpg', alt: 'Person looking at their phone', className: 'col-span-1 row-span-1' },
    { src: '/assets/images/socgal8.jpg', alt: 'Wedding couple', className: 'col-span-1 row-span-2' },
    { src: '/assets/images/socgal9.jpg', alt: 'Outdoor scene with furniture', className: 'col-span-1 row-span-1' },
    { src: '/assets/images/socgal10.jpg', alt: 'Traditional decorative items', className: 'col-span-1 row-span-1' },
  ];

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
