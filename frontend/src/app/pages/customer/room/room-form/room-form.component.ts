import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Facility, Room, RoomType, Status } from '../../../../model/room.model';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomService } from '../../../../services/room.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';
import { RouterConfig } from '../../../../config/route.constants';
import { KeyValue } from '../../../../model/key-value.model';
import { Reservation } from '../../../../model/reservation.model';
import { SafeUrl } from '@angular/platform-browser';

import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core'; // For native Date support
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CUSTOM_DATE_FORMATS } from '../../../../util/custom-date.util';
import { RangeDates } from '../../../../model/range-dates';
import { ReservationService } from '../../../../services/reservation/reservation.service';

@Component({
  selector: 'app-room-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent,
    FormsModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule
  ],
  templateUrl: './room-form.component.html',
  providers: [
    DatePipe,
    provideIcons({ heroChevronLeft}),
    {
      provide: MAT_DATE_FORMATS,
      useValue: CUSTOM_DATE_FORMATS
    },
    {
      provide: MAT_DATE_LOCALE,
      useValue: 'en-GB'
    }
  ]
})
export class RoomFormComponent implements OnInit {
  @Input() rsvp: Reservation | null = null;
  @Input() action?: string;
  @Output() save = new EventEmitter<Reservation>();
  @Output() cancel = new EventEmitter<void>();

  rsvpForm: FormGroup;
  room: Room | null = null;
  roomTypes: RoomType = RoomType.DOUBLE;
  roomStatus: Status = Status.ACTIVE;
  roomId: string | null = null;

  photo: SafeUrl | null = null;

  checkIn: string = '';
  checkOut: string = '';

  checkInDate: Date | null = null;
  checkOutDate: Date | null = null;

  queryParam: any = {};
  description: string = '';
  selectedFacility: Facility[] = [];
  facilityList: string[] = [];

  minDate: Date = new Date();
  maxCheckOutDate: Date | null = null;

  disabledRanges: RangeDates[] = [];

  constructor(
    private fb: FormBuilder,
    private roomService: RoomService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe,
    private reservationService: ReservationService
  ) {
    this.rsvpForm = this.fb.group({
      checkIn: ['', [Validators.required]],
      checkOut: [{value: null, disabled: true}, [Validators.required]],
      roomId: ['']
    });

    this.activatedRoute.queryParams.subscribe(params => {
      const checkInParam = params['checkIn'] !== undefined ? params['checkIn'] : '';
      const checkOutParam = params['checkOut'] !== undefined ? params['checkOut'] : '';

      if (checkInParam && checkOutParam) {
        const checkInParsedDate = this.parseDate(checkInParam); 
        const checkOutParsedDate = this.parseDate(checkOutParam);
        if (checkInParsedDate) {
          this.rsvpForm.controls['checkIn'].setValue(checkInParsedDate);
          this.rsvpForm.controls['checkOut'].setValue(checkOutParsedDate);
        }
      }
    });
  }

  roomDescription: KeyValue[] = [
    { key: 'SUITE', value: 'Experience luxury and comfort in our Deluxe Suite, designed to offer a spacious and elegant retreat. The suite features a plush king-sized bed, a separate living area with stylish furnishings, and large windows offering breathtaking city or garden views. Enjoy modern amenities including a flat-screen TV, complimentary high-speed Wi-Fi, a fully stocked minibar, and a luxurious marble bathroom with a soaking tub and rain shower. Perfect for both relaxation and business, this suite provides a serene and sophisticated atmosphere, ensuring a memorable stay.' },
    { key: 'SINGLE', value: 'Designed for solo travelers, our Standard Single Room offers a cozy and comfortable space with all the essentials for a perfect stay. The room features a plush single bed, contemporary décor, a work desk, and a flat-screen TV. Enjoy complimentary Wi-Fi, a minibar, and tea/coffee-making facilities. The private bathroom includes a walk-in shower and premium toiletries, ensuring a refreshing experience. This well-appointed room provides a quiet and restful environment, making it an ideal choice for business or leisure stays.' },
    { key: 'DOUBLE', value: 'Our Classic Double Room offers a perfect blend of comfort and style, ideal for couples or friends traveling together. The room features two cozy double beds, modern décor, and thoughtful amenities, including a flat-screen TV, complimentary Wi-Fi, a work desk, and a tea/coffee maker. The elegant bathroom comes equipped with premium toiletries and a refreshing shower. With ample space and soft lighting, this room provides a relaxing atmosphere for a restful night’s sleep, making it the perfect choice for your stay.' },
  ];


  ngOnInit(): void {
    this.roomId = this.activatedRoute.snapshot.paramMap.get('id');
    if (this.roomId) {
      this.roomService.getRoomById(this.roomId).subscribe((room: Room) => {
        this.room = room;
        this.getRoomDescription();
        this.updateSelectedFacilities();

        if (this.room?.photo) {
          this.fetchRoomPhoto(this.room.photo);
        }  
      });
      this.reservationService.getAvailableDates(this.roomId).subscribe((dateRanges: RangeDates[]) => {
        this.disabledRanges = dateRanges;
      });
      
    }

    this.rsvpForm.get('checkIn')?.valueChanges.subscribe((checkInValue) => {
      this.rsvpForm.get('checkOut')?.reset();
      this.rsvpForm.get('checkOut')?.disable();

      if (checkInValue) {
        this.rsvpForm.get('checkOut')?.enable();
      }
    });
  }

  getUnavailableRangeDate(date: Date): RangeDates | null {
    for (const range of this.disabledRanges) {
      if (range.from && date < range.from) {
        return range;
      }
    }
    return null;
  }

  parseDate(dateString: string): Date | null {
    const parts = dateString.split('-');
    if (parts.length === 3) {
      const year = parseInt(parts[0], 10);
      const month = parseInt(parts[1], 10) - 1;
      const day = parseInt(parts[2], 10);
      console.log(new Date(year, month, day));
      return new Date(year, month, day);
    }
    return null;
  }
  

  goBack(): void {
    this.router.navigate(['/rooms']);
  }

  bookRoom() {
    if (this.roomId) {
        this.queryParam['roomId'] = this.roomId;
        this.queryParam['checkIn'] = this.checkIn;
        this.queryParam['checkOut'] = this.checkOut;

        this.router.navigate([RouterConfig.RESERVATION.link, 'create'], {
          queryParams: this.queryParam
        });
    }
  }

  getRoomDescription(): void {
    if (this.room) {
      const roomDesc = this.roomDescription.find(desc => desc.key === this.room?.roomType);
      this.description = roomDesc ? roomDesc.value : 'Room description not available';
    }
    console.log(this.description);
  }

  updateSelectedFacilities(): void {
    if (this.room?.facility) {
      this.selectedFacility = this.room.facility.map((facility: Facility) => facility);
    } else {
      this.selectedFacility = [];
    }

    this.selectedFacility.forEach((facility) => {
      if (facility.toString() === 'TELEVISION'){
        this.facilityList.push('Television');
      }
      else if (facility.toString() === 'REFRIGERATOR'){
        this.facilityList.push('Refrigerator');
      }
      else if (facility.toString() === 'MINIBAR'){
        this.facilityList.push('Minibar');
      }
      else if (facility.toString() === 'WIFI'){
        this.facilityList.push('Wi-Fi');
      }
      else if (facility.toString() === 'COFFEE_MAKER'){
        this.facilityList.push('Coffee Maker');
      }
      else if (facility.toString() === 'HAIR_DRYER'){
        this.facilityList.push('Hair Dryer');
      }
    })

  }

  onSubmit(): void {
    if (this.rsvpForm.valid) {
      const formValues = this.rsvpForm.value;
      const queryParams = {
        roomId: this.room?.id,
        checkIn: this.datePipe.transform(formValues.checkIn, 'yyyy-MM-dd'),
        checkOut: this.datePipe.transform(formValues.checkOut, 'yyyy-MM-dd')
      };
      this.router.navigate(['reservation/create'], { queryParams });
    } else {
      console.error('Form is invalid');
    }
  }

  fetchRoomPhoto(photo: string) {
    this.roomService.getRoomPhoto(photo).subscribe({
      next: (response) => {
        const objectURL = URL.createObjectURL(response);
        this.photo = objectURL;
      },
      error: (error) => {
        console.error('fetch photo error', error);
      }
    });
  }

  disableDates = (d: Date | null): boolean => {
    if (!d) {
      return false;
    }

    for (const range of this.disabledRanges) {
      if (range.from && range.to) {
        if (d >= range.from && d <= range.to) {
          return false;
        }
      }
    }
    return true;
  }
}
