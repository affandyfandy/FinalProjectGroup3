import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { KeyLabel } from '../../../../model/key-label.model';

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
  type: string = '';
  searchText: string = '';
  status: string = '';
  fields: KeyLabel[] = [];

  @Output() filter = new EventEmitter<{ searchText: string, status: string }>();
  @Output() close = new EventEmitter<void>();

  applyFilter() {
    this.filter.emit({ searchText: this.searchText, status: this.status });
  }

  onClose() {
    this.close.emit();
  }
}
