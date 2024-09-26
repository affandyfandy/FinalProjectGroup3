import { CommonModule, DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Reservation, ReservationStatus } from '../../../../model/reservation.model';
import { ReservationService } from '../../../../services/reservation/reservation.service';
import { NgIconComponent, provideIcons, provideNgIconsConfig } from '@ng-icons/core';
import { heroCalendarDays, heroChevronLeft, heroCreditCard, heroInboxStack, heroMapPin } from '@ng-icons/heroicons/outline';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { heroStarSolid, heroUserSolid } from '@ng-icons/heroicons/solid';
import { Facility, Room } from '../../../../model/room.model';
import { RoomService } from '../../../../services/room.service';
import { User } from '../../../../model/user.model';
import { UserService } from '../../../../services/user.service';

import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core'; // For native Date support
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CUSTOM_DATE_FORMATS } from '../../../../util/custom-date.util';
import { catchError, Observable } from 'rxjs';
import { PaymentService } from '../../../../services/reservation/payment.service';
import { Payment } from '../../../../model/payment.model';
import { ToastService } from '../../../../services/toast.service';


@Component({
  selector: 'app-reservation-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent,
    RouterModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule
  ],
  templateUrl: './reservation-form.component.html',
  providers: [
    provideIcons({ heroInboxStack, heroMapPin, heroChevronLeft, heroStarSolid, heroCalendarDays, heroUserSolid, heroCreditCard})
  ]
})
export class ReservationFormComponent implements OnInit {
  @Input() reservation: Reservation | null = null;
  @Input() action?: string;
  @Output() save = new EventEmitter<Reservation>();
  @Output() cancel = new EventEmitter<void>();
  checkInMinDate?: string;
  reservationId?: string | null = null;

  room?: Room;
  selectedRoom: Room | null = null; 

  facilityList: string[] = [];

  user?: User;
  payment?: Payment;
  rooms: Room[] = [];

  rsvpStatus = Object.values(ReservationStatus); 
  selectedRsvpStatus: ReservationStatus = ReservationStatus.CONFIRMED; 

  reservationForm: FormGroup;
  totalAmount: number = 0;

  duration: number = 0;

  disabledRanges: { from: Date, to: Date }[] = [
    { from: new Date(2024, 8, 28), to: new Date(2024, 8, 30) }, // 28 Sept to 30 Sept 2024
    { from: new Date(2024, 9, 2), to: new Date(2024, 9, 5) },   // 02 Oct to 05 Oct 2024
  ];

  constructor(
    private fb: FormBuilder,
    private reservationService: ReservationService,
    private route: ActivatedRoute,
    private router: Router, 
    private roomService: RoomService,
    private userService: UserService,
    private paymentService: PaymentService,
    private toastService: ToastService) {
    this.reservationForm = this.fb.group({
      customerFullName: [''],
      customerEmail: [''],
      customerPhone: [''],
      customerAddress: [''],
      roomId: [''],
      checkInDate: [''],
      checkOutDate: [''],
      status: [''],
      reservationDate: [''],
      accountNumber: [''],
      accountName: [''],
      amount: ['']
    });

    const today = new Date();
    this.checkInMinDate = today.toISOString().split('T')[0];
  }

  ngOnInit(): void {
    const currentRoute = this.route.snapshot.url[this.route.snapshot.url.length - 1]?.path;
    this.action = currentRoute === 'edit' ? 'Edit Reservation' : (currentRoute === 'create' ? 'Create Reservation' : 'Reservation Details');
    if (this.action === 'Create Reservation'){
      this.loadRooms();
    }
    this.reservationId = this.route.snapshot.paramMap.get('id');
    this.loadReservation();
  }

  loadRooms(): void{
    this.roomService.getAllRoomsAsList().subscribe({
      next: (response: Room[]) => {
        console.log("Full response:", response);
        this.rooms = response; 
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  loadReservation(): void {
    if (this.reservationId) {
      this.reservationService.getReservationById(this.reservationId).subscribe({
        next: (reservation: Reservation) => {
          this.reservation = reservation;
          this.findPayment(reservation.id);
          this.reservationForm.patchValue({
            customerName: this.reservation.userId,
            roomId: this.reservation.roomId,
            checkInDate: this.reservation.checkInDate,
            checkOutDate: this.reservation.checkOutDate,
            status: this.reservation.status
          });
          if (this.reservation.roomId) {
            console.log("masuk k eisni nyari room");
            this.roomService.getRoomById(this.reservation.roomId).subscribe({
              next: (room) => {
                this.room = room;
                this.updateSelectedFacilities();
              },
              error: (err) => {
                console.error('Error fetching room details:', err);
              }
            });
          }
          console.log(this.room);
          console.log(this.reservation.userId);
          if (this.reservation.userId){
            this.userService.getUserById(this.reservation.userId).subscribe({
              next: (user) => {
                this.user = user;
                console.log(this.user);
              },
              error: (err) => {
                console.error('Error fetching user details:', err);
              }
            });
          }
        },
        error: (err) => {
          console.error('Error fetching reservation:', err);
        }
      });

    }
  }

  findPayment(id: string): void{
    if (this.reservation){
      this.paymentService.getByReservationId(id).subscribe({
        next: (payment) => {
          this.payment = payment;
          console.log(this.payment);
        },
        error: (err) => {
          console.error('Error fetching payment details:', err);
        }
      });
    }
  }
  
  onSubmit(): void {
    if (this.reservationForm.valid) {
      const reservationData = { ...this.reservationForm.value };
      console.log(reservationData);
      reservationData.userId = reservationData.customerEmail;

      this.checkUser(reservationData);
  
      if (this.action === 'Create Reservation') {
        this.reservationService.createReservation(reservationData).subscribe({
          next: (createdReservation) => {
            this.reservation = createdReservation;
            this.save.emit(createdReservation);
  
            // Call procedPayment only after the reservation is created successfully
            this.procedPayment();
            this.toastService.showToast('Reservation created successful!', 'success');
            this.navigateToReservationList();
          },
          error: (err) => {
            console.error('Error creating reservation:', err);
            this.toastService.showToast('Error creating reservation: '+err, 'error');
            this.navigateToReservationList();
          }
        });
      }
    }
  }

  navigateToReservationList(): void {
    this.router.navigateByUrl('/admin/reservation', { skipLocationChange: true }).then(() => {
      this.router.navigate(['/admin/reservation']); // Refreshes the component
    });
  }
  

  checkUser(reservationForm: FormGroup): void {
    const reservationData = { ...this.reservationForm.value };
  
    this.userService.getUserById(reservationData.customerEmail).subscribe({
      next: (user) => {
        console.log("User found:", user);
        this.user = user;
      },
      error: (err) => {
        console.log(err);
        if (err) {
          console.log("User not found. Creating a new user.");
          
          const userData: User = {
            email: reservationData.customerEmail,
            fullName: reservationData.customerFullName,
            role: 'CUSTOMER',
            phone: reservationData.customerPhone,
            dateOfBirth: '',
            address: reservationData.customerAddress,
            status: 'ACTIVE',
            password: 'bitway123'
          };
  
          // Create a new user
          this.userService.createUser(userData).subscribe({
            next: (createdUser) => {
              this.user = createdUser;
              console.log('New user created:', this.user);
            },
            error: (createErr) => {
              console.error('Error creating user:', createErr);
            }
          });
        } else {
          console.error('Error checking user:', err);
        }
      }
    });
  }

  procedPayment(): void {
    console.log("masuk ke si ini");
    console.log(this.reservation);
    if (this.reservationForm.valid && this.reservation) {
      const reservationData = { ...this.reservationForm.value };
  
      const paymentData: Payment = {
        accountNumber: reservationData.accountNumber,
        accountName: reservationData.accountName,
        amount: reservationData.amount,
        id: '',
        reservationId: this.reservation?.id
      };

      console.log("paymenttt " + paymentData);
  
      if (this.action === 'Create Reservation') {
        this.paymentService.createPayment(paymentData, this.reservation.id).subscribe({
          next: (createdPayment) => {
            this.payment = createdPayment;
          },
          error: (err) => {
            console.error('Error creating payment:', err);
          }
        });
      }
    } else {
      console.error('Form is invalid or reservation is missing.');
    }
  }

  onClose(): void {
    this.router.navigate(['/admin/reservations']);
    this.cancel.emit();
  }
  
  disableDates = (d: Date | null): boolean => {
    if (!d) {
      return false;
    }

    for (const range of this.disabledRanges) {
      if (d >= range.from && d <= range.to) {
        return false;
      }
    }
    return true;
  }

  onDateChange(): void {
    const checkIn = this.reservationForm.get('checkInDate')?.value;
    const checkOut = this.reservationForm.get('checkOutDate')?.value;

    console.log(checkIn);
    if (checkIn && checkOut) {
      this.calculateDuration(checkIn, checkOut);
    } else {
      this.duration = 0;
    }
  }

  calculateDuration(checkIn: string, checkOut: string): void {
    const checkInDate = new Date(checkIn);
    const checkOutDate = new Date(checkOut);
    const timeDifference = checkOutDate.getTime() - checkInDate.getTime();
    console.log(timeDifference);
    this.duration = Math.ceil(timeDifference / (1000 * 3600 * 24));
  }

  onRoomChange(event: Event): void {
    console.log(this.duration);
    const selectedRoomId = (event.target as HTMLSelectElement).value;
    this.selectedRoom = this.rooms.find(room => room.id === selectedRoomId) || null;
    if (this.selectedRoom){
      this.totalAmount = this.selectedRoom.price * this.duration;
    }
  }

  updateSelectedFacilities(): void {
    if (this.room?.facility) {
      this.room.facility = this.room.facility.map((facility: Facility) => facility);
    }

    this.room?.facility.forEach((facility) => {
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
}
