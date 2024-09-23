import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Room, RoomType, Status } from '../../../../model/room.model';
import { RoomService } from '../../../../services/room.service';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';

@Component({
  selector: 'app-room-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent
  ],
  templateUrl: './room-form.component.html',
  providers: [
    provideIcons({ heroChevronLeft})
  ]
})
export class RoomFormComponent implements OnInit {
  @Input() room: Room | null = null;
  @Input() action?: string;
  @Output() save = new EventEmitter<Room>();
  @Output() cancel = new EventEmitter<void>();

  roomTypes = Object.values(RoomType); 
  selectedRoomType: RoomType = RoomType.SINGLE; 
  
  roomStatus = Object.values(Status); 
  selectedRoomStatus: Status = Status.ACTIVE; 

  roomForm: FormGroup;
  isVisible = true;
  showFileUpload = false;
  selectedFile: File | null = null;
  message: string | null = null;

  constructor(
    private fb: FormBuilder,
    private roomService: RoomService) {
    this.roomForm = this.fb.group({
      roomType: ['', Validators.required],
      roomNumber: ['', [Validators.required, Validators.min(0)]],
      capacity: ['', [Validators.required, Validators.min(0)]],
      status: ['', [Validators.required, Validators.min(0)]],
      price: ['', [Validators.required, Validators.min(0)]],
      photo: ['', [Validators.required, Validators.min(0)]],
      facility: ['', [Validators.required, Validators.min(0)]]
    });
  }
  
  ngOnInit(): void {
    if (this.room) {
      this.roomForm.patchValue({
        roomType: this.room.roomType,
        roomNumber: this.room.roomNumber,
        capacity: this.room.capacity,
        status: this.room.status,
        price: this.room.price,
        photo: this.room.photo,
        facility: this.room.facility
      });
    }
  }

  onSubmit(): void {
    if (this.roomForm.valid) {
      const roomData = this.roomForm.value;
      if (this.room) {
        roomData.id = this.room.id;
      }

      this.save.emit(roomData);
      this.onClose();
    }
  }

  onClose(): void {
    this.isVisible = false;
    this.cancel.emit();
  }

  onRoomTypeChange(type: RoomType) {
    this.selectedRoomType = type;
  }
}
