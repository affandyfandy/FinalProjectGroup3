import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Room } from '../../../../model/room.model';
import { UserService } from '../../../../services/user.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { RoomService } from '../../../../services/room.service';
import { User } from '../../../../model/user.model';
import { CommonModule } from '@angular/common';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft, heroEllipsisVertical, heroExclamationTriangle, heroInformationCircle, heroStar } from '@ng-icons/heroicons/outline';
import { heroUserSolid, heroStarSolid, heroAdjustmentsHorizontalSolid, heroInformationCircleSolid, heroHomeSolid, heroCreditCardSolid, heroUserCircleSolid } from '@ng-icons/heroicons/solid';
import { ToastService } from '../../../../services/toast.service';

@Component({
  selector: 'app-reservation-form',
  standalone: true,
  imports: [
    CommonModule,
    NgIconComponent
    
  ],
  templateUrl: './reservation-form.component.html',
  styleUrl: './reservation-form.component.scss',
  providers: [
    provideIcons({ heroEllipsisVertical, heroChevronLeft, heroUserSolid, heroUserCircleSolid, heroStarSolid, heroExclamationTriangle, heroHomeSolid, heroCreditCardSolid})
  ]
})
export class ReservationFormComponent implements OnInit {

  user: User | null = null;
  room: Room | null = null;

  queryParam: any = {};
  totalDays: number = 0;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    private roomService: RoomService,
    private toastService: ToastService,
  ) {}

  ngOnInit(): void {
    if (!this.authService.checkCredentials()) {
      this.router.navigate(['/auth/login']);
      this.toastService.showToast('Please login first', 'warning');
    }

    this.activatedRoute.queryParams.subscribe(params => {
      this.queryParam = params;
    });
    this.loadData();
  }

  loadData(): void {
    if (this.authService.checkCredentials()) {
      this.userService.getUserById(this.authService.getUserInformation()[1].value).subscribe((user: User) => {
        this.user = user;
      });
      this.getTotalDays();

      this.roomService.getRoomById(this.queryParam.roomId).subscribe((room: Room) => {
        this.room = room;
      });
    }
  }
  confirmReservation() {
    this.router.navigate(['/reservation/payment']);
  }

  getTotalDays(): void{
    this.totalDays = this.queryParam.checkIn - this.queryParam.checkOut;
    console.log(this.totalDays);
  }
}
