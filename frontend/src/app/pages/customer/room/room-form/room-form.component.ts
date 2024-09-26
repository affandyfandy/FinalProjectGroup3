import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Facility, Room, RoomType, Status } from '../../../../model/room.model';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomService } from '../../../../services/room.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';
import { RouterConfig } from '../../../../config/route.constants';
import { KeyValue } from '../../../../model/key-value.model';
import { Reservation } from '../../../../model/reservation.model';

@Component({
  selector: 'app-room-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent,
    FormsModule
  ],
  templateUrl: './room-form.component.html',
  providers: [
    provideIcons({ heroChevronLeft})
  ]
})
export class RoomFormComponent implements OnInit {
  @Input() rsvp: Reservation | null = null;
  @Input() action?: string;
  @Output() save = new EventEmitter<Reservation>();
  @Output() cancel = new EventEmitter<void>();

  rsvpForm: FormGroup; // Declare rsvpForm
  room: Room | null = null;
  roomTypes: RoomType = RoomType.DOUBLE;
  roomStatus: Status = Status.ACTIVE;
  roomId: string | null = null;

  checkIn: string = '';
  checkOut: string = '';

  queryParam: any = {};
  description: string = '';
  selectedFacility: Facility[] = [];
  facilityList: string[] = [];

  constructor(
    private fb: FormBuilder,
    private roomService: RoomService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {
    this.rsvpForm = this.fb.group({
      checkIn: ['', [Validators.required]],
      checkOut: ['', [Validators.required]],
      roomId: ['']
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
      });
    }

    this.activatedRoute.queryParams.subscribe(params => {
      this.checkIn = params['checkIn'] !== undefined ? params['checkIn'] : '';
      this.checkOut = params['checkOut'] !== undefined ? params['checkOut'] : '';
    });
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
        checkIn: formValues.checkIn,
        checkOut: formValues.checkOut
      };
      this.router.navigate(['reservation/create'], { queryParams });
    } else {
      console.error('Form is invalid');
    }
  }
}
