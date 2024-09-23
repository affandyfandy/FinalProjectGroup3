import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroEllipsisVertical } from '@ng-icons/heroicons/outline';
import { User } from '../../../../model/user.model';
import { UserService } from '../../../../services/user.service';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NgIconComponent
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss',
  providers: [
    provideIcons({ heroEllipsisVertical })
  ]
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  totalPages: number = 0;
  currentPage: number = 0;
  totalElements: number = 0;
  pageSize: number = 10;

  constructor(
    private userService: UserService
  ){}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(page: number = 0, size: number = 10): void {
    this.userService.getUsers(page, size).subscribe((data) => {
      this.users = data.content;
      this.totalPages = data.totalPages;
      this.currentPage = data.number;
      this.totalElements = data.totalElements;
    });
  }

  changePage(page: number): void {
    this.loadUsers(page, this.pageSize);
  }

  toggleItemStatus(user: User): void {
    this.userService.toggleUserStatus(user.email).subscribe(() => {
      this.loadUsers(this.currentPage, this.pageSize);
    });
  }
}
