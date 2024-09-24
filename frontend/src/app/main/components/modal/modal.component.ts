import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [],
  templateUrl: './modal.component.html'
})
export class ModalComponent {
  @Input() isOpen = false;  // To control modal visibility
  @Output() close = new EventEmitter<void>();  // Event emitter to notify when modal is closed

  // Method to close the modal
  onClose(): void {
    this.isOpen = false;
    this.close.emit(); // Emit close event
  }
}
