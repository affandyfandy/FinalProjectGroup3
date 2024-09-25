import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronDown, heroChevronUp, heroChevronUpDown, heroEllipsisVertical } from '@ng-icons/heroicons/outline';
import { User } from '../../../../model/user.model';
import { UserService } from '../../../../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { heroAdjustmentsHorizontalSolid, heroStarSolid, heroUserSolid } from '@ng-icons/heroicons/solid';
import { DeleteModalComponent } from '../../../../main/components/modal/delete-modal/delete-modal.component';
import { FilterModalComponent } from '../../../../main/components/modal/filter-modal/filter-modal.component';
import { KeyValue } from '../../../../model/key-value.model';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NgIconComponent,
    DeleteModalComponent,
    FilterModalComponent
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss',
  providers: [
    provideIcons({
      heroEllipsisVertical,
      heroUserSolid,
      heroStarSolid,
      heroAdjustmentsHorizontalSolid,
      heroChevronUpDown,
      heroChevronUp,
      heroChevronDown})
  ]
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  totalPages: number[] = [];
  currentPage: number = 1;
  totalElements: number = 0;
  pageSize: number = 10;

  selectedUser: User | null = null;
  showOptions: boolean = false;

  showDeleteModal: boolean = false;
  showFilterModal: boolean = false;

  queryParam: string = '';

  headers: KeyValue[] = [
    { key: 'no', value: 'No' },
    { key: 'fullName', value: 'Full Name' },
    { key: 'email', value: 'Email' },
    { key: 'role', value: 'Role' },
    { key: 'lastModifiedBy', value: 'Last Modified By' },
    { key: 'lastModifiedDate', value: 'Last Modified Date' },
    { key: 'status', value: 'Status' },
    { key: '', value: '' }
  ];

  userFieldSearch: KeyValue[] = [
    { key: 'email', value: 'Email' },
    { key: 'fullName', value: 'Full Name' },
    { key: 'phone', value: 'Phone' },
    { key: 'role', value: 'Role' },
    { key: 'phone', value: 'Phone' },
    { key: 'dateOfBirth', value: 'Date of Birth' },
    { key: 'address', value: 'Address' },
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
  role: string = '';

  constructor(
    private userService: UserService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ){}

  ngOnInit(): void {
    this.queryParam = this.getQueryParamsAsString(this.activatedRoute.snapshot.queryParams);
    this.loadUsers(this.currentPage, this.pageSize, '', this.queryParam);
  }

  loadUsers(page: number = 1, size: number = 10, sort: string = '', search: any = {}): void {
    this.userService.getUsers(page - 1, size, sort, search).subscribe((data) => {
      this.users = data.content;
      this.totalPages = Array.from({ length: data.totalPages }, (_, i) => i + 1);
      this.totalElements = data.totalElements;
    });
  }

  getQueryParamsAsString(params: any): string {
    const queryArray = [];
    let foundFirstQuery = false;

    for (const key in params) {
      if (params.hasOwnProperty(key)) {
        const value = params[key];
        queryArray.push(`${key}=${encodeURIComponent(params[key])}`);

        if (key === 'role') {
          this.role = value;
        }

        if (key === 'status') {
          this.status = value;
        }

        if (key !== 'role' && key !== 'status' && !foundFirstQuery) {
          this.type = key;
          this.searchText = value;
          foundFirstQuery = true;
        }
      }
    }

    return queryArray.join('&');
  }

  changePage(page: number): void {
    this.loadUsers(page, this.pageSize);
  }

  toggleItemStatus(user: User): void {
    this.userService.toggleUserStatus(user.email).subscribe(() => {
      this.loadUsers(this.currentPage, this.pageSize);
    });
  }

  createUser() {
    this.router.navigate(['/admin/users/create']);
  }

  editUser(user: User) {
    this.selectedUser = user;
    this.router.navigate(['/admin/users', this.selectedUser.email, 'edit']);
  }

  viewUser(user: User) {
    this.selectedUser = user;
    this.router.navigate(['/admin/users', this.selectedUser.email]);
  }

  deleteUser(user: User): void {
    this.selectedUser = user;
    this.showDeleteModal = true;
    this.showOptions = false;
  }

  filterUser(): void {
    this.showFilterModal = true;
    this.showOptions = false;
  }

  sortData(header: string) {
    if (!header) return;

    const direction = this.sortDirection[header] === 'asc' ? 'desc' : 'asc';
    this.sortDirection[header] = direction;

    this.users.sort((a, b) => {
      const aValue = a[header as keyof User];
      const bValue = b[header as keyof User];

      if (aValue! < bValue!) {
        return direction === 'asc' ? -1 : 1;
      } else if (aValue! > bValue!) {
        return direction === 'asc' ? 1 : -1;
      } else {
        return 0;
      }
    });
  }

  getSortIcon(header: string) {
    if (!this.sortDirection[header]) {
      return 'heroChevronUpDown';
    }
  
    return this.sortDirection[header] === 'asc' ? 'heroChevronUp' : 'heroChevronDown';
  }

  toggleOptions(event: Event, user: any) {
    event.stopPropagation();
    this.selectedUser = user;
    this.showOptions = this.selectedUser === user ? !this.showOptions : true;
  }

  onPageSizeChange(event: Event): void {
    this.pageSize = +(event.target as HTMLSelectElement).value;
    this.currentPage = 1;
    this.loadUsers(this.currentPage);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.showOptions = false;
    this.loadUsers(this.currentPage);
  }

  onDeleteConfirmed() {
    if (this.selectedUser) {
      this.userService.deleteUser(this.selectedUser.email).subscribe(() => {
        this.loadUsers(this.currentPage);
        this.showDeleteModal = false;
      });
    }
  }

  onCancelDelete() {
    this.showDeleteModal = false;
  }

  onApplyFilter(queryParams: any) {
    this.queryParam = this.getQueryParamsAsString(queryParams);
    console.log('Query Params:', this.queryParam); 
    this.loadUsers(this.currentPage, this.pageSize, '', this.queryParam);
    this.showFilterModal = false;
  }

  onCloseFilter() {
    this.showFilterModal = false;
  }
}
