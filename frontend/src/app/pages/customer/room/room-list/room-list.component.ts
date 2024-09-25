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

  queryParam: string = '';

  roomFieldSearch: KeyValue[] = [
    { key: 'roomNumber', value: 'Room Number' },
    { key: 'capacity', value: 'Capacity' },
    { key: 'roomType', value: 'Room Type' },
    { key: 'price', value: 'Price' },
    { key: 'status', value: 'Status' },
    { key: 'createdBy', value: 'Created By' },
    { key: 'createdDate', value: 'Created Date' },
    { key: 'lastModifiedBy', value: 'Last Modified By' },
    { key: 'lastModifiedDate', value: 'Last Modified Date' },
  ];

  constructor(
    private roomService: RoomService,
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ){}

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.checkIn = params['checkIn'] !== undefined ? params['checkIn'] : '';
      this.checkOut = params['checkOut'] !== undefined ? params['checkOut'] : '';
      this.guest = params['guest'] !== undefined ? +params['guest'] : 1; 
    });

    this.isAdmin = this.authService.isAdmin();
    this.loadRooms(this.currentPage);
  }

  loadRooms(currentPage: number) {
    this.roomService.getAvailableRooms(this.checkIn, this.checkOut, this.guest, currentPage-1, this.pageSize).subscribe({
      next: (response: RoomResponse) => {
        console.log("Full response:", response);
        this.rooms = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = Array.from({ length: Math.ceil(this.totalElements / this.pageSize) }, (_, i) => i + 1);
        this.currentPage = currentPage;
      },
      error: (err) => {
        this.error = 'Nothing to see..';
        console.error(err);
      }
    });
  }

  viewRoom(id: string) {
    this.router.navigate(['/rooms', id]);
  }

  bookRoom(id: string) {
    this.router.navigate(['/reservation', id]);
  }

  // Handle changes in page size
  onPageSizeChange(event: Event): void {
    this.pageSize = +(event.target as HTMLSelectElement).value;
    this.currentPage = 1;
    this.loadRooms(this.currentPage);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadRooms(this.currentPage);
  }

  createReservation(id: string, facility: Facility[]): void{
    const currentUrl = this.router.url.split('?')[0];

    const queryParams: any = {};

    queryParams['checkIn'] = this.checkIn;
    queryParams['checkOut'] = this.checkOut;
    
    this.router.navigate([currentUrl, id], {
      queryParams: queryParams,
      relativeTo: this.activatedRoute,
    });
  }

  filterRoom(): void {
    this.showFilterModal = true;
    this.showOptions = false;
  }

  onApplyFilter(queryParams: {
    pageNo?: number;
    pageSize?: number;
    status?: string;
    facility?: string;
    capacity?: number;
    roomType?: string;
    lowerLimitPrice?: number;
  }) {
    // Update queryParam string for logging or debugging
    this.queryParam = this.getQueryParamsAsString(queryParams);
    console.log('Query Params:', this.queryParam); 

    // Call the service method to filter rooms
    this.filterRooms(this.currentPage, queryParams);
    this.showFilterModal = false; // Close the filter modal
  }

  getQueryParamsAsString(params: any): string {
    const queryArray = [];
    let foundFirstQuery = false;

    for (const key in params) {
      if (params.hasOwnProperty(key)) {
        const value = params[key];
        queryArray.push(`${key}=${encodeURIComponent(params[key])}`);

        if (key === 'status') {
          this.status = value;
        }

        if (key !== '' && key !== 'status' && !foundFirstQuery) {
          this.type = key;
          this.searchText = value;
          foundFirstQuery = true;
        }
      }
    }

    return queryArray.join('&');
  }

  filterRooms(
    pageNo: number = 0,
    queryParams: {
      pageSize?: number;
      status?: string;
      facility?: string;
      capacity?: number;
      roomType?: string;
      lowerLimitPrice?: number;
    }
  ) {
    const { pageSize, status, facility, capacity, roomType, lowerLimitPrice } = queryParams;

    this.roomService
      .filterRooms(
        pageNo,
        pageSize ?? this.pageSize,
        status,
        facility,
        capacity,
        roomType,
        lowerLimitPrice
      )
      .subscribe({
        next: (response: RoomResponse) => {
          this.rooms = response.content;
          this.totalElements = response.totalElements;
          this.currentPage = pageNo;
        },
        error: (error) => {
          console.error('Error filtering rooms:', error);
        }
      });
  }

  onCloseFilter() {
    this.showFilterModal = false;
  }

}
