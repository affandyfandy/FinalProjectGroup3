import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroEllipsisVertical, heroStar, heroUser } from '@ng-icons/heroicons/outline';
import { heroAdjustmentsHorizontalSolid, heroStarSolid, heroUserSolid } from '@ng-icons/heroicons/solid';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomFormComponent } from '../room-form/room-form.component';
import { Facility, Room, RoomResponse } from '../../../../model/room.model';
import { RoomService } from '../../../../services/room.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { SafeUrl } from '@angular/platform-browser';
import { ModalComponent } from '../../../../main/components/modal/modal.component';
import { FilterModalComponent } from '../../../../main/components/modal/filter-modal/filter-modal.component';
import { KeyValue } from '../../../../model/key-value.model';


@Component({
  selector: 'app-room-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RoomFormComponent,
    NgIconComponent,
    FilterModalComponent
  ],
  templateUrl: './room-list.component.html',
  providers: [
    provideIcons({ heroEllipsisVertical, heroUserSolid, heroStarSolid, heroAdjustmentsHorizontalSolid})
  ]
})
export class RoomListComponent implements OnInit{
  rooms: Room[] = [];
  totalElements: number = 0;
  totalPages: number[] = [];
  currentPage: number = 1;
  pageSize: number = 6;
  sortColumn: string = 'name';
  sortDirection: string = 'asc';
  error: string = '';
  isAdmin: boolean = false;

  selectedRoom: Room | null = null;
  showOptions: boolean = false;
  isModalVisible: boolean = false;
  action: string = 'detail';

  showFilterModal: boolean = false;

  type: string = '';
  searchText: string = '';
  status: string = '';
  checkIn: string = '';
  checkOut: string = '';
  guest: number =0;

  selectedRoomType: string | null = null;

  constructor(
    private roomService: RoomService,
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
  ){
    
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.checkIn = params['checkIn'] !== undefined ? params['checkIn'] : '';
      this.checkOut = params['checkOut'] !== undefined ? params['checkOut'] : '';
      this.guest = params['guest'] !== undefined ? +params['guest'] : 1; 
      this.selectedRoomType = params['roomType'] !== undefined ? params['roomType'] : null;
    });

    this.isAdmin = this.authService.isAdmin();
    this.loadRooms(this.currentPage, this.selectedRoomType);
  }

  isAllType(): boolean {
    return this.router.url === '/rooms';
  }

  loadRooms(currentPage: number, roomType: string | null = null): void {
    this.roomService.getAvailableRooms(this.checkIn, this.checkOut, this.guest, currentPage-1, 6).subscribe({
      next: (response: RoomResponse) => {
        console.log("Full response:", response);
        this.rooms = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = Array.from({ length: Math.ceil(this.totalElements / this.pageSize) }, (_, i) => i + 1);
        this.currentPage = currentPage;

        this.rooms.forEach((room: Room) => {
          this.roomService.fetchRoomPhoto(room.photo).subscribe({
            next: (photoUrl) => {
              if (photoUrl) {
                room.photoSafeUrl = photoUrl;
              } else {
                console.log('Photo URL is null or an error occurred');
              }
            }
          });
          
        });

      },
      error: (err) => {
        this.error = 'Nothing to see..';
        console.error(err);
      }
    });
  }

  selectRoomType(roomType: string | null) {
    this.selectedRoomType = roomType;

    if (roomType === null) {
      this.router.navigate([], {
        relativeTo: this.activatedRoute,
        queryParams: { roomType: null },
        queryParamsHandling: 'merge'
      });
    } else {
      this.router.navigate([], {
        relativeTo: this.activatedRoute,
        queryParams: { roomType },
        queryParamsHandling: 'merge'
      });
    }

    this.loadRooms(this.currentPage, this.selectedRoomType);
  }

  viewRoom(id: string) {
    this.router.navigate(['/rooms', id]);
  }

  bookRoom(id: string) {
    this.router.navigate(['/reservation', id]);
  }

  onPageSizeChange(event: Event): void {
    this.pageSize = +(event.target as HTMLSelectElement).value;
    this.currentPage = 1;
    this.loadRooms(this.currentPage);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadRooms(this.currentPage);
    console.log("Room photo:", this.rooms[0].photoSafeUrl);

  }

  createReservation(id: string): void{
    console.log("Clicked");
    const currentUrl = this.router.url.split('?')[0];

    const queryParams: any = {};

    queryParams['checkIn'] = this.checkIn;
    queryParams['checkOut'] = this.checkOut;
    
    this.router.navigate([currentUrl, id], {
      queryParams: queryParams,
      relativeTo: this.activatedRoute,
    });
  }


}
