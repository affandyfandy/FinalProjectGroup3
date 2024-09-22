import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroEllipsisVertical, heroStar, heroUser } from '@ng-icons/heroicons/outline';
import { heroAdjustmentsHorizontalSolid, heroStarSolid, heroUserSolid } from '@ng-icons/heroicons/solid';
import { Router } from '@angular/router';
import { RoomFormComponent } from '../room-form/room-form.component';
import { Room, RoomResponse } from '../../../model/room.model';
import { RoomService } from '../../../services/room.service';
import { AuthService } from '../../../services/auth/auth.service';


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
    provideIcons({ heroEllipsisVertical, heroUserSolid, heroStarSolid, heroAdjustmentsHorizontalSolid})
  ]
})
export class RoomListComponent implements OnInit{
  rooms: Room[] = [];
  totalElements: number = 0;
  totalPages: number = 0;
  currentPage: number = 1;
  pageSize: number = 10;
  sortColumn: string = 'name';
  sortDirection: string = 'asc';
  error: string = '';
  isAdmin: boolean = false;

  selectedRoom: Room | null = null;
  showOptions: boolean = false;
  isModalVisible: boolean = false;
  action: string = 'add';

  constructor(
    private roomService: RoomService,
    private authService: AuthService,
    private router: Router
  ){}

  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();
    this.loadRooms(this.currentPage);
  }

  loadRooms(currentPage: number) {
    this.roomService.getAllActiveRooms(currentPage-1, this.pageSize).subscribe({
      next: (response: RoomResponse) => {
        console.log("Full response:", response);
        this.rooms = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.currentPage = currentPage;
      },
      error: (err) => {
        this.error = 'Nothing to see..';
        console.error(err);
        // this.toastService.showToast('Failed to load products', 'error');
      }
    });
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.loadRooms(this.currentPage);
    // this.toastService.showToast('Changed page', 'success');
  }

  toggleItemStatus(room: Room): void {
    if (room.status === 'ACTIVE') {
      this.roomService.deactivateRoom(room.id).subscribe(
        (updatedRoom: Room) => {
          console.log('Room deactivated successfully:', updatedRoom);
          room.status === 'INACTIVE';
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
          room.status === 'ACTIVE';
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

  createRoom() {
    this.router.navigate(['/rooms/create']);
  }

  editRoom(room: any) {
    // Implement the edit functionality
    console.log('Edit room', room);
    this.showOptions = false; // Hide options after selection
  }

  viewRoom(room: any) {
    // Implement the view functionality
    console.log('View room', room);
    this.showOptions = false; // Hide options after selection
  }

}
