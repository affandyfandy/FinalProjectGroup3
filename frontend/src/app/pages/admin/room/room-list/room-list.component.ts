import { Component } from '@angular/core';
import { Room, RoomResponse } from '../../../../model/room.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { AuthService } from '../../../../services/auth/auth.service';
import { RoomService } from '../../../../services/room.service';
import { RoomFormComponent } from '../room-form/room-form.component';
import { heroChevronDown, heroChevronUp, heroChevronUpDown, heroEllipsisVertical } from '@ng-icons/heroicons/outline';
import { heroUserSolid, heroStarSolid, heroAdjustmentsHorizontalSolid } from '@ng-icons/heroicons/solid';

@Component({
  selector: 'app-room-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RoomFormComponent,
    NgIconComponent
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

  constructor(
    private roomService: RoomService,
    private authService: AuthService,
    private router: Router
  ){}

  ngOnInit(): void {
    // this.isAdmin = this.authService.isAdmin();
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
        // this.toastService.showToast('Failed to load products', 'error');
      }
    });
  }

  toggleItemStatus(room: Room): void {
    if (room.status === 'ACTIVE') {
      this.roomService.deactivateRoom(room.id).subscribe(
        (updatedRoom: Room) => {
          console.log('Room deactivated successfully:', updatedRoom);
          room.status = 'INACTIVE';
          // this.toastService.showToast('Product deactivated successfully!', 'success');
        },
        (error: any) => {
          console.error('Error deactivating room:', error);
          // this.toastService.showToast('Error deactivating product.', 'error');
        }
      );
    } else {
      this.roomService.activateRoom(room.id).subscribe(
        (updatedRoom: Room) => {
          console.log('Product activated successfully:', updatedRoom);
          room.status = 'ACTIVE';
          // this.toastService.showToast('Product activated successfully!', 'success');
        },
        (error: any) => {
          console.error('Error activating product:', error);
          // this.toastService.showToast('Error deactivating product.', 'error');
        }
      );
    }
  }


  toggleOptions(event: Event, room: any) {
    event.stopPropagation(); // Prevent event from propagating to parent elements
    this.selectedRoom = this.selectedRoom === room ? null : room;
    this.showOptions = this.selectedRoom === room ? !this.showOptions : true;
  }

  // changePage(page: number): void {
  //   this.currentPage = page;
  //   this.loadRooms(this.currentPage);
  //   // this.toastService.showToast('Changed page', 'success');
  // }
  
  createRoom() {
    this.router.navigate(['/admin/rooms/create']);
  }

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
}
