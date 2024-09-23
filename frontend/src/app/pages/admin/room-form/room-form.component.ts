import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Room, RoomType, Status } from '../../../model/room.model';
import { RoomService } from '../../../services/room.service';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-room-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent,
    RouterModule
  ],
  templateUrl: './room-form.component.html',
  providers: [
    provideIcons({ heroChevronLeft })
  ]
})
export class RoomFormComponent implements OnInit {
  @Input() room: Room | null = null;
  @Input() action?: string;
  @Output() save = new EventEmitter<Room>();
  @Output() cancel = new EventEmitter<void>();

  roomId?: string | null = null;
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
    private roomService: RoomService,
    private route: ActivatedRoute) {
    this.roomForm = this.fb.group({
      roomType: [''],
      roomNumber: [''],
      capacity: [''],
      status: [''],
      price: [''],
      photo: [''],
      facility: ['']
    });
  }
  
  ngOnInit(): void {
    console.log(this.route.snapshot.url);
    const currentRoute = this.route.snapshot.url[this.route.snapshot.url.length - 1]?.path;
    this.action = currentRoute === 'edit' ? 'edit' : (currentRoute === 'create' ? 'add' : 'detail');
    this.roomId = this.route.snapshot.paramMap.get('id');
    this.loadRoom();
  }

  loadRoom(): void {
    if (this.roomId) {
      this.roomService.getRoomById(this.roomId).subscribe({
        next: (room: Room) => {
          this.room = room;
          this.roomForm.patchValue({
            roomType: this.room.roomType,
            roomNumber: this.room.roomNumber,
            capacity: this.room.capacity,
            status: this.room.status,
            price: this.room.price,
            photo: this.room.photo,
            facility: this.room.facility
          });
        },
        error: (err) => {
          console.error('Error fetching room:', err);
        }
      });
    }
  }

  onSubmit(): void {
    console.log("mantap");
    if (this.roomForm.valid) {
        const roomData = this.roomForm.value;
        console.log(roomData);
        
        console.log(this.action);
        if (this.action === 'edit') { 
          if (this.roomId) {
              this.roomService.editRoomData(this.roomId, roomData).subscribe({
                  next: (updatedRoom) => {
                      console.log('Room updated successfully:', updatedRoom);
                      this.save.emit(updatedRoom); 
                  },
                  error: (err) => {
                      console.error('Error updating room:', err);
                  }
              });
          } else {
              console.error('Room ID is not available for editing.');
          }
        }
        else if (this.action === 'add') {
          this.roomService.createRoom(roomData).subscribe({
            next: (createdRoom) => {
                console.log('Room created successfully:', createdRoom);
                this.save.emit(createdRoom); 
            },
            error: (err) => {
                console.error('Error creating room:', err);
            }
          });
        }
        else{
          this.save.emit(roomData);
        }

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
