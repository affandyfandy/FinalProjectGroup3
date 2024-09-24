import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroEllipsisVertical, heroStar, heroUser } from '@ng-icons/heroicons/outline';
import { heroAdjustmentsHorizontalSolid, heroStarSolid, heroUserSolid } from '@ng-icons/heroicons/solid';
import { Router } from '@angular/router';
import { RoomFormComponent } from '../room-form/room-form.component';
import { Room, RoomResponse } from '../../../../model/room.model';
import { RoomService } from '../../../../services/room.service';
import { AuthService } from '../../../../services/auth/auth.service';


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

}
