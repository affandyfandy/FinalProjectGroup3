import { Component } from '@angular/core';
import { Room, RoomResponse } from '../../../../model/room.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { AuthService } from '../../../../services/auth/auth.service';
import { RoomService } from '../../../../services/room.service';
import { RoomFormComponent } from '../room-form/room-form.component';
import { heroChevronDown, heroChevronUp, heroChevronUpDown, heroEllipsisVertical } from '@ng-icons/heroicons/outline';
import { heroUserSolid, heroStarSolid, heroAdjustmentsHorizontalSolid } from '@ng-icons/heroicons/solid';
import { ToastService } from '../../../../services/toast.service';
import { DeleteModalComponent } from '../../../../main/components/modal/delete-modal/delete-modal.component';
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
    DeleteModalComponent,
    FilterModalComponent
  ],
  templateUrl: './room-list.component.html',
  providers: [
    provideIcons({
      heroEllipsisVertical,
      heroUserSolid,
      heroStarSolid,
      heroAdjustmentsHorizontalSolid,
      heroChevronUpDown})
  ]
})
export class RoomListComponent {
  rooms: Room[] = [];
  totalElements: number = 0;
  totalPages: number[] = [];
  currentPage: number = 1;
  pageSize: number = 10;
  sortBy: string = 'roomType';
  sortOrder: string = 'asc';
  error: string = '';
  isAdmin: boolean = false;

  selectedRoom: Room | null = null;
  showOptions: boolean = false;

  isModalVisible: boolean = false;
  modalTitle: string = '';
  modalMessage: string = '';

  showDeleteModal: boolean = false;
  showFilterModal: boolean = false;

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
  
  sortDirection: { [key: string]: string } = {};
  currentSort: string = '';

  type: string = '';
  searchText: string = '';
  status: string = '';

  queryParam: string = '';

  constructor(
    private roomService: RoomService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastService: ToastService
  ){}

  ngOnInit(): void {
    this.queryParam = this.getQueryParamsAsString(this.activatedRoute.snapshot.queryParams);
    this.loadRooms(this.currentPage);
  }

  loadRooms(currentPage: number) {
    this.roomService.getAllRooms(currentPage-1, this.pageSize, this.sortBy, this.sortOrder).subscribe({
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

  toggleItemStatus(room: Room): void {
    if (room.status === 'ACTIVE') {
      this.roomService.deactivateRoom(room.id).subscribe(
        (updatedRoom: Room) => {
          console.log('Room deactivated successful!', updatedRoom);
          room.status = 'INACTIVE';
          this.toastService.showToast('Room deactivated successful!', 'success');
        },
        (error: any) => {
          console.error('Error deactivating room:', error);
          this.toastService.showToast('Error deactivating room: ' + error, 'error');
        }
      );
    } else {
      this.roomService.activateRoom(room.id).subscribe(
        (updatedRoom: Room) => {
          console.log('Room activated successful!', updatedRoom);
          room.status = 'ACTIVE';
          this.toastService.showToast('Room activated successful!', 'success');
        },
        (error: any) => {
          console.error('Error activating room:', error);
          this.toastService.showToast('Error deactivating room: ' + error, 'error');
        }
      );
    }
  }


  toggleOptions(event: Event, room: any) {
    event.stopPropagation(); // Prevent event from propagating to parent elements
    this.selectedRoom = this.selectedRoom === room ? null : room;
    this.showOptions = this.selectedRoom === room ? !this.showOptions : true;
  }
  
  createRoom() {
    this.router.navigate(['/admin/rooms/create']);
  }

  // importData() {
  //   this.router.navigate(['/admin/rooms/import']);
  // }

  editRoom(room: Room) {
    console.log('Edit room', room);
    this.selectedRoom = room;
    this.router.navigate(['/admin/rooms', this.selectedRoom.id, 'edit']);
  }

  viewRoom(room: Room) {
    console.log('View room', room);
    this.selectedRoom = room;
    this.router.navigate(['/admin/rooms', this.selectedRoom?.id, 'view']);
  }

  onDeleteConfirmed(): void {  
    if (this.selectedRoom) {
      this.roomService.deleteRoom(this.selectedRoom.id).subscribe({
        next: () => {
          console.log('Room deleted successful!');
          this.loadRooms(this.currentPage);
          this.toastService.showToast('Room deleted successful!', 'success');
        },
        error: (err) => {
          console.error('Error deleting room:', err);
          this.toastService.showToast('Error deleting room: ' + err, 'error');
        }
      });
    }
    this.showDeleteModal = false;
  }

  deleteRoomButton(room: Room): void {
    this.selectedRoom = room;
    this.showDeleteModal = true;
    this.showOptions = false;
  }

  onCancelDelete() {
    this.showDeleteModal = false;
  }

  sortData(header: string) {
    console.log(header);
    var columnName = '';
    if (header === 'roomnumber'){
      columnName = 'roomNumber';
    }
    else if (header === 'roomtype'){
      columnName = 'roomType'
    }
    if (this.sortBy === columnName){
      this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = columnName;
      this.sortOrder = 'asc';
    }
    this.loadRooms(this.currentPage);
  }

  // Handle changes in page size
  onPageSizeChange(event: Event): void {
    this.pageSize = +(event.target as HTMLSelectElement).value;
    this.currentPage = 1; // Reset to the first page
    this.loadRooms(this.currentPage);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadRooms(this.currentPage);
  }

  openModal() {
    this.isModalVisible = true;
  }

  closeModal() {
    this.isModalVisible = false;
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
}
