import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Facility, Room, RoomType, Status } from '../../../../model/room.model';
import { RoomService } from '../../../../services/room.service';
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
  roomFacility = Object.values(Facility);

  selectedFacility: string[] = [];

  roomForm: FormGroup;
  isVisible = true;
  showFileUpload = false;
  selectedFile: File | null = null;
  message: string | null = null;

  constructor(
    private fb: FormBuilder,
    private roomService: RoomService,
    private route: ActivatedRoute,
    private router: Router) {
    this.roomForm = this.fb.group({
      roomType: ['',[Validators.required]],
      roomNumber: ['',[Validators.required]],
      capacity: ['',[Validators.required, Validators.pattern('^[0-9]+$')]],
      status: ['',[Validators.required]],
      price: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      photo: [''],
      facility: [[]]
    });
  }
  
  ngOnInit(): void {
    console.log(this.route.snapshot.url);
    const currentRoute = this.route.snapshot.url[this.route.snapshot.url.length - 1]?.path;
    console.log(currentRoute);
    this.action = currentRoute === 'edit' ? 'Edit Room' : (currentRoute === 'create' ? 'Add New Room' : 'Room Details');
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
            facility: this.room.facility,
          });
          this.updateSelectedFacilities();
        },
        error: (err) => {
          console.error('Error fetching room:', err);
          this.message = err;
        }
      });
    }
  }

  updateSelectedFacilities(): void {
    if (this.room?.facility) {
      this.selectedFacility = [...this.room.facility];
    }
  }

  onSubmit(): void {
    if (this.roomForm.valid) {
      const roomData = {
        ...this.roomForm.value
      };

      console.log(roomData);
      if (this.action === 'Edit Room' && this.roomId) {
        this.roomService.editRoomData(this.roomId, roomData).subscribe({
          next: (updatedRoom) => {
            console.log('Room updated successfully:', updatedRoom);
            this.save.emit(updatedRoom);
          },
          error: (err) => {
            console.error('Error updating room:', err);
          }
        });
      } else if (this.action === 'Add New Room') {
        this.roomService.createRoom(roomData).subscribe({
          next: (createdRoom) => {
            console.log('Room created successfully:', createdRoom);
            this.save.emit(createdRoom);
          },
          error: (err) => {
            console.error('Error creating room:', err);
            this.message = err;
          }
        });
      }
    }
  }

  onClose(): void {
    this.router.navigate(['/admin/rooms']);
    this.cancel.emit();
  }

  onFacilityChange(facility: string, event: Event) {
    const facilities = this.roomForm.get('facility')?.value || [];
    const isChecked = (event.target as HTMLInputElement).checked;
  
    if (isChecked) {
      var upperCaseFacility = facility.toUpperCase();
      upperCaseFacility = upperCaseFacility.replace(' ', '_');
      facilities.push(upperCaseFacility);
    } else {
      const index = facilities.indexOf(facility);
      if (index > -1) {
        facilities.splice(index, 1);
      }
    }
    console.log(facilities);
    this.roomForm.get('facility')?.setValue(facilities);
  }
}
