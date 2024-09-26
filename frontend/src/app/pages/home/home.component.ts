import { CommonModule, DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
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
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  providers: [
    DatePipe,
    provideNgIconsConfig({
      size: '2em',
    }),
    provideIcons({ 
      bootstrapTwitterX,
      bootstrapInstagram,
      bootstrapFacebook,
      bootstrapYoutube,
      bootstrapLinkedin
    }),
  ]
})
export class HomeComponent {

  checkInMinDate?: string;
  checkOutMinDate?: string;

  searchRoomForm: FormGroup;
  queryParams: any = {};

  constructor(private fb: FormBuilder, private router: Router, private datePipe: DatePipe) {
    this.searchRoomForm = this.fb.group({
      checkIn: ['', Validators.required],
      checkOut: [{value: null, disabled: true}, Validators.required],
      guest: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.searchRoomForm.get('checkIn')?.valueChanges.subscribe((value) => {
      this.checkOutMinDate = value;

      this.searchRoomForm.get('checkOut')?.reset();

      if (value) {
        this.searchRoomForm.get('checkOut')?.enable();
      }
    });

    const today = new Date();
    this.checkInMinDate = today.toISOString().split('T')[0];
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
    console.log(this.searchRoomForm.value);
      this.queryParams = {
        checkIn: this.datePipe.transform(this.searchRoomForm.value.checkIn, 'yyyy-MM-dd') ?? '',
        checkOut: this.datePipe.transform(this.searchRoomForm.value.checkOut, 'yyyy-MM-dd') ?? '',
        guest: this.searchRoomForm.value.guest
      };
      this.router.navigate(['/rooms'], {
        queryParams: this.queryParams
      });
  }
}
