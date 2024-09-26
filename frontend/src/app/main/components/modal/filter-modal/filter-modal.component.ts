import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { KeyValue } from '../../../../model/key-value.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-filter-modal',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './filter-modal.component.html',
  styleUrl: './filter-modal.component.scss'
})
export class FilterModalComponent {
  
  @Input() type: string = '';
  @Input() searchText: string = '';
  @Input() status: string = '';
  @Input() role: string = '';

  queryParams: any = {};

  @Input() fields: KeyValue[] = [];

  @Output() filter = new EventEmitter<any>();
  @Output() close = new EventEmitter<void>();

  constructor(private router: Router) { }

  applyFilter() {
    if (this.type && this.searchText) {
      this.queryParams[this.type] = this.searchText;
    }
    if (this.role) {
      this.queryParams.role = this.role;
    }
    if (this.status) {
      this.queryParams.status = this.status;
    }

    this.router.navigate([], {
      queryParams: this.queryParams
    });

    this.filter.emit(this.queryParams);
  }

  clearFilter() {
    this.queryParams = {};
    this.searchText = '';
    this.role = '';
    this.status = '';

    this.router.navigate([], {
      queryParams: this.queryParams
    });

    this.filter.emit(this.queryParams);
  }

  isRoomPage(): boolean {
    return this.router.url.includes('rooms');
  }

  onClose() {
    this.close.emit();
  }
}
